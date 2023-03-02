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

/**
 * 内部类，其中的私有成员变量与sdk_config_default.json中的JSON key对应。
 * 使用Jackson.getObjectMapper().readValue对把JSON文件中的数据转化为该对象
 */
class InternalConfigJsonHelper {
    private SignerConfigJsonHelper defaultSigner;
    private JsonIndex<SignerConfigJsonHelper, SignerConfig>[] serviceSigners;
    private JsonIndex<SignerConfigJsonHelper, SignerConfig>[] regionSigners;
    private JsonIndex<SignerConfigJsonHelper, SignerConfig>[] serviceRegionSigners;

    SignerConfigJsonHelper getDefaultSigner() {
        return defaultSigner;
    }

    void setDefaultSigner(SignerConfigJsonHelper defaultSigner) {
        this.defaultSigner = defaultSigner;
    }

    public JsonIndex<SignerConfigJsonHelper, SignerConfig>[] getServiceSigners() {
        return serviceSigners;
    }

    void setServiceSigners(JsonIndex<SignerConfigJsonHelper, SignerConfig> ... serviceSigners) {
        this.serviceSigners = serviceSigners;
    }

    public JsonIndex<SignerConfigJsonHelper, SignerConfig>[] getRegionSigners() {
        return regionSigners;
    }

    void setRegionSigners(JsonIndex<SignerConfigJsonHelper, SignerConfig> ... regionSigners) {
        this.regionSigners = regionSigners;
    }

    public JsonIndex<SignerConfigJsonHelper, SignerConfig>[] getServiceRegionSigners() {
        return serviceRegionSigners;
    }

    void setServiceRegionSigners(JsonIndex<SignerConfigJsonHelper, SignerConfig> ... serviceRegionSigners) {
        this.serviceRegionSigners = serviceRegionSigners;
    }
}