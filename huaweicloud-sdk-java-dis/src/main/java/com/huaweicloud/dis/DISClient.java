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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.huaweicloud.dis.util.JstackUtils;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig.BodySerializeType;
import com.huaweicloud.dis.core.DISCredentials;
import com.huaweicloud.dis.core.DefaultRequest;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.restresource.*;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.http.AbstractDISClient;
import com.huaweicloud.dis.http.exception.HttpClientErrorException;
import com.huaweicloud.dis.iface.api.protobuf.ProtobufUtils;
import com.huaweicloud.dis.iface.app.request.CreateAppRequest;
import com.huaweicloud.dis.iface.app.request.ListAppsRequest;
import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.DescribeAppResult;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import com.huaweicloud.dis.iface.stream.request.*;
import com.huaweicloud.dis.iface.stream.response.*;
import com.huaweicloud.dis.iface.transfertask.request.*;
import com.huaweicloud.dis.iface.transfertask.response.*;
import com.huaweicloud.dis.util.ExponentialBackOff;
import com.huaweicloud.dis.util.Utils;
import com.huaweicloud.dis.util.cache.CacheResenderThread;
import com.huaweicloud.dis.util.cache.CacheUtils;

public class DISClient extends AbstractDISClient implements DIS {
    private static final Logger LOG = LoggerFactory.getLogger(DISClient.class);

    protected ReentrantLock recordsRetryLock = new ReentrantLock();

    private CacheResenderThread cacheResenderThread;

    public DISClient(DISConfig disConfig) {
        super(disConfig);
    }

    /**
     * @deprecated use {@link DISClientBuilder#defaultClient()}
     */
    public DISClient() {
        super();
    }

    @Override
    public PutRecordsResult putRecords(PutRecordsRequest putRecordsParam) {
        Thread thread = Thread.currentThread();
        try {
            JstackUtils.put(thread, disConfig.getConnectionTimeOut());
            return innerPutRecordsSupportingCache(putRecordsParam);
        } finally {
            JstackUtils.remove(thread);
        }
    }

    protected PutRecordsResult innerPutRecordsSupportingCache(PutRecordsRequest putRecordsParam) {
        if (disConfig.isDataCacheEnabled()) {
            // 开启本地缓存
            PutRecordsResult putRecordsResult = null;
            try {
                // 若本地缓存开关打开，则启动缓存数据重发线程
                if (this.cacheResenderThread == null) {
                    cacheResenderThread = new CacheResenderThread("DisClient", disConfig);
                    cacheResenderThread.start();
                }

                putRecordsResult = innerPutRecordsWithRetry(putRecordsParam);
                // 部分记录上传失败
                if (putRecordsResult.getFailedRecordCount().get() > 0) {
                    // 过滤出上传失败的记录
                    List<PutRecordsResultEntry> putRecordsResultEntries = putRecordsResult.getRecords();
                    List<PutRecordsRequestEntry> failedPutRecordsRequestEntries = new ArrayList<>();
                    int index = 0;
                    for (PutRecordsResultEntry putRecordsResultEntry : putRecordsResultEntries) {
                        if (!StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode())) {
                            failedPutRecordsRequestEntries.add(putRecordsParam.getRecords().get(index));
                        }
                        index++;
                    }
                    putRecordsParam.setRecords(failedPutRecordsRequestEntries);

                    LOG.info("Local data cache is enabled, try to put failed records to local.");

                    CacheUtils.putToCache(putRecordsParam, disConfig); // 写入本地缓存
                }
            } catch (Exception e) {
                if (!(e.getCause() instanceof HttpClientErrorException)) {
                    // 网络异常
                    LOG.info("Local data cache is enabled, try to put failed records to local.");

                    CacheUtils.putToCache(putRecordsParam, disConfig); // 写入本地缓存
                }
                throw e;
            }
            return putRecordsResult;
        } else {
            return innerPutRecordsWithRetry(putRecordsParam);
        }
    }

    protected PutRecordsResult innerPutRecordsWithRetry(PutRecordsRequest putRecordsParam) {
        PutRecordsResult putRecordsResult = null;
        PutRecordsResultEntry[] putRecordsResultEntryList = null;
        Integer[] retryIndex = null;
        PutRecordsRequest retryPutRecordsRequest = putRecordsParam;

        int retryCount = -1;
        int currentFailed = 0;
        int noRetryRecordsCount = 0;
        ExponentialBackOff backOff = null;
        try {
            do {
                retryCount++;
                if (retryCount > 0) {
                    // 等待一段时间再发起重试
                    if (backOff == null) {
                        recordsRetryLock.lock();
                        LOG.trace("Put records retry lock.");
                        backOff = new ExponentialBackOff(ExponentialBackOff.DEFAULT_INITIAL_INTERVAL,
                                ExponentialBackOff.DEFAULT_MULTIPLIER, disConfig.getBackOffMaxIntervalMs(),
                                ExponentialBackOff.DEFAULT_MAX_ELAPSED_TIME);
                    }

                    if (putRecordsResult != null && currentFailed != putRecordsResult.getRecords().size()) {
                        // 部分失败则重置退避时间
                        backOff.resetCurrentInterval();
                    }

                    long sleepMs = backOff.getNextBackOff();

                    if (retryPutRecordsRequest.getRecords().size() > 0) {
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

                try {
                    putRecordsResult = innerPutRecords(retryPutRecordsRequest);
                } catch (Throwable t) {
                    if (putRecordsResultEntryList != null) {
                        LOG.error(t.getMessage(), t);
                        break;
                    }
                    throw t;
                }

                if (putRecordsResult != null) {
                    currentFailed = putRecordsResult.getFailedRecordCount().get();

                    if (putRecordsResultEntryList == null && currentFailed == 0 || disConfig.getRecordsRetries() == 0) {
                        // 第一次发送全部成功或者不需要重试，则直接返回结果
                        return putRecordsResult;
                    }

                    if (putRecordsResultEntryList == null) {
                        // 存在发送失败的情况，需要重试，则使用数组来汇总每次请求后的结果。
                        putRecordsResultEntryList = new PutRecordsResultEntry[putRecordsParam.getRecords().size()];
                    }

                    // 需要重试发送数据的原始下标
                    List<Integer> retryIndexTemp = new ArrayList<>(currentFailed);

                    if (currentFailed > 0) {
                        // 初始化重试发送的数据请求
                        retryPutRecordsRequest = new PutRecordsRequest();
                        retryPutRecordsRequest.setStreamName(putRecordsParam.getStreamName());
                        retryPutRecordsRequest.setStreamId(putRecordsParam.getStreamId());
                        retryPutRecordsRequest.setRecords(new ArrayList<>(currentFailed));
                    }

                    // 对每条结果分析，更新结果数据
                    for (int i = 0; i < putRecordsResult.getRecords().size(); i++) {
                        // 获取重试数据在原始数据中的下标位置
                        int originalIndex = retryIndex == null ? i : retryIndex[i];
                        PutRecordsResultEntry putRecordsResultEntry = putRecordsResult.getRecords().get(i);

                        if (!StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode())) {
                            // 只对指定异常(如流控与服务端内核异常)进行重试
                            if (isRecordsRetriableErrorCode(putRecordsResultEntry.getErrorCode())) {
                                retryIndexTemp.add(originalIndex);
                                retryPutRecordsRequest.getRecords().add(putRecordsParam.getRecords().get(originalIndex));
                            } else {
                                noRetryRecordsCount++;
                            }
                        }
                        putRecordsResultEntryList[originalIndex] = putRecordsResultEntry;
                    }
                    retryIndex = retryIndexTemp.size() > 0 ? retryIndexTemp.toArray(new Integer[retryIndexTemp.size()])
                            : new Integer[0];
                }
            } while ((retryIndex == null || retryIndex.length > 0) && retryCount < disConfig.getRecordsRetries());
        } finally {
            if (retryCount > 0) {
                recordsRetryLock.unlock();
                LOG.trace("Put records retry unlock.");
            }
        }
        putRecordsResult = new PutRecordsResult();
        if (retryIndex == null) {
            // 不可能存在此情况，完全没有发送出去会直接抛出异常
            putRecordsResult.setFailedRecordCount(new AtomicInteger(putRecordsParam.getRecords().size()));
        } else {
            putRecordsResult.setFailedRecordCount(new AtomicInteger(retryIndex.length + noRetryRecordsCount));
            putRecordsResult.setRecords(Arrays.asList(putRecordsResultEntryList));
        }

        return putRecordsResult;
    }

    /*
     * Internal API
     */
    protected final PutRecordsResult innerPutRecords(PutRecordsRequest putRecordsParam) {
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
        if (BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())) {
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");

            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest protoRequest = ProtobufUtils.toProtobufPutRecordsRequest(putRecordsParam);

            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult putRecordsResult = request(protoRequest.toByteArray(), request, com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult.class);

            PutRecordsResult result = ProtobufUtils.toPutRecordsResult(putRecordsResult);

            return result;

        } else {
            return request(putRecordsParam, request, PutRecordsResult.class);
        }
    }


    @Override
    public GetPartitionCursorResult getPartitionCursor(GetPartitionCursorRequest getPartitionCursorParam) {
        Thread thread = Thread.currentThread();
        try {
            JstackUtils.put(thread, disConfig.getConnectionTimeOut());
            return innerGetPartitionCursor(getPartitionCursorParam);
        } finally {
            JstackUtils.remove(thread);
        }
    }

    /*
     * Internal API
     */
    protected final GetPartitionCursorResult innerGetPartitionCursor(GetPartitionCursorRequest getPartitionCursorParam) {
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
    public GetRecordsResult getRecords(GetRecordsRequest getRecordsParam) {
        Thread thread = Thread.currentThread();
        try {
            JstackUtils.put(thread, disConfig.getConnectionTimeOut());
            return innerGetRecords(getRecordsParam);
        } finally {
            JstackUtils.remove(thread);
        }
    }

    /*
     * Internal API
     */
    protected final GetRecordsResult innerGetRecords(GetRecordsRequest getRecordsParam) {
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

        if (BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())) {
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");

            com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult protoResult = request(getRecordsParam, request, com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult.class);
            result = ProtobufUtils.toGetRecordsResult(protoResult);
        } else {
            result = request(getRecordsParam, request, GetRecordsResult.class);
        }

        return decorateRecords(result);
    }

    // ###################### delegate IStreamService #########################
    @Override
    public CreateStreamResult createStream(CreateStreamRequest createStreamRequest) {
        return innerCreateStream(createStreamRequest);
    }

    protected final CreateStreamResult innerCreateStream(CreateStreamRequest createStreamRequest) {
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
    public DescribeStreamResult describeStream(DescribeStreamRequest describeStreamRequest) {
        return innerDescribeStream(describeStreamRequest);
    }

    /*
     * Internal API
     */
    protected final DescribeStreamResult innerDescribeStream(DescribeStreamRequest describeStreamRequest) {
        // change to shardId format
        describeStreamRequest.setStartPartitionId(Utils.getShardIdFromPartitionId(describeStreamRequest.getStartPartitionId()));

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
    public UpdatePartitionCountResult updatePartitionCount(UpdatePartitionCountRequest updatePartitionCountRequest) {
        return innerUpdatePartitionCount(updatePartitionCountRequest);
    }

    protected final UpdatePartitionCountResult innerUpdatePartitionCount(
            UpdatePartitionCountRequest updatePartitionCountRequest) {
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
    public DeleteStreamResult deleteStream(DeleteStreamRequest deleteStreamRequest) {
        return innerDeleteStream(deleteStreamRequest);
    }

    protected final DeleteStreamResult innerDeleteStream(DeleteStreamRequest deleteStreamRequest) {
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
    public ListStreamsResult listStreams(ListStreamsRequest listStreamsRequest) {
        return innerListStreams(listStreamsRequest);
    }

    protected final ListStreamsResult innerListStreams(ListStreamsRequest listStreamsRequest) {
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
    public PutRecordResult putRecord(PutRecordRequest putRecordParam) {
        return toPutRecordResult(putRecords(toPutRecordsRequest(putRecordParam)));
    }

    /*
     * Internal API
     */
    protected final FileUploadResult innerGetFileUploadResult(QueryFileState queryFileState) {
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

    public void createApp(String appName) {
        innerCreateApp(appName);
    }

    public final void innerCreateApp(String appName) {
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

    public void deleteApp(String appName) {
        innerDeleteApp(appName);
    }

    public final void innerDeleteApp(String appName) {
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

    @Override
    public DescribeAppResult describeApp(String appName) {
        return innerDescribeApp(appName);
    }

    public final DescribeAppResult innerDescribeApp(String appName) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(appName))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        return request(null, request, DescribeAppResult.class);
    }

    @Override
    public ListAppsResult listApps(ListAppsRequest listAppsRequest) {
        return innerListApps(listAppsRequest);
    }

    public final ListAppsResult innerListApps(ListAppsRequest listAppsRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        return request(listAppsRequest, request, ListAppsResult.class);
    }


    public CommitCheckpointResult commitCheckpoint(CommitCheckpointRequest commitCheckpointParam) {
        return innerCommitCheckpoint(commitCheckpointParam);
    }

    protected final CommitCheckpointResult innerCommitCheckpoint(CommitCheckpointRequest commitCheckpointParam) {
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

    public GetCheckpointResult getCheckpoint(GetCheckpointRequest getCheckpointRequest) {
        return innerGetCheckpoint(getCheckpointRequest);
    }

    protected final GetCheckpointResult innerGetCheckpoint(GetCheckpointRequest getCheckpointRequest) {
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


    public DeleteCheckpointResult deleteCheckpoint(DeleteCheckpointRequest deleteCheckpointRequest) {
        return innerDeleteCheckpoint(deleteCheckpointRequest);
    }

    protected final DeleteCheckpointResult innerDeleteCheckpoint(DeleteCheckpointRequest deleteCheckpointRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.DELETE);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new CheckPointResource(null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        return request(deleteCheckpointRequest, request, DeleteCheckpointResult.class);
    }

    @Override
    public ListStreamConsumingStateResult listStreamConsumingState(ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
        return innerListStreamConsumingState(listStreamConsumingStateRequest);
    }

    protected ListStreamConsumingStateResult innerListStreamConsumingState(ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(listStreamConsumingStateRequest.getAppName()))
                .withResource(new StreamResource(listStreamConsumingStateRequest.getStreamName()))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        return request(listStreamConsumingStateRequest, request, ListStreamConsumingStateResult.class);
    }

    @Override
    public UpdateStreamResult updateStream(UpdateStreamRequest updateStreamRequest) {
        return innerUpdateStream(updateStreamRequest);
    }

    protected UpdateStreamResult innerUpdateStream(UpdateStreamRequest updateStreamRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.PUT);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(StreamResource.DEFAULT_RESOURCE_NAME, updateStreamRequest.getStreamName(), "update"))
                .build();

        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());
        return request(updateStreamRequest, request, UpdateStreamResult.class);
    }

    @Override
    public void updateCredentials(DISCredentials credentials) {
        super.innerUpdateCredentials(credentials);
    }

    //@Override
    public void updateAuthToken(String authToken) {
        super.innerUpdateAuthToken(authToken);
    }

    // ------------ITransferTaskService------------
    public CreateTransferTaskResult createTransferTask(CreateTransferTaskRequest createTransferTaskRequest) {
        return innerCreateTransferTask(createTransferTaskRequest);
    }

    public final CreateTransferTaskResult innerCreateTransferTask(CreateTransferTaskRequest createTransferTaskRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.POST);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, createTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());

        CreateTransferTaskResult result = request(createTransferTaskRequest, request, CreateTransferTaskResult.class);

        return result;
    }

    public UpdateTransferTaskResult updateTransferTask(UpdateTransferTaskRequest updateTransferTaskRequest) {
        return innerUpdateTransferTask(updateTransferTaskRequest);
    }

    public final UpdateTransferTaskResult innerUpdateTransferTask(UpdateTransferTaskRequest updateTransferTaskRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.PUT);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, updateTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, null))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());

        UpdateTransferTaskResult result = request(updateTransferTaskRequest, request, UpdateTransferTaskResult.class);

        return result;
    }

    public DeleteTransferTaskResult deleteTransferTask(DeleteTransferTaskRequest deleteTransferTaskRequest) {
        return innerDeleteTransferTask(deleteTransferTaskRequest);
    }

    public final DeleteTransferTaskResult innerDeleteTransferTask(DeleteTransferTaskRequest deleteTransferTaskRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.DELETE);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, deleteTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, deleteTransferTaskRequest.getTransferTaskName()))
                .build();

        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());

        DeleteTransferTaskResult result = request(deleteTransferTaskRequest, request, DeleteTransferTaskResult.class);
        return result;
    }

    @Override
    public DescribeTransferTaskResult describeTransferTask(DescribeTransferTaskRequest describeTransferTaskRequest) {
        return innerDescribeTransferTask(describeTransferTaskRequest);
    }

    public final DescribeTransferTaskResult innerDescribeTransferTask(
            DescribeTransferTaskRequest describeTransferTaskRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, describeTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, describeTransferTaskRequest.getTransferTaskName()))
                .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());

        DescribeTransferTaskResult result =
                request(describeTransferTaskRequest, request, DescribeTransferTaskResult.class);

        return result;
    }

    @Override
    public ListTransferTasksResult listTransferTasks(ListTransferTasksRquest listTransferTasksRquest) {
        return innerListTransferTasks(listTransferTasksRquest);
    }

    public final ListTransferTasksResult innerListTransferTasks(ListTransferTasksRquest listTransferTasksRquest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, listTransferTasksRquest.getStreamName()))
                .withResource(new TransferTaskResource(null, null))
                .build();

        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());

        ListTransferTasksResult result = request(listTransferTasksRquest, request, ListTransferTasksResult.class);
        return result;
    }

    @Override
    public BatchTransferTaskResult batchTransferTask(BatchTransferTaskRequest batchTransferTaskRequest) {
        return innerBatchTransferTask(batchTransferTaskRequest);
    }

    public final BatchTransferTaskResult innerBatchTransferTask(BatchTransferTaskRequest batchTransferTaskRequest) {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.POST);

        final String resourcePath = ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, batchTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(TransferTaskResource.DEFAULT_RESOURCE_NAME, null, "action"))
                .build();

        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getManagerEndpoint());

        BatchTransferTaskResult result = request(batchTransferTaskRequest, request, BatchTransferTaskResult.class);
        return result;
    }
}
