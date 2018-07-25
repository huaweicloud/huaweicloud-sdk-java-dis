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

package com.huaweicloud.dis.core.internal.config;

/**
 * An internal class used to represent a key-object pair for JSON persistence
 * purposes.
 */
final class JsonIndex<C extends Builder<T>, T> {
    private String key;
    private C config;

    JsonIndex(String key, C config) {
        this.key = key;
        this.config = config;
    }

    JsonIndex(String key) {
        this.key = key;
    }

    JsonIndex() {}

    public String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    public C getConfig() {
        return config;
    }

    T newReadOnlyConfig() {
        return config.build();
    }
}