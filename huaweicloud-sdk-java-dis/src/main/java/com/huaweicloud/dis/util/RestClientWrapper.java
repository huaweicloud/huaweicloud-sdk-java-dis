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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloud.sdk.Request;
import com.cloud.sdk.http.HttpMethodName;
import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.exception.HttpStatusCodeException;
import com.huaweicloud.dis.http.exception.RestClientResponseException;
import com.huaweicloud.dis.http.exception.UnknownHttpStatusCodeException;

public class RestClientWrapper
{
    private static final Logger LOG = LoggerFactory.getLogger(RestClientWrapper.class);
    
    private static final String HEADER_SDK_VERSION = "X-SDK-Version";
    
    private DISConfig disConfig;
    
    private RestClient restClient;
    
    private Request<HttpRequest> request;
    
    public RestClientWrapper(Request<HttpRequest> request, DISConfig disConfig)
    {
        this.request = request;
        this.disConfig = disConfig;
        this.restClient = RestClient.getInstance(disConfig);
    }
    
    public <T> T request(Object requestContent, String ak, String sk, String region, Class<T> returnType)
    {
        beforeRequest(requestContent, region);
        
        request = SignUtil.sign(request, ak, sk, region);
        
        return doRequest(request, requestContent, returnType);
    }
    
    private void beforeRequest(Object requestContent, String region)
    {
        // set request header
        setContentType();
        setSdkVersion();
        
        // set request parameters
        setParameters(requestContent);
        
        // set request content
        setContent(requestContent);
    }
    
    private void setContent(Object requestContent)
    {
        HttpMethodName methodName = request.getHttpMethod();
        if (methodName.equals(HttpMethodName.POST) || methodName.equals(HttpMethodName.PUT))
        {
            if(requestContent instanceof byte[]){
                request.setContent(new ByteArrayInputStream((byte[])requestContent));
            }else if(requestContent instanceof String || requestContent instanceof Integer){
                request.setContent(new ByteArrayInputStream(Utils.encodingBytes(requestContent.toString())));
            }else{
                String reqJson = JsonUtils.objToJson(requestContent);
                request.setContent(new ByteArrayInputStream(Utils.encodingBytes(reqJson)));
            }
        }
    }
    
    private void setContentType()
    {
        //默认为json格式
        if(!request.getHeaders().containsKey("Content-Type")){
            request.addHeader("Content-Type", "application/json; charset=utf-8");
        }
        
        if(!request.getHeaders().containsKey("accept")){
            request.addHeader("accept", "*/*; charset=utf-8");    
        }
    }
    
    private void setSdkVersion()
    {
        request.addHeader(HEADER_SDK_VERSION, VersionUtils.getVersion() + "/" + VersionUtils.getPlatform());
    }    
    
    @SuppressWarnings("unchecked")
    private void setParameters(Object requestContent)
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
    
    //将Request转为restTemplate的请求参数.由于请求需要签名，故请求的body直接传byte[]，响应的反序列化，可以直接利用spring的messageConvert机制
    private <T> T doRequest(Request<HttpRequest> request, Object requestContent, Class<T> returnType)
    {
        
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
                return restClient.exchange(uri.toString(), request.getHttpMethod(), request.getHeaders(), requestContent, returnType);
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
                if (!isRetriableSendException(t) || retryCount >= disConfig.getExceptionRetries())
                {
                    throw new DISClientException(errorMsg, t);
                }
                LOG.warn("Find Retriable Exception [{}], currRetryCount is {}",
                    errorMsg.replaceAll("[\\r\\n]", ""),
                    retryCount);
            }
        } while (retryCount < disConfig.getExceptionRetries());
        
        return null;
    }
    
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }


    /**
     * 判断此异常是否可以重试
     *
     * @param t
     * @return
     */
    protected boolean isRetriableSendException(Throwable t)
    {
        // 对于连接超时/网络闪断/Socket异常/服务端5xx错误进行重试
        return t instanceof ConnectTimeoutException || t instanceof NoHttpResponseException
            || t instanceof SocketException || t instanceof SSLException
            || (t instanceof RestClientResponseException
                && ((RestClientResponseException)t).getRawStatusCode() / 100 == 5)
            || (t.getCause() != null && isRetriableSendException(t.getCause()));
    }
}
