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

package com.huaweicloud.dis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.core.AsyncClientParams;
import com.huaweicloud.dis.core.builder.AsyncClientBuilder;
import com.huaweicloud.dis.core.builder.ExecutorFactory;


/**
 * Fluent builder for {@link com.huaweicloud.dis.DISAsync}.
 */
public class DISClientAsyncBuilder extends AsyncClientBuilder<DISClientAsyncBuilder, DISAsync>
{
    
    private boolean dataEncryptEnabled;
    
    private boolean defaultClientCertAuthEnabled;
    
    public final DISClientAsyncBuilder withDataEncryptEnabled(boolean dataEncryptEnabled)
    {
        setDataEncryptEnabled(dataEncryptEnabled);
        return getSubclass();
    }
    
    public final DISClientAsyncBuilder withDefaultClientCertAuthEnabled(boolean defaultClientCertAuthEnabled)
    {
        setDefaultClientCertAuthEnabled(defaultClientCertAuthEnabled);
        return getSubclass();
    }
    
    public final boolean isDataEncryptEnabled()
    {
        return dataEncryptEnabled;
    }

    public final void setDataEncryptEnabled(boolean dataEncryptEnabled)
    {
        this.dataEncryptEnabled = dataEncryptEnabled;
    }
    
    public final boolean isDefaultClientCertAuthEnabled()
    {
        return defaultClientCertAuthEnabled;
    }

    public final void setDefaultClientCertAuthEnabled(boolean defaultClientCertAuthEnabled)
    {
        this.defaultClientCertAuthEnabled = defaultClientCertAuthEnabled;
    }

    /**
     * @return Create new instance of builder with all defaults set.
     */
    public static DISClientAsyncBuilder standard() {
        return new DISClientAsyncBuilder();
    }
    
    /**
     * @return Default client using the {@link DISConfig}
     */
    public static DISAsync defaultClient() {
        return standard().build();
    }
    
    @Override
    public DISAsync build() {
        DISConfig disConfig = new DISConfig();
        
        if (null != credentials)
        {
            disConfig.setCredentials(credentials);
        }
        if (!StringUtils.isNullOrEmpty(ak))
            disConfig.setAK(ak);
        if (!StringUtils.isNullOrEmpty(sk))
            disConfig.setSK(sk);
        if (!StringUtils.isNullOrEmpty(projectId))
            disConfig.setProjectId(projectId);
        if (!StringUtils.isNullOrEmpty(region))
            disConfig.setRegion(region);
        if (!StringUtils.isNullOrEmpty(endpoint))
            disConfig.setEndpoint(endpoint);
        disConfig.setDataEncryptEnabled(dataEncryptEnabled);
        disConfig.setDefaultClientCertAuthEnabled(defaultClientCertAuthEnabled);
        
        if(executorFactory == null){
            executorFactory = new DefaultExecutorFactory();
        }
        return new DISClientAsync(disConfig, executorFactory.newExecutor());
    }

	/**
	 * Construct a asynchronous implementation of DisIngestionClientAsync using the
	 * current builder configuration.
	 *
	 * @param asyncClientParams
	 *            Current builder configuration represented as a parameter object.
	 * @return Fully configured implementation of DisIngestionClientAsync.
	 */
    @Override
    protected DISAsync build(AsyncClientParams asyncClientParams)
    {
        // TODO 待完善
        return null;
    }
    
    private static class DefaultExecutorFactory implements ExecutorFactory{
        @Override
        public ExecutorService newExecutor()
        {
            return Executors.newFixedThreadPool(100);
        }
        
    }
}
