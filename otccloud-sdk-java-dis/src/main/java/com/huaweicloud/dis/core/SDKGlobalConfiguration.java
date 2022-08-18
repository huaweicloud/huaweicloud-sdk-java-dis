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

/**
 * SDKGlobalConfiguration is to configure any global settings
 */
public class SDKGlobalConfiguration {
    /////////////////////// System Properties ///////////////////////

    /**
     * Disable validation of server certificates when using the HTTPS protocol.
     * This should ONLY be used to do quick smoke tests against endpoints which
     * don't yet have valid certificates; it should NEVER be used in
     * production.
     */
    public static final String DISABLE_CERT_CHECKING_SYSTEM_PROPERTY = "com.sdk.disableCertChecking";

    /**
     * System property name for the access key ID
     */
    public static final String ACCESS_KEY_SYSTEM_PROPERTY = "accessKeyId";

    /**
     * System property name for the secret key
     */
    public static final String SECRET_KEY_SYSTEM_PROPERTY = "secretKey";


    /////////////////////// Environment Variables ///////////////////////
    /**
     * Environment variable name for the access key ID
     */
    public static final String ACCESS_KEY_ENV_VAR = "ACCESS_KEY_ID";

    /**
     * Alternate environment variable name for the access key ID
     */
    public static final String ALTERNATE_ACCESS_KEY_ENV_VAR = "ACCESS_KEY";

    /**
     * Environment variable name for the secret key
     */
    public static final String SECRET_KEY_ENV_VAR = "SECRET_KEY";

    /**
     * Alternate environment variable name for the secret key
     */
    public static final String ALTERNATE_SECRET_KEY_ENV_VAR = "SECRET_ACCESS_KEY";
}