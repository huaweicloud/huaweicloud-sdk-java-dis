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

package com.cloud.dis.core.auth.credentials;

/**
 * Basic implementation of the Credentials interface that allows callers to
 * pass in the access key and secret access in the constructor.
 */
public class BasicCredentials implements Credentials {

    private final String accessKey;
    private final String secretKey;

    /**
     * Constructs a new BasicCredentials object, with the specified
     * access key and secret key.
     *
     * @param accessKey The access key.
     * @param secretKey The secret access key.
     */
    public BasicCredentials(String accessKey, String secretKey) {
        if (accessKey == null) {
            throw new IllegalArgumentException("Access key cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null.");
        }

        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /* (non-Javadoc)
     * @see Credentials#getAccessKeyId()
     */
    public String getAccessKeyId() {
        return accessKey;
    }

    /* (non-Javadoc)
     * @see Credentials#getSecretKey()
     */
    public String getSecretKey() {
        return secretKey;
    }

}