package com.huaweicloud.dis.util.cache;

import static com.huaweicloud.dis.Constants.CACHE_EXPIRETIME;
import static com.huaweicloud.dis.Constants.STREAMINFO_EXPIRETIME;
import static com.huaweicloud.dis.Constants.STREAMINFO_REAL_EXPIRETIME;

import com.huawei.bigdata.dataaccess.common.cache.Cache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.util.Map;

public class StreamInfoCache<K, V> implements Cache {

    public static final int DEFAULT_CONCURENCY_LEVEL = 32;

    private final ConcurrentLinkedHashMap map;

    public StreamInfoCache(int capacity) {
        this(capacity, DEFAULT_CONCURENCY_LEVEL);
    }

    public StreamInfoCache(int capacity, int concurrency) {
        map = new ConcurrentLinkedHashMap.Builder<K, V>().weigher(Weighers.singleton())
            .initialCapacity(capacity)
            .maximumWeightedCapacity(capacity)
            .concurrencyLevel(concurrency)
            .build();
    }

    public boolean isMemoryExpired(Map<String, String> streamInfo) {
        long cacheTime = Long.parseLong(streamInfo.get(STREAMINFO_EXPIRETIME));
        long expiresRealTime;
        if (streamInfo.get(STREAMINFO_REAL_EXPIRETIME) != null) {
            expiresRealTime = Long.parseLong(streamInfo.get(STREAMINFO_REAL_EXPIRETIME));
        } else {
            expiresRealTime = cacheTime;
        }
        return System.currentTimeMillis() > expiresRealTime || System.currentTimeMillis() > cacheTime;

    }

    @Override
    public void put(Object key, Object value) {
        map.put(key, value);
    }

    @Override
    public Object get(Object key) {
        Map<String, String> userInfo = (Map<String, String>) map.get(key);
        if (null != userInfo && !userInfo.isEmpty()) {
            return userInfo;
        } else {
            return null;
        }
    }

    @Override
    public void remove(Object key) {
        map.remove(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }
}
