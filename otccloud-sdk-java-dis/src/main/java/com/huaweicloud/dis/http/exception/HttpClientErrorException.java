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

package com.otccloud.dis.http.exception;

import java.nio.charset.Charset;

import org.apache.http.HttpStatus;

import com.otccloud.dis.http.DefaultResponseErrorHandler;


/**
 * Exception thrown when an HTTP 4xx is received.
 *
 * @since 1.3.0
 * @see DefaultResponseErrorHandler
 */
public class HttpClientErrorException extends HttpStatusCodeException {

	private static final long serialVersionUID = 5177019431887513952L;


	/**
	 * Construct a new instance of {@code HttpClientErrorException} based on
	 * an {@link HttpStatus}.
	 * @param statusCode the status code
	 */
	public HttpClientErrorException(int statusCode) {
		super(statusCode);
	}

	/**
	 * Construct a new instance of {@code HttpClientErrorException} based on
	 * an {@link HttpStatus} and status text.
	 * @param statusCode the status code
	 * @param statusText the status text
	 */
	public HttpClientErrorException(int statusCode, String statusText) {
		super(statusCode, statusText);
	}

	/**
	 * Construct a new instance of {@code HttpClientErrorException} based on
	 * an {@link HttpStatus}, status text, and response body content.
	 * @param statusCode the status code
	 * @param statusText the status text
	 * @param responseBody the response body content (may be {@code null})
	 * @param responseCharset the response body charset (may be {@code null})
	 */
	public HttpClientErrorException(int statusCode, String statusText,
			byte[] responseBody, Charset responseCharset) {

		super(statusCode, statusText, responseBody, responseCharset);
	}

}
