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

package com.huaweicloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitPartitionRequest
{
    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("partition_to_split")
    private String partitionToSplit;

    @JsonProperty("starting_hash_key")
    private String startingHashKey;

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
    
    public String getPartitionToSplit()
    {
        return partitionToSplit;
    }
    
    public void setPartitionToSplit(String partitionToSplit)
    {
        this.partitionToSplit = partitionToSplit;
    }
    
    public String getStartingHashKey()
    {
        return startingHashKey;
    }
    
    public void setStartingHashKey(String startingHashKey)
    {
        this.startingHashKey = startingHashKey;
    }
    
    public String getStreamRowkey() {
        return streamRowkey;
    }
    
    public void setStreamRowkey(String streamRowkey) {
        this.streamRowkey = streamRowkey;
    }
    
    @Override
    public String toString() {
        return "SplitShardRequest{" +
                "streamName='" + streamName + '\'' +
                ", shardToSplit='" + partitionToSplit + '\'' +
                ", startingHashKey='" + startingHashKey + '\'' +
                ", streamRowkey='" + streamRowkey + '\'' +
                '}';
    }
}
