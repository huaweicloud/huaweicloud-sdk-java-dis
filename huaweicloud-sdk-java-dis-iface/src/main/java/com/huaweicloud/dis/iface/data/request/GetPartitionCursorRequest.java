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

package com.huaweicloud.dis.iface.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPartitionCursorRequest
{
    
    /**
     * <p>
     * 通道名称。
     * </p>
     */
    @JsonProperty("stream-name")
    private String streamName;

    /**
     * <p>
     * 通道ID，当消费被授权通道时必选
     * </p>
     * */
    @JsonProperty("stream-id")
    private String streamId;
    /**
     * <p>
     * 分区值。
     * </p>
     */
    @JsonProperty("partition-id")
    private String partitionId;
    
    /**
     * <p>
     * 迭代类型。
     * </p>
     * <p>
     * DIS 服务中有以下迭代类型
     * </p>
     * <ul>
     * <li>
     * <p>
     * AT_SEQUENCE_NUMBER - 从特定序列号所在的记录开始读取。
     * </p>
     * </li>
     * <li>
     * <p>
     * AFTER_SEQUENCE_NUMBER - 从特定序列号后的记录开始读取。
     * </p>
     * </li>
	 * <li>
     * <p>
     * AT_TIMESTAMP - 从分区中最接近指定时间戳的记录开始读取。需要传入Timestamp字段
     * </p>
     * </li>
     * <li>
     * <p>
     * TRIM_HORIZON - 从分区中最时间最长的记录开始读取。
     * </p>
     * </li>
     * <li>
     * <p>
     * LATEST - 从分区中最新的记录开始读取。
     * </p>
     * </li>
     * </ul>
     */
    @JsonProperty("cursor-type")
    private String cursorType;
    
    /**
     * <p>
     * 序列号。序列号是每个记录的唯一标识符。序列号由DIS在数据生产者调用 <code>PutRecord</code> 操作以添加数据到DIS数据通道时DIS服务自动分配的。
     * 同一分区键的序列号通常会随时间变化增加。
     * </p>
     */
    @JsonProperty("starting-sequence-number")
    private String startingSequenceNumber;

    /**
     * <p>
     * 时间戳。
     * </p>
     */
    @JsonProperty("timestamp")
    private Long timestamp;
    
    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getStreamName()
    {
        return streamName;
    }

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }


    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getPartitionId()
    {
        return partitionId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId = partitionId;
    }

    public String getCursorType()
    {
        return cursorType;
    }

    public void setCursorType(String cursorType)
    {
        this.cursorType = cursorType;
    }

    public String getStartingSequenceNumber()
    {
        return startingSequenceNumber;
    }

    public void setStartingSequenceNumber(String startingSequenceNumber)
    {
        this.startingSequenceNumber = startingSequenceNumber;
    }

    @Override
    public String toString()
    {
        return "GetIteratorParam [streamName=" + streamName + ", partitionId=" + partitionId + ", cursorType="
            + cursorType + ", startingSequenceNumber=" + startingSequenceNumber + "]";
    }
    
}
