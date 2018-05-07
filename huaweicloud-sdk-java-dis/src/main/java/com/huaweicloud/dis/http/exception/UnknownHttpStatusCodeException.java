/*
 * Copyright 2002-2016 the original author or authors.
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

package com.huaweicloud.dis.http.exception;

import java.nio.charset.Charset;

import org.apache.http.HttpStatus;

/**
 * Exception thrown when an unknown (or custom) HTTP status code is received.
 *
 * @since 1.3.0
 */
public class UnknownHttpStatusCodeException extends RestClientResponseException
{
    
    private static final long serialVersionUID = 7103980251635005491L;
    
    /**
     * Construct a new instance of {@code HttpStatusCodeException} based on an {@link HttpStatus}, status text, and
     * response body content.
     * 
     * @param rawStatusCode the raw status code value
     * @param statusText the status text
     * @param responseBody the response body content, may be {@code null}
     * @param responseCharset the response body charset, may be {@code null}
     */
    public UnknownHttpStatusCodeException(int rawStatusCode, String statusText, byte[] responseBody,
        Charset responseCharset)
    {
        
        super("Unknown status code [" + String.valueOf(rawStatusCode) + "]" + " " + statusText, rawStatusCode,
            statusText, responseBody, responseCharset);
    }
    
}
