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

package com.huaweicloud.dis.util;

import com.huaweicloud.dis.DISClient;
import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.*;
import com.huaweicloud.dis.http.converter.ByteArrayHttpMessageConverter;
import com.huaweicloud.dis.http.converter.HttpMessageConverter;
import com.huaweicloud.dis.http.converter.StringHttpMessageConverter;
import com.huaweicloud.dis.http.converter.json.JsonHttpMessageConverter;
import com.huaweicloud.dis.http.converter.protobuf.ProtobufHttpMessageConverter;
import com.huaweicloud.dis.http.exception.ResourceAccessException;
import com.huaweicloud.dis.util.config.ConfigurationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Rest Client for RESTful API
 *
 * @since 1.3.0
 */
public class RestClient
{
    
    private final Logger logger = LoggerFactory.getLogger(RestClient.class);
    
    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    
    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
    
    private static RestClient restClient;
    
    private static CloseableHttpClient httpClient;
    
    private final DISConfig disConfig;
    
    private RestClient(DISConfig disConfig)
    {
        this.disConfig = disConfig;
        
        this.messageConverters.add(new JsonHttpMessageConverter());
        this.messageConverters.add(new ProtobufHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
    }
    
    private void init()
    {
        httpClient = getHttpClient();
    }
    
    public synchronized static RestClient getInstance(DISConfig disConfig)
    {
        if (restClient == null)
        {
            restClient = new RestClient(disConfig);
            restClient.init();
        }
        
        return restClient;
    }
    
    /**
     * Set the message body converters to use.
     * <p>
     * These converters are used to convert from and to HTTP requests and responses.
     * 
     * @param messageConverters List of message converters.
     */
    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters)
    {
        if (messageConverters != null && messageConverters.size() > 0)
        {
            // Take getMessageConverters() List as-is when passed in here
            if (this.messageConverters != messageConverters)
            {
                this.messageConverters.clear();
                this.messageConverters.addAll(messageConverters);
            }
        }
    }
    
    /**
     * Return the message body converters.
     * 
     * @return List of message converters.
     */
    public List<HttpMessageConverter<?>> getMessageConverters()
    {
        return this.messageConverters;
    }
    
    /**
     * Set the error handler.
     * <p>
     * By default, RestTemplate uses a {@link DefaultResponseErrorHandler}.
     * 
     * @param errorHandler response error handler.
     */
    public void setErrorHandler(ResponseErrorHandler errorHandler)
    {
        if (errorHandler != null)
        {
            this.errorHandler = errorHandler;
        }
    }
    
    /**
     * Return the error handler.
     * 
     * @return response error handler.
     */
    public ResponseErrorHandler getErrorHandler()
    {
        return this.errorHandler;
    }
    
    public <T> T exchange(String url, HttpMethodName httpMethod, Map<String, String> headers, Object requestContent,
        Class<T> responseClazz)
    {
        switch (httpMethod)
        {
            case PUT:
                return put(url, responseClazz, headers, requestContent);
            case POST:
                return post(url, responseClazz, headers, requestContent);
            case GET:
                return get(url, responseClazz, headers);
            case DELETE:
                delete(url, headers);
                
                return null;
            default:
                throw new DISClientException("unimplemented.");
        }
    }
    
    /*
     * HttpClient Get Request
     * 
     */
    public <T> T get(String url, Class<T> responseClazz, Map<String, String> headers)
    {
        HttpGet request = new HttpGet(url);
        request = this.setHeaders(request, headers);
        
        HttpMessageConverterExtractor<T> responseExtractor =
            new HttpMessageConverterExtractor<T>(responseClazz, getMessageConverters());
        long requestConnectionTimeOut = disConfig.getConnectionTimeOut();
        return execute(request, responseExtractor, requestConnectionTimeOut);
    }
    
    public <T> T getForObject(String url, Class<T> responseClazz)
    {
        return get(url, responseClazz, null);
    }
    
    public <T> T post(String url, Class<T> responseClazz, Map<String, String> headers, Object requestBody)
    {
        return post(url, responseClazz, headers, buildHttpEntity(requestBody));
    }
    
    public <T> T post(String url, Class<T> responseClazz, Map<String, String> headers, HttpEntity entity)
    {
        HttpPost request = new HttpPost(url);
        request = this.setHeaders(request, headers);
        
        request.setEntity(entity);
        
        HttpMessageConverterExtractor<T> responseExtractor =
            new HttpMessageConverterExtractor<T>(responseClazz, getMessageConverters());
        long requestConnectionTimeOut = disConfig.getConnectionTimeOut();
        return execute(request, responseExtractor, requestConnectionTimeOut);
    }
    
    public <T> T put(String url, Class<T> responseClazz, Map<String, String> headers, Object requestBody)
    {
        return put(url, responseClazz, headers, buildHttpEntity(requestBody));
    }
    
    public <T> T put(String url, Class<T> responseClazz, Map<String, String> headers, HttpEntity entity)
    {
        HttpPut request = new HttpPut(url);
        request = this.setHeaders(request, headers);
        
        request.setEntity(entity);
        
        HttpMessageConverterExtractor<T> responseExtractor =
            new HttpMessageConverterExtractor<T>(responseClazz, getMessageConverters());
        long requestConnectionTimeOut = disConfig.getConnectionTimeOut();
        return execute(request, responseExtractor, requestConnectionTimeOut);
        
    }
    
    public void delete(String url, Map<String, String> headers)
    {
        
        HttpDelete request = new HttpDelete(url);
        request = this.setHeaders(request, headers);
        long requestConnectionTimeOut = disConfig.getConnectionTimeOut();
        execute(request, null, requestConnectionTimeOut);
    }
    
    /**
     * 发送请求并处理响应
     * 
     * @param request 请求体
     * @param responseExtractor 响应解析器
     * @param <T> Generic type
     * @param requestConnectionTimeOut 请求过期时间
     * @return 响应体
     */
    protected <T> T execute(final HttpUriRequest request, ResponseExtractor<T> responseExtractor,
                            long requestConnectionTimeOut)
    {
        HttpResponse response;
        long startTime = System.currentTimeMillis();
        try
        {
            RequestCache.put(request, requestConnectionTimeOut);
            response = httpClient.execute(request);

            if (InterfaceLogUtils.IS_INTERFACE_LOGGER_ENABLED)
            {
                InterfaceLogUtils.TOTAL_REQUEST_TIMES.incrementAndGet();
                long cost = System.currentTimeMillis() - startTime;
                long requestSize = request instanceof HttpEntityEnclosingRequestBase ?
                        ((HttpEntityEnclosingRequestBase) request).getEntity().getContentLength() : 0;
                int responseCode = response.getStatusLine().getStatusCode();
                int resultFlag = 0;
                if (responseCode < 400 || responseCode >= 600)
                {
                    // normal
                    InterfaceLogUtils.TOTAL_REQUEST_SUCCESSFUL_TIMES.incrementAndGet();
                    InterfaceLogUtils.TOTAL_REQUEST_POSTPONE_MILLIS.addAndGet(cost);
                    InterfaceLogUtils.TOTAL_REQUEST_ENTITY_SIZE.addAndGet(requestSize);
                }
                else
                {
                    // exception
                    resultFlag = 1;
                    InterfaceLogUtils.TOTAL_REQUEST_FAILED_TIMES.incrementAndGet();
                }

                // method uri responseCode requestSize cost result
                InterfaceLogUtils.INTERFACE_DETAIL_LOGGER.trace("{} {} {} {} {} {}",
                        request.getMethod(),
                        request.getURI().getPath(),
                        responseCode,
                        requestSize,
                        cost,
                        resultFlag);
            }

            handleResponse(response);

            if (responseExtractor != null)
            {
                return responseExtractor.extractData(response);
            }
            else
            {
                return null;
            }
        }
        catch (IOException ex)
        {
            if (InterfaceLogUtils.IS_INTERFACE_LOGGER_ENABLED)
            {
                InterfaceLogUtils.TOTAL_REQUEST_TIMES.incrementAndGet();
                long cost = System.currentTimeMillis() - startTime;
                long requestSize = request instanceof HttpEntityEnclosingRequestBase ?
                        ((HttpEntityEnclosingRequestBase) request).getEntity().getContentLength() : 0;
                InterfaceLogUtils.TOTAL_REQUEST_FAILED_TIMES.incrementAndGet();
                InterfaceLogUtils.INTERFACE_DETAIL_LOGGER.trace("{} {} {} {} {} {}",
                        request.getMethod(),
                        request.getURI().getPath(),
                        ex.getClass().getName(),
                        requestSize,
                        cost,
                        2);
            }

            String resource = request.getURI().toString();
            String query = request.getURI().getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf(query) - 1) : resource);
            throw new ResourceAccessException(
                "I/O error on " + request.getMethod() + " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            RequestCache.remove(request);
        }
    }
    
    /**
     * Handle the given response, performing appropriate logging and invoking the {@link ResponseErrorHandler} if
     * necessary.
     * <p>
     * Can be overridden in subclasses.
     * 
     * @param response the resulting {@link HttpResponse}
     * @throws IOException if propagated from {@link ResponseErrorHandler}
     * @since 1.3.0
     * @see #setErrorHandler
     */
    protected void handleResponse(HttpResponse response)
        throws IOException
    {
        ResponseErrorHandler errorHandler = getErrorHandler();
        boolean hasError = errorHandler.hasError(response);
        
        if (hasError)
        {
            errorHandler.handleError(response);
        }
    }
    
    /**
     * 设置请求头域
     * 
     * @param request 请求体
     * @param headers 需要添加到请求体重的头域
     * @return 修改后的请求体
     */
    private <T> T setHeaders(T request, Map<String, String> headers)
    {
        if (headers != null)
        {
            for (Entry<String, String> entry : headers.entrySet())
            {
                ((HttpRequest)request).setHeader(entry.getKey(), entry.getValue());
            }
        }
        else
        {
            ((HttpRequest)request).setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        }
        
        return request;
    }
    
    private HttpEntity buildHttpEntity(Object data)
    {
        
        // TODO 使用 HttpMessageConverter 来实现
        if (data instanceof byte[])
        {
            return new ByteArrayEntity((byte[])data);
        }
        else if (data instanceof String || data instanceof Integer)
        {
            return new StringEntity(data.toString(), "UTF-8");
        }
        else
        {
            return new StringEntity(JsonUtils.objToJson(data), "UTF-8");
        }
    }
    
    private CloseableHttpClient getHttpClient()
    {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());

        X509HostnameVerifier verifier = null;
        // 指定信任密钥存储对象和连接套接字工厂
        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            boolean isDefaultTrustedJKSEnabled = disConfig.getIsDefaultTrustedJksEnabled();
            SSLContext sslContext = null;
            
            // 启用客户端证书校验
            if (isDefaultTrustedJKSEnabled)
            {
            	trustStore = getDefaultTrustStore();
                sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, null).build();
            }
            else
            {
                // 信任任何链接
                TrustStrategy anyTrustStrategy = new TrustStrategy()
                {
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException
                    {
                        return true;
                    }
                };
                sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                verifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            }
            
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
                new String[] {"TLSv1.2"}, null, verifier);
            registryBuilder.register("https", sslSF);
        }
        catch (KeyStoreException e)
        {
            throw new RuntimeException(e);
        }
        catch (KeyManagementException e)
        {
            throw new RuntimeException(e);
        }
		catch (NoSuchAlgorithmException e) 
        {
			throw new RuntimeException(e);
		}
		catch (CertificateException e) 
        {
			throw new RuntimeException(e);
		} catch (IOException e) 
        {
			throw new RuntimeException(e);
		}
		 
			 
        finally
        {
            // if (null != in)
            // {
            // try
            // {
            // in.close();
            // }
            // catch (IOException e)
            // {
            // log.error(e.getMessage());
            // }
            // }
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
//        if (disConfig.isProxyEnabled() && disConfig.isAuthenticatedProxy())
//        {
//            List<String> apacheAuthenticationSchemes = new ArrayList<>();
//            apacheAuthenticationSchemes.add(AuthSchemes.NTLM);
//            apacheAuthenticationSchemes.add(AuthSchemes.BASIC);
//            apacheAuthenticationSchemes.add(AuthSchemes.DIGEST);
//            
//            requestConfigBuilder.setProxyPreferredAuthSchemes(apacheAuthenticationSchemes);
//        }
        RequestConfig requestConfig = requestConfigBuilder.setSocketTimeout(disConfig.getSocketTimeOut()).setConnectTimeout(disConfig.getConnectionTimeOut()).build();
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setDefaultMaxPerRoute(disConfig.getMaxPerRoute());
        connManager.setMaxTotal(disConfig.getMaxTotal());
        // connManager.setDefaultConnectionConfig(connConfig);
        // connManager.setDefaultSocketConfig(socketConfig);
        
        // 构建客户端
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (disConfig.isProxyEnabled())
        {
            logger.info("Configuring Proxy. Proxy Host: {}, Proxy Port: {}.",
                disConfig.getProxyHost(),
                disConfig.getProxyPort());
            
            httpClientBuilder.setRoutePlanner(new SdkProxyRoutePlanner(disConfig.getProxyHost(),
                disConfig.getProxyPort(), disConfig.getProxyProtocol(), disConfig.getNonProxyHosts()));
            
            if (disConfig.isAuthenticatedProxy())
            {
                httpClientBuilder.setDefaultCredentialsProvider(ApacheUtils.newProxyCredentialsProvider(disConfig));
            }
        }
//        Registry<AuthSchemeProvider> authSchemeRegistry =
//            RegistryBuilder.<AuthSchemeProvider> create()
//                .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
//                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
//                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
//                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
//                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
//                .build();
//        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeRegistry);
        
        return httpClientBuilder.setConnectionManager(connManager)
            .setRetryHandler(new HttpRequestRetryHandler(3, true))
            .setDefaultRequestConfig(requestConfig)
            .build();
    }
    
    /**
     * 读取Java默认的TrustStore
     * 
     * @return Default Java TrustStore
     * @throws KeyStoreException KeyStoreException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws CertificateException CertificateException
     * @throws IOException
     */
    private KeyStore getDefaultTrustStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
    {
		final String CACERTS_PATH = System.getProperty("javax.net.ssl.trustStore") == null
				? System.getProperties().getProperty("java.home") + File.separator + "lib" + File.separator + "security"
						+ File.separator + "cacerts"
				: System.getProperty("javax.net.ssl.keyStore");
		final String CACERTS_PASSWORD = System.getProperty("javax.net.ssl.trustStorePassword") == null ? "changeit"
				: System.getProperty("javax.net.ssl.trustStorePassword");
            
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = null;
        
        try
        {
            in = new FileInputStream(CACERTS_PATH);
            trustStore.load(in, CACERTS_PASSWORD.toCharArray());
        }
        finally
        {
            if (null != in)
            {
                in.close();
            }
        }
        
        return trustStore;
    }

    private static class RequestCache {
        private static final Map<HttpUriRequest, Long> cache = new ConcurrentHashMap<>();
        static {
            Thread thread = new Thread(() -> cleanup(), "RequestCache-Thread");
            thread.setDaemon(true);
            thread.start();
        }

        public static void put(HttpUriRequest request, long requestConnectionTimeOut) {
            long requestExpireTimeout = requestConnectionTimeOut + 10000;
            cache.put(request, System.currentTimeMillis() + requestExpireTimeout);
        }

        public static void remove(HttpUriRequest request) {
            cache.remove(request);
        }

        private static void cleanup() {
            while (true) {
                long now = System.currentTimeMillis();
                // find expired requests
                List<HttpUriRequest> expired = cache.entrySet().stream()
                    .filter(e -> e.getValue() < now)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
                // abort requests
                expired.forEach(request -> {
                    if(!request.isAborted()) {
                        request.abort();
                    }
                    remove(request);
                });
                sleep(5000);
            }
        }

        private static void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
