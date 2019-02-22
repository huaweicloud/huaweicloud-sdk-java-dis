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

package com.huaweicloud.dis.iface.api.protobuf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.ByteString;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntryExtendedInfo;
import com.huaweicloud.dis.iface.data.response.GetRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;
import com.huaweicloud.dis.iface.data.response.Record;

public class ProtobufUtils
{
    /**
     * 从protobuf类型的上传数据响应，转换为标准的响应类型
     * */
    public static PutRecordsResult toPutRecordsResult(com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult putRecordsResult)
    {
        PutRecordsResult result = new PutRecordsResult();
        result.setFailedRecordCount(new AtomicInteger(putRecordsResult.getFailedRecordCount()));
        
        List<PutRecordsResultEntry> records = new ArrayList<PutRecordsResultEntry>();
        for(com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResultEntry protoEntry : putRecordsResult.getRecordsList()){
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
    public static com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest toProtobufPutRecordsRequest(PutRecordsRequest putRecordsParam)
    {
        com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest.Builder builder = com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest.newBuilder();
        
        builder.setStreamName(putRecordsParam.getStreamId());
        for(PutRecordsRequestEntry putRecordsRequestEntry : putRecordsParam.getRecords()){
            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequestEntry.Builder ebuilder = com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequestEntry.newBuilder();
            
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
            
            PutRecordsRequestEntryExtendedInfo putRecordsRequestEntryExtendedInfo = putRecordsRequestEntry.getExtendedInfo();
            if(putRecordsRequestEntryExtendedInfo != null){
                com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequestEntryExtendedInfo.Builder exbuilder = com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequestEntryExtendedInfo.newBuilder();
                
                if(putRecordsRequestEntryExtendedInfo.getDeliverDataId() != null){
                    exbuilder.setDeliverDataId(putRecordsRequestEntryExtendedInfo.getDeliverDataId());
                }
                if(putRecordsRequestEntryExtendedInfo.getEndFlag() != null){
                    exbuilder.setEndFlag(putRecordsRequestEntryExtendedInfo.getEndFlag());
                }
                if(putRecordsRequestEntryExtendedInfo.getFileName() != null){
                    exbuilder.setFileName(putRecordsRequestEntryExtendedInfo.getFileName());
                }
                if(putRecordsRequestEntryExtendedInfo.getSeqNum() != null){
                    exbuilder.setSeqNum(putRecordsRequestEntryExtendedInfo.getSeqNum());
                }
                
                ebuilder.setExtendedInfo(exbuilder);    
            }
            
            builder.addRecords(ebuilder);
        }
        
        com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest protoRequest = builder.build();
        return protoRequest;
    }
    
    public static PutRecordsRequest toPutRecordsRequest(com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest putRecordsParam)
    {
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamId(putRecordsParam.getStreamName());
        
        List<PutRecordsRequestEntry> records = new ArrayList<PutRecordsRequestEntry>();
        for(com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequestEntry protoEntry : putRecordsParam.getRecordsList()){
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

            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequestEntryExtendedInfo protoExtendedInfo = protoEntry.getExtendedInfo(); 
            if(protoExtendedInfo != null){
                PutRecordsRequestEntryExtendedInfo extendedInfo =  new PutRecordsRequestEntryExtendedInfo();
                
                extendedInfo.setDeliverDataId(protoExtendedInfo.getDeliverDataId());
                extendedInfo.setEndFlag(protoExtendedInfo.getEndFlag());
                extendedInfo.setFileName(protoExtendedInfo.getFileName());
                extendedInfo.setSeqNum(protoExtendedInfo.getSeqNum());
                
                record.setExtendedInfo(extendedInfo);
            }

            records.add(record);
        }
        putRecordsRequest.setRecords(records);
        return putRecordsRequest;
    }
    
    public static com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult toProtobufPutRecordsResult(
        PutRecordsResult putRecordsResult)
    {
        com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult.Builder builder = com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult.newBuilder();
        builder.setFailedRecordCount(putRecordsResult.getFailedRecordCount().intValue());
        for(PutRecordsResultEntry resultEntry : putRecordsResult.getRecords()){
            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResultEntry.Builder entryBuilder = com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResultEntry.newBuilder();
            
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

    public static com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult toProtobufGetRecordsResult(
        GetRecordsResult getRecordsResult)
    {
        com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult.Builder builder = com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult.newBuilder();
        builder.setNextShardIterator(getRecordsResult.getNextPartitionCursor());
        
        for(Record record : getRecordsResult.getRecords()){
            com.huaweicloud.dis.iface.api.protobuf.Message.Record.Builder rBuilder = com.huaweicloud.dis.iface.api.protobuf.Message.Record.newBuilder();
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

    public static GetRecordsResult toGetRecordsResult(com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult protoResult)
    {
        GetRecordsResult result = new GetRecordsResult();
        result.setNextPartitionCursor(protoResult.getNextShardIterator());
        
        List<Record> records = new ArrayList<Record>();
        for(com.huaweicloud.dis.iface.api.protobuf.Message.Record protoRecord : protoResult.getRecordsList()){
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
