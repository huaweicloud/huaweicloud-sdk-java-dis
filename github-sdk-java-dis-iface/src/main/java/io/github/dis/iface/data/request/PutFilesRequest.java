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

package io.github.dis.iface.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutFilesRequest
{
    /**
     * 通道名称。
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("stream_name")
    private String streamName;
    
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
     * 数据写入分区的分区键。
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("partition_key")
    private String partitionKey;
    
    /**
     * <p>
     * 文件名称。
     * </p>
     */
    @JsonProperty("file_name")
    private String fileName;
    
    /**
     * <p>
     * 文件路径。
     * </p>
     */
    @JsonProperty("file_path")
    private String filePath;
    
    public String getStreamName()
    {
        return streamName;
    }

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
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

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    @Override
    public String toString()
    {
        return "PutFilesRequest [streamName=" + streamName + ", explicitHashKey=" + explicitHashKey + ", partitionKey="
            + partitionKey + ", fileName=" + fileName + ", filePath=" + filePath + "]";
    }

}
