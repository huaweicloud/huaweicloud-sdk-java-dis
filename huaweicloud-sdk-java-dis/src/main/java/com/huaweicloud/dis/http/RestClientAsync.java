package com.huaweicloud.dis.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.converter.HttpMessageConverter;

public class RestClientAsync extends AbstractRestClient{
	private static final Logger logger = LoggerFactory.getLogger(RestClientAsync.class);
    
    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
    
    private static RestClientAsync restAsyncClient;
    
    private static CloseableHttpAsyncClient httpAsyncClient;
    
    
    private RestClientAsync(DISConfig disConfig)
    {
    	super(disConfig);
    }
    
    private void init()
    {
    	httpAsyncClient = getHttpAsyncClient();
    }
    
    public synchronized static RestClientAsync getInstance(DISConfig disConfig)
    {
        if (restAsyncClient == null)
        {
        	restAsyncClient = new RestClientAsync(disConfig);
        	restAsyncClient.init();
        }
        
        return restAsyncClient;
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
    
    public <T> Future<T> exchangeAsync(String url, HttpMethodName httpMethod, Map<String, String> headers, Object requestContent,
        Class<T> responseClazz, AsyncHandler<T> callback)
    {
        switch (httpMethod)
        {
            case PUT:
                return putAsync(url, responseClazz, headers, requestContent, callback);
            case POST:
                return postAsync(url, responseClazz, headers, requestContent, callback);
            case GET:
                return getAsync(url, responseClazz, headers, callback);
            case DELETE:
                deleteAsync(url, headers, callback);
                
                return null;
            default:
                throw new DISClientException("unimplemented.");
        }
    }
    
    /*
     * HttpClient Get Request
     * 
     */
    public <T> Future<T> getAsync(String url, Class<T> responseClazz, Map<String, String> headers, AsyncHandler<T> callback)
    {
        HttpGet request = new HttpGet(url);
        request = this.setHeaders(request, headers);
        
        HttpMessageConverterExtractor<T> responseExtractor =
            new HttpMessageConverterExtractor<T>(responseClazz, getMessageConverters());
        return executeAsync(request, responseExtractor, callback);
    }
    
    public <T> Future<T> postAsync(String url, Class<T> responseClazz, Map<String, String> headers, Object requestBody, AsyncHandler<T> callback)
    {
        HttpPost request = new HttpPost(url);
        request = this.setHeaders(request, headers);
        
        request.setEntity(buildHttpEntity(requestBody));
        
        HttpMessageConverterExtractor<T> responseExtractor =
            new HttpMessageConverterExtractor<T>(responseClazz, getMessageConverters());
        return executeAsync(request, responseExtractor, callback);
    }
    
    public <T> Future<T> putAsync(String url, Class<T> responseClazz, Map<String, String> headers, Object requestBody, AsyncHandler<T> callback)
    {
        HttpPut request = new HttpPut(url);
        request = this.setHeaders(request, headers);
        
        request.setEntity(buildHttpEntity(requestBody));
        
        HttpMessageConverterExtractor<T> responseExtractor =
            new HttpMessageConverterExtractor<T>(responseClazz, getMessageConverters());
        return executeAsync(request, responseExtractor, callback);
    }
    
    public <T> Future<T> deleteAsync(String url, Map<String, String> headers, AsyncHandler<T> callback)
    {
        HttpDelete request = new HttpDelete(url);
        request = this.setHeaders(request, headers);
        
        return executeAsync(request, null, callback);
    }
    
    /**
     * 发送请求并处理响应
     * 
     * @param request 请求体
     * @param responseExtractor 响应解析器
     * @param callback 
     * @param <T> Generic type
     * @return 响应体
     */
    protected <T> Future<T> executeAsync(final HttpUriRequest request, ResponseExtractor<T> responseExtractor, AsyncHandler<T> callback)
    {
    	HttpFutureAdapter<T> futureAdapter = new HttpFutureAdapter<>(responseExtractor);
    	
    	HttpFutureCallbackAdapter<T> httpFutureCallbackAdapter = null;
    	if(callback != null) {
    		httpFutureCallbackAdapter = new HttpFutureCallbackAdapter<>(callback, responseExtractor, futureAdapter);
    	}
    	
    	Future<HttpResponse> httpFuture = httpAsyncClient.execute(request, httpFutureCallbackAdapter);
    	
    	futureAdapter.setInnerFuture(httpFuture);
    	
    	return futureAdapter;
    }
    
    private CloseableHttpAsyncClient getHttpAsyncClient()
    {
    	TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
					
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
				
        }};
    	
    	SSLContext sslContext=null;
    	try {
    		sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);	
    	}catch(NoSuchAlgorithmException |KeyManagementException e) {
    		throw new RuntimeException(e);
    	}
        
        SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext, SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", sslioSessionStrategy)
                .build();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(disConfig.getSocketTimeOut()) // 设置请求响应超时时间
                .setConnectTimeout(disConfig.getConnectionTimeOut()) // 设置请求连接超时时间
                .build();

        
    	IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors())
    			.setSoKeepAlive(true).build();
    	ConnectingIOReactor ioReactor = null;
    	try {
			ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
		} catch (IOReactorException e) {
			throw new RuntimeException(e);
		}
    	
    	PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor, registry);
    	connManager.setMaxTotal(disConfig.getMaxTotal());
    	connManager.setDefaultMaxPerRoute(disConfig.getMaxPerRoute());
    	
    	CloseableHttpAsyncClient asyncClient = HttpAsyncClients.custom().setConnectionManager(connManager)
    			.setDefaultRequestConfig(requestConfig)
    			.build();
    	
    	asyncClient.start();
    	
    	return asyncClient;
    }
}
