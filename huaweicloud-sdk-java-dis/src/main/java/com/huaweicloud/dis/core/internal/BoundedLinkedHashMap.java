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

package com.huaweicloud.dis.core.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A bounded linked hash map that would remove the eldest entry when the map
 * size exceeds a configurable maximum.
 */
final class BoundedLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    private static final long serialVersionUID = 1L;
    private final int maxSize;
    BoundedLinkedHashMap(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * {@inheritDoc}
     * 
     * Returns true if the size of this map exceeds the maximum.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

    /**
     * Returns the maximum size of this map beyond which the eldest entry
     * will get removed.
     */
    int getMaxSize() {
        return maxSize;
    }
}