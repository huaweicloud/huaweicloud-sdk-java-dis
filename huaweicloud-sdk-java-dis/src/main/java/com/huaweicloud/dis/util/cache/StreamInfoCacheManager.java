package com.huaweicloud.dis.util.cache;

public class StreamInfoCacheManager {
    private static final int CACHE_SIZE = 10000;

    private static StreamInfoCache streamInfoInstance = new StreamInfoCache(CACHE_SIZE);

    public static StreamInfoCache getAkUserInfoCacheInstance() {
        return streamInfoInstance;
    }

}
