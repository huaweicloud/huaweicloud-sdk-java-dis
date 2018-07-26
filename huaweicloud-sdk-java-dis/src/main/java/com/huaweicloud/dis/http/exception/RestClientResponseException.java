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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Common base class for exceptions that contain actual HTTP response data.
 *
 * @since 1.3.0
 */
public class RestClientResponseException extends RestClientException
{
    
    private static final long serialVersionUID = -3836421908742613113L;
    
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    
    private final int rawStatusCode;
    
    private final String statusText;
    
    private final byte[] responseBody;
    
    private final String responseCharset;
    
    /**
     * Construct a new instance of with the given response data.
     * 
     * @param message the response message
     * @param statusCode the raw status code value
     * @param statusText the status text
     * @param responseBody the response body content (may be {@code null})
     * @param responseCharset the response body charset (may be {@code null})
     */
    public RestClientResponseException(String message, int statusCode, String statusText, byte[] responseBody,
        Charset responseCharset)
    {
        
        super(message);
        this.rawStatusCode = statusCode;
        this.statusText = statusText;
        this.responseBody = (responseBody != null ? responseBody : new byte[0]);
        this.responseCharset = (responseCharset != null ? responseCharset.name() : DEFAULT_CHARSET);
    }
    
    /**
     * Return the raw HTTP status code value.
     * 
     * @return The raw status code.
     */
    public int getRawStatusCode()
    {
        return this.rawStatusCode;
    }
    
    /**
     * Return the HTTP status text.
     * 
     * @return The status text.
     */
    public String getStatusText()
    {
        return this.statusText;
    }
    
    /**
     * Return the response body as a byte array.
     * 
     * @return The response body as a byte array.
     */
    public byte[] getResponseBodyAsByteArray()
    {
        return this.responseBody;
    }
    
    /**
     * Return the response body as a string.
     * 
     * @return The response body as a string.
     */
    public String getResponseBodyAsString()
    {
        try
        {
            return new String(this.responseBody, this.responseCharset);
        }
        catch (UnsupportedEncodingException ex)
        {
            // should not occur
            throw new IllegalStateException(ex);
        }
    }
    
}
