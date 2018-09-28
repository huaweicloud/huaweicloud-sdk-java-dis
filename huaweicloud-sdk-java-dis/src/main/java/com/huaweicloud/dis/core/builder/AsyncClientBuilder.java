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

package com.huaweicloud.dis.core.builder;

import java.util.concurrent.ExecutorService;

import com.huaweicloud.dis.core.AsyncClientParams;


public abstract class AsyncClientBuilder<Subclass extends AsyncClientBuilder, TypeToBuild> extends AbstractDISClientBuilder<Subclass, TypeToBuild>
{
    
    protected ExecutorFactory executorFactory;
    
    /**
     * Sets a custom executor service factory to use for the async clients. The factory will be
     * called for each async client created through the builder.
     *
     * @param executorFactory Factory supplying new instances of {@link ExecutorService}
     * @return This object for method chaining.
     */
    public final Subclass withExecutorFactory(ExecutorFactory executorFactory) {
        setExecutorFactory(executorFactory);
        return getSubclass();
    }
    
    /**
     * @return The {@link ExecutorFactory} currently configured by the client.
     */
    public final ExecutorFactory getExecutorFactory() {
        return executorFactory;
    }
    
    /**
     * Sets a custom executor service factory to use for the async clients. The factory will be
     * called for each async client created through the builder.
     *
     * @param executorFactory Factory supplying new instances of {@link ExecutorService}
     */
    public final void setExecutorFactory(ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }
    
	/**
	 * Builds a client with the configure properties.
	 *
	 * @param asyncClientParams
	 *            Client params
	 * @return Client instance to make API calls with.
	 */
    protected abstract TypeToBuild build(AsyncClientParams asyncClientParams);
    
}
