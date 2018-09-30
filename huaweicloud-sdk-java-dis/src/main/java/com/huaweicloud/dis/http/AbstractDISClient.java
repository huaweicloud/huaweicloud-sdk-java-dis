package com.huaweicloud.dis.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLException;

import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.Constants;
import com.huaweicloud.dis.DISClientBuilder;
import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.DISCredentials;
import com.huaweicloud.dis.core.DefaultRequest;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.auth.signer.internal.SignerConstants;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.exception.HttpStatusCodeException;
import com.huaweicloud.dis.http.exception.RestClientResponseException;
import com.huaweicloud.dis.http.exception.UnknownHttpStatusCodeException;
import com.huaweicloud.dis.iface.data.request.PutRecordRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.GetRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;
import com.huaweicloud.dis.iface.data.response.Record;
import com.huaweicloud.dis.util.ExponentialBackOff;
import com.huaweicloud.dis.util.JsonUtils;
import com.huaweicloud.dis.util.RestClient;
import com.huaweicloud.dis.util.SignUtil;
import com.huaweicloud.dis.util.SnappyUtils;
import com.huaweicloud.dis.util.Utils;
import com.huaweicloud.dis.util.VersionUtils;
import com.huaweicloud.dis.util.config.ICredentialsProvider;
import com.huaweicloud.dis.util.encrypt.EncryptUtils;

public class AbstractDISClient {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDISClient.class);

    protected static final String HTTP_X_PROJECT_ID = "X-Project-Id";

    protected static final String HTTP_X_SECURITY_TOKEN = "X-Security-Token";

    protected static final String HEADER_SDK_VERSION = "X-SDK-Version";
    
    protected String region;
    
    protected DISConfig disConfig;
    
    protected DISCredentials credentials;
    
    protected ICredentialsProvider credentialsProvider;
    
    public AbstractDISClient(DISConfig disConfig)
    {
        this.disConfig = DISConfig.buildConfig(disConfig);
        init();
    }
    
    private void init(){
    	this.credentials = new DISCredentials(this.disConfig);
        this.region = this.disConfig.getRegion();
        check();
        initCredentialsProvider();
    }
    
    /**
     * @deprecated use {@link DISClientBuilder#defaultClient()}
     */
    public AbstractDISClient()
    {
        this.disConfig = DISConfig.buildDefaultConfig();
        init();
    }
    
    protected Request<HttpRequest> buildRequest(HttpMethodName httpMethod, String endpoint, String resourcePath){
    	Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(httpMethod);
        
        request.setResourcePath(resourcePath);
        setEndpoint(request, endpoint);
        
        return request;
    }
    
    /**
     * Decorate {@link PutRecordsRequest} before sending HTTP Request.
     * 
     * @param putRecordsParam A <code>PutRecords</code> request.
     * @return A <code>PutRecords</code> request after decorating.
     */
    protected PutRecordsRequest decorateRecords(PutRecordsRequest putRecordsParam)
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
    protected GetRecordsResult decorateRecords(GetRecordsResult getRecordsResult)
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
    
    protected boolean isEncrypt()
    {
        return disConfig.getIsDefaultDataEncryptEnabled() && !StringUtils.isNullOrEmpty(disConfig.getDataPassword());
    }
    
    protected ByteBuffer encrypt(ByteBuffer src)
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
    
    protected ByteBuffer decrypt(ByteBuffer cipher)
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

    private void beforeRequest(Request<HttpRequest> request, Object requestContent, String region, String projectId, String securityToken)
    {
        request.addHeader(HTTP_X_PROJECT_ID, projectId);
        
        if (!StringUtils.isNullOrEmpty(securityToken))
        {
            request.addHeader(HTTP_X_SECURITY_TOKEN, securityToken);
        }
    	
        // set request header
        setContentType(request);
        setSdkVersion(request);
        
        // set request parameters
        setParameters(request, requestContent);
        
        // set request content
        setContent(request, requestContent);
    }
    
    private void setContent(Request<HttpRequest> request, Object requestContent)
    {
        HttpMethodName methodName = request.getHttpMethod();
        if (methodName.equals(HttpMethodName.POST) || methodName.equals(HttpMethodName.PUT))
        {
            if (requestContent instanceof byte[])
            {
                request.setContent(new ByteArrayInputStream((byte[])requestContent));
            }
            else if (requestContent instanceof String || requestContent instanceof Integer)
            {
                request.setContent(new ByteArrayInputStream(Utils.encodingBytes(requestContent.toString())));
            }
            else
            {
                String reqJson = JsonUtils.objToJson(requestContent);
                request.setContent(new ByteArrayInputStream(Utils.encodingBytes(reqJson)));
            }
        }
    }
    
    private void setContentType(Request<HttpRequest> request)
    {
        // 默认为json格式
        if (!request.getHeaders().containsKey("Content-Type"))
        {
            request.addHeader("Content-Type", "application/json; charset=utf-8");
        }
        
        if (!request.getHeaders().containsKey("accept"))
        {
            request.addHeader("accept", "*/*; charset=utf-8");
        }
    }
    
    private void setSdkVersion(Request<HttpRequest> request)
    {
        request.addHeader(HEADER_SDK_VERSION, VersionUtils.getVersion() + "/" + VersionUtils.getPlatform());
    }
    
    private void setParameters(Request<HttpRequest> request, Object requestContent)
    {
        if (request.getHttpMethod().equals(HttpMethodName.GET))
        {
            if (requestContent != null)
            {
                Map<String, String> parametersMap = new HashMap<String, String>();
                Map<String, Object> getParamsObj = null;
                if (requestContent instanceof Map)
                {
                    getParamsObj = (Map<String, Object>)requestContent;
                }
                else
                {
                    String tmpJson = JsonUtils.objToJson(requestContent);
                    getParamsObj = JsonUtils.jsonToObj(tmpJson, HashMap.class);
                }

                if (getParamsObj.size() != 0)
                {
                    for (Map.Entry<String, Object> temp : getParamsObj.entrySet())
                    {
                        Object value = temp.getValue();
                        if (value == null)
                        {
                            continue;
                        }

                        parametersMap.put(temp.getKey(), String.valueOf(value));
                    }
                }
                
                if (null != parametersMap && parametersMap.size() > 0)
                {
                    request.setParameters(parametersMap);
                }
            }
        }
        
    }
    
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
        
        beforeRequest(request, param, region, disConfig.getProjectId(), credentials.getSecurityToken());
        
        // 发送请求
        return doRequest(request, param, credentials.getAccessKeyId(), credentials.getSecretKey(), region, clazz);
    }
    
    protected <T> Future<T> requestAsync(Object param, Request<HttpRequest> request, Class<T> clazz, AsyncHandler<T> callback)
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
        
        beforeRequest(request, param, region, disConfig.getProjectId(), credentials.getSecurityToken());
        
        return doRequestAsync(request, param, credentials.getAccessKeyId(), credentials.getSecretKey(), region, clazz, callback);
    }

	private <T> Future<T> doRequestAsync(Request<HttpRequest> request, Object requestContent, String ak, String sk,
			String region, Class<T> returnType, AsyncHandler<T> callback) {
		String uri = buildURI(request);
		request = SignUtil.sign(request, ak, sk, region);
		
		ConnectRetryFuture<T> connectRetryFuture = new ConnectRetryFuture<T>(request, ak, sk, requestContent, callback, uri, returnType);
		
		ConnectRetryCallback<T> connectRetryCallback = null;
		if(callback != null){
			connectRetryCallback = new ConnectRetryCallback<T>(callback, connectRetryFuture, 0);
		}
		
        Future<T> restFuture = RestClientAsync.getInstance(disConfig).exchangeAsync(uri, 
        		request.getHttpMethod(), request.getHeaders(), requestContent, returnType, connectRetryCallback);
        
        connectRetryFuture.setInnerFuture(restFuture);
        
        return connectRetryFuture;
	}
	
	private static class ConnectRetryCallback<T> extends AbstractCallbackAdapter<T, T> implements AsyncHandler<T>{
		private final int retryIndex;
		
		public ConnectRetryCallback(AsyncHandler<T> innerAsyncHandler, AbstractFutureAdapter<T, T> futureAdapter, int retryIndex) {
			super(innerAsyncHandler, futureAdapter);
			this.retryIndex = retryIndex;
		}

		@Override
		protected T toInnerT(T result) {
			return result;
		}
		
		@Override
		public void onError(Exception exception) {
			ConnectRetryFuture<T> connectRetryFuture = (ConnectRetryFuture<T>) futureAdapter;
			try {
				connectRetryFuture.retryHandle(exception, true, retryIndex);
			} catch (Exception e) {
				super.onError(e);
			}
		}
		
	}
	
	private class ConnectRetryFuture<T> extends AbstractFutureAdapter<T, T> implements Future<T> {
		//为了避免future.get和callback的各种并发情况下的重复重试，使用该锁和计数进行控制
		private AtomicInteger retryCount = new AtomicInteger();
		private ReentrantLock retryLock = new ReentrantLock();
		
		private volatile Request<HttpRequest> request;
		private String ak;
		private String sk;
		private Object requestContent;
		private AsyncHandler<T> callback;
		private String uri;
		private Class<T> returnType;

		public ConnectRetryFuture(Request<HttpRequest> request,
		        String ak,
		        String sk,
		        Object requestContent,
		        AsyncHandler<T> callback,
		        String uri,
		        Class<T> returnType){
		            super();
		            this.request = request;
		            this.ak = ak;
		            this.sk = sk;
		            this.requestContent = requestContent;
		            this.callback = callback;
		            this.uri = uri;
		            this.returnType = returnType;
		        }
		
		public void retryHandle(Throwable t, boolean tryLock, int retryIndex) throws ExecutionException, InterruptedException{
			String errorMsg = t.getMessage();
            if (t instanceof UnknownHttpStatusCodeException || t instanceof HttpStatusCodeException)
            {
                errorMsg = ((RestClientResponseException)t).getRawStatusCode() + " : "
                    + ((RestClientResponseException)t).getResponseBodyAsString();
            }
            
            // 如果不是可以重试的异常 或者 已达到重试次数，则直接抛出异常
            if (!AbstractDISClient.this.isRetriableSendException(t, request) || retryIndex >= disConfig.getExceptionRetries())
            {
            	throw new DISClientException(errorMsg, t);
            }
            
            if(tryLock) {
            	if(!retryLock.tryLock()) {
            		return;
            	}
            }else {
            	retryLock.lock();
            }
            
            try {
            	if(retryIndex != retryCount.get()) {
            		return;
            	}
            	
            	int tmpRetryIndex = retryCount.incrementAndGet();
                
                request.getHeaders().remove(SignerConstants.AUTHORIZATION);
                request = SignUtil.sign(request, ak, sk, region);
                
                ConnectRetryCallback<T> connectRetryCallback = null;
                if(callback != null){
                	connectRetryCallback = new ConnectRetryCallback<T>(callback, this, tmpRetryIndex);
                }
                
                Future<T> restFuture = RestClientAsync.getInstance(disConfig).exchangeAsync(uri, 
                		request.getHttpMethod(), request.getHeaders(), requestContent, returnType, connectRetryCallback);
                
                this.setInnerFuture(restFuture);
            }finally {
            	retryLock.unlock();
            }
            
        }

		@Override
		public T get() throws InterruptedException, ExecutionException {
			retryLock.lock();
            
            int getThreadRetryIndex = retryCount.get();
            try
            {
                return super.get();
            }
            catch (ExecutionException e)
            {
                if (e.getCause() == null)
                {
                    throw e;
                }
                retryHandle(e.getCause(), false, getThreadRetryIndex);
                return this.get();
            }
            finally
            {
                retryLock.unlock();
            }
		}
		
		@Override
		public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            retryLock.lock();
            
            int getThreadRetryIndex = retryCount.get();
            try
            {
                return super.get(timeout, unit);
            }
            catch (ExecutionException e)
            {
                if (e.getCause() == null)
                {
                    throw e;
                }
                retryHandle(e.getCause(), false, getThreadRetryIndex);
                return this.get(timeout, unit);
            }
            finally
            {
                retryLock.unlock();
            }
        
		}
		
		@Override
		protected T toT(T innerT) {
			return innerT;
		}
	}
	
	private String buildURI(Request<HttpRequest> request){
		Map<String, String> parameters = request.getParameters();
        
        StringBuilder uri = new StringBuilder(request.getEndpoint().toString()).append(request.getResourcePath());
            
        // Set<String> paramKeys = getParams.keySet();
        if (parameters != null && !parameters.isEmpty())
        {
            uri.append("?");
            for (Map.Entry<String, String> temp : parameters.entrySet())
            {
                uri.append(temp.getKey());
                uri.append("=");
                uri.append(temp.getValue());
                uri.append("&");
            }
        }
        
        return uri.toString();
	}
	
 // 将Request转为restTemplate的请求参数.由于请求需要签名，故请求的body直接传byte[]，响应的反序列化，可以直接利用spring的messageConvert机制
    private <T> T doRequest(Request<HttpRequest> request, Object requestContent, String ak, String sk, String region,
        Class<T> returnType)
    {
    	String uri = buildURI(request);
        
        int retryCount = -1;
        ExponentialBackOff backOff = null;
        do
        {
            retryCount++;
            if (retryCount > 0)
            {
                // 等待一段时间再发起重试
                if (backOff == null)
                {
                    backOff = new ExponentialBackOff(250, 2.0, disConfig.getBackOffMaxIntervalMs(),
                        ExponentialBackOff.DEFAULT_MAX_ELAPSED_TIME);
                }
                backOff.backOff(backOff.getNextBackOff());
            }
            
            try
            {
                request.getHeaders().remove(SignerConstants.AUTHORIZATION);
                // 每次重传需要重新签名
                request = SignUtil.sign(request, ak, sk, region);
                return RestClient.getInstance(disConfig).exchange(uri, 
                		request.getHttpMethod(), request.getHeaders(), requestContent, returnType);
            }
            catch (Throwable t)
            {
                String errorMsg = t.getMessage();
                if (t instanceof UnknownHttpStatusCodeException || t instanceof HttpStatusCodeException)
                {
                    errorMsg = ((RestClientResponseException)t).getRawStatusCode() + " : "
                        + ((RestClientResponseException)t).getResponseBodyAsString();
                }
                
                // 如果不是可以重试的异常 或者 已达到重试次数，则直接抛出异常
                if (!isRetriableSendException(t, request) || retryCount >= disConfig.getExceptionRetries())
                {
                    throw new DISClientException(errorMsg, t);
                }
                LOG.warn("Find Retriable Exception [{}], url [{} {}], currRetryCount is {}",
                    errorMsg.replaceAll("[\\r\\n]", ""),
                    request.getHttpMethod(),
                    uri.toString(),
                    retryCount);
            }
        } while (retryCount < disConfig.getExceptionRetries());
        
        return null;
    }
    
    /**
	 * 判断此异常是否可以重试
	 *
	 * @param t
	 *            throwable exception
	 * @param request
     * @return {@code true} retriable {@code false} not retriable
	 */
    protected boolean isRetriableSendException(Throwable t, Request<HttpRequest> request)
    {
        // 对于连接超时/网络闪断/Socket异常/服务端5xx错误进行重试
        return t instanceof ConnectTimeoutException || t instanceof NoHttpResponseException
                || t instanceof SocketException || t instanceof SSLException
                || (t instanceof SocketTimeoutException && request.getHttpMethod() == HttpMethodName.GET)
                || (t instanceof RestClientResponseException && ((RestClientResponseException) t).getRawStatusCode() / 100 == 5)
                || (t.getCause() != null && isRetriableSendException(t.getCause(), request));
    }

    protected void check()
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
    
    protected void setEndpoint(Request<HttpRequest> request, String endpointStr)
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
    
    protected PutRecordsRequest toPutRecordsRequest(PutRecordRequest putRecordRequest)
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
    
    protected PutRecordResult toPutRecordResult(PutRecordsResult putRecordsResult)
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

    /**
     * 开放认证修改接口，用户自定义实现ICredentialsProvider，更新认证信息
     */
    protected void initCredentialsProvider()
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

    protected boolean isRecordsRetriableErrorCode(String errorCode)
    {
        for (String item : disConfig.getRecordsRetriesErrorCode())
        {
            if (errorCode.contains(item))
            {
                return true;
            }
        }
        return false;
    }
}
