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

package com.huaweicloud.dis.core.restresource;

import com.huaweicloud.dis.core.util.StringUtils;

/**
 * Fluent builder for Restful resource path.
 * 
 * @see {@code RestResource}
 */
public final class ResourcePathBuilder
{
 
    private static final String VERSION = "v2";
    
    private String version;
    
    private String projectId;
    
    private StringBuilder resourcePath;
    
    public final ResourcePathBuilder withVersion(String version)
    {
        setVersion(version);
        return this;
    }
    
    public final ResourcePathBuilder withProjectId(String projectId)
    {
        setProjectId(projectId);
        return this;
    }

    /**
     * 绑定资源并按照绑定顺序生成RESTful资源路径，生成形式：/pResource/pResourceId/cResource/cResourceId...
     * 
     * @param restResource
     * @return
     */
    public final ResourcePathBuilder withResource(RestResource restResource)
    {
        setResourcePath(restResource);
        return this;
    }
    
    public final void setResourcePath(RestResource restResource)
    {
        String resourceId = restResource.getResourceId();
        String resourceName = restResource.getResourceName();
        String action = restResource.getAction();
        
        if (null == resourcePath)
        {
            resourcePath = new StringBuilder("/");
        }
        
        resourcePath = resourcePath.append(resourceName).append("/");
        if (!StringUtils.isNullOrEmpty(resourceId))
        {
            resourcePath.append(resourceId).append("/");
        }
    
        if (!StringUtils.isNullOrEmpty(action))
        {
            resourcePath.append(action).append("/");
        }
    }
    
    public final void setVersion(String version)
    {
        this.version = version;
    }
    
    public final void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }
    
    /**
     * @return Create new instance of builder with all defaults set.
     */
    public static ResourcePathBuilder standard()
    {
        return new ResourcePathBuilder();
    }
    
    public String build()
    {
        version = (StringUtils.isNullOrEmpty(version)) ? VERSION : version;
        String prefix = "/" + version;
        
        // FIXME 暂时取消校验
        /*if (!StringUtils.isNullOrEmpty(projectId))
        {
            prefix = prefix + "/" + projectId;
        }*/
        prefix = prefix + "/" + projectId;
        
        return prefix + resourcePath.toString();
    }
    
}
