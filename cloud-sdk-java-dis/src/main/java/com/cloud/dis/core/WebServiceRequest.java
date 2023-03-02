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

package com.cloud.dis.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.annotation.Contract;

import com.cloud.dis.core.auth.credentials.Credentials;

/**
 * Base class for all user facing web service requests.
 */
@Contract()
public abstract class WebServiceRequest implements Cloneable {
    public static final WebServiceRequest NOOP = new WebServiceRequest() {
    };
    /**
     * Arbitrary options storage for individual {@link WebServiceRequest}s. This
     * field is not intended to be used by clients.
     */
    private final RequestClientOptions requestClientOptions = new RequestClientOptions();

    /**
     * The optional credentials to use for this request - overrides the
     * default credentials set at the client level.
     */
    private Credentials credentials;

    /**
     * A map of custom header names to header values.
     */
    private Map<String, String> customRequestHeaders;

    /**
     * Sets the optional credentials to use for this request, overriding the
     * default credentials set at the client level.
     *
     * @param credentials The optional security credentials to use for this request,
     *                    overriding the default credentials set at the client level.
     */
    public void setRequestCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Returns the optional credentials to use to sign this request, overriding
     * the default credentials set at the client level.
     *
     * @return The optional credentials to use to sign this request, overriding
     * the default credentials set at the client level.
     */
    public Credentials getRequestCredentials() {
        return credentials;
    }

    /**
     * Internal only method for accessing private, internal request parameters.
     * Not intended for direct use by callers.
     *
     * @return private, internal request parameter information.
     */
    public Map<String, String> copyPrivateRequestParameters() {
        return new HashMap<String, String>();
    }

    /**
     * Gets the options stored with this request object. Intended for internal
     * use only.
     * 
     * @return request client options.
     */
    public RequestClientOptions getRequestClientOptions() {
        return requestClientOptions;
    }

    /**
     * Returns an immutable map of custom header names to header values.
     *
     * @return The immutable map of custom header names to header values.
     */
    public Map<String, String> getCustomRequestHeaders() {
        if (customRequestHeaders == null) {
            return null;
        }
        return Collections.unmodifiableMap(customRequestHeaders);
    }

    /**
     * <p>
     * Put a new custom header to the map of custom header names to custom
     * header values, and return the previous value if the header has already
     * been set in this map.
     * </p>
     * NOTE: Custom header values set via this method will overwrite any
     * conflicting values coming from the request parameters.
     *
     * @param name  The name of the header to add
     * @param value The value of the header to add
     * @return the previous value for the name if it was set, null otherwise
     */
    public String putCustomRequestHeader(String name, String value) {
        if (customRequestHeaders == null) {
            customRequestHeaders = new HashMap<String, String>();
        }
        return customRequestHeaders.put(name, value);
    }

    /**
     * Convenient method to return the optional read limit for mark-and-reset
     * during retries.
     * 
     * @return read limit.
     */
    public final int getReadLimit() {
        return requestClientOptions.getReadLimit();
    }

    /**
     * Copies the internal state of this base class to that of the target
     * request.
     *
     * @param target target request
     * @param <T> Generic type
     * @return the target request
     */
    protected final <T extends WebServiceRequest> T copyBaseTo(T target) {
        if (customRequestHeaders != null) {
            for (Map.Entry<String, String> e : customRequestHeaders.entrySet()) {
                target.putCustomRequestHeader(e.getKey(), e.getValue());
            }
        }
        target.setRequestCredentials(credentials);
        requestClientOptions.copyTo(target.getRequestClientOptions());
        return target;
    }

    /**
     * Creates a shallow clone of this request. Explicitly does <em>not</em>
     * clone the deep structure of the request object.
     *
     * @see Object#clone()
     */
    @Override
    public WebServiceRequest clone() {
        try {
            return (WebServiceRequest) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", e);
        }
    }
}