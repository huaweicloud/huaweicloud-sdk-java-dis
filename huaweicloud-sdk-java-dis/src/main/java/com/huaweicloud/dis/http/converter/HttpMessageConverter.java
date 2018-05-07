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

package com.huaweicloud.dis.http.converter;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

/**
 * Strategy interface that specifies a converter that can convert from and to HTTP requests and responses.
 *
 */
public interface HttpMessageConverter<T> {

	/**
	 * Indicates whether the given class can be read by this converter.
	 * @param clazz the class to test for readability
	 * @param contentType the content type to read, can be {@code null} if not specified.
	 * Typically the value of a {@code Content-Type} header.
	 * @return {@code true} if readable; {@code false} otherwise
	 */
	boolean canRead(Class<?> clazz, ContentType contentType);

	/**
	 * Return the list of {@link ContentType} objects supported by this converter.
	 * @return the list of supported content types
	 */
	List<ContentType> getSupportedContentTypes();

	/**
	 * Read an object of the given type form the given input message, and returns it.
	 * @param clazz the type of object to return. This type must have previously been passed to the
	 * {@link #canRead canRead} method of this interface, which must have returned {@code true}.
	 * @param entity the HTTP entity {@link HttpEntity} to read from
	 * @return the converted object
	 * @throws IOException in case of I/O errors
	 */
	T read(Class<? extends T> clazz, HttpEntity entity)
			throws IOException;

}
