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

package com.otccloud.dis.iface.data.request;

import java.nio.ByteBuffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.otccloud.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutRecordsRequestEntry
{
    /**
     * <p>
     * 需要上传的数据，不可为空字符串。
     * </p>
     */
    @JsonProperty("data")
    private ByteBuffer data;
    
    /**
     * <p>
     * 用于明确数据需要写入分区的哈希值，此哈希值将覆盖“partition_key”的哈希值。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("explicit_hash_key")
    private String explicitHashKey;
    
    /**
     * <p>
     * 数据写入分区的分区值， 优先使用
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("partition_id")
    private String partitionId;
    
    /**
     * <p>
     * 数据写入分区的分区键。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("partition_key")
    private String partitionKey;

    @JsonProperty("timestamp")
    private Long timestamp;

    public ByteBuffer getData()
    {
        return data;
    }

    public void setData(ByteBuffer data)
    {
        this.data = data;
    }

    public String getExplicitHashKey()
    {
        return explicitHashKey;
    }

    public void setExplicitHashKey(String explicitHashKey)
    {
        this.explicitHashKey = explicitHashKey;
    }

    public String getPartitionKey()
    {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey)
    {
        this.partitionKey = partitionKey;
    }
    
    public String getPartitionId()
    {
        return partitionId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId = partitionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "PutRecordsRequestEntry [data=" + data + ", explicitHashKey=" + explicitHashKey + ", partitionId="
            + partitionId + ", partitionKey=" + partitionKey + ", timestamp=" + timestamp + "]";
    }


}
