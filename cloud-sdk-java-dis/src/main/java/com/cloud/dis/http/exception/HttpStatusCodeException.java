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

package com.cloud.dis.http.exception;

import java.nio.charset.Charset;

/**
 * Abstract base class for exceptions based on status code.
 *
 * @since 1.3.0
 */
public abstract class HttpStatusCodeException extends RestClientResponseException {

	private static final long serialVersionUID = 5696801857651587810L;


	private final int statusCode;


	/**
	 * Construct a new instance with a status code.
	 * @param statusCode the status code
	 */
	protected HttpStatusCodeException(int statusCode) {
		this(statusCode, null, null, null);
	}

	/**
	 * Construct a new instance with a status code and status text.
	 * @param statusCode the status code
	 * @param statusText the status text
	 */
	protected HttpStatusCodeException(int statusCode, String statusText) {
		this(statusCode, statusText, null, null);
	}

    /**
     * Construct instance with a status code, status text, content, and a response charset.
     * 
     * @param statusCode the status code
     * @param statusText the status text
     * @param responseBody the response body content, may be {@code null}
     * @param responseCharset the response body charset, may be {@code null}
     * @since 3.1.2
     */
    protected HttpStatusCodeException(int statusCode, String statusText, byte[] responseBody, Charset responseCharset)
    {
        
        super(statusCode + " " + statusText, statusCode, statusText, responseBody, responseCharset);
        this.statusCode = statusCode;
    }


	/**
	 * Return the HTTP status code.
	 * 
	 * @return The status code.
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

}
