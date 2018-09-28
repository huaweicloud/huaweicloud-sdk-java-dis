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

import java.util.Enumeration;

import com.huaweicloud.dis.core.ClientParams;
import com.huaweicloud.dis.core.builder.SyncClientBuilder;
import com.huaweicloud.dis.core.util.StringUtils;

/**
 * Fluent builder for {@link com.huaweicloud.dis.DIS}.
 */
public final class DISClientBuilder extends SyncClientBuilder<DISClientBuilder, DIS>
{
    
    /**
     * @return Create new instance of builder with all defaults set.
     */
    public static DISClientBuilder standard() {
        return new DISClientBuilder();
    }
    
    /**
     * 默认客户端，从dis.properties文件中读取配置项并生成 {@link DISConfig} 实例
     * 
     * @return Default client using the {@link com.huaweicloud.dis.DISConfig}
     */
    public static DIS defaultClient() {
        return standard().build();
    }

    @Override
    public DIS build()
    {
        return build(new DISConfig());
    }
    
	/**
	 * Construct a synchronous implementation of DISClient using the current builder
	 * configuration.
	 *
	 * @param clientParams
	 *            Current builder configuration represented as a parameter object.
	 * @return Fully configured implementation of DISIngestionClient.
	 */
    @Override
    protected DIS build(ClientParams clientParams) {
        DISConfig disConfig = configDISConfig((DISConfig)clientParams);
        return new DISClient(disConfig);
    }
    
}
