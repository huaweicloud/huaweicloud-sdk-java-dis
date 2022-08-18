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

package com.otccloud.dis.core;

import com.otccloud.dis.DISConfig;
import com.otccloud.dis.core.auth.AuthType;
import com.otccloud.dis.core.util.StringUtils;
import com.otccloud.dis.exception.DISClientException;

public class DISCredentials implements Cloneable {
    private String accessKeyId;

    private String secretKey;

    private String securityToken;

    private String dataPassword;

    private long timestamp;

    private String authToken;

    private String authType;

    public DISCredentials(DISConfig disConfig) {
        this(disConfig.getAK(), disConfig.getSK(), disConfig.getSecurityToken(), disConfig.getDataPassword(), disConfig.getAuthToken(), disConfig.getAuthType());
    }

    public DISCredentials(String accessKeyId, String secretKey) {
        this(accessKeyId, secretKey, null, null, null, null, System.currentTimeMillis());
    }

    public DISCredentials(String accessKeyId, String secretKey, long timestamp) {
        this(accessKeyId, secretKey, null, null, null, null, timestamp);
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken) {
        this(accessKeyId, secretKey, securityToken, null, null, null, System.currentTimeMillis());
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken, long timestamp) {
        this(accessKeyId, secretKey, securityToken, null, null, null, timestamp);
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken, String dataPassword, String authToken, String authType) {
        this(accessKeyId, secretKey, securityToken, dataPassword, authToken, authType, System.currentTimeMillis());
    }

    public DISCredentials(String accessKeyId, String secretKey, String securityToken, String dataPassword, String authToken, String authType,
                          long timestamp) {
        /*if (accessKeyId == null)
        {
            throw new IllegalArgumentException("Access key cannot be null.");
        }
        if (secretKey == null)
        {
            throw new IllegalArgumentException("Secret key cannot be null.");
        }*/
        if (StringUtils.isNullOrEmpty(authType)) {
            this.authType = AuthType.AKSK.getAuthType();
            if (accessKeyId == null) {
                throw new IllegalArgumentException("Access key cannot be null.");
            }
            if (secretKey == null) {
                throw new IllegalArgumentException("Secret key cannot be null.");
            }
        } else {
            this.authType = AuthType.AUTHTOKEN.getAuthType();
            if (StringUtils.isNullOrEmpty(authToken)) {
                throw new IllegalArgumentException("authToken cannot be null.");
            }
        }

        this.accessKeyId = accessKeyId;
        this.secretKey = secretKey;
        this.securityToken = securityToken;
        this.dataPassword = dataPassword;
        this.timestamp = timestamp;
        this.authToken = authToken;
        //this.authType = authType;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public String getDataPassword() {
        return dataPassword;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAuthToken() {
        return authToken;
    }

    //需求：在消费过程中可以重置X-Auth-Token
    public void setAuthToken(String authToken) {
        if (StringUtils.isNullOrEmpty(authType)) {
            throw new IllegalArgumentException("authType cannot be null.");
        }
        this.authToken = authToken;
    }


    public String getAuthType() {
        return authType;
    }

    @Override
    public DISCredentials clone() {
        try {
            return (DISCredentials) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("clone error.");
        }
    }
}