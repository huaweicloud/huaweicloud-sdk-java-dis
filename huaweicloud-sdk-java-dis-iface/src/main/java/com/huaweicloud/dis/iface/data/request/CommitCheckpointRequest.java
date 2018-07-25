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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommitCheckpointRequest
{

    @JsonProperty("app_name")
    private String appName;

    @JsonProperty("checkpoint_type")
    private String checkpointType;
    
    /**
     * <p>
     * 流名称。
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * <p>
     * 分区值。
     * </p>
     */
    @JsonProperty("partition_id")
    private String partitionId;
    
    /**
     * <p>
     * 分区序列号。分区序列号是每个记录的唯一标识符。
     * </p>
     */
    @JsonProperty("sequence_number")
    private String sequenceNumber;
    
    /**
     * <p>
     * 用户自定义元数据信息。
     * </p>
     */
    @JsonProperty("metadata")
    private String metadata;

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getCheckpointType()
    {
        return checkpointType;
    }

    public void setCheckpointType(String checkpointType)
    {
        this.checkpointType = checkpointType;
    }

    public String getStreamName()
    {
        return streamName;
    }

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    public String getPartitionId()
    {
        return partitionId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId = partitionId;
    }

    public String getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    public String getMetadata()
    {
        return metadata;
    }

    public void setMetadata(String metadata)
    {
        this.metadata = metadata;
    }

    @Override
    public String toString()
    {
        return "CommitCheckpointRequest [appName=" + appName + ", checkpointType=" + checkpointType + ", streamName="
            + streamName + ", partitionId=" + partitionId + ", sequenceNumber=" + sequenceNumber + ", metadata="
            + metadata + "]";
    }
    
}
