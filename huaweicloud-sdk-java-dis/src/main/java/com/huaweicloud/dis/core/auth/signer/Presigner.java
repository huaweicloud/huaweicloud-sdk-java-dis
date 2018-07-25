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

import java.util.Date;

import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.auth.credentials.Credentials;

/**
 * A request signer that has special-case logic to presign requests, generating
 * a URL which embeds the signature suitable for hyperlinking.
 */
public interface Presigner {
    /**
     * Signs the request by adding the signature to the URL rather than as a
     * header. This method is expected to modify the passed-in request to
     * add the signature.
     *
     * @param request     The request to sign.
     * @param credentials The credentials to sign it with.
     * @param expiration  The time when this presigned URL will expire.
     */
    void presignRequest(Request<?> request, Credentials credentials, Date expiration);
}