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

package com.huaweicloud.dis.producer;

import com.huaweicloud.dis.DISAsync;
import com.huaweicloud.dis.DISClientAsync;
import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.core.builder.DefaultExecutorFactory;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;
import com.huaweicloud.dis.producer.internals.RecordAccumulator;
import com.huaweicloud.dis.producer.internals.Sender;
import com.huaweicloud.dis.producer.internals.StreamPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


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
        this.lingerMs = config.getLingerMs();
        this.maxBlockMs = config.getMaxBlockMs();
        long batchSize = config.getBatchSize();
        int batchCount = config.getBatchCount();
        long bufferSize = config.getBufferMemory();
        int bufferCount = config.getBufferCount();
        if (disAsync != null)
        {
            this.disAsync = disAsync;
        }
        else
        {
            this.disAsync = new DISClientAsync(config, executorService);
        }
        this.accumulator = new RecordAccumulator(batchSize, batchCount, bufferSize, bufferCount, this.lingerMs);
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
    
    public Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsRequest, AsyncHandler<PutRecordsResult> callback) throws InterruptedException
    {
        // TODO 先不按partition分组,streamPartition表示的其实是流而不是分片，分片字段传固定的
        StreamPartition tp = new StreamPartition(putRecordsRequest.getStreamName(), STABLE_PARTITION_ID);
        
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
        accumulator.close();
        sender.close();
        disAsync.close();
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
        public void onError(Exception exception)
        {
            asyncHandler.onError(exception);
        }

        @Override
        public void onSuccess(PutRecordsResult result)
        {
            asyncHandler.onSuccess(result.getRecords().get(0));
            
        }
        
    }

}
