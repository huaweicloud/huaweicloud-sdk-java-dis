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

package com.huaweicloud.dis.iface.data.response;

import java.nio.ByteBuffer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Record
{
    /**
     * <p>
     * 数据写入分区的分区键。
     * </p>
     */
    @JsonProperty("partition_key")
    protected String partitionKey;
    
    /**
     * <p>
     * 序列号。序列号是每个记录的唯一标识符。序列号由DIS在数据生产者调用 PutRecord 操作以添加数据到DIS数据通道时DIS服务自动分配的。
     * 同一分区键的序列号通常会随时间变化增加。
     * </p>
     */
    @JsonProperty("sequence_number")
    protected String sequenceNumber;
    
    /**
     * <p>
     * 数据。
     * </p>
     */
    @JsonProperty("data")
    protected ByteBuffer data;
    
    @JsonProperty("ApproximateArrivalTimestamp")
    @JsonIgnore
    protected Long approximateArrivalTimestamp;

    @JsonProperty("timestamp")
    protected Long timestamp;

    @JsonProperty("timestamp_type")
    protected String timestampType;

    public String getPartitionKey()
    {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey)
    {
        this.partitionKey = partitionKey;
    }

    

    public String getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    public ByteBuffer getData()
    {
        return data;
    }

    public void setData(ByteBuffer data)
    {
        this.data = data;
    }

    public Long getApproximateArrivalTimestamp()
    {
        return approximateArrivalTimestamp;
    }

    public void setApproximateArrivalTimestamp(Long approximateArrivalTimestamp)
    {
        this.approximateArrivalTimestamp = approximateArrivalTimestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampType() {
        return timestampType;
    }

    public void setTimestampType(String timestampType) {
        this.timestampType = timestampType;
    }

    @Override
    public String toString()
    {
        return "Record [partitionKey=" + partitionKey + ", sequenceNumber=" + sequenceNumber
            + ", approximateArrivalTimestamp=" + approximateArrivalTimestamp +  "]";
    }

    
}
