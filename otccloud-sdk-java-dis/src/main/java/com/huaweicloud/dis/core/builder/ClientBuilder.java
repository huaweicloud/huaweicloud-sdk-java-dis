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

package com.otccloud.dis.core.builder;

import com.otccloud.dis.core.auth.credentials.Credentials;

/**
 * Base class for all service specific client builders.
 *
 * @param <Subclass> Concrete builder type, used for better fluent methods.
 * @param <TypeToBuild>  Type that this builder builds.
 */
public abstract class ClientBuilder<Subclass extends ClientBuilder, TypeToBuild>
{
    
    protected Credentials credentials;
    
    protected String ak;
    
    protected String sk;
    
    protected String region;
    
    protected String projectId;
    
    protected String endpoint;

    protected String authToken;

    protected String authType;
    
    public final Subclass withCredentials(Credentials credentials) {
        setCredentials(credentials);
        return getSubclass();
    }
    
    public final Subclass withAk(String ak) {
        setAk(ak);
        return getSubclass();
    }
    
    public final Subclass withSk(String sk) {
        setSk(sk);
        return getSubclass();
    }
    
    public final Subclass withRegion(String region) {
        setRegion(region);
        return getSubclass();
    }
    
    public final Subclass withProjectId(String projectId) {
        setProjectId(projectId);
        return getSubclass();
    }
    
    public final Subclass withEndpoint(String endpoint) {
        setEndpoint(endpoint);
        return getSubclass();
    }

    public final Subclass withAuthToken(String authToken) {
        setAuthToken(authToken);
        return getSubclass();
    }

    public final Subclass withAuthType(String authType) {
        setAuthType(authType);
        return getSubclass();
    }
    
    /*
     * Gets the AWSCredentialsProvider currently configured in the builder.
     */
    public final Credentials getCredentials() {
        return this.credentials;
    }
    
    public final void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    
    public final String getAk()
    {
        return ak;
    }

    public final void setAk(String ak)
    {
        this.ak = ak;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthType() {
        return authType;
    }
    
    public final String getSk()
    {
        return sk;
    }

    public final void setSk(String sk)
    {
        this.sk = sk;
    }
    
    public final String getRegion()
    {
        return region;
    }

    public final void setRegion(String region)
    {
        this.region = region;
    }

    public final void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public final String getAuthToken()
    {
        return authToken;
    }

    public final String getProjectId()
    {
        return projectId;
    }

    public final void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }
    
    public final String getEndpoint()
    {
        return endpoint;
    }

    public final void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    @SuppressWarnings("unchecked")
    protected final Subclass getSubclass() {
        return (Subclass) this;
    }
    
    /**
     * Builds a client with the configure properties.
     *
     * @return Client instance to make API calls with.
     */
    public abstract TypeToBuild build();
    
}
