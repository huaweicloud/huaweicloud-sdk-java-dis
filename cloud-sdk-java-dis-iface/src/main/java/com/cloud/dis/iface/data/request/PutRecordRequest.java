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

package com.cloud.dis.iface.data.request;

import java.nio.ByteBuffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.cloud.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutRecordRequest
{
    
    /**
     * <p>
     * 数据写入分区的分区键。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("PartitionKey")
    private String partitionKey;

    /**
     * <p>
     * 通道名称。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("StreamName")
    private String streamName;
    
    /**
     * <p>
     * 需要上传的数据，不可为空字符串。
     * </p>
     */
    @JsonProperty("Data")
    private ByteBuffer data;
    
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("SequenceNumberForOrdering")
    private String sequenceNumberForOrdering;
    
    /**
     * <p>
     * 用于明确数据需要写入分区的哈希值，此哈希值将覆盖“partition_key”的哈希值。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("ExplicitHashKey")
    private String explicitHashKey;

    @JsonProperty("timestamp")
    private Long timestamp;
    
    /**
     * <p>
     * 通道ID，用于授权访问， 与streamName二选一。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("StreamId")
    private String streamId;
    
    public String getPartitionKey()
    {
        return partitionKey;
    }
    
    public void setPartitionKey(String partitionKey)
    {
        this.partitionKey = partitionKey;
    }
    
    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public String getSequenceNumberForOrdering()
    {
        return sequenceNumberForOrdering;
    }
    
    public void setSequenceNumberForOrdering(String sequenceNumberForOrdering)
    {
        this.sequenceNumberForOrdering = sequenceNumberForOrdering;
    }
    
    public String getExplicitHashKey()
    {
        return explicitHashKey;
    }
    
    public void setExplicitHashKey(String explicitHashKey)
    {
        this.explicitHashKey = explicitHashKey;
    }

    public ByteBuffer getData()
    {
        return data;
    }

    public void setData(ByteBuffer data)
    {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStreamId()
    {
        return streamId;
    }
    
    public void setStreamId(String streamId)
    {
        this.streamId = streamId;
    }
    
    @Override
    public String toString()
    {
        return "PutRecordParam [partitionKey=" + partitionKey + ", streamName=" + streamName
            + ", sequenceNumberForOrdering=" + sequenceNumberForOrdering + ", explicitHashKey=" + explicitHashKey
            + ", timestamp=" + timestamp
            + "]";
    }
    
    
    
}