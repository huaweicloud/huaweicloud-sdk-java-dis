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

package com.cloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class DescribeStreamRequest
{
    protected static final int PARTITION_LIMITS = 100;

    /**
     * <p>
     * 通道名称。
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;

    /**
     * <p>
     * 从该分区值开始返回分区列表，返回的分区列表不包括此分区。
     * 分区格式为shardId-%010d，比如0分区为shardId-0000000000；15分区为shardId-0000000015
     * </p>
     */
    @JsonProperty("start_partitionId")
    private String startPartitionId;

    /**
     * <p>
     * 单次请求返回的最大分区数。最小值是1，最大值是1000；默认值是100。
     * </p>
     */
    @JsonProperty("limit_partitions")
    private int limitPartitions = PARTITION_LIMITS;

    @JsonProperty("stream_id")
    private String streamId;

    public String getStreamName()
    {
        return streamName;
    }

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    public String getStartPartitionId()
    {
        return startPartitionId;
    }

    public void setStartPartitionId(String startPartitionId)
    {
        this.startPartitionId = startPartitionId;
    }

    public int getLimitPartitions()
    {
        return limitPartitions;
    }

    public void setLimitPartitions(int limitPartitions)
    {
        this.limitPartitions = limitPartitions;
    }


    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    @Override
    public String toString()
    {
        return "DescribeStreamRequest{" +
                "streamName='" + streamName + '\'' +
                ", startPartitionId='" + startPartitionId + '\'' +
                ", limitPartitions=" + limitPartitions +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DescribeStreamRequest that = (DescribeStreamRequest) o;

        if (limitPartitions != that.limitPartitions) return false;
        if (!streamName.equals(that.streamName)) return false;
        return startPartitionId.equals(that.startPartitionId);
    }

    @Override
    public int hashCode()
    {
        int result = streamName.hashCode();
        result = 31 * result + startPartitionId.hashCode();
        result = 31 * result + limitPartitions;
        return result;
    }

}
