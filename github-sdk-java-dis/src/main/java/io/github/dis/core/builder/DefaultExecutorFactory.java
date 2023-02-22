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

package io.github.dis.core.builder;

import io.github.dis.Constants;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultExecutorFactory implements ExecutorFactory
{
    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final AtomicInteger count = new AtomicInteger(1);

    private final ExecutorService executorService;

    public DefaultExecutorFactory()
    {
        this(Constants.DEFAULT_THREAD_POOL_SIZE);
    }

    public DefaultExecutorFactory(int poolSize)
    {
        this(poolSize, "dis-pool");
    }

    public DefaultExecutorFactory(int poolSize, String namePrefix)
    {
        if (namePrefix != null)
        {
            namePrefix += "-" + poolNumber.getAndIncrement() + "-";
        }
        final String finalNamePrefix = namePrefix;
        this.executorService = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory()
                {
                    @Override
                    public Thread newThread(Runnable r)
                    {
                        Thread thread = Executors.defaultThreadFactory().newThread(r);
                        if (finalNamePrefix != null)
                        {
                            thread.setName(finalNamePrefix + String.format("%03d", count.getAndIncrement()));
                        }
                        thread.setDaemon(true);
                        return thread;
                    }
                });
    }

    @Override
    public ExecutorService newExecutor()
    {
        return executorService;
    }
}