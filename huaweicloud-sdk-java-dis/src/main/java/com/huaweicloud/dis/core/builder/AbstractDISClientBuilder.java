package com.huaweicloud.dis.core.builder;

import java.util.Enumeration;
import java.util.Properties;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.util.AkSkUtils;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.http.Protocol;
import com.huaweicloud.dis.util.compress.CompressionType;

public abstract class AbstractDISClientBuilder<Subclass extends ClientBuilder, TypeToBuild> extends ClientBuilder<Subclass, TypeToBuild>{
	
	protected Properties extendProperties = new Properties();
    
	protected boolean dataEncryptEnabled;
    
	protected boolean defaultClientCertAuthEnabled;
    
	protected boolean dataCompressEnabled;
	
	protected boolean dataCacheEnabled;
	
	protected String dataCacheDir;
	
	protected String dataCacheDiskMaxSize;
	
	protected String dataCacheArchiveMaxSize;
	
	protected String dataCacheArchiveLifeCycle;
	
	protected String proxyHost;
	
	protected String proxyPort;
	
	protected Protocol proxyProtocol;
	
	protected String proxyUsername;
	
	protected String proxyPassword;
	
	protected String proxyDomain;
	
	protected String nonProxyHosts;

    protected boolean bodyCompressEnabled;

    protected CompressionType bodyCompressType;
	

	protected DISConfig configDISConfig(DISConfig disConfig){
		if(disConfig == null) {
			disConfig = new DISConfig();
		}
        if(akSkHolder!=null){
            AkSkUtils akSkUtils = AkSkUtils.getInstance();
            akSkUtils.setAkSkHolder(akSkHolder);
        }
        if (null != credentials) {
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
        if(!StringUtils.isNullOrEmpty(authToken))
            disConfig.setAuthToken(authToken);
        if(!StringUtils.isNullOrEmpty(authType))
            disConfig.setAuthType(authType);
        if (dataEncryptEnabled) {
        	disConfig.setDataEncryptEnabled(dataEncryptEnabled);
		}
        if (defaultClientCertAuthEnabled) {
        	disConfig.setDefaultClientCertAuthEnabled(defaultClientCertAuthEnabled);
		}
        if (dataCompressEnabled) {
        	disConfig.setDataCompressEnabled(dataCompressEnabled);
		}
        if (dataCacheEnabled) {
        	disConfig.setDataCacheEnabled(dataCacheEnabled);
		}
        if (!StringUtils.isNullOrEmpty(dataCacheDir))
        {
            disConfig.setDataCacheDir(dataCacheDir);
        }
        if (!StringUtils.isNullOrEmpty(dataCacheDiskMaxSize))
        {
            disConfig.setDataCacheDiskMaxSize(dataCacheDiskMaxSize);
        }
        if (!StringUtils.isNullOrEmpty(dataCacheArchiveMaxSize))
        {
            disConfig.setDataCacheArchiveMaxSize(dataCacheArchiveMaxSize);
        }
        if (!StringUtils.isNullOrEmpty(dataCacheArchiveLifeCycle))
        {
            disConfig.setDataCacheArchiveLifeCycle(dataCacheArchiveLifeCycle);
        }
        if (!StringUtils.isNullOrEmpty(proxyHost))
        {
            disConfig.setProxyHost(proxyHost);
        }
        if (!StringUtils.isNullOrEmpty(proxyPort))
        {
            disConfig.setProxyPort(proxyPort);
        }
        if (proxyProtocol != null)
        {
            disConfig.setProxyProtocol(proxyProtocol);
        }
        if (!StringUtils.isNullOrEmpty(proxyUsername))
        {
            disConfig.setProxyUsername(proxyUsername);
        }
        if (!StringUtils.isNullOrEmpty(proxyPassword))
        {
            disConfig.setProxyPassword(proxyPassword);
        }
        if (!StringUtils.isNullOrEmpty(proxyDomain))
        {
            disConfig.setProxyDomain(proxyDomain);
        }
        if (!StringUtils.isNullOrEmpty(nonProxyHosts))
        {
            disConfig.setNonProxyHosts(nonProxyHosts);
        }
        disConfig.setBodyCompressEnabled(bodyCompressEnabled);
        if (bodyCompressType != null)
        {
            disConfig.setBodyCompressType(bodyCompressType);
        }

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
    
    public final Subclass withDataCacheEnabled(boolean dataCacheEnabled)
    {
        this.dataCacheEnabled = dataCacheEnabled;
        return getSubclass();
    }
    
    public final Subclass withDataCacheDir(String dataCacheDir)
    {
        this.dataCacheDir = dataCacheDir;
        return getSubclass();
    }
    
    public final Subclass withDataCacheDiskMaxSize(String dataCacheDiskMaxSize)
    {
        this.dataCacheDiskMaxSize = dataCacheDiskMaxSize;
        return getSubclass();
    }
    
    public final Subclass withDataCacheArchiveMaxSize(String dataCacheArchiveMaxSize)
    {
        this.dataCacheArchiveMaxSize = dataCacheArchiveMaxSize;
        return getSubclass();
    }
    
    public final Subclass withDataCacheArchiveLifeCycle(String dataCacheArchiveLifeCycle)
    {
        this.dataCacheArchiveLifeCycle = dataCacheArchiveLifeCycle;
        return getSubclass();
    }
    
    public final Subclass withProxyHost(String proxyHost)
    {
        this.proxyHost = proxyHost;
        return getSubclass();
    }
    
    public final Subclass withProxyPort(String proxyPort)
    {
        this.proxyPort = proxyPort;
        return getSubclass();
    }
    
    public final Subclass withProxyProtocol(Protocol proxyProtocol)
    {
        this.proxyProtocol = proxyProtocol;
        return getSubclass();
    }
    
    public final Subclass withProxyUsername(String proxyUsername)
    {
        this.proxyUsername = proxyUsername;
        return getSubclass();
    }
    
    public final Subclass withProxyPassword(String proxyPassword)
    {
        this.proxyPassword = proxyPassword;
        return getSubclass();
    }
    
    public final Subclass withProxyDomain(String proxyDomain)
    {
        this.proxyDomain = proxyDomain;
        return getSubclass();
    }
    
    public final Subclass withNonProxyHosts(String nonProxyHosts)
    {
        this.nonProxyHosts = nonProxyHosts;
        return getSubclass();
    }

    public final Subclass withBodyCompressEnabled(boolean bodyCompressEnabled)
    {
        this.bodyCompressEnabled = bodyCompressEnabled;
        return getSubclass();
    }

    public final Subclass withBodyCompressType(CompressionType compressType)
    {
        this.bodyCompressType = compressType;
        return getSubclass();
    }
}
