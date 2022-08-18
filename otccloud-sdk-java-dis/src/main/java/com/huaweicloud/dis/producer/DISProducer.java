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

package com.otccloud.dis.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otccloud.dis.DISAsync;
import com.otccloud.dis.DISClientAsync;
import com.otccloud.dis.DISConfig;
import com.otccloud.dis.core.builder.DefaultExecutorFactory;
import com.otccloud.dis.core.handler.AsyncHandler;
import com.otccloud.dis.core.util.StringUtils;
import com.otccloud.dis.iface.data.request.PutRecordsRequest;
import com.otccloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.otccloud.dis.iface.data.response.PutRecordsResult;
import com.otccloud.dis.iface.data.response.PutRecordsResultEntry;
import com.otccloud.dis.iface.stream.request.DescribeStreamRequest;
import com.otccloud.dis.iface.stream.response.DescribeStreamResult;
import com.otccloud.dis.producer.internals.RecordAccumulator;
import com.otccloud.dis.producer.internals.Sender;
import com.otccloud.dis.producer.internals.StreamPartition;


/**
 * 数据生产者高层次封装，通过异步并发、pipeline能力提升吞吐
 * */
public class DISProducer
{
    private static final Logger log = LoggerFactory.getLogger(DISProducer.class);
    
    private static final String STABLE_PARTITION_ID = "nb";
    
    // buffer满时，新数据发送的阻塞时间
    private long maxBlockMs = 0;

    // 发送前等待的最长时间(ms)
    private long lingerMs = 0;

    private RecordAccumulator accumulator;
    
    private Sender sender;

    private DISAsync disAsync;

    private DISConfig disConfig;
    
    private boolean orderByPartition;

    private long metadataTimeoutMS;

    public DISProducer(DISConfig disConfig)
    {
        this(disConfig, new DefaultExecutorFactory(disConfig.getMaxInFlightRequestsPerConnection()).newExecutor());
    }

    public DISProducer(DISConfig disConfig, DISAsync disAsync)
    {
        this(disConfig, disAsync, null);
    }

    public DISProducer(DISConfig disConfig, ExecutorService executorService)
    {
        this(disConfig, null, executorService);
    }

    private DISProducer(DISConfig disConfig, DISAsync disAsync, ExecutorService executorService)
    {
        DISConfig config = DISConfig.buildConfig(disConfig);
        this.disConfig = config;
        this.lingerMs = config.getLingerMs();
        this.maxBlockMs = config.getMaxBlockMs();
        long batchSize = config.getBatchSize();
        int batchCount = config.getBatchCount();
        long bufferSize = config.getBufferMemory();
        int bufferCount = config.getBufferCount();
        boolean orderByPartition = config.isOrderByPartition();
        this.orderByPartition = orderByPartition;
        this.metadataTimeoutMS = config.getMetadataTimeoutMs();

        if (disAsync != null)
        {
            this.disAsync = disAsync;
        }
        else
        {
            this.disAsync = new DISClientAsync(config, executorService);
        }
        this.accumulator = new RecordAccumulator(batchSize, batchCount, bufferSize, bufferCount, this.lingerMs, orderByPartition);
        this.sender = new Sender(this.disAsync, accumulator, this.lingerMs);

        sender.start();
    }
    
    public Future<PutRecordsResultEntry> putRecordAsync(String streamName, PutRecordsRequestEntry putRecordsRequestEntry, AsyncHandler<PutRecordsResultEntry> callback) throws InterruptedException{
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamName(streamName);
        
        List<PutRecordsRequestEntry> records = new ArrayList<PutRecordsRequestEntry>();
        records.add(putRecordsRequestEntry);
        putRecordsRequest.setRecords(records);
        
        Future<PutRecordsResult> future = this.putRecordsAsync(putRecordsRequest, new PutRecordsResultAsyncHandler(callback));
        
        return new PutRecordsResultEntryFuture(future);
    }
    
    private ConcurrentHashMap<String, StreamInfo> metadata = new ConcurrentHashMap<String, StreamInfo>();
    private CopyOnWriteArrayList<String> onSyncStreams = new CopyOnWriteArrayList<>();
    
    private StreamInfo fetchMetadata(String streamName){
        StreamInfo streamInfo = metadata.get(streamName);
        if(streamInfo == null){
            DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
            describeStreamRequest.setStreamName(streamName);
            DescribeStreamResult describeStreamResult = disAsync.describeStream(describeStreamRequest);
            
            streamInfo = new StreamInfo();
            streamInfo.setStreamName(streamName);
            streamInfo.setSyncTimestamp(System.currentTimeMillis());
            streamInfo.setPartitions(describeStreamResult.getWritablePartitionCount());
            
            metadata.put(streamName, streamInfo);
        }else{
            if(System.currentTimeMillis() - streamInfo.getSyncTimestamp() > metadataTimeoutMS){
                if(!onSyncStreams.contains(streamName)){
                    onSyncStreams.add(streamName);
                    
                    DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
                    describeStreamRequest.setStreamName(streamName);
                    disAsync.describeStreamAsync(describeStreamRequest, new AsyncHandler<DescribeStreamResult>()
                    {
                        @Override
                        public void onError(Exception exception)
                        {
                            log.error(exception.getMessage());
                            onSyncStreams.remove(streamName);
                        }

                        @Override
                        public void onSuccess(DescribeStreamResult result)
                        {
                            StreamInfo streamInfo = new StreamInfo();
                            streamInfo.setStreamName(streamName);
                            streamInfo.setSyncTimestamp(System.currentTimeMillis());
                            streamInfo.setPartitions(result.getWritablePartitionCount());
                            
                            metadata.put(streamName, streamInfo);
                            
                            onSyncStreams.remove(streamName);
                        }
                    });
                
                }
            }
        }
        
        return streamInfo;
    }
    
    private int calPartitionId(StreamInfo streamInfo, PutRecordsRequestEntry entry)
    {
        if(!StringUtils.isNullOrEmpty(entry.getPartitionId())){
            return PartitionKeyUtils.getPartitionNumberFromShardId(entry.getPartitionId());
        }
        
        Long hashKey = PartitionKeyUtils.getHashKey(entry.getPartitionKey(), entry.getExplicitHashKey());
        
        return PartitionKeyUtils.calPartitionIndex(streamInfo.getPartitions(), hashKey);
    }
    
    public Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsRequest, AsyncHandler<PutRecordsResult> callback) throws InterruptedException
    {
        String streamName = putRecordsRequest.getStreamName();
        String streamId = putRecordsRequest.getStreamId();
        
        //不按partition排序的话，就不按partition分组,streamPartition表示的其实是流而不是分片，分片字段传固定的
        String partitionId = STABLE_PARTITION_ID;
        if(orderByPartition){
            StreamInfo streamInfo = fetchMetadata(streamName);
            
            int caledPartitionId = -1;
            for(PutRecordsRequestEntry entry : putRecordsRequest.getRecords()){
                int tmpPartition = calPartitionId(streamInfo, entry);
                if(caledPartitionId != -1 && caledPartitionId != tmpPartition){
                    throw new RuntimeException("one batch should in one partition when orderByPartition on.");
                }
                caledPartitionId = tmpPartition;
            }
            
            partitionId = Integer.toString(caledPartitionId);
        }
       
        StreamPartition tp = new StreamPartition(streamName, streamId, partitionId);
        
        long timestamp = System.currentTimeMillis();
        log.trace("Sending records {} with callback {} to streampartition ", putRecordsRequest, callback, tp);

        RecordAccumulator.RecordAppendResult result =
            accumulator.append(tp, timestamp, putRecordsRequest, callback, this.maxBlockMs);
//        if (result.batchIsFull || result.newBatchCreated)
        if (result.batchIsFull || lingerMs == 0)
        {
            log.trace("Waking up the sender since topic partition {} is either full or getting a new batch", tp);
            this.sender.wakeup();
        }
        return result.future;
        // handling exceptions and record the errors;
        // for API exceptions return them in the future,
        // for other exceptions throw directly
    }

    public void flush()
    {
        this.sender.flush();
    }

    public void close()
    {
        close(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public void close(long timeout, TimeUnit timeUnit)
    {
        if (timeout < 0)
        {
            throw new IllegalArgumentException("The timeout cannot be negative.");
        }
        log.debug("Closing the DIS producer with timeoutMillis = {} ms.", timeUnit.toMillis(timeout));

        accumulator.close();
        sender.close(timeUnit.toMillis(timeout));
        disAsync.close();
        log.debug("The DIS producer has closed.");
    }

    private static class PutRecordsResultEntryFuture implements Future<PutRecordsResultEntry>{

        private Future<PutRecordsResult> future;
        
        public PutRecordsResultEntryFuture(Future<PutRecordsResult> future)
        {
            this.future = future;
        }

        @Override
        public boolean isDone()
        {
            return future.isDone();
        }
        
        @Override
        public boolean isCancelled()
        {
            return future.isCancelled();
        }
        
        @Override
        public PutRecordsResultEntry get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException
        {
            PutRecordsResult result = future.get(timeout, unit);
            if(result != null && result.getRecords() != null && !result.getRecords().isEmpty()){
                return result.getRecords().get(0);
            }else{
                return null;
            }
        }
        
        @Override
        public PutRecordsResultEntry get()
            throws InterruptedException, ExecutionException
        {
            PutRecordsResult result = future.get();
            if(result != null && result.getRecords() != null && !result.getRecords().isEmpty()){
                return result.getRecords().get(0);
            }else{
                return null;
            }
        }
        
        @Override
        public boolean cancel(boolean mayInterruptIfRunning)
        {
            return future.cancel(mayInterruptIfRunning);
        }
        
    }
    
    private static class PutRecordsResultAsyncHandler implements AsyncHandler<PutRecordsResult>{

        private AsyncHandler<PutRecordsResultEntry> asyncHandler;
        
        public PutRecordsResultAsyncHandler(AsyncHandler<PutRecordsResultEntry> callback)
        {
            this.asyncHandler = callback;
        }

        @Override
        public void onError(Exception exception) throws Exception
        {
            asyncHandler.onError(exception);
        }

        @Override
        public void onSuccess(PutRecordsResult result) throws Exception
        {
            asyncHandler.onSuccess(result.getRecords().get(0));
            
        }
        
    }

    private static class StreamInfo{
        private String streamName;
        private int partitions;
        private long syncTimestamp;
        public String getStreamName()
        {
            return streamName;
        }
        public void setStreamName(String streamName)
        {
            this.streamName = streamName;
        }
        public int getPartitions()
        {
            return partitions;
        }
        public void setPartitions(int partitions)
        {
            this.partitions = partitions;
        }
        public long getSyncTimestamp()
        {
            return syncTimestamp;
        }
        public void setSyncTimestamp(long syncTimestamp)
        {
            this.syncTimestamp = syncTimestamp;
        }
    }
    
}
