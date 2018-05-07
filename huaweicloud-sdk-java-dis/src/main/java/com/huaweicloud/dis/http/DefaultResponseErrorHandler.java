/*
 * Copyright 2002-2012 the original author or authors.
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import com.huaweicloud.dis.http.exception.HttpClientErrorException;
import com.huaweicloud.dis.http.exception.HttpServerErrorException;
import com.huaweicloud.dis.http.exception.RestClientException;
import com.huaweicloud.dis.util.FileCopyUtils;
import com.huaweicloud.dis.util.RestClient;

/**
 * Default implementation of the {@link ResponseErrorHandler} interface.
 *
 * @since 3.0
 * @see RestClient#setErrorHandler
 */
public class DefaultResponseErrorHandler implements ResponseErrorHandler {

	/**
	 * Delegates to {@link #hasError(int)} with the response status code.
	 */
	@Override
	public boolean hasError(HttpResponse response) throws IOException {
		return hasError(getHttpStatusCode(response));
	}

	private int getHttpStatusCode(HttpResponse response) throws IOException {
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * Template method called from {@link #hasError(HttpResponse)}.
	 * @param statusCode the HTTP status code
	 * @return {@code true} if the response has an error; {@code false} otherwise
	 */
    protected boolean hasError(int statusCode)
    {
        return (isClientError(statusCode) || isServerError(statusCode));
    }

    @Override
    public void handleError(HttpResponse response)
        throws IOException
    {
        int statusCode = getHttpStatusCode(response);
        
        if (isClientError(statusCode))
        {
            throw new HttpClientErrorException(statusCode, response.getStatusLine().getReasonPhrase(),
                getResponseBody(response), getCharset(response));
        }
        else if (isServerError(statusCode))
        {
            throw new HttpServerErrorException(statusCode, response.getStatusLine().getReasonPhrase(),
                getResponseBody(response), getCharset(response));
        }
        else
        {
            throw new RestClientException("Unknown status code [" + statusCode + "]");
        }
    }
	
	private boolean isClientError(int statusCode)
	{
	    return (statusCode >= 400 && statusCode < 500);
	}
	
	private boolean isServerError(int statusCode)
	{
	    return (statusCode >= 500 && statusCode < 600);
	}

	private byte[] getResponseBody(HttpResponse response) {
		try {
			InputStream responseBody = response.getEntity().getContent();
			if (responseBody != null) {
				return FileCopyUtils.copyToByteArray(responseBody);
			}
		}
		catch (IOException ex) {
			// ignore
		}
		return new byte[0];
	}

	private Charset getCharset(HttpResponse response) {
		ContentType contentType = ContentType.getOrDefault(response.getEntity());
		return contentType != null ? contentType.getCharset() : null;
	}

}
