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

package com.otccloud.dis.http.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base class for most {@link HttpMessageConverter} implementations.
 *
 */
public abstract class AbstractHttpMessageConverter<T> implements HttpMessageConverter<T> {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	private List<ContentType> supportedContentTypes = Collections.emptyList();

	private Charset defaultCharset;


	/**
	 * Construct an {@code AbstractHttpMessageConverter} with no supported content types.
	 * @see #setSupportedContentTypes(List)
	 */
	protected AbstractHttpMessageConverter() {
	}

	/**
	 * Construct an {@code AbstractHttpMessageConverter} with one supported content type.
	 * @param supportedContentType the supported content type
	 */
	protected AbstractHttpMessageConverter(ContentType supportedContentType) {
	    setSupportedContentTypes(Collections.singletonList(supportedContentType));
	}

	/**
	 * Construct an {@code AbstractHttpMessageConverter} with multiple supported content types.
	 * @param supportedContentTypes the supported content types
	 */
	protected AbstractHttpMessageConverter(ContentType... supportedContentTypes) {
		setSupportedContentTypes(Arrays.asList(supportedContentTypes));
	}

	/**
	 * Construct an {@code AbstractHttpMessageConverter} with a default charset and
	 * multiple supported content types.
	 * @param defaultCharset the default character set
	 * @param supportedContentTypes the supported content types
	 * @since 4.3
	 */
	protected AbstractHttpMessageConverter(Charset defaultCharset, ContentType... supportedContentTypes) {
		this.defaultCharset = defaultCharset;
		setSupportedContentTypes(Arrays.asList(supportedContentTypes));
	}


	/**
	 * Set the list of {@link ContentType} objects supported by this converter.
	 * 
	 * @param supportedContentTypes List of supported content types.
	 */
	public void setSupportedContentTypes(List<ContentType> supportedContentTypes) {
		this.supportedContentTypes = new ArrayList<ContentType>(supportedContentTypes);
	}

	@Override
	public List<ContentType> getSupportedContentTypes() {
		return Collections.unmodifiableList(this.supportedContentTypes);
	}

	/**
	 * Set the default character set, if any.
	 * 
	 * @param defaultCharset Default charset.
	 * @since 1.3.0
	 */
	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	/**
	 * Return the default character set, if any.
	 * 
	 * @return The default charset.
	 * @since 1.3.0
	 */
	public Charset getDefaultCharset() {
		return this.defaultCharset;
	}


	/**
	 * This implementation checks if the given class is {@linkplain #supports(Class) supported}
	 */
	@Override
	public boolean canRead(Class<?> clazz, ContentType contentType) {
		return supports(clazz) && canRead(contentType);
	}

	/**
	 * Returns {@code true} if any of the {@linkplain #setSupportedContentTypes(List)
	 * supported} content types include the given content type.
	 * @param contentType the content type to read, can be {@code null} if not specified.
	 * Typically the value of a {@code Content-Type} header.
	 * @return {@code true} if the supported content types include the content type,
	 * or if the content type is {@code null}
	 */
	protected boolean canRead(ContentType contentType) {
		if (contentType == null) {
			return true;
		}
		for (ContentType supportedContentType : getSupportedContentTypes()) {
			if (supportedContentType.getMimeType().equals(contentType.getMimeType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This implementation simple delegates to {@link #readInternal(Class, HttpEntity)}.
	 * Future implementations might add some default behavior, however.
	 */
	@Override
	public final T read(Class<? extends T> clazz, HttpEntity entity) throws IOException {
		return readInternal(clazz, entity);
	}

	/**
	 * Returns the default content type for the given type.
	 * <p>By default, this returns the first element of the
	 * {@link #setSupportedContentTypes(List) supportedContentTypes} property, if any.
	 * Can be overridden in subclasses.
	 * @param t the type to return the content type for
	 * @return the content type, or {@code null} if not known
	 * @throws IOException in case of I/O errors
	 */
	protected ContentType getDefaultContentType(T t) throws IOException {
		List<ContentType> contentTypes = getSupportedContentTypes();
		return (!contentTypes.isEmpty() ? contentTypes.get(0) : null);
	}

	/**
	 * Returns the content length for the given type.
	 * <p>By default, this returns {@code null}, meaning that the content length is unknown.
	 * Can be overridden in subclasses.
	 * @param t the type to return the content length for
	 * @param contentType content type
	 * @return the content length, or {@code null} if not known
	 * @throws IOException in case of I/O errors
	 */
	protected Long getContentLength(T t, ContentType contentType) throws IOException {
		return null;
	}


	/**
	 * Indicates whether the given class is supported by this converter.
	 * @param clazz the class to test for support
	 * @return {@code true} if supported; {@code false} otherwise
	 */
	protected abstract boolean supports(Class<?> clazz);

	/**
	 * Abstract template method that reads the actual object. Invoked from {@link #read}.
	 * @param clazz the type of object to return
	 * @param entity the HTTP entity to read from
	 * @return the converted object
	 * @throws IOException in case of I/O errors
	 */
	protected abstract T readInternal(Class<? extends T> clazz, HttpEntity entity)
			throws IOException;

}
