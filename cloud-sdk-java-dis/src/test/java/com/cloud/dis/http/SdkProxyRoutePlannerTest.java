package com.cloud.dis.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.cloud.dis.DISConfig;
import com.cloud.dis.util.ApacheUtils;
import com.cloud.dis.util.HttpRequestRetryHandler;

public class SdkProxyRoutePlannerTest
{
    
    @Test
    @Ignore
    public void testProxy()
        throws IOException
    {
        DISConfig disConfig = new DISConfig();
        disConfig.setProxyHost("proxy.com");
        disConfig.setProxyPort("8080");
        disConfig.setProxyUsername("username");
        disConfig.setProxyPassword("password");
        
        CloseableHttpClient httpclient = getHttpClient(disConfig);
        
        CloseableHttpResponse response = null;
        
        HttpHost target = new HttpHost("www.targethost.com", 80, "http");
        HttpHost proxy = new HttpHost(disConfig.getProxyHost(), disConfig.getProxyPort());
        
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        HttpGet httpget = new HttpGet("/");
        httpget.setConfig(config);
        
        System.out.println("Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxy);
        
        response = httpclient.execute(target, httpget);
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
        
        Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }
    
    private CloseableHttpClient getHttpClient(DISConfig disConfig)
    {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        RequestConfig requestConfig = requestConfigBuilder.setSocketTimeout(disConfig.getSocketTimeOut())
            .setConnectTimeout(disConfig.getConnectionTimeOut())
            .build();
        connManager.setDefaultMaxPerRoute(disConfig.getMaxPerRoute());
        connManager.setMaxTotal(disConfig.getMaxTotal());
        
        // 构建客户端
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (disConfig.isProxyEnabled())
        {
            httpClientBuilder.setRoutePlanner(new SdkProxyRoutePlanner(disConfig.getProxyHost(),
                disConfig.getProxyPort(), disConfig.getProxyProtocol(), disConfig.getNonProxyHosts()));
            
            if (disConfig.isAuthenticatedProxy())
            {
                httpClientBuilder.setDefaultCredentialsProvider(ApacheUtils.newProxyCredentialsProvider(disConfig));
            }
        }
        Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider> create()
            .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
            .register(AuthSchemes.BASIC, new BasicSchemeFactory())
            .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
            .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
            .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
            .build();
        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeRegistry);
        
        return httpClientBuilder.setConnectionManager(connManager)
            .setRetryHandler(new HttpRequestRetryHandler(3, true))
            .setDefaultRequestConfig(requestConfig)
            .build();
    }
}
