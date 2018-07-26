/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huaweicloud.dis;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.core.DefaultRequest;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.DISConfig.BodySerializeType;
import com.huaweicloud.dis.core.DISCredentials;
import com.huaweicloud.dis.core.restresource.*;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.iface.api.protobuf.ProtobufUtils;
import com.huaweicloud.dis.iface.app.request.CreateAppRequest;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import com.huaweicloud.dis.iface.stream.request.*;
import com.huaweicloud.dis.iface.stream.response.*;
import com.huaweicloud.dis.util.ExponentialBackOff;
import com.huaweicloud.dis.util.RestClientWrapper;
import com.huaweicloud.dis.util.SnappyUtils;
import com.huaweicloud.dis.util.Utils;
import com.huaweicloud.dis.util.config.ICredentialsProvider;
import com.huaweicloud.dis.util.encrypt.EncryptUtils;

public class DISClient implements DIS
{
    private static final Logger LOG = LoggerFactory.getLogger(DISClient.class);

    protected static final String HTTP_X_PROJECT_ID = "X-Project-Id";

    protected static final String HTTP_X_SECURITY_TOKEN = "X-Security-Token";

    protected String region;
    
    protected DISConfig disConfig;
    
    protected DISCredentials credentials;
    
    protected ReentrantLock recordsRetryLock = new ReentrantLock();
    
    protected ICredentialsProvider credentialsProvider;
    
    public DISClient(DISConfig disConfig)
    {
        this.disConfig = DISConfig.buildConfig(disConfig);
        this.credentials = new DISCredentials(this.disConfig);
        this.region = this.disConfig.getRegion();
        check();
        initCredentialsProvider();
    }
    
    /**
     * @deprecated use {@link DISClientBuilder#defaultClient()}
     */
    public DISClient()
    {
        this.disConfig = DISConfig.buildDefaultConfig();
        this.credentials = new DISCredentials(this.disConfig);
        this.region = disConfig.getRegion();
        check();
        initCredentialsProvider();
    }    
    
    @Override
    public PutRecordsResult putRecords(PutRecordsRequest putRecordsParam)
    {
        return innerPutRecordsWithRetry(putRecordsParam);
    }

    protected PutRecordsResult innerPutRecordsWithRetry(PutRecordsRequest putRecordsParam)
    {
        PutRecordsResult putRecordsResult = null;
        PutRecordsResultEntry[] putRecordsResultEntryList = null;
        Integer[] retryIndex = null;
        PutRecordsRequest retryPutRecordsRequest = putRecordsParam;
        
        int retryCount = -1;
        int currentFailed = 0;
        ExponentialBackOff backOff = null;
        try
        {
            do
            {
                retryCount++;
                if (retryCount > 0)
                {
                    // 等待一段时间再发起重试
                    if (backOff == null)
                    {
                        recordsRetryLock.lock();
                        LOG.trace("Put records retry lock.");
                        backOff = new ExponentialBackOff(ExponentialBackOff.DEFAULT_INITIAL_INTERVAL,
                            ExponentialBackOff.DEFAULT_MULTIPLIER, disConfig.getBackOffMaxIntervalMs(),
                            ExponentialBackOff.DEFAULT_MAX_ELAPSED_TIME);
                    }
                    
                    if (putRecordsResult != null && currentFailed != putRecordsResult.getRecords().size())
                    {
                        // 部分失败则重置退避时间
                        backOff.resetCurrentInterval();
                    }
                    
                    long sleepMs = backOff.getNextBackOff();
                    
                    if (retryPutRecordsRequest.getRecords().size() > 0)
                    {
                        LOG.debug(
                            "Put {} records but {} failed, will re-try after backoff {} ms, current retry count is {}.",
                            putRecordsResult != null ? putRecordsResult.getRecords().size()
                                : putRecordsParam.getRecords().size(),
                            currentFailed,
                            sleepMs,
                            retryCount);
                    }
                    
                    backOff.backOff(sleepMs);
                }
                
                try
                {
                    putRecordsResult = innerPutRecords(retryPutRecordsRequest);
                }
                catch (Throwable t)
                {
                    if (putRecordsResultEntryList != null)
                    {
                        LOG.error(t.getMessage(), t);
                        break;
                    }
                    throw t;
                }
                
                if (putRecordsResult != null)
                {
                    currentFailed = putRecordsResult.getFailedRecordCount().get();
                    
                    if (putRecordsResultEntryList == null && currentFailed == 0 || disConfig.getRecordsRetries() == 0)
                    {
                        // 第一次发送全部成功或者不需要重试，则直接返回结果
                        return putRecordsResult;
                    }
                    
                    if (putRecordsResultEntryList == null)
                    {
                        // 存在发送失败的情况，需要重试，则使用数组来汇总每次请求后的结果。
                        putRecordsResultEntryList = new PutRecordsResultEntry[putRecordsParam.getRecords().size()];
                    }
                    
                    // 需要重试发送数据的原始下标
                    List<Integer> retryIndexTemp = new ArrayList<>(currentFailed);
                    
                    if (currentFailed > 0)
                    {
                        // 初始化重试发送的数据请求
                        retryPutRecordsRequest = new PutRecordsRequest();
                        retryPutRecordsRequest.setStreamName(putRecordsParam.getStreamName());
                        retryPutRecordsRequest.setRecords(new ArrayList<>(currentFailed));
                    }
                    
                    // 对每条结果分析，更新结果数据
                    for (int i = 0; i < putRecordsResult.getRecords().size(); i++)
                    {
                        // 获取重试数据在原始数据中的下标位置
                        int originalIndex = retryIndex == null ? i : retryIndex[i];
                        PutRecordsResultEntry putRecordsResultEntry = putRecordsResult.getRecords().get(i);
                        // 对所有异常进行重试 && "DIS.4303".equals(putRecordsResultEntry.getErrorCode())
                        if (!StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode()))
                        {
                            retryIndexTemp.add(originalIndex);
                            retryPutRecordsRequest.getRecords().add(putRecordsParam.getRecords().get(originalIndex));
                        }
                        putRecordsResultEntryList[originalIndex] = putRecordsResultEntry;
                    }
                    retryIndex = retryIndexTemp.size() > 0 ? retryIndexTemp.toArray(new Integer[retryIndexTemp.size()])
                        : new Integer[0];
                }
            } while ((retryIndex == null || retryIndex.length > 0) && retryCount < disConfig.getRecordsRetries());
        }
        finally
        {
            if (retryCount > 0)
            {
                recordsRetryLock.unlock();
                LOG.trace("Put records retry unlock.");
            }
        }
        putRecordsResult = new PutRecordsResult();
        if (retryIndex == null)
        {
            // 不可能存在此情况，完全没有发送出去会直接抛出异常
            putRecordsResult.setFailedRecordCount(new AtomicInteger(putRecordsParam.getRecords().size()));
        }
        else
        {
            putRecordsResult.setFailedRecordCount(new AtomicInteger(retryIndex.length));
            putRecordsResult.setRecords(Arrays.asList(putRecordsResultEntryList));
        }
        
        return putRecordsResult;
    }
    
	/*
	 * Internal API
	 */
    protected final PutRecordsResult innerPutRecords(PutRecordsRequest putRecordsParam)
    {
        // Decorate PutRecordsRequest if needed
        putRecordsParam = decorateRecords(putRecordsParam);
        
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.POST);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new RecordResource(null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        if(BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())){            
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");
            
            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest protoRequest = ProtobufUtils.toProtobufPutRecordsRequest(putRecordsParam);
            
            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult putRecordsResult = request(protoRequest.toByteArray(), request, com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult.class);            
            
            PutRecordsResult result = ProtobufUtils.toPutRecordsResult(putRecordsResult);
            
            return result;
            
        }else{
            return request(putRecordsParam, request, PutRecordsResult.class);
        }
    }

    
    
    @Override
    public GetPartitionCursorResult getPartitionCursor(GetPartitionCursorRequest getPartitionCursorParam)
    {
        return innerGetPartitionCursor(getPartitionCursorParam);
    }
    
    /*
     * Internal API
     */
    protected final GetPartitionCursorResult innerGetPartitionCursor(GetPartitionCursorRequest getPartitionCursorParam)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new CursorResource(null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        
        return request(getPartitionCursorParam, request, GetPartitionCursorResult.class);
    }
    
    @Override
    public GetRecordsResult getRecords(GetRecordsRequest getRecordsParam)
    {
        return innerGetRecords(getRecordsParam);
    }
    
    /*
     * Internal API
     */
    protected final GetRecordsResult innerGetRecords(GetRecordsRequest getRecordsParam)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new RecordResource(null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());

        GetRecordsResult result;
        
        if(BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())){            
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");
            
            com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult protoResult = request(getRecordsParam, request, com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult.class);
            result = ProtobufUtils.toGetRecordsResult(protoResult);
        }else{
            result = request(getRecordsParam, request, GetRecordsResult.class);
        }

        return decorateRecords(result);
    }
    
    /**
     * Decorate {@link PutRecordsRequest} before sending HTTP Request.
     * 
     * @param putRecordsParam A <code>PutRecords</code> request.
     * @return A <code>PutRecords</code> request after decorating.
     */
    private PutRecordsRequest decorateRecords(PutRecordsRequest putRecordsParam)
    {
        // compress with snappy-java
        if (disConfig.isDataCompressEnabled())
        {
            if (putRecordsParam.getRecords() != null)
            {
                for (PutRecordsRequestEntry record : putRecordsParam.getRecords())
                {
                    byte[] input = record.getData().array();
                    try
                    {
                        byte[] compressedInput = SnappyUtils.compress(input);
                        record.setData(ByteBuffer.wrap(compressedInput));
                    }
                    catch (IOException e)
                    {
                        LOG.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        
        // encrypt
        if (isEncrypt())
        {
            if (putRecordsParam.getRecords() != null)
            {
                for (PutRecordsRequestEntry record : putRecordsParam.getRecords())
                {
                    record.setData(encrypt(record.getData()));
                }
            }
        }
        
        return putRecordsParam;
    }
    
    /**
     * Decorate {@link GetRecordsResult} after getting HTTP Response.
     * 
     * @param getRecordsResult A <code>GetRecords</code> response.
     * @return A <code>GetRecords</code> response after decorating.
     */
    private GetRecordsResult decorateRecords(GetRecordsResult getRecordsResult)
    {
        // decrypt
        if (isEncrypt())
        {
            if (getRecordsResult.getRecords() != null)
            {
                for (Record record : getRecordsResult.getRecords())
                {
                    record.setData(decrypt(record.getData()));
                }
            }
        }
        
        // uncompress with snappy-java
        if (disConfig.isDataCompressEnabled())
        {
            if (getRecordsResult.getRecords() != null)
            {
                for (Record record : getRecordsResult.getRecords())
                {
                    byte[] input = record.getData().array();
                    try
                    {
                        byte[] uncompressedInput = SnappyUtils.uncompress(input);
                        record.setData(ByteBuffer.wrap(uncompressedInput));
                    }
                    catch (IOException e)
                    {
                        LOG.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        
        return getRecordsResult;
    }
    
    private boolean isEncrypt()
    {
        return disConfig.getIsDefaultDataEncryptEnabled() && !StringUtils.isNullOrEmpty(disConfig.getDataPassword());
    }
    
    private ByteBuffer encrypt(ByteBuffer src)
    {
        String cipher = null;
        try
        {
            cipher = EncryptUtils.gen(new String[] {disConfig.getDataPassword()}, src.array());
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
            | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e)
        {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return ByteBuffer.wrap(cipher.getBytes());
    }
    
    private ByteBuffer decrypt(ByteBuffer cipher)
    {
        Charset utf8 = Charset.forName("UTF-8");
        String src;
        try
        {
            src = EncryptUtils.dec(new String[] {disConfig.getDataPassword()}, new String(cipher.array(), utf8));
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
            | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e)
        {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        
        return ByteBuffer.wrap(src.getBytes(utf8));
    }
    
    // protected <T> T request(Object param, Object target, Class<T> clazz){
    // check();
    //
    // String result = new RestClientWrapper(new DefaultRequest<>(Constants.SERVICENAME), disConfig).request(param,
    // credentials.getAccessKeyId(), credentials.getSecretKey(), region);
    // return JsonUtils.jsonToObj(result, clazz);
    // }

    protected <T> T request(Object param, Request<HttpRequest> request, Class<T> clazz)
    {
        DISCredentials credentials = this.credentials;
        if (credentialsProvider != null)
        {
            DISCredentials cloneCredentials = this.credentials.clone();
            credentials = credentialsProvider.updateCredentials(cloneCredentials);
            if (credentials != cloneCredentials)
            {
                this.credentials = credentials;
            }
        }
        
        request.addHeader(HTTP_X_PROJECT_ID, disConfig.getProjectId());
        
        String securityToken = credentials.getSecurityToken();
        if (!StringUtils.isNullOrEmpty(securityToken))
        {
            request.addHeader(HTTP_X_SECURITY_TOKEN, securityToken);
        }
        
        // 发送请求
        return new RestClientWrapper(request, disConfig)
            .request(param, credentials.getAccessKeyId(), credentials.getSecretKey(), region, clazz);
    }
    
    private void check()
    {
        if (credentials == null)
        {
            throw new DISClientException("credentials can not be null.");
        }
        
        if (StringUtils.isNullOrEmpty(credentials.getAccessKeyId()))
        {
            throw new DISClientException("credentials ak can not be null.");
        }
        
        if (StringUtils.isNullOrEmpty(credentials.getSecretKey()))
        {
            throw new DISClientException("credentials sk can not be null.");
        }
        
        if (StringUtils.isNullOrEmpty(region))
        {
            throw new DISClientException("region can not be null.");
        }
        
        if (StringUtils.isNullOrEmpty(disConfig.getProjectId()))
        {
            throw new RuntimeException("project id can not be null.");
        }
        
        String endpoint = disConfig.getEndpoint();
        if (StringUtils.isNullOrEmpty(endpoint))
        {
            throw new DISClientException("endpoint can not be null.");
        }
        if (!Utils.isValidEndpoint(endpoint))
        {
            throw new DISClientException("invalid endpoint.");
        }
    }
    
    private void setEndpoint(Request<HttpRequest> request, String endpointStr)
    {
        URI endpoint;
        try
        {
            endpoint = new URI(endpointStr);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        
        request.setEndpoint(endpoint);
    }
    
    // ###################### delegate IStreamService #########################
    @Override
    public CreateStreamResult createStream(CreateStreamRequest createStreamRequest)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.POST);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        
        CreateStreamResult result = request(createStreamRequest, request, CreateStreamResult.class);
        
        return result;
    }
    
    
    @Override
    public DescribeStreamResult describeStream(DescribeStreamRequest describeStreamRequest)
    {
        return innerDescribeStream(describeStreamRequest);
    }
    
    /*
     * Internal API
     */
    protected final DescribeStreamResult innerDescribeStream(DescribeStreamRequest describeStreamRequest)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, describeStreamRequest.getStreamName()))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        
        DescribeStreamResult result = request(describeStreamRequest, request, DescribeStreamResult.class);
        
        return result;
    }

    @Override
    public UpdatePartitionCountResult updatePartitionCount(UpdatePartitionCountRequest updatePartitionCountRequest)
    {
        return innerUpdatePartitionCount(updatePartitionCountRequest);
    }
    
    protected final UpdatePartitionCountResult innerUpdatePartitionCount(
        UpdatePartitionCountRequest updatePartitionCountRequest)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.PUT);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new StreamResource(null, updatePartitionCountRequest.getStreamName()))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        return request(updatePartitionCountRequest, request, UpdatePartitionCountResult.class);
    }
    
    @Override
    public DeleteStreamResult deleteStream(DeleteStreamRequest deleteStreamRequest)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.DELETE);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, deleteStreamRequest.getStreamName()))
                .build();
        
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        
        DeleteStreamResult result = request(deleteStreamRequest, request, DeleteStreamResult.class);
        return result;
    }
    
    @Override
    public ListStreamsResult listStreams(ListStreamsRequest listStreamsRequest)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath =
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, null))
                .build();
        
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        
        ListStreamsResult result = request(listStreamsRequest, request, ListStreamsResult.class);
        return result;
    }
 
    
    
    
    
    
    
    
    
    //##################### extended Method ######################
    @Override
    public PutRecordResult putRecord(PutRecordRequest putRecordParam)
    {
        if (isEncrypt())
        {
            putRecordParam.setData(encrypt(putRecordParam.getData()));
        }
        
        return toPutRecordResult(putRecords(toPutRecordsRequest(putRecordParam)));
    }
    
    private PutRecordsRequest toPutRecordsRequest(PutRecordRequest putRecordRequest)
    {
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamName(putRecordRequest.getStreamName());
        
        List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<PutRecordsRequestEntry>();
        PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
        putRecordsRequestEntry.setData(putRecordRequest.getData());
        putRecordsRequestEntry.setPartitionKey(putRecordRequest.getPartitionKey());
        putRecordsRequestEntry.setTimestamp(putRecordRequest.getTimestamp());
        putRecordsRequestEntryList.add(putRecordsRequestEntry);
        
        putRecordsRequest.setRecords(putRecordsRequestEntryList);
        
        return putRecordsRequest;
    }
    
    private PutRecordResult toPutRecordResult(PutRecordsResult putRecordsResult)
    {
        if (null != putRecordsResult && null != putRecordsResult.getRecords() && putRecordsResult.getRecords().size() > 0)
        {
            List<PutRecordsResultEntry> records = putRecordsResult.getRecords();
            PutRecordsResultEntry record = records.get(0);
            
            PutRecordResult result = new PutRecordResult();
            result.setPartitionId(record.getPartitionId());
            result.setSequenceNumber(record.getSequenceNumber());
            
            return result;
        }
        
        return null;
    }

    /*
     * Internal API
     */
    protected final FileUploadResult innerGetFileUploadResult(QueryFileState queryFileState)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new FileResource(queryFileState.getDeliverDataId()))
            .withResource(new StateResource(null))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        
        return request(queryFileState, request, FileUploadResult.class);
    }
    
    public void createApp(String appName)
    {
        innerCreateApp(appName);
    }
    
    public final void innerCreateApp(String appName)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.POST);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new AppsResource(null))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        CreateAppRequest createAppIdRequest = new CreateAppRequest();
        createAppIdRequest.setAppName(appName);
        setEndpoint(request, disConfig.getManagerEndpoint());
        request(createAppIdRequest, request, null);
    }
    
    public void deleteApp(String appName)
    {
        innerDeleteApp(appName);
    }

    public final void innerDeleteApp(String appName)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.DELETE);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new AppsResource(appName))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        request(null, request, null);
    }

    public CommitCheckpointResult commitCheckpoint(CommitCheckpointRequest commitCheckpointParam)
    {
        return innerCommitCheckpoint(commitCheckpointParam);
    }
    
    protected final CommitCheckpointResult innerCommitCheckpoint(CommitCheckpointRequest commitCheckpointParam)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.POST);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new CheckPointResource(null))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        return request(commitCheckpointParam, request, CommitCheckpointResult.class);
    }
    
    public GetCheckpointResult getCheckpoint(GetCheckpointRequest getCheckpointRequest)
    {
        return innerGetCheckpoint(getCheckpointRequest);
    }
    
    protected final GetCheckpointResult innerGetCheckpoint(GetCheckpointRequest getCheckpointRequest)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new CheckPointResource(null))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        return request(getCheckpointRequest, request, GetCheckpointResult.class);
    }
    
    /**
     * 开放认证修改接口，用户自定义实现ICredentialsProvider，更新认证信息
     */
    private void initCredentialsProvider()
    {
        // Provider转换
        String credentialsProviderClass = disConfig.get(DISConfig.PROPERTY_CONFIG_PROVIDER_CLASS, null);
        if (!StringUtils.isNullOrEmpty(credentialsProviderClass))
        {
            try
            {
                this.credentialsProvider = (ICredentialsProvider)Class.forName(credentialsProviderClass).newInstance();
                this.credentials = credentialsProvider.updateCredentials(this.credentials.clone());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Failed to call ICredentialsProvider[" + credentialsProviderClass
                    + "], error [" + e.toString() + "]", e);
            }
        }
    }
}
