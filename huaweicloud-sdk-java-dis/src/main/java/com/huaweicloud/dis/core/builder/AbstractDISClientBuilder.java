package com.huaweicloud.dis.core.builder;

import java.util.Enumeration;
import java.util.Properties;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.util.StringUtils;

public abstract class AbstractDISClientBuilder<Subclass extends ClientBuilder, TypeToBuild> extends ClientBuilder<Subclass, TypeToBuild>{
	
	protected Properties extendProperties = new Properties();
    
	protected boolean dataEncryptEnabled;
    
	protected boolean defaultClientCertAuthEnabled;
    
	protected boolean dataCompressEnabled;
	

	protected DISConfig configDISConfig(DISConfig disConfig){
		if(disConfig == null) {
			disConfig = new DISConfig();
		}
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
	

    public final Subclass withProperty(String key, String value)
    {
        extendProperties.put(key, value);
        return getSubclass();
    }
    
    public final Subclass withDataEncryptEnabled(boolean dataEncryptEnabled)
    {
        this.dataEncryptEnabled = dataEncryptEnabled;
        return getSubclass();
    }
    
    public final Subclass withDefaultClientCertAuthEnabled(boolean defaultClientCertAuthEnabled)
    {
        this.defaultClientCertAuthEnabled = defaultClientCertAuthEnabled;
        return getSubclass();
    }
    
    public final Subclass withDataCompressEnabled(boolean dataCompressEnabled)
    {
        this.dataCompressEnabled = dataCompressEnabled;
        return getSubclass();
    }
}
