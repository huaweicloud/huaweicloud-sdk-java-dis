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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
     * 通道的分区列表。
     * </p>
     */
    @JsonProperty("partitions")
    private List<PartitionResult> partitions;
    
    @JsonProperty("has_more_partitions")
    private Boolean hasMorePartitions = false;

    @JsonProperty("update_partition_counts")
    private List<UpdatePartitionCount> updatePartitionCounts;
    
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
     * @see {@link StreamType}
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
     * @see {@link StreamType}
     * @param 通道类型。表示通道支持的最大带宽。
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

    @Override
    public String toString()
    {
        return "DescribeStreamResult [streamId=" + streamId + ", streamName=" + streamName + ", createTime="
            + createTime + ", lastModifiedTime=" + lastModifiedTime + ", retentionPeriod=" + retentionPeriod
            + ", status=" + status + ", streamType=" + streamType + ", partitions=" + partitions
            + ", hasMorePartitions=" + hasMorePartitions + ", updatePartitionCounts=" + updatePartitionCounts + "]";
    }



}
