package com.huaweicloud.dis.http;

import com.huaweicloud.dis.Constants;
import com.huaweicloud.dis.DISClientBuilder;
import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.DISCredentials;
import com.huaweicloud.dis.core.DefaultRequest;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.auth.AuthType;
import com.huaweicloud.dis.core.auth.signer.internal.SignerConstants;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.exception.*;
import com.huaweicloud.dis.http.exception.HttpStatusCodeException;
import com.huaweicloud.dis.http.exception.RestClientResponseException;
import com.huaweicloud.dis.http.exception.UnknownHttpStatusCodeException;
import com.huaweicloud.dis.iface.common.ErrorMessage;
import com.huaweicloud.dis.iface.data.request.PutRecordRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.*;
import com.huaweicloud.dis.util.*;
import com.huaweicloud.dis.util.compress.Lz4Util;
import com.huaweicloud.dis.util.compress.ZstdUtil;
import com.huaweicloud.dis.util.config.ICredentialsProvider;
import com.huaweicloud.dis.util.encrypt.EncryptUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLException;
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

public class AbstractDISClient {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDISClient.class);

    protected static final String HTTP_X_PROJECT_ID = "X-Project-Id";

    protected static final String HTTP_X_SECURITY_TOKEN = "X-Security-Token";

    protected static final String HEADER_SDK_VERSION = "X-SDK-Version";

    protected String region;

    protected DISConfig disConfig;

    protected DISCredentials credentials;

    protected ICredentialsProvider credentialsProvider;

    public AbstractDISClient(DISConfig disConfig) {
        this.disConfig = DISConfig.buildConfig(disConfig);
        init();
    }

    private void init() {
        this.credentials = new DISCredentials(this.disConfig);
        this.region = this.disConfig.getRegion();
        check();
        initCredentialsProvider();
    }

    /**
     * @deprecated use {@link DISClientBuilder#defaultClient()}
     */
    public AbstractDISClient() {
        this.disConfig = DISConfig.buildDefaultConfig();
        init();
    }

    protected Request<HttpRequest> buildRequest(HttpMethodName httpMethod, String endpoint, String resourcePath) {
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
    protected PutRecordsRequest decorateRecords(PutRecordsRequest putRecordsParam) {
        // compress with snappy-java
        if (disConfig.isDataCompressEnabled()) {
            if (putRecordsParam.getRecords() != null) {
                for (PutRecordsRequestEntry record : putRecordsParam.getRecords()) {
                    byte[] input = record.getData().array();
                    try {
                        byte[] compressedInput = SnappyUtils.compress(input);
                        record.setData(ByteBuffer.wrap(compressedInput));
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        // encrypt
        if (isEncrypt()) {
            if (putRecordsParam.getRecords() != null) {
                for (PutRecordsRequestEntry record : putRecordsParam.getRecords()) {
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
    protected GetRecordsResult decorateRecords(GetRecordsResult getRecordsResult) {
        // decrypt
        if (isEncrypt()) {
            if (getRecordsResult.getRecords() != null) {
                for (Record record : getRecordsResult.getRecords()) {
                    record.setData(decrypt(record.getData()));
                }
            }
        }

        // uncompress with snappy-java
        if (disConfig.isDataCompressEnabled()) {
            if (getRecordsResult.getRecords() != null) {
                for (Record record : getRecordsResult.getRecords()) {
                    byte[] input = record.getData().array();
                    try {
                        byte[] uncompressedInput = SnappyUtils.uncompress(input);
                        record.setData(ByteBuffer.wrap(uncompressedInput));
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return getRecordsResult;
    }

    protected boolean isEncrypt() {
        return disConfig.getIsDefaultDataEncryptEnabled() && !StringUtils.isNullOrEmpty(disConfig.getDataPassword());
    }

    protected ByteBuffer encrypt(ByteBuffer src) {
        String cipher = null;
        try {
            cipher = EncryptUtils.gen(new String[]{disConfig.getDataPassword()}, src.array());
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return ByteBuffer.wrap(cipher.getBytes());
    }

    protected ByteBuffer decrypt(ByteBuffer cipher) {
        Charset utf8 = Charset.forName("UTF-8");
        String src;
        try {
            src = EncryptUtils.dec(new String[]{disConfig.getDataPassword()}, new String(cipher.array(), utf8));
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return ByteBuffer.wrap(src.getBytes(utf8));
    }

    private byte[] beforeRequest(Request<HttpRequest> request, Object requestContent, String region, String projectId, String securityToken) {
        request.addHeader(HTTP_X_PROJECT_ID, projectId);

        if (!StringUtils.isNullOrEmpty(securityToken)) {
            request.addHeader(HTTP_X_SECURITY_TOKEN, securityToken);
        }

        // set request header
        setContentType(request);
        setSdkVersion(request);

        // set request parameters
        setParameters(request, requestContent);

        // set request content
        return setContent(request, requestContent);
    }

    private byte[] setContent(Request<HttpRequest> request, Object requestContent) {
        HttpMethodName methodName = request.getHttpMethod();
        if (methodName.equals(HttpMethodName.POST) || methodName.equals(HttpMethodName.PUT)) {

            byte[] content = null;
            if (requestContent instanceof byte[]) {
                content = (byte[]) requestContent;
            } else if (requestContent instanceof String || requestContent instanceof Integer) {
                content = Utils.encodingBytes(requestContent.toString());
            } else {
                String reqJson = JsonUtils.objToJson(requestContent);
                content = Utils.encodingBytes(reqJson);
            }

            content = compressBody(request, content);

            request.setContent(new ByteArrayInputStream(content));

            return content;
        }
        return null;
    }

    private byte[] compressBody(Request<HttpRequest> request, byte[] source) {
        if (!disConfig.getBoolean(DISConfig.PROPERTY_BODY_COMPRESS_ENABLED, false)) {
            return source;
        }

        String compressType = disConfig.get(DISConfig.PROPERTY_BODY_COMPRESS_TYPE, Constants.COMPRESS_LZ4);

        byte[] target = null;
        if (Constants.COMPRESS_LZ4.equals(compressType)) {
            request.addHeader("Content-Encoding", Constants.COMPRESS_LZ4);
            request.addHeader("Accept-Encoding", Constants.COMPRESS_LZ4);
            request.addHeader(Constants.COMPRESS_LZ4_CONTENT_LENGTH, String.valueOf(source.length));
            target = Lz4Util.compressedByte(source);
        } else if (Constants.COMPRESS_SNAPPY.equals(compressType)) {
            request.addHeader("Content-Encoding", Constants.COMPRESS_SNAPPY);
            request.addHeader("Accept-Encoding", Constants.COMPRESS_SNAPPY);
            try {
                target = SnappyUtils.compress(source);
            } catch (IOException e) {
                throw new DISClientException(e);
            }
        } else if (Constants.COMPRESS_ZSTD.equals(compressType)) {
            request.addHeader("Content-Encoding", Constants.COMPRESS_ZSTD);
            request.addHeader("Accept-Encoding", Constants.COMPRESS_ZSTD);
            request.addHeader(Constants.COMPRESS_ZSTD_CONTENT_LENGTH, String.valueOf(source.length));
            target = ZstdUtil.compressedByte(source);
        }

        return target;
    }

    private void setContentType(Request<HttpRequest> request) {
        // 默认为json格式
        if (!request.getHeaders().containsKey("Content-Type")) {
            request.addHeader("Content-Type", "application/json; charset=utf-8");
        }

        if (!request.getHeaders().containsKey("accept")) {
            request.addHeader("accept", "*/*; charset=utf-8");
        }
    }

    private void setSdkVersion(Request<HttpRequest> request) {
        request.addHeader(HEADER_SDK_VERSION, VersionUtils.getVersion() + "/" + VersionUtils.getPlatform());
    }

    private void setParameters(Request<HttpRequest> request, Object requestContent) {
        if (request.getHttpMethod().equals(HttpMethodName.GET) || request.getHttpMethod().equals(HttpMethodName.DELETE)) {
            if (requestContent != null) {
                Map<String, String> parametersMap = new HashMap<String, String>();
                Map<String, Object> getParamsObj = null;
                if (requestContent instanceof Map) {
                    getParamsObj = (Map<String, Object>) requestContent;
                } else {
                    String tmpJson = JsonUtils.objToJson(requestContent);
                    getParamsObj = JsonUtils.jsonToObj(tmpJson, HashMap.class);
                }

                if (getParamsObj.size() != 0) {
                    for (Map.Entry<String, Object> temp : getParamsObj.entrySet()) {
                        Object value = temp.getValue();
                        if (value == null) {
                            continue;
                        }

                        parametersMap.put(temp.getKey(), String.valueOf(value));
                    }
                }

                if (null != parametersMap && parametersMap.size() > 0) {
                    request.setParameters(parametersMap);
                }
            }
        }

    }

    protected <T> T request(Object param, Request<HttpRequest> request, Class<T> clazz) {
        DISCredentials credentials = this.credentials;
        if (credentialsProvider != null) {
            DISCredentials cloneCredentials = this.credentials.clone();
            credentials = credentialsProvider.updateCredentials(cloneCredentials);
            if (credentials != cloneCredentials) {
                this.credentials = credentials;
            }
        }

        byte[] bodyBytes = beforeRequest(request, param, region, disConfig.getProjectId(), credentials.getSecurityToken());

        if (credentials.getAuthToken() == null) {
            // 发送请求--通过ak sk
            return doRequest(request, bodyBytes, credentials.getAccessKeyId(), credentials.getSecretKey(), region, clazz);
        } else { //发送请求--通过x-auth-token
            return doRequest(request, bodyBytes, credentials.getAuthToken(), region, clazz);
        }

    }

    protected <T> Future<T> requestAsync(Object param, Request<HttpRequest> request, Class<T> clazz, AsyncHandler<T> callback) {
        DISCredentials credentials = this.credentials;
        if (credentialsProvider != null) {
            DISCredentials cloneCredentials = this.credentials.clone();
            credentials = credentialsProvider.updateCredentials(cloneCredentials);
            if (credentials != cloneCredentials) {
                this.credentials = credentials;
            }
        }

        byte[] bodyBytes = beforeRequest(request, param, region, disConfig.getProjectId(), credentials.getSecurityToken());

        return doRequestAsync(request, bodyBytes, credentials.getAccessKeyId(), credentials.getSecretKey(), region, clazz, callback);
    }

    private <T> Future<T> doRequestAsync(Request<HttpRequest> request, Object requestContent, String ak, String sk,
                                         String region, Class<T> returnType, AsyncHandler<T> callback) {
        String uri = buildURI(request);

        request.getHeaders().remove(SignerConstants.AUTHORIZATION);
        request = SignUtil.sign(request, ak, sk, region, disConfig);

        ConnectRetryFuture<T> connectRetryFuture = new ConnectRetryFuture<T>(request, ak, sk, requestContent, callback, uri, returnType);

        ConnectRetryCallback<T> connectRetryCallback = null;
        if (callback != null) {
            connectRetryCallback = new ConnectRetryCallback<T>(callback, connectRetryFuture, 0);
        }

        Future<T> restFuture = RestClientAsync.getInstance(disConfig).exchangeAsync(uri,
                request.getHttpMethod(), request.getHeaders(), requestContent, returnType, connectRetryCallback);

        connectRetryFuture.setInnerFuture(restFuture);

        return connectRetryFuture;
    }

    private static class ConnectRetryCallback<T> extends AbstractCallbackAdapter<T, T> implements AsyncHandler<T> {
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
        public void onError(Exception exception) throws Exception {
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
                                  Class<T> returnType) {
            super();
            this.request = request;
            this.ak = ak;
            this.sk = sk;
            this.requestContent = requestContent;
            this.callback = callback;
            this.uri = uri;
            this.returnType = returnType;
        }

        public void retryHandle(Throwable t, boolean tryLock, int retryIndex) throws ExecutionException, InterruptedException {
            String errorMsg = t.getMessage();
            if (t instanceof UnknownHttpStatusCodeException || t instanceof HttpStatusCodeException) {
                errorMsg = ((RestClientResponseException) t).getRawStatusCode() + " : "
                        + ((RestClientResponseException) t).getResponseBodyAsString();
            }

            // 如果不是可以重试的异常 或者 已达到重试次数，则直接抛出异常
            boolean isRetriable = isRetriableSendException(t, request);
            if (!isRetriable || retryIndex >= disConfig.getExceptionRetries()) {
                handleError(t, errorMsg, isRetriable);
            }

            // 重试
            if (tryLock) {
                if (!retryLock.tryLock()) {
                    return;
                }
            } else {
                retryLock.lock();
            }

            try {
                if (retryIndex != retryCount.get()) {
                    return;
                }

                int tmpRetryIndex = retryCount.incrementAndGet();

                request.getHeaders().remove(SignerConstants.AUTHORIZATION);
                request = SignUtil.sign(request, ak, sk, region, disConfig);

                ConnectRetryCallback<T> connectRetryCallback = null;
                if (callback != null) {
                    connectRetryCallback = new ConnectRetryCallback<T>(callback, this, tmpRetryIndex);
                }

                LOG.warn("connect or system error retry [{}] [{}] [{}]", this.hashCode(), retryIndex, errorMsg);
                Future<T> restFuture = RestClientAsync.getInstance(disConfig).exchangeAsync(uri,
                        request.getHttpMethod(), request.getHeaders(), requestContent, returnType, connectRetryCallback);

                this.setInnerFuture(restFuture);
            } finally {
                retryLock.unlock();
            }

        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            retryLock.lock();

            int getThreadRetryIndex = retryCount.get();
            try {
                return super.get();
            } catch (ExecutionException e) {
                if (e.getCause() == null) {
                    throw e;
                }
                retryHandle(e.getCause(), false, getThreadRetryIndex);
                return this.get();
            } finally {
                retryLock.unlock();
            }
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            retryLock.lock();

            int getThreadRetryIndex = retryCount.get();
            try {
                return super.get(timeout, unit);
            } catch (ExecutionException e) {
                if (e.getCause() == null) {
                    throw e;
                }
                retryHandle(e.getCause(), false, getThreadRetryIndex);
                return this.get(timeout, unit);
            } finally {
                retryLock.unlock();
            }

        }

        @Override
        protected T toT(T innerT) {
            return innerT;
        }
    }

    private String buildURI(Request<HttpRequest> request) {
        Map<String, String> parameters = request.getParameters();

        StringBuilder uri = new StringBuilder(request.getEndpoint().toString()).append(request.getResourcePath());

        // Set<String> paramKeys = getParams.keySet();
        if (parameters != null && !parameters.isEmpty()) {
            uri.append("?");
            for (Map.Entry<String, String> temp : parameters.entrySet()) {
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
                            Class<T> returnType) {
        String uri = buildURI(request);
        LOG.info("url:" + uri);
        int retryCount = -1;
        ExponentialBackOff backOff = null;
        do {
            retryCount++;
            if (retryCount > 0) {
                // 等待一段时间再发起重试
                if (backOff == null) {
                    backOff = new ExponentialBackOff(250, 2.0, disConfig.getBackOffMaxIntervalMs(),
                            ExponentialBackOff.DEFAULT_MAX_ELAPSED_TIME);
                }
                backOff.backOff(backOff.getNextBackOff());
            }

            try {
                request.getHeaders().remove(SignerConstants.AUTHORIZATION);
                // 每次重传需要重新签名
                request = SignUtil.sign(request, ak, sk, region, disConfig);
                //request.addHeader("X-Auth-Token","MIITVwYJKoZIhvcNAQcCoIITSDCCE0QCAQExDTALBglghkgBZQMEAgEwghFnBgkqhkiG9w0BBwGgghFYBIIRVHsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjAtMDQtMTlUMDg6MDI6MDguNTM1MDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZyI6W10sInJvbGVzIjpbeyJuYW1lIjoidGVfYWRtaW4iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDQxNSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZl9ub2RlZ3JvdXAiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yZHNfZ2F1c3NkYnQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ldnNfZXNzZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX21lZXRpbmdfZW5kcG9pbnRfYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdGVzdDA0MjIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zaXNfc2Fzcl9lbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29wX2dhdGVkX2lydGMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDQyMCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Rlc3QwNDIxIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZXZzX3ZvbHVtZV9yZWN5Y2xlX2JpbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FwcF9jb25zaXN0ZW5jeSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2N2ciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3drcy1rcCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29jc21hcnRjYW1wdXMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tdWx0aV9iaW5kIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbmxwX2x1X3NhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbmxwX210IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfd3NkLXRlc3QwMzEyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2NpX2FybSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZl9mdW5jdGlvbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzYnNfY2xlYXJfc25hcHNob3QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9EZXJlZ2lzdHJhdGlvbkFwcGxpY2F0aW9uIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfc2VydmljZV90aWNrZXRzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfd2hpdGVsaXN0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX3hzZXJ2ZXIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ubHBfbGdfdHMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9Jb1RQcm92aXNpb25pbmciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zZnN0dXJibyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Jkc2kzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfSUVDIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfb3JvYXMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zaXNfYXNzZXNzX211bHRpbW9kZWwiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92cGNfdnBjZXB0b29icyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX25scF9sZ190ZyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3NlcnZpY2VzdGFnZV9tZ3JfZHRtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW90ZWRnZV9iYXNpYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Rlc3QwNDA5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfTW9kZWxBcnRzQXNjZW5kOTEwIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYmJiYiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX21lZXRpbmdfaGlzdG9yeV9jdXN0b21fYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfc2VydmljZXN0YWdlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaWNzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdGVzdDA0MDciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yaV9kd3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yZWYiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWNzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdmd2YXMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9haWNzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdGVzdDA0MTQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9nZW1pbmlkYl9hcm0iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWZfZWRnZW1lc2giLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ibGFja2xpc3QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zaXNfYXNzZXNzX2F1ZGlvIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfSVdCX05PUklHSFQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9haWNtcy5rcGkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92aXBfYmFuZHdpZHRoIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfb3BfZ2F0ZWRfZGRzX2FybSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29jZWFuYm9vc3RlciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29wX2dhdGVkX3JpX2R3cyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VkY20iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jc2JzX3Jlc3RvcmUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF96aGFvemhvbmd0ZXRzMDAyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaXB2Nl9kdWFsc3RhY2siLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92cG5fdmd3IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGxpX2FybV9wb2MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pcnRjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcGNhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcmRzX2FybSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2xlZ2FjeSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19jNnhfdmlydGlvX25ldCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2hpbGVucyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2V2c19wb29sX2NhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYWFhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbmxwX2x1X3RjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWVldGluZ19jbG91ZF9idXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jYnNzcCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VwcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2ludGVyYWN0aXZld2hpdGVib2FyZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3NlcnZpY2VzdGFnZV9tZ3JfYXJtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfV2VMaW5rX2VuZHBvaW50X2J1eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3F1aWNrYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW90YW5hbHl0aWNzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWF4aHViX2VuZHBvaW50X2J1eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NjZV9kdWFsc3RhY2siLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ubHBfa2ciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWZfZGV2aWNlX2RpcmVjdCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3ZpcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3VybGZpbHRlciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzX2FybV9wb2MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zdXBwb3J0X3BsYW4iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDQwOTIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDQwOTEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDQwOTQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90ZXN0MDQwOTMiLCJpZCI6IjAifV0sInByb2plY3QiOnsiZG9tYWluIjp7Im5hbWUiOiJlaV9kaXNfZDAwMzUyMjIxXzA5IiwiaWQiOiIzYjNmMjM3MTIyNTc0MDY1OGI3NDQ4MmFlMTEwMDViYSJ9LCJuYW1lIjoiY24tbm9ydGgtNyIsImlkIjoiMjVkNGVhOTRhMmMxNDY4OGFmYzUxMTRmZTZiMzMzZGYifSwiaXNzdWVkX2F0IjoiMjAyMC0wNC0xOFQwODowMjowOC41MzUwMDBaIiwidXNlciI6eyJkb21haW4iOnsibmFtZSI6ImVpX2Rpc19kMDAzNTIyMjFfMDkiLCJpZCI6IjNiM2YyMzcxMjI1NzQwNjU4Yjc0NDgyYWUxMTAwNWJhIn0sIm5hbWUiOiJlaV9kaXNfZDAwMzUyMjIxXzA5IiwicGFzc3dvcmRfZXhwaXJlc19hdCI6IiIsImlkIjoiZDM5ZjEzN2ZiMzdhNDgyMTk4MzJhNjJjZDkwM2U1ZjcifX19MYIBwzCCAb8CAQEwgZkwgYsxCzAJBgNVBAYTAkNOMRIwEAYDVQQIDAlHdWFuZ0RvbmcxETAPBgNVBAcMCFNoZW5aaGVuMS4wLAYDVQQKDCVIdWF3ZWkgU29mdHdhcmUgVGVjaG5vbG9naWVzIENvLiwgTHRkMQ4wDAYDVQQLDAVDbG91ZDEVMBMGA1UEAwwMY2E2Ni5wa2kuaWFtAgkA+GWAA0mEWfAwCwYJYIZIAWUDBAIBMA0GCSqGSIb3DQEBAQUABIIBAEBMNM+DcOvsWmZV8CBMkj9ecx6yYUWsOqTbjIaDmLjP3k56913w5mMl2A91xNUiktvHyScIHyL4fkTpFch3V8QllrTGWCfdpOqPeJ0uO2eDzFm1ViZor3UVyLto-wmTO2d54xH3g2k8kynR1ZdzgH+VjATIwiCAcklI0XOfpGCWKBrc-3HFwWtUejdRYevUWL4nUOCA5wyXUDIL7eqb7Ig0VMcOzNhRt1MGjWdYypY7lzbkf0DJGRXOZig7bkqQmIOHGhjI2YD-OEW+luNFQzKtT+ro38BlC2SVc-+QwwJsR--sJXksoGSKFseNiabdZUP1vlIRWHGBR-QXiQYZxDo=");
                return RestClient.getInstance(disConfig).exchange(uri,
                        request.getHttpMethod(), request.getHeaders(), requestContent, returnType);
            } catch (Throwable t) {
                String errorMsg = t.getMessage();
                if (t instanceof UnknownHttpStatusCodeException || t instanceof HttpStatusCodeException) {
                    errorMsg = ((RestClientResponseException) t).getRawStatusCode() + " : "
                            + ((RestClientResponseException) t).getResponseBodyAsString();
                }

                // 如果不是可以重试的异常 或者 已达到重试次数，则直接抛出异常
                boolean isRetriable = isRetriableSendException(t, request);
                if (!isRetriable || retryCount >= disConfig.getExceptionRetries()) {
                    handleError(t, errorMsg, isRetriable);
                }

                LOG.warn("Find Retriable Exception [{}], url [{} {}], currRetryCount is {}",
                        errorMsg.replaceAll("[\\r\\n]", ""),
                        request.getHttpMethod(),
                        uri,
                        retryCount);
            }
        } while (retryCount < disConfig.getExceptionRetries());

        return null;
    }

    //通过X-Auth-Token请求数据
    private <T> T doRequest(Request<HttpRequest> request, Object requestContent, String authToken, String region,
                            Class<T> returnType) {
        String uri = buildURI(request);
        LOG.info("authToken:" + authToken);
        int retryCount = -1;
        ExponentialBackOff backOff = null;
        do {
            retryCount++;
            if (retryCount > 0) {
                // 等待一段时间再发起重试
                if (backOff == null) {
                    backOff = new ExponentialBackOff(250, 2.0, disConfig.getBackOffMaxIntervalMs(),
                            ExponentialBackOff.DEFAULT_MAX_ELAPSED_TIME);
                }
                backOff.backOff(backOff.getNextBackOff());
            }

            try {
                /*request.getHeaders().remove(SignerConstants.AUTHORIZATION);
                // 每次重传需要重新签名
                request = SignUtil.sign(request, ak, sk, region,disConfig);*/

                request.addHeader("X-Auth-Token", authToken);
                return RestClient.getInstance(disConfig).exchange(uri,
                        request.getHttpMethod(), request.getHeaders(), requestContent, returnType);
            } catch (Throwable t) {
                String errorMsg = t.getMessage();
                if (t instanceof UnknownHttpStatusCodeException || t instanceof HttpStatusCodeException) {
                    errorMsg = ((RestClientResponseException) t).getRawStatusCode() + " : "
                            + ((RestClientResponseException) t).getResponseBodyAsString();
                }

                // 如果不是可以重试的异常 或者 已达到重试次数，则直接抛出异常
                boolean isRetriable = isRetriableSendException(t, request);
                if (!isRetriable || retryCount >= disConfig.getExceptionRetries()) {
                    handleError(t, errorMsg, isRetriable);
                }

                LOG.warn("Find Retriable Exception [{}], url [{} {}], currRetryCount is {}",
                        errorMsg.replaceAll("[\\r\\n]", ""),
                        request.getHttpMethod(),
                        uri,
                        retryCount);
            }
        } while (retryCount < disConfig.getExceptionRetries());

        return null;
    }

    /**
     * 判断此异常是否可以重试
     *
     * @param t       throwable exception
     * @param request HttpReuest
     * @return {@code true} retriable {@code false} not retriable
     */
    protected boolean isRetriableSendException(Throwable t, Request<HttpRequest> request) {
        // 对于连接超时/网络闪断/Socket异常/服务端5xx错误进行重试
        return t instanceof ConnectTimeoutException || t instanceof NoHttpResponseException
                || t instanceof HttpHostConnectException || t instanceof SocketException || t instanceof SSLException
                || (t instanceof SocketTimeoutException && request.getHttpMethod() == HttpMethodName.GET)
                || (t instanceof RestClientResponseException && ((RestClientResponseException) t).getRawStatusCode() / 100 == 5)
                || isRetriableErrorCodeException(t)
                || (t.getCause() != null && isRetriableSendException(t.getCause(), request));
    }

    /**
     * 根据异常中响应的DIS错误码来判断此异常是否可以重试
     *
     * @param t throwable exception
     * @return
     */
    protected boolean isRetriableErrorCodeException(Throwable t) {
        if (disConfig.getExceptionRetriesErrorCode().length > 0 && t instanceof RestClientResponseException
                && ((RestClientResponseException) t).getRawStatusCode() / 100 == 4) {
            String responseBody = ((RestClientResponseException) t).getResponseBodyAsString();
            ErrorMessage errorMessage = JsonUtils.jsonToObj(responseBody, ErrorMessage.class);
            for (String item : disConfig.getExceptionRetriesErrorCode()) {
                if (errorMessage.getErrorCode().contains(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void check() {
        if (credentials == null) {
            throw new DISClientException("credentials can not be null.");
        }
        if (credentials.getAuthType().equals(AuthType.AUTHTOKEN.getAuthType())) {
            if (StringUtils.isNullOrEmpty(disConfig.getAuthToken())) {
                throw new IllegalArgumentException("authToken cannot be null.");
            }
        }
        if (credentials.getAuthType().equals(AuthType.AKSK.getAuthType())) {
            if (disConfig.getAK() == null) {
                throw new IllegalArgumentException("Access key cannot be null.");
            }
            if (disConfig.getSK() == null) {
                throw new IllegalArgumentException("Secret key cannot be null.");
            }
        }


        if (StringUtils.isNullOrEmpty(region)) {
            throw new DISClientException("region can not be null.");
        }

        if (StringUtils.isNullOrEmpty(disConfig.getProjectId())) {
            throw new RuntimeException("project id can not be null.");
        }

        String endpoint = disConfig.getEndpoint();
        if (StringUtils.isNullOrEmpty(endpoint)) {
            throw new DISClientException("endpoint can not be null.");
        }
        if (!Utils.isValidEndpoint(endpoint)) {
            throw new DISClientException("invalid endpoint.");
        }
    }

    protected void setEndpoint(Request<HttpRequest> request, String endpointStr) {
        URI endpoint;
        try {
            endpoint = new URI(endpointStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        request.setEndpoint(endpoint);
    }

    protected PutRecordsRequest toPutRecordsRequest(PutRecordRequest putRecordRequest) {
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamName(putRecordRequest.getStreamName());
        putRecordRequest.setStreamId(putRecordRequest.getStreamId());

        List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<PutRecordsRequestEntry>();
        PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
        putRecordsRequestEntry.setData(putRecordRequest.getData());
        putRecordsRequestEntry.setPartitionKey(putRecordRequest.getPartitionKey());
        putRecordsRequestEntry.setTimestamp(putRecordRequest.getTimestamp());
        putRecordsRequestEntryList.add(putRecordsRequestEntry);

        putRecordsRequest.setRecords(putRecordsRequestEntryList);

        return putRecordsRequest;
    }

    protected PutRecordResult toPutRecordResult(PutRecordsResult putRecordsResult) {
        if (null != putRecordsResult && null != putRecordsResult.getRecords() && putRecordsResult.getRecords().size() > 0) {
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
    protected void initCredentialsProvider() {
        // Provider转换
        String credentialsProviderClass = disConfig.get(DISConfig.PROPERTY_CONFIG_PROVIDER_CLASS, null);
        if (!StringUtils.isNullOrEmpty(credentialsProviderClass)) {
            try {
                this.credentialsProvider = (ICredentialsProvider) Class.forName(credentialsProviderClass).newInstance();
                this.credentials = credentialsProvider.updateCredentials(this.credentials.clone());
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to call ICredentialsProvider[" + credentialsProviderClass
                        + "], error [" + e.toString() + "]", e);
            }
        }
    }

    protected boolean isRecordsRetriableErrorCode(String errorCode) {
        for (String item : disConfig.getRecordsRetriesErrorCode()) {
            if (errorCode.contains(item)) {
                return true;
            }
        }
        return false;
    }

    protected void innerUpdateCredentials(DISCredentials credentials) {
        if (credentials != null) {
            this.credentials = credentials.clone();
        }
    }

    //更新认证token
    protected void innerUpdateAuthToken(String authToken) {
        this.credentials.setAuthToken(authToken);
    }

    protected void handleError(final Throwable t, String errorMsg, final boolean isRetriableException) {
        if (t instanceof HttpStatusCodeException) {
            int statusCode = ((HttpStatusCodeException) t).getRawStatusCode();
            // 401
            if (Constants.HTTP_CODE_DIS_AUTHENTICATION_FAILED == statusCode) {
                throw new DISAuthenticationException(errorMsg, t);
            }
            // 400
            else if (Constants.HTTP_CODE_BAD_REQUEST == statusCode) {
                if (errorMsg.contains(Constants.ERROR_CODE_SEQUENCE_NUMBER_OUT_OF_RANGE)) {
                    throw new DISSequenceNumberOutOfRangeException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_SEQUENCE_NUMBER_OUT_OF_RANGE_GETTING_RECORDS)) {
                    throw new DISSeqNumberOutOfRangeGettingRecordsException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_PARTITION_IS_EXPIRED)) {
                    throw new DISPartitionExpiredException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_PARTITION_NOT_EXISTS)) {
                    throw new DISPartitionNotExistsException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_STREAM_NOT_EXISTS)) {
                    throw new DISStreamNotExistsException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_TRAFFIC_CONTROL_LIMIT)) {
                    throw new DISTrafficControlException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_CONSUMER_MEMBER_NOT_EXIST)) {
                    throw new DISConsumerMemberNotExistsException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_CONSUMER_GROUP_REBALANCE_IN_PROGRESS)) {
                    throw new DISConsumerGroupRebalanceInProgressException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_CODE_CONSUMER_GROUP_ILLEGAL_GENERATION)) {
                    throw new DISConsumerGroupIllegalGenerationException(errorMsg, t);
                } else if (errorMsg.contains(Constants.ERROR_INFO_TIMESTAMP_IS_EXPIRED)) {
                    throw new DISTimestampOutOfRangeException(errorMsg, t);
                }
            }
            // 410
            else if (Constants.HTTP_CODE_GONE == statusCode) {
                // 410 will be reported when putRecords
                throw new DISStreamNotExistsException(errorMsg, t);
            }
            // 413
            else if (Constants.HTTP_CODE_REQUEST_ENTITY_TOO_LARGE == statusCode) {
                throw new DISRequestEntityTooLargeException(errorMsg, t);
            }
        }

        if (isRetriableException) {
            throw new DISClientRetriableException(errorMsg, t);
        } else {
            throw new DISClientException(errorMsg, t);
        }
    }
}