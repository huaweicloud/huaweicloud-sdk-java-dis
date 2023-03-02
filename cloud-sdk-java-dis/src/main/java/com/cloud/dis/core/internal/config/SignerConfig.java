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

package com.cloud.dis.core.internal.config;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

/**
 * Signer configuration.
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class SignerConfig {

    private final String signerType;

    SignerConfig(String signerType) {
        this.signerType = signerType;
    }

    SignerConfig(SignerConfig from) {
        this.signerType = from.getSignerType();
    }

    public String getSignerType() {
        return signerType;
    }

    @Override
    public String toString() {
        return signerType;
    }
}