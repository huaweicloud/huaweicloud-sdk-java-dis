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
public class MergePartitionsRequest {
    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("partition_to_merge")
    private String partitionToMerge;

    @JsonProperty("adjacent_partition")
    private String adjacentPartition;

    @JsonProperty("stream_rowkey")
    private String streamRowkey;
    
    public String getStreamName()
    {
        return streamName;
    }

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    public String getPartitionToMerge()
    {
        return partitionToMerge;
    }

    public void setPartitionToMerge(String shardToMerge)
    {
        this.partitionToMerge = shardToMerge;
    }

    public String getAdjacentPartition()
    {
        return adjacentPartition;
    }

    public void setAdjacentPartition(String adjacentPartition)
    {
        this.adjacentPartition = adjacentPartition;
    }
    
    public String getStreamRowkey() {
        return streamRowkey;
    }
    
    public void setStreamRowkey(String streamRowkey) {
        this.streamRowkey = streamRowkey;
    }
    
    @Override
    public String toString() {
        return "MergeShardsRequest{" +
                "streamName='" + streamName + '\'' +
                ", shardToMerge='" + partitionToMerge + '\'' +
                ", adjacentShard='" + adjacentPartition + '\'' +
                ", streamRowkey='" + streamRowkey + '\'' +
                '}';
    }
}
