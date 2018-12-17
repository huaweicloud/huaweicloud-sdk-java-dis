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

import java.io.IOException;
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

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.DefaultResponseErrorHandler;
import com.huaweicloud.dis.http.HttpMessageConverterExtractor;
import com.huaweicloud.dis.http.ResponseErrorHandler;
import com.huaweicloud.dis.http.ResponseExtractor;
import com.huaweicloud.dis.http.SdkProxyRoutePlanner;
import com.huaweicloud.dis.http.converter.ByteArrayHttpMessageConverter;
import com.huaweicloud.dis.http.converter.HttpMessageConverter;
import com.huaweicloud.dis.http.converter.StringHttpMessageConverter;
import com.huaweicloud.dis.http.converter.json.JsonHttpMessageConverter;
import com.huaweicloud.dis.http.converter.protobuf.ProtobufHttpMessageConverter;
import com.huaweicloud.dis.http.exception.ResourceAccessException;

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
    
    private DISConfig disConfig;
    
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
        return execute(request, responseExtractor);
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
        return execute(request, responseExtractor);
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
        return execute(request, responseExtractor);
        
    }
    
    public void delete(String url, Map<String, String> headers)
    {
        
        HttpDelete request = new HttpDelete(url);
        request = this.setHeaders(request, headers);
        
        execute(request, null);
    }
    
    /**
     * 发送请求并处理响应
     * 
     * @param request 请求体
     * @param responseExtractor 响应解析器
     * @param <T> Generic type
     * @return 响应体
     */
    protected <T> T execute(final HttpUriRequest request, ResponseExtractor<T> responseExtractor)
    {
        HttpResponse response;
        try
        {
            response = httpClient.execute(request);
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
            String resource = request.getURI().toString();
            String query = request.getURI().getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf(query) - 1) : resource);
            throw new ResourceAccessException(
                "I/O error on " + request.getMethod() + " request for \"" + resource + "\": " + ex.getMessage(), ex);
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
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
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
                new String[] {"TLSv1.2", "TLSv1.1"}, null, verifier);
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
    
}
