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

package com.cloud.dis.core.auth.signer.internal;

public final class SignerConstants {

    public static final String LINE_SEPARATOR = "\n";

    public static final String SDK_NAME = "SDK";

    public static final String SDK_TERMINATOR = "sdk_request";

    public static final String SDK_SIGNING_ALGORITHM = "SDK-HMAC-SHA256";

    /** Seconds in a week, which is the max expiration time Sig-v4 accepts */
    public static final long PRESIGN_URL_MAX_EXPIRATION_SECONDS = 60 * 60 * 24 * 7;

    public static final String X_SDK_SECURITY_TOKEN = "X-Sdk-Security-Token";

    public static final String X_SDK_CREDENTIAL = "X-Sdk-Credential";

    public static final String X_SDK_DATE = "X-Sdk-Date";

    public static final String X_SDK_EXPIRES = "X-Sdk-Expires";

    public static final String X_SDK_SIGNED_HEADER = "X-Sdk-SignedHeaders";

    public static final String X_SDK_CONTENT_SHA256 = "x-sdk-content-sha256";

    public static final String X_SDK_SIGNATURE = "X-Sdk-Signature";

    public static final String X_SDK_ALGORITHM = "X-Sdk-Algorithm";

    public static final String AUTHORIZATION = "Authorization";

    public static final String HOST = "Host";

    public static final String SIGN_PROVIDER = "sign.provider";
}