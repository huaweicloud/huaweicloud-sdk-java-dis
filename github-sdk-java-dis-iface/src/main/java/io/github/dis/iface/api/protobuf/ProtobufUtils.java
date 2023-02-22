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

package io.github.dis.iface.api.protobuf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.ByteString;
import io.github.dis.iface.data.request.PutRecordsRequest;
import io.github.dis.iface.data.request.PutRecordsRequestEntry;
import io.github.dis.iface.data.response.GetRecordsResult;
import io.github.dis.iface.data.response.PutRecordsResult;
import io.github.dis.iface.data.response.PutRecordsResultEntry;
import io.github.dis.iface.data.response.Record;

public class ProtobufUtils
{
    /**
     * 从protobuf类型的上传数据响应，转换为标准的响应类型
     * */
    public static PutRecordsResult toPutRecordsResult(Message.PutRecordsResult putRecordsResult)
    {
        PutRecordsResult result = new PutRecordsResult();
        result.setFailedRecordCount(new AtomicInteger(putRecordsResult.getFailedRecordCount()));
        
        List<PutRecordsResultEntry> records = new ArrayList<PutRecordsResultEntry>();
        for(Message.PutRecordsResultEntry protoEntry : putRecordsResult.getRecordsList()){
            PutRecordsResultEntry entry = new PutRecordsResultEntry();
            entry.setErrorCode(protoEntry.getErrorCode().isEmpty() ? null : protoEntry.getErrorCode());
            entry.setErrorMessage(protoEntry.getErrorMessage().isEmpty() ? null : protoEntry.getErrorMessage());
            entry.setSequenceNumber(protoEntry.getSequenceNumber());
            entry.setPartitionId(protoEntry.getShardId());
            
            records.add(entry);
        }
        
        result.setRecords(records);
        return result;
    }

    /**
     * 将标准请求类型的对象转换为protobuf的请求参数类型
     * */
    public static Message.PutRecordsRequest toProtobufPutRecordsRequest(PutRecordsRequest putRecordsParam)
    {
        Message.PutRecordsRequest.Builder builder = Message.PutRecordsRequest.newBuilder();
        
        if(putRecordsParam.getStreamName() != null)
        {
            builder.setStreamName(putRecordsParam.getStreamName());
        }
    
        if(putRecordsParam.getStreamId() != null)
        {
            builder.setStreamId(putRecordsParam.getStreamId());
        }
        
        for(PutRecordsRequestEntry putRecordsRequestEntry : putRecordsParam.getRecords()){
            Message.PutRecordsRequestEntry.Builder ebuilder = Message.PutRecordsRequestEntry.newBuilder();
            
            if(putRecordsRequestEntry.getData() != null){
                ebuilder.setData(ByteString.copyFrom(putRecordsRequestEntry.getData().array()));
            }
            if(putRecordsRequestEntry.getPartitionKey() != null){
                ebuilder.setPartitionKey(putRecordsRequestEntry.getPartitionKey());
            }
            if(putRecordsRequestEntry.getExplicitHashKey() != null){
                ebuilder.setExplicitHashKey(putRecordsRequestEntry.getExplicitHashKey());
            }
            if(putRecordsRequestEntry.getPartitionId() != null){
                ebuilder.setPartitionId(putRecordsRequestEntry.getPartitionId());
            }
            if(putRecordsRequestEntry.getTimestamp() != null)
            {
                ebuilder.setTimestamp(putRecordsRequestEntry.getTimestamp());
            }
            
            builder.addRecords(ebuilder);
        }
        
        Message.PutRecordsRequest protoRequest = builder.build();
        return protoRequest;
    }
    
    public static PutRecordsRequest toPutRecordsRequest(Message.PutRecordsRequest putRecordsParam)
    {
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
    
        if(putRecordsParam.getStreamName() != null)
        {
            putRecordsRequest.setStreamName(putRecordsParam.getStreamName());
        }
    
        if(putRecordsParam.getStreamId() != null)
        {
            putRecordsRequest.setStreamId(putRecordsParam.getStreamId());
        }
        
        List<PutRecordsRequestEntry> records = new ArrayList<PutRecordsRequestEntry>();
        for(Message.PutRecordsRequestEntry protoEntry : putRecordsParam.getRecordsList()){
            PutRecordsRequestEntry record = new PutRecordsRequestEntry();
            if(protoEntry.getData() != null){
                record.setData(ByteBuffer.wrap(protoEntry.getData().toByteArray()));
            }
            record.setExplicitHashKey(protoEntry.getExplicitHashKey());
            record.setPartitionId(protoEntry.getPartitionId());
            record.setPartitionKey(protoEntry.getPartitionKey());
            if(protoEntry.hasTimestamp())
            {
                record.setTimestamp(protoEntry.getTimestamp());
            }

            records.add(record);
        }
        putRecordsRequest.setRecords(records);
        return putRecordsRequest;
    }
    
    public static Message.PutRecordsResult toProtobufPutRecordsResult(
        PutRecordsResult putRecordsResult)
    {
        Message.PutRecordsResult.Builder builder = Message.PutRecordsResult.newBuilder();
        builder.setFailedRecordCount(putRecordsResult.getFailedRecordCount().intValue());
        for(PutRecordsResultEntry resultEntry : putRecordsResult.getRecords()){
            Message.PutRecordsResultEntry.Builder entryBuilder = Message.PutRecordsResultEntry.newBuilder();
            
            if(resultEntry.getErrorCode() != null){
                entryBuilder.setErrorCode(resultEntry.getErrorCode());
            }
            if(resultEntry.getErrorMessage() != null){
                entryBuilder.setErrorMessage(resultEntry.getErrorMessage());
            }
            if(resultEntry.getSequenceNumber() != null){
                entryBuilder.setSequenceNumber(resultEntry.getSequenceNumber());
            }
            if(resultEntry.getPartitionId() != null){
                entryBuilder.setShardId(resultEntry.getPartitionId());
            }
            
            builder.addRecords(entryBuilder);
        }
        
        return builder.build();
    }

    public static Message.GetRecordsResult toProtobufGetRecordsResult(
        GetRecordsResult getRecordsResult)
    {
        Message.GetRecordsResult.Builder builder = Message.GetRecordsResult.newBuilder();
        builder.setNextShardIterator(getRecordsResult.getNextPartitionCursor());
        
        for(Record record : getRecordsResult.getRecords()){
            Message.Record.Builder rBuilder = Message.Record.newBuilder();
            if(record.getPartitionKey() != null){
                rBuilder.setPartitionKey(record.getPartitionKey());
            }
            if(record.getSequenceNumber() != null){
                rBuilder.setSequenceNumber(record.getSequenceNumber());
            }
            
            if(record.getData() != null){
               rBuilder.setData(ByteString.copyFrom(record.getData().array()));
            }

            if(record.getTimestamp() != null)
            {
                rBuilder.setTimestamp(record.getTimestamp());
            }

            if(record.getTimestampType() != null)
            {
                rBuilder.setTimestampType(record.getTimestampType());
            }
            
            builder.addRecords(rBuilder);
        }
        return builder.build();
    }

    public static GetRecordsResult toGetRecordsResult(Message.GetRecordsResult protoResult)
    {
        GetRecordsResult result = new GetRecordsResult();
        result.setNextPartitionCursor(protoResult.getNextShardIterator());
        
        List<Record> records = new ArrayList<Record>();
        for(Message.Record protoRecord : protoResult.getRecordsList()){
            Record record = new Record();
            
            record.setSequenceNumber(protoRecord.getSequenceNumber());
            record.setPartitionKey(protoRecord.getPartitionKey());
            if(protoRecord.toByteArray() != null){
                record.setData(ByteBuffer.wrap(protoRecord.getData().toByteArray()));
            }
            if(protoRecord.hasTimestamp())
            {
                record.setTimestamp(protoRecord.getTimestamp());
            }
            if(protoRecord.hasTimestampType())
            {
                record.setTimestampType(protoRecord.getTimestampType());
            }
            records.add(record);
        }
        result.setRecords(records);
        
        return result;
    }
    
}
