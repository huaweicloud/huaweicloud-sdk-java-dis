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

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.cloud.sdk.util.StringUtils;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;

/**
 * The future result of one original batch records send
 */
public final class FutureRecordsMetadata implements Future<PutRecordsResult> {

    private final ProduceRequestResult result;
    
    /*
     * TODO 当前对用户发送数据，只是简单的合并为一个大的批量请求。
     * 后续进一步优化，可按partition进行分组，然后每个partition发批量http请求，那么这里就会对应多个ProduceRequestResult
     * private final List<ProduceRequestResult> result;
     * */ 
    
    private final int relativeOffset;
    
    private final int length;

    public FutureRecordsMetadata(ProduceRequestResult result, int relativeOffset, int length) {
        this.result = result;
        this.relativeOffset = relativeOffset;
        this.length = length;
    }

    @Override
    public boolean cancel(boolean interrupt) {
        return false;
    }

    @Override
    public PutRecordsResult get() throws InterruptedException, ExecutionException {
        this.result.await();
        return valueOrError();
    }

    @Override
    public PutRecordsResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean occurred = this.result.await(timeout, unit);
        if (!occurred)
            throw new TimeoutException("Timeout after waiting for " + TimeUnit.MILLISECONDS.convert(timeout, unit) + " ms.");
        return valueOrError();
    }

    PutRecordsResult valueOrError() throws ExecutionException {
        if (this.result.error() != null)
            throw new ExecutionException(this.result.error());
        else
            return value();
    }
    
    PutRecordsResult value() {
        PutRecordsResult curResult = new PutRecordsResult();
        
        PutRecordsResult producePutRecordsResult = result.putRecordsResult();
        
        List<PutRecordsResultEntry> entrys = producePutRecordsResult.getRecords().subList(relativeOffset, relativeOffset + length);
        
        int failedRecordCount = 0;
        for(PutRecordsResultEntry entry : entrys){
            if(!StringUtils.isNullOrEmpty(entry.getErrorCode())){
                failedRecordCount++;
            }
        }
        
        curResult.setFailedRecordCount(new AtomicInteger(failedRecordCount));
        curResult.setRecords(entrys);
        
        return curResult;
    }
    
    public long relativeOffset() {
        return this.relativeOffset;
    }
    
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.result.completed();
    }

}
