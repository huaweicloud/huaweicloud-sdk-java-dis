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
public class GetCheckpointRequest
{
    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("partition_id")
    private String partitionId;

    @JsonProperty("checkpoint_type")
    private String checkpointType;

    @JsonProperty("app_name")
    private String appName;

    @JsonProperty("timestamp")
    private Long timeStamp;

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

    public String getCheckpointType()
    {
        return checkpointType;
    }

    public void setCheckpointType(String checkpointType)
    {
        this.checkpointType = checkpointType;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppId(String appId)
    {
        this.appName = appId;
    }

    public Long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString()
    {
        return "GetIteratorParam [streamName=" + streamName + ", shardId=" + partitionId + ", checkpointType="
                + checkpointType + ", appId=" + appName  + ", timeStamp=" + timeStamp +  "]";
    }
}
