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

package com.otccloud.dis;

import com.otccloud.dis.core.AsyncClientParams;
import com.otccloud.dis.core.builder.AsyncClientBuilder;
import com.otccloud.dis.core.builder.DefaultExecutorFactory;


/**
 * Fluent builder for {@link com.otccloud.dis.DISAsync}.
 */
public class DISClientAsyncBuilder extends AsyncClientBuilder<DISClientAsyncBuilder, DISAsync>
{

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
    	return build(null);
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
    	DISConfig disConfig = configDISConfig(null);
    	if(executorFactory == null){
            executorFactory = new DefaultExecutorFactory();
        }
    	return new DISClientAsync(disConfig, executorFactory.newExecutor());
    }
}
