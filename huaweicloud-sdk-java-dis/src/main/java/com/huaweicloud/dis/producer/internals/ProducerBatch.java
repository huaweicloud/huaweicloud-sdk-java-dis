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

import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



public final class ProducerBatch {

    private static final Logger log = LoggerFactory.getLogger(ProducerBatch.class);
    
    final ProduceRequestResult produceFuture;
    
    private List<PutRecordsRequestEntry> batchPutRecordsRequestEntrys = new ArrayList<PutRecordsRequestEntry>();
   
    private int relativeOffset;
    private long totolByteSize;

    private boolean appendClosed;
    
    private StreamPartition tp;
    
    private long lastAttemptMs;
    private long drainedMs;
    
    private long maxBatchSize;

    private int maxBatchCount;
    
    private List<Thunk> asyncHandlers = new CopyOnWriteArrayList<>();
    
    public ProducerBatch(StreamPartition tp, long maxBatchSize, int maxBatchCount) {
        this.tp = tp;
        this.maxBatchSize = maxBatchSize;
        this.maxBatchCount = maxBatchCount;
        this.produceFuture = new ProduceRequestResult(tp);
        this.lastAttemptMs = System.currentTimeMillis();
    }
    
    //调用者那里加了锁，这里不用考虑并发问题
    public FutureRecordsMetadata tryAppend(long timestamp, PutRecordsRequest putRecordsRequest, AsyncHandler<PutRecordsResult> callback)
    {
        if(appendClosed || isFull()){
            return null;
        }
        
        FutureRecordsMetadata futureRecordsMetadata = new FutureRecordsMetadata(produceFuture, relativeOffset, putRecordsRequest.getRecords().size());
        
        for(PutRecordsRequestEntry entry : putRecordsRequest.getRecords()){
            batchPutRecordsRequestEntrys.add(entry);
            totolByteSize+=entry.getData().array().length;
        }
        relativeOffset+=putRecordsRequest.getRecords().size();
        
        if(callback != null) {
            asyncHandlers.add(new Thunk(callback, futureRecordsMetadata));
        }
        
        return futureRecordsMetadata;
    }
    
    public void done(PutRecordsResult putRecordsResult, RuntimeException exception)
    {
        produceFuture.set(putRecordsResult, exception);

        for (Thunk thunk : asyncHandlers)
        {
            try
            {
                if (exception == null)
                {
                    thunk.callback.onSuccess(thunk.future.value());
                }
                else
                {
                    thunk.callback.onError(exception);
                }
            }
            catch (Exception e)
            {
                log.error("Error executing user-provided callback on message for stream {} : {}", tp.topic(), e.getMessage(), e);
            }
        }

        produceFuture.done();
    }
    
    public boolean isEmpty(){
        return relativeOffset == 0;
    }
    
    public void reenqueued(long now) {
//        attempts.getAndIncrement();
//        lastAttemptMs = Math.max(lastAppendTime, now);
//        lastAppendTime = Math.max(lastAppendTime, now);
//        retry = true;
        lastAttemptMs = now;
    }
    
    void drained(long nowMs) {
        this.drainedMs = Math.max(drainedMs, nowMs);
    }
    
    public int getRelativeOffset()
    {
        return relativeOffset;
    }

    public long getTotolByteSize()
    {
        return totolByteSize;
    }

    public boolean isFull()
    {
        return relativeOffset >= maxBatchCount || totolByteSize >= maxBatchSize;
    }

    public void closeForRecordAppends()
    {
        this.appendClosed = true;
    }

    public long waitedTimeMs(long nowMs)
    {
        return Math.max(0, nowMs - lastAttemptMs);
    }

    public List<PutRecordsRequestEntry> getBatchPutRecordsRequestEntrys()
    {
        return batchPutRecordsRequestEntrys;
    }

    public StreamPartition getTp()
    {
        return tp;
    }

    private static final class Thunk
    {
        final AsyncHandler<PutRecordsResult> callback;

        final FutureRecordsMetadata future;

        public Thunk(AsyncHandler<PutRecordsResult> callback, FutureRecordsMetadata future)
        {
            this.callback = callback;
            this.future = future;
        }
    }
    
}
