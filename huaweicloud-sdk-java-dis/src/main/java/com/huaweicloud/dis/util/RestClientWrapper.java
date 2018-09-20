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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.Request;

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
    
    public static byte[] toByteArray(InputStream input)
        throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }


	
}
