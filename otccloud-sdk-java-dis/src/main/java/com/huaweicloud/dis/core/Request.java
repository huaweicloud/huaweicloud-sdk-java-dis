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

package com.otccloud.dis.core;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import com.otccloud.dis.core.http.HttpMethodName;

/**
 * Represents a request being sent to an Web Service, including the
 * parameters being sent as part of the request, the endpoint to which the
 * request should be sent, etc.
 * <p>
 * This class is only intended for internal use inside the client libraries.
 * Callers shouldn't ever interact directly with objects of this class.
 *
 * @param <T>
 *            The type of original, user facing request represented by this
 *            request.
 */
public interface Request<T> {

    /**
     * Sets the specified header for this request.
     *
     * @param name
     *            The name of the header to set.
     * @param value
     *            The header's value.
     */
    void addHeader(String name, String value);

    /**
     * Returns a map of all the headers included in this request.
     *
     * @return A map of all the headers included in this request.
     */
    Map<String, String> getHeaders();
    
	/**
	 * Sets all headers, clearing any existing ones.
	 * 
	 * @param headers
	 *            A map of headers
	 */
    void setHeaders(Map<String, String> headers);

    /**
     * Sets the path to the resource being requested.
     *
     * @param path
     *            The path to the resource being requested.
     */
    void setResourcePath(String path);

    /**
     * Returns the path to the resource being requested.
     *
     * @return The path to the resource being requested.
     */
    String getResourcePath();

    /**
     * Adds the specified request parameter to this request.
     *
     * @param name
     *            The name of the request parameter.
     * @param value
     *            The value of the request parameter.
     */
    void addParameter(String name, String value);

    /**
     * Adds the specified request parameter to this request, and returns the
     * updated request object.
     *
     * @param name
     *            The name of the request parameter.
     * @param value
     *            The value of the request parameter.
     *
     * @return The updated request object.
     */
    Request<T> withParameter(String name, String value);

    /**
     * Returns a map of all parameters in this request.
     *
     * @return A map of all parameters in this request.
     */
    Map<String, String> getParameters();
    
	/**
	 * Sets all parameters, clearing any existing values.
	 * 
	 * @param parameters
	 *            A map of parameters
	 */
    void setParameters(Map<String, String> parameters);

    /**
     * Returns the service endpoint (ex: "https://ec2.sdk.com") to which
     * this request should be sent.
     *
     * @return The service endpoint to which this request should be sent.
     */
    URI getEndpoint();

    /**
     * Sets the service endpoint (ex: "https://ec2.sdk.com") to which this
     * request should be sent.
     *
     * @param endpoint
     *            The service endpoint to which this request should be sent.
     */
    void setEndpoint(URI endpoint);

	/**
	 * Returns the HTTP method (GET, POST, etc) to use when sending this
	 * request.
	 * 
	 * @return The HTTP method to use when sending this request.
	 */
    HttpMethodName getHttpMethod();

	/**
	 * Sets the HTTP method (GET, POST, etc) to use when sending this request.
	 * 
	 * @param httpMethod
	 *            The HTTP method to use when sending this request.
	 */
    void setHttpMethod(HttpMethodName httpMethod);

	/**
	 * Returns the optional stream containing the payload data to include for
	 * this request.  Not all requests will contain payload data.
	 * 
	 * @return The optional stream containing the payload data to include for
	 *         this request.
	 */
    InputStream getContent();

	/**
	 * Sets the optional stream containing the payload data to include for this
	 * request. Not all requests will contain payload data.
	 * 
	 * @param content
	 *            The optional stream containing the payload data to include for
	 *            this request.
	 */
    void setContent(InputStream content);

    /**
     * Returns the name of the Amazon service this request is for.
     *
     * @return The name of the Amazon service this request is for.
     */
    String getServiceName();

    /**
     * Returns the original, user facing request object which this internal
     * request object is representing.
     *
     * @return The original, user facing request object which this request
     *         object is representing.
     */
    WebServiceRequest getOriginalRequest();
    
    /**
     * Returns the optional value for time offset for this request.  This
     * will be used by the signer to adjust for potential clock skew.  
     * Value is in seconds, positive values imply the current clock is "fast",
     * negative values imply clock is slow.
     * 
     * @return The optional value for time offset (in seconds) for this request.
     */
    int getTimeOffset();
    
    /**
     * Sets the optional value for time offset for this request.  This
     * will be used by the signer to adjust for potential clock skew.  
     * Value is in seconds, positive values imply the current clock is "fast",
     * negative values imply clock is slow.
     *
     * @param timeOffset
     *            The optional value for time offset (in seconds) for this request.
     */
    void setTimeOffset(int timeOffset);
    
    
	/**
	 * Sets the optional value for time offset for this request. This will be used
	 * by the signer to adjust for potential clock skew. Value is in seconds,
	 * positive values imply the current clock is "fast", negative values imply
	 * clock is slow.
	 *
	 * @param timeOffset
	 *            The time offset
	 * @return The updated request object.
	 */
    Request<T> withTimeOffset(int timeOffset);
}