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

package com.huaweicloud.dis.core;

import com.huaweicloud.dis.DISConfig;

public class DISCredentials implements Cloneable
{
    private String accessKeyId;

    private String secretKey;

    private String securityToken;

    private String dataPassword;

    private long timestamp;

    public DISCredentials(DISConfig disConfig)
    {
        this(disConfig.getAK(), disConfig.getSK(), disConfig.getSecurityToken(), disConfig.getDataPassword());
    }

    public DISCredentials(String accessKeyId, String secretKey)
    {
        this(accessKeyId, secretKey, null, null, System.currentTimeMillis());
    }

    public DISCredentials(String accessKeyId, String secretKey, long timestamp)
    {
        this(accessKeyId, secretKey, null, null, timestamp);
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken)
    {
        this(accessKeyId, secretKey, securityToken, null, System.currentTimeMillis());
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken, long timestamp)
    {
        this(accessKeyId, secretKey, securityToken, null, timestamp);
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken, String dataPassword)
    {
        this(accessKeyId, secretKey, securityToken, dataPassword, System.currentTimeMillis());
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken, String dataPassword,
                          long timestamp)
    {
        if (accessKeyId == null)
        {
            throw new IllegalArgumentException("Access key cannot be null.");
        }
        if (secretKey == null)
        {
            throw new IllegalArgumentException("Secret key cannot be null.");
        }

        this.accessKeyId = accessKeyId;
        this.secretKey = secretKey;
        this.securityToken = securityToken;
        this.dataPassword = dataPassword;
        this.timestamp = timestamp;
    }

    public String getAccessKeyId()
    {
        return accessKeyId;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public String getSecurityToken()
    {
        return securityToken;
    }

    public String getDataPassword()
    {
        return dataPassword;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    @Override
    public DISCredentials clone()
    {
        try
        {
            return (DISCredentials)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new UnsupportedOperationException("clone error.");
        }
    }
}