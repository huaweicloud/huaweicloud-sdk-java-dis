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

package com.huaweicloud.dis.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.http.converter.ByteArrayHttpMessageConverter;
import com.huaweicloud.dis.http.converter.HttpMessageConverter;
import com.huaweicloud.dis.http.converter.StringHttpMessageConverter;
import com.huaweicloud.dis.http.converter.json.JsonHttpMessageConverter;
import com.huaweicloud.dis.http.converter.protobuf.ProtobufHttpMessageConverter;
import com.huaweicloud.dis.util.JsonUtils;

/**
 * Rest Client for RESTful API
 *
 * @since 1.3.0
 */
public abstract class AbstractRestClient
{
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractRestClient.class);
    
    protected final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    
    protected DISConfig disConfig;
    
    protected AbstractRestClient(DISConfig disConfig)
    {
        this.disConfig = disConfig;
        
        this.messageConverters.add(new JsonHttpMessageConverter());
        this.messageConverters.add(new ProtobufHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
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
     * 设置请求头域
     *
     * @param request 请求体
     * @param headers 需要添加到请求体重的头域
     * @param <T> Generic Type
     * @return 修改后的请求体
     */
    protected <T> T setHeaders(T request, Map<String, String> headers)
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
    
    protected HttpEntity buildHttpEntity(Object data)
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
}
