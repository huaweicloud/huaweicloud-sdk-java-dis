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

package com.cloud.dis.core.internal;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * A bounded cache that has a FIFO eviction policy when the cache is full.
 * 
 * @param <T>
 *            value type
 */
@Contract(threading = ThreadingBehavior.SAFE)
public final class FIFOCache<T> {
    private final BoundedLinkedHashMap<String, T> map;
    private final ReadLock rlock;
    private final WriteLock wlock;
    
    /**
     * @param maxSize
     *            the maximum number of entries of the cache
     */
    public FIFOCache(final int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize " + maxSize
                    + " must be at least 1");
        }
        map = new BoundedLinkedHashMap<String, T>(maxSize);
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        rlock = lock.readLock();
        wlock = lock.writeLock();
    }

    /**
     * Adds an entry to the cache, evicting the ealiest entry if necessary.
     */
    public T add(String key, T value) {
        wlock.lock();
        try {
            return map.put(key, value);
        } finally {
            wlock.unlock();
        }
    }

    /** Returns the value of the given key; or null of no such entry exists. */
    public T get(String key) {
        rlock.lock();
        try {
            return map.get(key);
        } finally {
            rlock.unlock();
        }
    }

    /**
     * Returns the current size of the cache.
     */
    public int size() {
        rlock.lock();
        try {
            return map.size();
        } finally {
            rlock.unlock();
        }
    }

    /**
     * Returns the maximum size of the cache.
     */
    public int getMaxSize() {
        return map.getMaxSize();
    }

    @Override
    public String toString() {
        rlock.lock();
        try {
            return map.toString();
        } finally {
            rlock.unlock();
        }
    }
}