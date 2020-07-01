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

package com.huaweicloud.dis.core;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

import com.huaweicloud.dis.core.http.HttpMethodName;

/**
 * <p>
 * Default implementation of the {@linkplain Request} interface.
 * </p>
 * This class is only intended for internal use inside the AWS client libraries.
 * Callers shouldn't ever interact directly with objects of this class.
 */
@Contract()
public class DefaultRequest<T> implements Request<T> {

    /**
     * The resource path being requested
     */
    private String resourcePath;

    /**
     * Map of the parameters being sent as part of this request.
     * <p/>
     * Note that a LinkedHashMap is used, since we want to preserve the
     * insertion order so that members of a list parameter will still be ordered
     * by their indices when they are marshalled into the query string.
     */
    private Map<String, String> parameters = new LinkedHashMap<String, String>();

    /**
     * Map of the headers included in this request
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * The service endpoint to which this request should be sent
     */
    private URI endpoint;

    /**
     * The name of the service to which this request is being sent
     */
    private String serviceName;

    /**
     * The original, user facing request object which this internal request
     * object is representing
     */
    private final WebServiceRequest originalRequest;

    /**
     * The HTTP method to use when sending this request.
     */
    private HttpMethodName httpMethod = HttpMethodName.POST;

    /**
     * An optional stream from which to read the request payload.
     */
    private InputStream content;

    /**
     * An optional time offset to account for clock skew
     */
    private int timeOffset;

    /**
     * Constructs a new DefaultRequest with the specified service name and the
     * original, user facing request object.
     *
     * @param originalRequest The original, user facing, request being represented by
     *                        this internal request object.
     * @param serviceName     The name of the service to which this request is being sent.
     */
    public DefaultRequest(WebServiceRequest originalRequest, String serviceName) {
        this.serviceName = serviceName;
        this.originalRequest = originalRequest == null ? WebServiceRequest.NOOP : originalRequest;
    }

    /**
     * Constructs a new DefaultRequest with the specified service name and no
     * specified original, user facing request object.
     *
     * @param serviceName The name of the service to which this request is being sent.
     */
    public DefaultRequest(String serviceName) {
        this(null, serviceName);
    }


    /**
     * Returns the original, user facing request object which this internal
     * request object is representing.
     *
     * @return The original, user facing request object which this request
     * object is representing.
     */
    public WebServiceRequest getOriginalRequest() {
        return originalRequest;
    }

    /**
     * @see Request#addHeader(String, String)
     */
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * @see Request#getHeaders()
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @see Request#setResourcePath(String)
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * @see Request#getResourcePath()
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @see Request#addParameter(String, String)
     */
    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    /**
     * @see Request#getParameters()
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * @see Request#withParameter(String, String)
     */
    public Request<T> withParameter(String name, String value) {
        addParameter(name, value);
        return this;
    }

    /**
     * @see Request#getHttpMethod()
     */
    public HttpMethodName getHttpMethod() {
        return httpMethod;
    }

    /**
     * @see Request#setHttpMethod
     */
    public void setHttpMethod(HttpMethodName httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * @see Request#setEndpoint(java.net.URI)
     */
    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @see Request#getEndpoint()
     */
    public URI getEndpoint() {
        return endpoint;
    }

    /**
     * @see Request#getServiceName()
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @see Request#getContent()
     */
    public InputStream getContent() {
        return content;
    }

    /**
     * @see Request#setContent(java.io.InputStream)
     */
    public void setContent(InputStream content) {
        this.content = content;
    }

    /**
     * @see Request#setHeaders(java.util.Map)
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    /**
     * @see Request#setParameters(java.util.Map)
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    /**
     * @see Request#getTimeOffset
     */
    public int getTimeOffset() {
        return timeOffset;
    }

    /**
     * @see Request#setTimeOffset(int)
     */
    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * @see Request#setTimeOffset(int)
     */
    public Request<T> withTimeOffset(int timeOffset) {
        setTimeOffset(timeOffset);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getHttpMethod()).append(" ");
        builder.append(getEndpoint()).append(" ");
        String resourcePath = getResourcePath();

        if (resourcePath == null) {
            builder.append("/");
        } else {
            if (!resourcePath.startsWith("/")) {
                builder.append("/");
            }
            builder.append(resourcePath);
        }
        builder.append(" ");
        if (!getParameters().isEmpty()) {
            builder.append("Parameters: (");
            for (String key : getParameters().keySet()) {
                String value = getParameters().get(key);
                builder.append(key).append(": ").append(value).append(", ");
            }
            builder.append(") ");
        }

        if (!getHeaders().isEmpty()) {
            builder.append("Headers: (");
            for (String key : getHeaders().keySet()) {
                String value = getHeaders().get(key);
                builder.append(key).append(": ").append(value).append(", ");
            }
            builder.append(") ");
        }

        return builder.toString();
    }
}