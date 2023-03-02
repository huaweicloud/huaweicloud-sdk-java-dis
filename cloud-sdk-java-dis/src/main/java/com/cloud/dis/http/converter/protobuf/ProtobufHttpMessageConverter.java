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

package com.cloud.dis.http.converter.protobuf;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.cloud.dis.http.converter.AbstractHttpMessageConverter;

/**
 * An {@code HttpMessageConverter} that reads and writes {@link com.google.protobuf.Message}s
 * using <a href="https://developers.google.com/protocol-buffers/">Google Protocol Buffers</a>.
 *
 * <p>By default, it supports {@code "application/x-protobuf"}, {@code "text/plain"},
 * {@code "application/json"}, {@code "application/xml"}.
 *
 * <p>To generate {@code Message} Java classes, you need to install the {@code protoc} binary.
 *
 * @since 1.3.0
 */
public class ProtobufHttpMessageConverter extends AbstractHttpMessageConverter<Message> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static final ContentType PROTOBUF = ContentType.create("application/x-protobuf", DEFAULT_CHARSET);



	private static final ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<Class<?>, Method>();

	private final ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();


	/**
	 * Construct a new instance.
	 */
	public ProtobufHttpMessageConverter() {
	    super(PROTOBUF, ContentType.TEXT_PLAIN, ContentType.APPLICATION_JSON, ContentType.APPLICATION_XML);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return Message.class.isAssignableFrom(clazz);
	}

	@Override
	protected ContentType getDefaultContentType(Message message) {
		return PROTOBUF;
	}

	@Override
	protected Message readInternal(Class<? extends Message> clazz, HttpEntity entity)
			throws IOException {

		ContentType contentType = ContentType.getOrDefault(entity);
		if (contentType == null) {
			contentType = PROTOBUF;
		}
		Charset charset = contentType.getCharset();
		if (charset == null) {
			charset = DEFAULT_CHARSET;
		}

		try {
			Message.Builder builder = getMessageBuilder(clazz);
			builder.mergeFrom(entity.getContent(), this.extensionRegistry);
			
			return builder.build();
		}
		catch (Exception ex) {
			throw new RuntimeException("Could not read Protobuf message: " + ex.getMessage(), ex);
		}
	}


	/**
	 * Create a new {@code Message.Builder} instance for the given class.
	 * <p>This method uses a ConcurrentHashMap for caching method lookups.
	 */
	private static Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
		Method method = methodCache.get(clazz);
		if (method == null) {
			method = clazz.getMethod("newBuilder");
			methodCache.put(clazz, method);
		}
		return (Message.Builder) method.invoke(clazz);
	}

}
