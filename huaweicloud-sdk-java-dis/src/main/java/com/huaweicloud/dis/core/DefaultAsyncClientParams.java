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

import java.util.concurrent.ExecutorService;

import com.cloud.sdk.auth.credentials.Credentials;

/**
 * Default implementation of {@code AsyncClientParams}
 */
public class DefaultAsyncClientParams extends AsyncClientParams
{
    
    private Credentials credentials;
    
    private String region;
    
    private String projectId;
    
    private ExecutorService executorService;
    
    @Override
    public Credentials getCredential()
    {
        return this.credentials;
    }

    @Override
    public String getRegion()
    {
        return this.region;
    }

    @Override
    public String getProjectId()
    {
        return this.projectId;
    }

    @Override
    public ExecutorService getExecutor()
    {
        return this.executorService;
    }
    
}
