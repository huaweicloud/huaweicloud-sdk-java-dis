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

package com.otccloud.dis.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a tool for reading/writing configurations,each kind of configuration is listed below(from highest priority to
 * lowest): 1.the configuration set explicitly in the code. 2.the configuration loaded from file. 3.the default
 * configuration. and the configuration with higher priority will overwrite the ones with lower priorities.
 * 用来读写配置的工具，配置的优先级由低到高是默认的配置，从配置文件读取的配置，在程序中动态进行的配置；优先级高的会覆盖优先级低的配置
 * 
 */
public class ConfigurationUtils
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtils.class);
    
    private HashMap<String, String> propertyMapFromFile = new HashMap<String, String>();;
    
    private HashMap<String, String> propertyMapExplicitlyDefined = new HashMap<String, String>();;
    
    /**
     * load configuration from a file
     * 
     * @param configurationFileName the file path , do nothing if it's null
     * @throws IOException IO exception throws when reading configuration file
     */
    public ConfigurationUtils(String configurationFileName) throws IOException
    {
        
        if (configurationFileName == null)
        {
            return;
        }
        
        InputStream inputStream = null;
        try
        {
            ClassLoader classLoader = ConfigurationUtils.class.getClassLoader();
            if (null != classLoader)
            {
                inputStream = classLoader.getResourceAsStream(configurationFileName);
                if(inputStream != null){
                    LOGGER.debug("get from classLoader");
                }
            }
            
            if (null == inputStream && this.getClass() != null)
            {
                inputStream = this.getClass().getResourceAsStream(configurationFileName);
                if(inputStream != null){
                    LOGGER.debug("get from class");
                }
            }
            
            if (null == inputStream && configurationFileName.startsWith("/") && null != classLoader){
                inputStream = classLoader.getResourceAsStream("." + configurationFileName);
                if(inputStream != null){
                    LOGGER.debug("get from ./");
                }
            }
            
            if (null == inputStream && configurationFileName.startsWith("/") && null != classLoader){
                inputStream = classLoader.getResourceAsStream(configurationFileName.substring(1));
                if(inputStream != null){
                    LOGGER.debug("get from no /");
                }
            }
            
            if (null == inputStream)
            {
                inputStream = ClassLoader.getSystemResourceAsStream(configurationFileName);
                if(inputStream != null){
                    LOGGER.debug("get from ClassLoader");
                }
            }
            
            if (null == inputStream)
            {
                File file = new File(configurationFileName);
                if(file.exists()){
                    inputStream = new FileInputStream(configurationFileName);    
                    if(inputStream != null){
                        LOGGER.debug("get from file.");
                    }
                }
            }
            
            if (null == inputStream)
            {
                LOGGER.warn("configuration file {} not found, ignore it.", configurationFileName);
                return;
            }
            
            Properties props = new Properties();
            props.load(inputStream);
            for (Enumeration propNames = props.propertyNames(); propNames.hasMoreElements();)
            {
                String propName = (String)propNames.nextElement();
                propertyMapFromFile.put(propName, props.getProperty(propName));
            }
            LOGGER.debug("propertyMapFromFile size : {}", propertyMapFromFile.size());
        }
        finally
        {
            if (null != inputStream)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("IOException: " + e);
                }
            }
        }
        return;
    }
    
    /**
     * Get the value string of a property, if the property does not exist,the defaultValue is returned.
     * 
     * @param propertyName property name
     * @param defaultValue default value
     * @return property value
     */
    
    public String getProperty(String propertyName, String defaultValue)
    {
        if (propertyMapExplicitlyDefined.containsKey(propertyName))
        {
            return propertyMapExplicitlyDefined.get(propertyName);
        }
        else if (propertyMapFromFile.containsKey(propertyName))
        {
            return propertyMapFromFile.get(propertyName);
        }
        else
        {
            return defaultValue;
        }
    }
    
    public String getProperty(String propertyName)
    {
        return getProperty(propertyName, null);
    }
    
    /**
     * set a property's value.
     * 
     * @param propertyName property name
     * @param value peoperty value
     */
    
    public void setProperty(String propertyName, String value)
    {
        propertyMapExplicitlyDefined.put(propertyName, value);
    }
    
}
