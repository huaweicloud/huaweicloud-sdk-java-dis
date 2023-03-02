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

package com.cloud.dis.core.restresource;

import com.cloud.dis.core.util.StringUtils;

/**
 * 代表Record资源
 *
 * @see RestResource
 */
public class RecordResource extends RestResource
{
    
    private static final String DEFAULT_RESOURCE_NAME = "records";
    
    private String resourceName;
    
    private String resourceId;
    
    public RecordResource(String resourceId)
    {
        this.resourceId = resourceId;
    }
    
    public RecordResource(String resourceName, String resourceId)
    {
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }
    
    @Override
    public String getResourceName()
    {
        if (StringUtils.isNullOrEmpty(resourceName))
        {
            return DEFAULT_RESOURCE_NAME;
        }
        return resourceName;
    }

    @Override
    public String getResourceId()
    {
        return resourceId;
    }
    
    @Override
    public String getAction() {
        return null;
    }
    
}
