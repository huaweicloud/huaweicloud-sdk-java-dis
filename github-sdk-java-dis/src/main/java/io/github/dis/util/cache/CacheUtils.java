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

package io.github.dis.util.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.github.dis.DISConfig;
import io.github.dis.iface.data.request.PutRecordsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheUtils
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
    
    private static final int DEFAULT_THREAD_POOL_SIZE = 100;
    
    private static final ExecutorService executorService =
        Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE, new ThreadFactory()
        {
            public Thread newThread(Runnable r)
            {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });
    
    public static void putToCache(PutRecordsRequest putRecordsRequest, DISConfig disConfig)
    {
        executorService.submit(new Runnable()
        {
            
            @Override
            public void run()
            {
                try
                {
                    CacheManager cacheManager = CacheManager.getInstance(disConfig);
                    cacheManager.putToCache(putRecordsRequest);
                }
                catch (Exception e)
                {
                    // Failed to put failed records to local cache file, continue.
                    LOGGER.error("Failed to write failed records to local cache file.", e);
                }
            }
        });
    }
    
}
