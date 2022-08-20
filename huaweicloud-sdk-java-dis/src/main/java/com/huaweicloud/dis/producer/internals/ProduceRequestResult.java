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

package com.huaweicloud.dis.producer.internals;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.huaweicloud.dis.iface.data.response.PutRecordsResult;



/**
 * A class that models the future completion of a produce request for a single partition. There is one of these per
 * partition in a produce request and it is shared by all the {@link FutureRecordsMetadata} instances that are batched together
 * for the same partition in the request.
 */
public final class ProduceRequestResult {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final StreamPartition topicPartition;

    private volatile PutRecordsResult putRecordsResult;
    
    private volatile RuntimeException error;

    /**
     * Create an instance of this class.
     *
     * @param topicPartition The topic and partition to which this record set was sent was sent
     */
    public ProduceRequestResult(StreamPartition topicPartition) {
        this.topicPartition = topicPartition;
    }

    /**
     * Set the result of the produce request.
     *
     * @param putRecordsResult Result of the PutRecords operation returned by the service.
     * @param error The error that occurred if there was one, or null
     */
    public void set(PutRecordsResult putRecordsResult, RuntimeException error) {
        this.putRecordsResult = putRecordsResult;
        this.error = error;
    }

    /**
     * Mark this request as complete and unblock any threads waiting on its completion.
     */
    public void done() {
        if (putRecordsResult == null && error == null)
            throw new IllegalStateException("The method `set` must be invoked before this method.");
        this.latch.countDown();
    }

    /**
     * Await the completion of this request
     *
     * @throws InterruptedException InterruptedException when being interrupted.
     */
    public void await() throws InterruptedException {
        latch.await();
    }

    /**
     * Await the completion of this request (up to the given time interval)
     * @param timeout The maximum time to wait
     * @param unit The unit for the max time
     * @return true if the request completed, false if we timed out
     * @throws InterruptedException InterruptedException when being interrupted.
     */
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    /**
     * The base offset for the request (the first offset in the record set)
     *
     * @return The base offset for the request.
     */
    public PutRecordsResult putRecordsResult() {
        return putRecordsResult;
    }

    /**
     * The error thrown (generally on the server) while processing this request
     *
     * @return The error thrown while processing this request.
     */
    public RuntimeException error() {
        return error;
    }

    /**
     * The topic and partition to which the record was appended
     *
     * @return The topic and partition to which the record was appended.
     */
    public StreamPartition topicPartition() {
        return topicPartition;
    }

	/**
	 * Has the request completed?
	 *
	 * @return {@code true} completed {@code false} otherwise
	 */
    public boolean completed() {
        return this.latch.getCount() == 0L;
    }
}
