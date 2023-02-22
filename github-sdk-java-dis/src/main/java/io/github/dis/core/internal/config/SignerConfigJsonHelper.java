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

package io.github.dis.core.internal.config;

/**
 * An internal class used to build {@link SignerConfig} after this
 * class per se has been unmarshalled from JSON. This class allows us to make
 * use of Jackson without the need to write any special parser or json
 * marshaller/unmarshaller.
 */
class SignerConfigJsonHelper implements Builder<SignerConfig> {

    private String signerType;

    SignerConfigJsonHelper(String signerType) {
        this.signerType = signerType;
    }

    SignerConfigJsonHelper() {}

    public String getSignerType() {
        return signerType;
    }

    void setSignerType(String signerType) {
        this.signerType = signerType;
    }

    @Override
    public SignerConfig build() {
        return new SignerConfig(signerType);
    }
}