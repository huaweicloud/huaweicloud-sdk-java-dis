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
import java.util.Properties;

import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.core.ClientParams;
import com.huaweicloud.dis.core.builder.SyncClientBuilder;

/**
 * Fluent builder for {@link com.bigdata.dis.sdk.DIS}.
 */
public final class DISClientBuilder extends SyncClientBuilder<DISClientBuilder, DIS>
{
    private Properties extendProperties = new Properties();
    
    private boolean dataEncryptEnabled;
    
    private boolean defaultClientCertAuthEnabled;
    
    private boolean dataCompressEnabled;
    
    public final DISClientBuilder withProperty(String key, String value)
    {
        extendProperties.put(key, value);
        return getSubclass();
    }
    
    public final DISClientBuilder withDataEncryptEnabled(boolean dataEncryptEnabled)
    {
        this.dataEncryptEnabled = dataEncryptEnabled;
        return getSubclass();
    }
    
    public final DISClientBuilder withDefaultClientCertAuthEnabled(boolean defaultClientCertAuthEnabled)
    {
        this.defaultClientCertAuthEnabled = defaultClientCertAuthEnabled;
        return getSubclass();
    }
    
    public final DISClientBuilder withDataCompressEnabled(boolean dataCompressEnabled)
    {
        this.dataCompressEnabled = dataCompressEnabled;
        return getSubclass();
    }
    
    public final boolean isDataEncryptEnabled()
    {
        return this.dataEncryptEnabled;
    }

    public final boolean isDefaultClientCertAuthEnabled()
    {
        return this.defaultClientCertAuthEnabled;
    }
    
    public final boolean isDataCompressEnabled()
    {
        return this.dataCompressEnabled;
    }

    /**
     * @return Create new instance of builder with all defaults set.
     */
    public static DISClientBuilder standard() {
        return new DISClientBuilder();
    }
    
    /**
     * 默认客户端，从dis.properties文件中读取配置项并生成 {@link DISConfig} 实例
     * 
     * @return Default client using the {@link com.bigdata.dis.sdk.DISConfig}
     */
    public static DIS defaultClient() {
        return standard().build();
    }

    @Override
    public DIS build()
    {
        return build(new DISConfig());
    }
    
    private DISConfig configDISConfig(DISConfig disConfig){
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
        disConfig.setDataCompressEnabled(dataCompressEnabled);
        
        Enumeration iter = extendProperties.propertyNames();// 得到配置文件的名字
        while (iter.hasMoreElements())
        {
            String strKey = (String)iter.nextElement();
            String strValue = extendProperties.getProperty(strKey);
            
            disConfig.set(strKey, strValue);
        }
        
        return disConfig;
    }
    
    /**
     * Construct a synchronous implementation of DISClient using the current builder configuration.
     *
     * @param params
     *        Current builder configuration represented as a parameter object.
     * @return Fully configured implementation of DISIngestionClient.
     */
    @Override
    protected DIS build(ClientParams clientParams) {
        DISConfig disConfig = configDISConfig((DISConfig)clientParams);
        return new DISClient(disConfig);
    }
    
}
