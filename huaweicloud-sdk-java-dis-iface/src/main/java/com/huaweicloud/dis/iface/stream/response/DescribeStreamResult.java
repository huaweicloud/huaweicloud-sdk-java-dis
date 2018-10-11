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

package com.huaweicloud.dis.iface.stream.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.data.request.StreamType;
import com.huaweicloud.dis.iface.stream.request.Tag;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DescribeStreamResult
{
    
    /**
     * <p>
     * 通道ID。
     * </p>
     */
    @JsonProperty("stream_id")
    private String streamId;
    
    /**
     * <p>
     * 通道名称。
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * <p>
     * 通道创建时间。
     * </p>
     */
    @JsonProperty("create_time")
    private long createTime;
    
    /**
     * <p>
     * 通道最近修改时间。
     * </p>
     */
    @JsonProperty("last_modified_time")
    private long lastModifiedTime;
    
    /**
     * <p>
     * 数据保留时长。
     * </p>
     */
    @JsonProperty("retention_period")
    private int retentionPeriod;
    
    /**
     * <p>
     * 通道的当前状态，可能是以下的某种状态：
     * </p>
     * <ul>
     * <li>
     * <p>
     * CREATING - 创建中。
     * </p>
     * </li>
     * <li>
     * <p>
     * RUNNING - 运行中。
     * </p>
     * </li>
     * <li>
     * <p>
     * TERMINATING - 删除中。
     * </p>
     * </li>
     * </ul>
     */
    @JsonProperty("status")
    private String status;
    
    /**
     * <p>
     * 通道类型。表示通道支持的最大带宽。
     * </p>
     * <ul>
     * <li>
     * <p>
     * COMMON - 表示1MB带宽。
     * </p>
     * </li>
     * <li>
     * <p>
     * ADVANCED - 表示5MB带宽。
     * </p>
     * </li>
     * </ul>
     */
    @JsonProperty("stream_type")
    private String streamType;
    
    /**
     * <p>
     * 源数据类型。表示用户上传通道的源数据类型。
     * </p>
     * <ul>
     * <li>
     * <p>
     * BLOB - 表示二进制数据，DIS支持将BLOB类型数据转储至OBS、MRS服务。
     * </p>
     * </li>
     * <li>
     * <p>
     * JSON - DIS支持将JSON格式的数据转储至OBS、MRS、CloudTable服务。
     * </p>
     * </li>
     * <li>
     * <p>
     * CSV - DIS支持将CSV格式数据转储至OBS、MRS、DWS、DLI服务。
     * </p>
     * </li>
     * <li>
     * <p>
     * FILE - 表示小文件，DIS支持将用户上传的小文件转储至OBS服务。
     * </p>
     * </li>
     * </ul>
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * <p>
     * 用户JOSN、CSV格式数据schema,用avro shema描述
     * </p>
     */
    @JsonProperty("data_schema")
    private String dataSchema;
    
    /**
     * <p>
     * 通道的分区列表。
     * </p>
     */
    @JsonProperty("partitions")
    private List<PartitionResult> partitions;
    
    @JsonProperty("has_more_partitions")
    private Boolean hasMorePartitions = false;
    
    @JsonProperty("update_partition_counts")
    private List<UpdatePartitionCount> updatePartitionCounts;
    
    /**
     * <p>
     * 通道的标签列表
     * </p>
     */
    @JsonProperty("tags")
    private List<Tag> tags;
    
    public String getStreamId()
    {
        return streamId;
    }
    
    public void setStreamId(String streamId)
    {
        this.streamId = streamId;
    }
    
    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public int getRetentionPeriod()
    {
        return retentionPeriod;
    }
    
    public void setRetentionPeriod(int retentionPeriod)
    {
        this.retentionPeriod = retentionPeriod;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    /**
     * <p>
     * 通道类型。
     * </p>
     * <ul>
     * <li>
     * <p>
     * COMMON - 表示1MB带宽。
     * </p>
     * </li>
     * <li>
     * <p>
     * ADVANCED - 表示5MB带宽。
     * </p>
     * </li>
     * </ul>
     * 
     * @see com.huaweicloud.dis.iface.data.request.StreamType
     * @return 通道类型。表示通道支持的最大带宽。
     */
    public String getStreamType()
    {
        return streamType;
    }
    
    /**
     * <p>
     * 通道类型。
     * </p>
     * <ul>
     * <li>
     * <p>
     * COMMON - 表示1MB带宽。
     * </p>
     * </li>
     * <li>
     * <p>
     * ADVANCED - 表示5MB带宽。
     * </p>
     * </li>
     * </ul>
     * 
     * @see com.huaweicloud.dis.iface.data.request.StreamType
     * @param streamType 通道类型。表示通道支持的最大带宽。
     */
    public void setStreamType(String streamType)
    {
        this.streamType = streamType;
    }
    
    public List<PartitionResult> getPartitions()
    {
        return partitions;
    }
    
    public void setPartitions(List<PartitionResult> partitions)
    {
        this.partitions = partitions;
    }
    
    public long getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }
    
    public long getLastModifiedTime()
    {
        return lastModifiedTime;
    }
    
    public void setLastModifiedTime(long lastModifiedTime)
    {
        this.lastModifiedTime = lastModifiedTime;
    }
    
    public Boolean getHasMorePartitions()
    {
        return hasMorePartitions;
    }
    
    public void setHasMorePartitions(Boolean hasMorePartitions)
    {
        this.hasMorePartitions = hasMorePartitions;
    }
    
    public List<UpdatePartitionCount> getUpdatePartitionCounts()
    {
        return updatePartitionCounts;
    }
    
    public void setUpdatePartitionCounts(List<UpdatePartitionCount> updatePartitionCounts)
    {
        this.updatePartitionCounts = updatePartitionCounts;
    }
    
    public String getDataType()
    {
        return dataType;
    }
    
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDataSchema() {
        return dataSchema;
    }

    public void setDataSchema(String dataSchema) {
        this.dataSchema = dataSchema;
    }

    public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
    public String toString()
    {
        return "DescribeStreamResult [streamId=" + streamId + ", streamName=" + streamName + ", createTime="
            + createTime + ", lastModifiedTime=" + lastModifiedTime + ", retentionPeriod=" + retentionPeriod
            + ", status=" + status + ", streamType=" + streamType + ", dataType=" + dataType + ", partitions="
            + partitions + ", hasMorePartitions=" + hasMorePartitions + ", updatePartitionCounts="
            + updatePartitionCounts + "]";
    }
    
}
