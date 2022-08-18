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

package com.otccloud.dis.producer.internals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otccloud.dis.core.handler.AsyncHandler;
import com.otccloud.dis.iface.data.request.PutRecordsRequest;
import com.otccloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.otccloud.dis.iface.data.response.PutRecordsResult;
import com.otccloud.dis.util.CopyOnWriteMap;


/**
 * 用来进行Record的缓冲
 * 
 */
public final class RecordAccumulator {

    private static final Logger log = LoggerFactory.getLogger(RecordAccumulator.class);

    private volatile boolean closed;
    private final AtomicInteger flushesInProgress;
    private final AtomicInteger appendsInProgress;
    private final long maxBatchSize;
    private final int maxBatchCount;
    private final long maxBufferSize;
    private final int maxBufferCount;
    private final long retryBackoffMs;
    
    //TODO 这个批的分类，按照partition是最好的。但是当前客户端传partitionKey，可能没法确定数据在哪个分区。除非维护上传流的metadata
    private final ConcurrentMap<StreamPartition, Deque<ProducerBatch>> batches;
    
    private AtomicInteger bufferCount = new AtomicInteger(0);
    private AtomicLong bufferSize = new AtomicLong(0);
    
    private Map<StreamPartition, Future<PutRecordsResult>> onSendingPartitions = Collections.synchronizedMap(new HashMap<>());
    
    private boolean orderByPartition;//分片串行发送，使数据按分片保序
    /**
     * 
     * @param maxBatchSize 最大批量大小
     * @param maxBatchCount 最大批量计数
     * @param maxBufferSize 最大缓冲大小
     * @param maxBufferCount 最大缓冲计数
     * @param retryBackoffMs 一个上传单元，缓冲的最长时间，到时间后，即使大小很小，也要上传远端
     */
    public RecordAccumulator(long maxBatchSize, int maxBatchCount, long maxBufferSize, int maxBufferCount, long retryBackoffMs, boolean orderByPartition)
    {
        this.closed = false;
        this.flushesInProgress = new AtomicInteger(0);
        this.appendsInProgress = new AtomicInteger(0);
        this.maxBatchSize = maxBatchSize;
        this.maxBatchCount = maxBatchCount;
        this.maxBufferSize = maxBufferSize;
        this.maxBufferCount = maxBufferCount;
        this.retryBackoffMs = retryBackoffMs;
        this.orderByPartition = orderByPartition;
        this.batches = new CopyOnWriteMap<>();
    }

    /**
     * Add a record to the accumulator, return the append result
     * <p>
     * The append result will contain the future metadata, and flag for whether the appended batch is full or a new batch is created
     * <p>
     *
     * @param tp The stream/partition to which this record is being sent
     * @param timestamp The timestamp of the records
     * @param putRecordsRequest the records to add
     * @param callback The user-supplied callback to execute when the request is complete
     * @param maxTimeToBlock The maximum time in milliseconds to block for adding
     * @return The append result
     * @throws InterruptedException The current thread was interrupted
     */
    public RecordAppendResult append(StreamPartition tp,
                                     long timestamp,
                                     PutRecordsRequest putRecordsRequest,
                                     AsyncHandler<PutRecordsResult> callback,
                                     long maxTimeToBlock) throws InterruptedException {
        // We keep track of the number of appending thread to make sure we do not miss batches in
        // abortIncompleteBatches().
        appendsInProgress.incrementAndGet();
        try {
            // check if we have an in-progress batch
            Deque<ProducerBatch> dq = getOrCreateDeque(tp);
            synchronized (dq) {
                if (closed)
                {
                    throw new IllegalStateException("Cannot send after the producer is closed.");
                }
                
                long newRequestSize = 0;
                for (PutRecordsRequestEntry entry : putRecordsRequest.getRecords())
                {
                    newRequestSize += entry.getData().array().length;
                }
                
                // 当总缓存大小超过阈值，则等待
                long currentBufferSize;
                while ((newRequestSize + (currentBufferSize = bufferSize.get())) > maxBufferSize)
                {
                    log.warn("Send blocked because BufferSize(total:{}, current:{}+{}) is full.",
                        maxBufferSize,
                        currentBufferSize,
                        newRequestSize);
                    long start = System.currentTimeMillis();
                    dq.wait(maxTimeToBlock);
                    long elapsed = System.currentTimeMillis() - start;
                    if (elapsed >= maxTimeToBlock)
                    {
                        log.warn(
                            "The BufferSize(total:" + maxBufferSize + ", current:" + currentBufferSize + "+"
                                + newRequestSize + ") is still full after blocking " + maxTimeToBlock + " ms.");
                    }
                    log.warn("Send unblocked after blocking {}ms", (System.currentTimeMillis() - start));
                }
                
                RecordAppendResult appendResult = tryAppend(timestamp, putRecordsRequest, callback, dq);
                if (appendResult != null)
                {
                    bufferSize.addAndGet(newRequestSize);
                    return appendResult;
                }

                // 当总批次或者总缓存大小超过阈值，则等待
                long currentBufferCount = bufferCount.get();
                while ((newRequestSize + (currentBufferSize = bufferSize.get())) >= maxBufferSize
                    || (currentBufferCount = bufferCount.get()) >= maxBufferCount)
                {
                    log.warn(
                        "Send blocked because BufferSize(total:{}, current:{}+{}) or BufferCount(total:{}, current:{}) is full.",
                        maxBufferSize,
                        currentBufferSize,
                        newRequestSize,
                        maxBufferCount,
                        currentBufferCount);
                    long start = System.currentTimeMillis();
                    dq.wait(maxTimeToBlock);
                    long elapsed = System.currentTimeMillis() - start;
                    if (elapsed >= maxTimeToBlock)
                    {
                        log.warn(
                            "The BufferSize(total:" + maxBufferSize + ", current:" + currentBufferSize + "+"
                                + newRequestSize + ") or BufferCount(total:" + maxBufferCount + ", current:"
                                + currentBufferCount + ") is still full after blocking " + maxTimeToBlock + " ms.");
                    }
                    log.warn("Send unblocked after blocking {}ms", (System.currentTimeMillis() - start));
                }
                
                ProducerBatch batch = new ProducerBatch(tp, maxBatchSize, maxBatchCount);
                FutureRecordsMetadata future = batch.tryAppend(timestamp, putRecordsRequest, callback);

                bufferCount.incrementAndGet();
                bufferSize.addAndGet(newRequestSize);
                dq.addLast(batch);
                return new RecordAppendResult(future, dq.size() > 1 || batch.isFull(), true);
            }
        } finally {
            appendsInProgress.decrementAndGet();
        }
    }


    /*
     *  Try to append to a ProducerBatch.
     *  If it is full, we return null and a new batch is created. We also close the batch for record appends
     */
    private RecordAppendResult tryAppend(long timestamp, PutRecordsRequest putRecordsRequest, AsyncHandler<PutRecordsResult> callback, Deque<ProducerBatch> deque) {
        ProducerBatch last = deque.peekLast();
        if (last != null) {
            FutureRecordsMetadata future = last.tryAppend(timestamp, putRecordsRequest, callback);
            if (future == null)
                last.closeForRecordAppends();
            else
                return new RecordAppendResult(future, deque.size() > 1 || last.isFull(), false);

        }
        return null;
    }

    /*
     * Re-enqueue the given record batch in the accumulator to retry
     */
    public void reenqueue(ProducerBatch batch, long now) {
        batch.reenqueued(now);
        Deque<ProducerBatch> deque = getOrCreateDeque(batch.getTp());
        synchronized (deque) {
            deque.addFirst(batch);
        }
    }

    /*
     * Check whether there are any batches which haven't been drained
     */
    public boolean hasUndrained() {
        for (Map.Entry<StreamPartition, Deque<ProducerBatch>> entry : this.batches.entrySet()) {
            Deque<ProducerBatch> deque = entry.getValue();
            synchronized (deque) {
                if (!deque.isEmpty())
                    return true;
            }
        }
        return false;
    }
    
    public void setOnSendingPartitionFuture(StreamPartition sp, Future<PutRecordsResult> future){
        this.onSendingPartitions.put(sp, future);
    }
    
    public List<ProducerBatch> drain(long now, CopyOnWriteArrayList<StreamPartition> onSendingStreamPartitions) {

        List<ProducerBatch> drainBatches = new ArrayList<>();
        
//        Iterator<Map.Entry<StreamPartition, Future<PutRecordsResult>>> itor = onSendingPartitions.entrySet().iterator();
//        while(itor.hasNext()){
//            Map.Entry<StreamPartition, Future<PutRecordsResult>> entry = itor.next();
//            if(entry.getValue() != null && entry.getValue().isDone()){
//                itor.remove();
//            }
//        }
        
        for(Map.Entry<StreamPartition, Deque<ProducerBatch>> entry : batches.entrySet()){
            StreamPartition sp = entry.getKey();
            Deque<ProducerBatch> deque = entry.getValue();
            
            if(orderByPartition){//如果需要分片保序，且分片正在发送中，待发送完完成后再取
                if(onSendingStreamPartitions.contains(sp)){
                    continue;
                }
            }
            //如果当前分片还有没发送完成的，待发送完成后再取
//            if(onSendingPartitions.containsKey(sp)){
//                continue;
//            }
//            
            synchronized (deque)
            {
                ProducerBatch first = deque.peekFirst();

                if (first == null)
                {
                    continue;
                }

                if (first.waitedTimeMs(now) < retryBackoffMs && !first.isFull())
                {
                    continue;
                }

                if (first.isEmpty())
                {
                    first.reenqueued(now);
                    continue;
                }

                ProducerBatch batch = deque.pollFirst();
                drainBatches.add(batch);
                batch.drained(now);

                log.debug(
                    "Drain batch({} records) success, currentBufferCount is {}, currentBufferSize is {}, queueSize {}.",
                    batch.getRelativeOffset(),
                    bufferCount.get(),
                    bufferSize.get(),
                    deque.size());
            }
        }
        
        return drainBatches;        
    }

    private Deque<ProducerBatch> getDeque(StreamPartition tp) {
        return batches.get(tp);
    }

    /*
     * Get the deque for the given topic-partition, creating it if necessary.
     */
    private Deque<ProducerBatch> getOrCreateDeque(StreamPartition tp) {
        Deque<ProducerBatch> d = this.batches.get(tp);
        if (d != null)
            return d;
        d = new ArrayDeque<>();
        Deque<ProducerBatch> previous = this.batches.putIfAbsent(tp, d);
        if (previous == null)
            return d;
        else
            return previous;
    }

    /*
     * Are there any threads currently waiting on a flush?
     *
     * package private for test
     */
    boolean flushInProgress() {
        return flushesInProgress.get() > 0;
    }

    /* Visible for testing */
    public Map<StreamPartition, Deque<ProducerBatch>> batches() {
        return Collections.unmodifiableMap(batches);
    }

    /*
     * Initiate the flushing of data from the accumulator...this makes all requests immediately ready
     */
    public void beginFlush() {
        this.flushesInProgress.getAndIncrement();
    }

    /*
     * Are there any threads currently appending messages?
     */
    private boolean appendsInProgress() {
        return appendsInProgress.get() > 0;
    }

    /*
     * Close this accumulator and force all the record buffers to be drained
     */
    public void close() {
        this.closed = true;
    }

    public void batchIsDone(ProducerBatch batch)
    {
        Deque<ProducerBatch> dq = getDeque(batch.getTp());
        if (dq != null)
        {
            synchronized (dq)
            {
                bufferCount.decrementAndGet();
                bufferSize.addAndGet(-batch.getTotolByteSize());
                dq.notify();
            }
        }
    }
    /*
     * Metadata about a record just appended to the record accumulator
     */
    public final static class RecordAppendResult {
        public final FutureRecordsMetadata future;
        public final boolean batchIsFull;
        public final boolean newBatchCreated;

        public RecordAppendResult(FutureRecordsMetadata future, boolean batchIsFull, boolean newBatchCreated) {
            this.future = future;
            this.batchIsFull = batchIsFull;
            this.newBatchCreated = newBatchCreated;
        }
    }

}
