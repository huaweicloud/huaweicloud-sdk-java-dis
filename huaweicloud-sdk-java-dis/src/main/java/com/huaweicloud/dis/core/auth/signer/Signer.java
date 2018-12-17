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

package com.huaweicloud.dis.core.auth.signer;

import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.auth.credentials.Credentials;

import java.util.Properties;

/**
 * A strategy for applying cryptographic signatures to a request, proving
 * that the request was made by someone in posession of the given set of
 * credentials without transmitting the secret key over the wire.
 */
public interface Signer {
    /**
     * Sign the given request with the given set of credentials. Modifies the
     * passed-in request to apply the signature.
     *
     * @param request     The request to sign.
     * @param credentials The credentials to sign the request with.
     */
    void sign(Request<?> request, Credentials credentials);

    /**
     * Sign the given request with the given set of credentials. Modifies the
     * passed-in request to apply the signature.
     *
     * @param request     The request to sign.
     * @param credentials The credentials to sign the request with.
     * @param prop Some config
     */
    void sign(Request<?> request, Credentials credentials, Properties prop);
}