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

/**
 * Exception thrown when an HTTP 5xx is received.
 *
 * @since 1.3.0
 */
public class HttpServerErrorException extends HttpStatusCodeException {

	private static final long serialVersionUID = -2915754006618138282L;


	/**
	 * Construct a new instance of {@code HttpServerErrorException} based on
	 * a status code.
	 * @param statusCode the status code
	 */
	public HttpServerErrorException(int statusCode) {
		super(statusCode);
	}

	/**
	 * Construct a new instance of {@code HttpServerErrorException} based on
	 * a status code and status text.
	 * @param statusCode the status code
	 * @param statusText the status text
	 */
	public HttpServerErrorException(int statusCode, String statusText) {
		super(statusCode, statusText);
	}

	/**
	 * Construct a new instance of {@code HttpServerErrorException} based on
	 * a status code, status text, and response body content.
	 * @param statusCode the status code
	 * @param statusText the status text
	 * @param responseBody the response body content (may be {@code null})
	 * @param responseCharset the response body charset (may be {@code null})
	 * @since 3.0.5
	 */
	public HttpServerErrorException(int statusCode, String statusText,
			byte[] responseBody, Charset responseCharset) {

		super(statusCode, statusText, responseBody, responseCharset);
	}

}
