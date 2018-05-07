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

package com.huaweicloud.dis.http.converter.json;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import com.huaweicloud.dis.http.converter.AbstractHttpMessageConverter;
import com.huaweicloud.dis.http.converter.HttpMessageConverter;
import com.huaweicloud.dis.util.JsonUtils;
import com.huaweicloud.dis.util.StreamUtils;


/**
 * Implementation of {@link HttpMessageConverter} that can read JSON using {@link JsonUtils}.
 *
 * <p>This converter can be used to bind to typed beans or untyped {@code HashMap}s.
 * By default, it supports {@code application/json} with {@code UTF-8} character set.
 *
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


	private final List<Charset> availableCharsets;


	/**
	 * A default constructor that uses {@code "UTF-8"} as the default charset.
	 * @see #JsonHttpMessageConverter(Charset)
	 */
	public JsonHttpMessageConverter() {
		this(DEFAULT_CHARSET);
	}

	/**
	 * A constructor accepting a default charset to use if the requested content
	 * type does not specify one.
	 */
	public JsonHttpMessageConverter(Charset defaultCharset) {
		super(defaultCharset, ContentType.APPLICATION_JSON);
		this.availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
	}


	
	@Override
    public boolean canRead(Class<?> clazz, ContentType contentType) {
        return canRead(contentType);
    }

	@Override
	public boolean supports(Class<?> clazz) {
	    throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpEntity entity) throws IOException {
		Charset charset = getContentTypeCharset(ContentType.getOrDefault(entity));
		return JsonUtils.jsonToObj(StreamUtils.copyToString(entity.getContent(), charset), clazz);
	}

	

	/**
	 * Return the list of supported {@link Charset}s.
	 * <p>By default, returns {@link Charset#availableCharsets()}.
	 * Can be overridden in subclasses.
	 * @return the list of accepted charsets
	 */
	protected List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}

	private Charset getContentTypeCharset(ContentType contentType) {
		if (contentType != null && contentType.getCharset() != null) {
			return contentType.getCharset();
		}
		else {
			return getDefaultCharset();
		}
	}

}
