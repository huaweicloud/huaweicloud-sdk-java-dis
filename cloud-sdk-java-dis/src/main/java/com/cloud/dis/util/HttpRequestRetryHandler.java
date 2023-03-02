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

package com.cloud.dis.util;

import java.io.IOException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestRetryHandler extends DefaultHttpRequestRetryHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRetryHandler.class);
    
    public HttpRequestRetryHandler(final int retryCount, final boolean requestSentRetryEnabled)
    {
        super(retryCount, requestSentRetryEnabled);
    }
    
    public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context)
    {
        boolean isRetry = super.retryRequest(exception, executionCount, context);
        if (!isRetry && executionCount <= this.getRetryCount() && (exception instanceof NoHttpResponseException))
        {
            LOGGER.debug("Found exception {}, current executionCount {}, will retry later.",
                exception.getMessage(),
                executionCount);
            isRetry = true;
        }
        
        return isRetry;
    }
}
