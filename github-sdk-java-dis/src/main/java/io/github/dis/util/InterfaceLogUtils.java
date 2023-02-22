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
package io.github.dis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class InterfaceLogUtils
{
    public final static Logger INTERFACE_LOGGER = LoggerFactory.getLogger("dis-interface");
    public final static Logger INTERFACE_STATISTICS_LOGGER_ = LoggerFactory.getLogger("dis-interface-statistics");
    public final static Logger INTERFACE_DETAIL_LOGGER = LoggerFactory.getLogger("dis-interface-detail");
    public final static Boolean IS_INTERFACE_LOGGER_ENABLED = INTERFACE_LOGGER.isTraceEnabled();
    public final static Boolean IS_INTERFACE_STATISTICS_LOGGER_ENABLED = INTERFACE_STATISTICS_LOGGER_.isTraceEnabled();
    public final static AtomicLong TOTAL_REQUEST_TIMES = new AtomicLong(0);
    public final static AtomicLong TOTAL_REQUEST_SUCCESSFUL_TIMES = new AtomicLong(0);
    public final static AtomicLong TOTAL_REQUEST_FAILED_TIMES = new AtomicLong(0);
    public final static AtomicLong TOTAL_REQUEST_POSTPONE_MILLIS = new AtomicLong(0);
    public final static AtomicLong TOTAL_REQUEST_ENTITY_SIZE = new AtomicLong(0);

    public DecimalFormat df = new DecimalFormat("0.00");
    private final static InterfaceLogUtils INSTANCE = new InterfaceLogUtils();
    private long startTime;

    public InterfaceLogUtils()
    {
        if (!IS_INTERFACE_STATISTICS_LOGGER_ENABLED)
        {
            return;
        }
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory()
        {
            @Override
            public Thread newThread(Runnable r)
            {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setName("dis-interface-statistics");
                thread.setDaemon(true);
                return thread;
            }
        });

        startTime = System.currentTimeMillis();
        scheduledExecutorService.scheduleAtFixedRate(new InnerRun(), 1000, 1000, TimeUnit.MILLISECONDS);
    }

    private class InnerRun implements Runnable
    {
        long lastCurrTotalRequestTimes = TOTAL_REQUEST_TIMES.get();
        long lastCurrTotalRequestSuccessfulTimes = TOTAL_REQUEST_SUCCESSFUL_TIMES.get();
        long lastCurrTotalRequestFailedTimes = TOTAL_REQUEST_FAILED_TIMES.get();
        long lastCurrTotalRequestPostponeMillis = TOTAL_REQUEST_POSTPONE_MILLIS.get();
        long lastCurrTotalRequestEntitySize = TOTAL_REQUEST_ENTITY_SIZE.get();

        @Override
        public void run()
        {
            try
            {
                long currContinueSecond = (System.currentTimeMillis() - startTime) / 1000;
                long currTotalRequestTimes = TOTAL_REQUEST_TIMES.get();
                long currTotalRequestSuccessfulTimes = TOTAL_REQUEST_SUCCESSFUL_TIMES.get();
                long currTotalRequestFailedTimes = TOTAL_REQUEST_FAILED_TIMES.get();
                long currTotalRequestPostponeMillis = TOTAL_REQUEST_POSTPONE_MILLIS.get();
                long currTotalRequestEntitySize = TOTAL_REQUEST_ENTITY_SIZE.get();

                long currTPS = currTotalRequestSuccessfulTimes - lastCurrTotalRequestSuccessfulTimes;
                INTERFACE_STATISTICS_LOGGER_.trace("TPS [{}] / [{}]({}/{}), " +
                                "\tLatency [{}] / [{}]({}/{}), " +
                                "\tRequestSize [{}] / [{}]({}/{}), " +
                                "\tRequestTotal [{}](successful {} / failed {})",
                        // TPS
                        currTPS,
                        df.format(1f * currTotalRequestSuccessfulTimes / currContinueSecond),
                        currTotalRequestSuccessfulTimes,
                        currContinueSecond,
                        // Latency
                        currTPS == 0 ? 0 : (currTotalRequestPostponeMillis - lastCurrTotalRequestPostponeMillis) / currTPS,
                        df.format(1f * currTotalRequestSuccessfulTimes == 0 ? 0 : currTotalRequestPostponeMillis / currTotalRequestSuccessfulTimes),
                        currTotalRequestPostponeMillis,
                        currTotalRequestSuccessfulTimes,
                        // Size
                        currTotalRequestEntitySize - lastCurrTotalRequestEntitySize,
                        df.format(1f * currTotalRequestEntitySize == 0 ? 0 : currTotalRequestEntitySize / currContinueSecond),
                        currTotalRequestEntitySize,
                        currContinueSecond,
                        // Total
                        currTotalRequestTimes,
                        currTotalRequestSuccessfulTimes,
                        currTotalRequestFailedTimes
                );

                lastCurrTotalRequestTimes = currTotalRequestTimes;
                lastCurrTotalRequestSuccessfulTimes = currTotalRequestSuccessfulTimes;
                lastCurrTotalRequestFailedTimes = currTotalRequestFailedTimes;
                lastCurrTotalRequestPostponeMillis = currTotalRequestPostponeMillis;
                lastCurrTotalRequestEntitySize = currTotalRequestEntitySize;
            }
            catch (Exception e)
            {
                INTERFACE_STATISTICS_LOGGER_.error(e.getMessage(), e);
            }
        }
    }
}
