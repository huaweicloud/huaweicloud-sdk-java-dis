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

package com.cloud.dis.util;

import com.cloud.dis.util.config.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 获取SDK版本号
 */
public class VersionUtils
{
    
    private static final Logger LOG = LoggerFactory.getLogger(VersionUtils.class);
    
    static final String VERSION_FILE = "version.properties";
    
    static final String DEFAULT_VERSION = "unknown";
    
    static final String DEFAULT_PLATFORM = "java";
    
    /** SDK version info */
    private static String version;
    
    /** SDK platform info */
    private static String platform;
    
    /* 获取版本信息 */
    public static String getVersion()
    {
        if (version == null)
        {
            synchronized (VersionUtils.class)
            {
                if (version == null)
                    initializeVersion();
            }
        }
        return version;
    }
    
    /* 获取平台信息 */
    public static String getPlatform()
    {
        if (platform == null)
        {
            synchronized (VersionUtils.class)
            {
                if (platform == null)
                    initializeVersion();
            }
        }
        return platform;
    }
    
    /* 从版本配置文件读取配置文件信息 */
    private static void initializeVersion()
    {
        try
        {
            ConfigurationUtils versionConfiguation = new ConfigurationUtils(VERSION_FILE);
            
            version = versionConfiguation.getProperty("version", "");
            platform = versionConfiguation.getProperty("platform", "");
        }
        catch (Exception e)
        {
            LOG.info("Unable to load version for the running SDK: " + e.getMessage());
            version = DEFAULT_VERSION;
            platform = DEFAULT_PLATFORM;
        }
    }
    
}
