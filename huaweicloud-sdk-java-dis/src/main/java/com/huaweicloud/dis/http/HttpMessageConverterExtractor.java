/*
 * Copyright 2002-2015 the original author or authors.
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
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.converter.HttpMessageConverter;



/**
 * Response extractor that uses the given {@linkplain HttpMessageConverter entity converters}
 * to convert the response into a type {@code T}.
 *
 * @since 1.3.0
 */
public class HttpMessageConverterExtractor<T> implements ResponseExtractor<T> {

	private final Type responseType;

	private final Class<T> responseClass;

	private final List<HttpMessageConverter<?>> messageConverters;

	private final Log logger;


	/**
	 * Create a new instance of the {@code HttpMessageConverterExtractor} with the given response
	 * type and message converters. The given converters must support the response type.
	 */
	public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
		this((Type) responseType, messageConverters);
	}

	/**
	 * Creates a new instance of the {@code HttpMessageConverterExtractor} with the given response
	 * type and message converters. The given converters must support the response type.
	 */
	public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
		this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
	}

	@SuppressWarnings("unchecked")
	HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters, Log logger) {
		this.responseType = responseType;
		this.responseClass = (responseType instanceof Class) ? (Class<T>) responseType : null;
		this.messageConverters = messageConverters;
		this.logger = logger;
	}


	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public T extractData(HttpResponse response) throws IOException {
	    long contentLength = response.getEntity().getContentLength();
        if (contentLength == 0)
        {
            return null;
        }
		ContentType contentType = getContentType(response);

		for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
			if (this.responseClass != null) {
				if (messageConverter.canRead(this.responseClass, contentType)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Reading [" + this.responseClass.getName() + "] as \"" +
								contentType + "\" using [" + messageConverter + "]");
					}
					return (T) messageConverter.read((Class) this.responseClass, response.getEntity());
				}
			}
		}

		throw new DISClientException("Could not extract response: no suitable HttpMessageConverter found " +
				"for response type [" + this.responseType + "] and content type [" + contentType + "]");
	}

	private ContentType getContentType(HttpResponse response) {
	    ContentType contentType = ContentType.get(response.getEntity());
        if (contentType == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("No Content-Type header found, defaulting to application/octet-stream");
            }
            contentType = ContentType.APPLICATION_OCTET_STREAM;
        }
        
        return contentType;
	}

}
