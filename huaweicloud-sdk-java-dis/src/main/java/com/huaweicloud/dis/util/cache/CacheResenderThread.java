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

package com.huaweicloud.dis.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存重发线程
 *
 */
public class CacheResenderThread extends Thread
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
    
    public CacheResenderThread(String name)
    {
        setDaemon(true);
        setName("Cache-ResenderThread-" + name);
    }
    
    @Override
    public void run()
    {
        LOGGER.info("Starting cache resender thread.");
        
        // TODO 归档缓存数据重传
        
        
        LOGGER.info("Terminate cache resender thread.");
    }
}
