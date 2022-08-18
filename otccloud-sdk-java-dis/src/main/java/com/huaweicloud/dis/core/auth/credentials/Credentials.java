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

package com.otccloud.dis.core.auth.credentials;

/**
 * Provides access to the credentials used for accessing services: HW
 * access key ID and secret access key. These credentials are used to securely
 * sign requests to services.
 * <p>
 * A basic implementation of this interface is provided in
 * {@link BasicCredentials}, but callers are free to provide their own
 * implementation, for example, to load credentials from an encrypted file.
 * <p>
 */
public interface Credentials {

    /**
     * Returns the access key ID for this credentials object.
     * 
     * @return The access key ID for this credentials object.
     */
    String getAccessKeyId();

    /**
     * Returns the secret access key for this credentials object.
     * 
     * @return The secret access key for this credentials object.
     */
    String getSecretKey();

}