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
import com.huaweicloud.dis.iface.stream.request.Tag;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamInfo
{
    
    /**
     * <p>
     * 通道id。
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
     * </ul>
     */
    @JsonProperty("data_type")
    private String dataType;
    
    /**
     * <p>
     * 分区数量。
     * </p>
     */
    @JsonProperty("partition_count")
    private Integer partitionCount;
    
    /**
     * <p>
     * 通道标签列表
     * </p>
     */
    @JsonProperty("tags")
    private List<Tag> tags;
    /**
     * <p>
     * 通道企业项目列表
     * </p>
     */
    @JsonProperty("sys_tags")
    private List<Tag> sysTags;
    
    @JsonProperty("auto_scale_enabled")
    private Boolean autoScaleEnabled;
    
    @JsonProperty("auto_scale_min_partition_count")
    private Integer autoScaleMinPartitionCount;
    
    @JsonProperty("auto_scale_max_partition_count")
    private Integer autoScaleMaxPartitionCount;
    
    public String getStreamId()
    {
        return streamId;
    }

    public void setStreamId(String streamId)
    {
        this.streamId = streamId;
    }

    public Boolean getAutoScaleEnabled()
    {
        return autoScaleEnabled;
    }

    public void setAutoScaleEnabled(Boolean autoScaleEnabled)
    {
        this.autoScaleEnabled = autoScaleEnabled;
    }

    public Integer getAutoScaleMinPartitionCount()
    {
        return autoScaleMinPartitionCount;
    }

    public void setAutoScaleMinPartitionCount(Integer autoScaleMinPartitionCount)
    {
        this.autoScaleMinPartitionCount = autoScaleMinPartitionCount;
    }

    public Integer getAutoScaleMaxPartitionCount()
    {
        return autoScaleMaxPartitionCount;
    }

    public void setAutoScaleMaxPartitionCount(Integer autoScaleMaxPartitionCount)
    {
        this.autoScaleMaxPartitionCount = autoScaleMaxPartitionCount;
    }

    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public long getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
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
    
    public String getStreamType()
    {
        return streamType;
    }
    
    public void setStreamType(String streamType)
    {
        this.streamType = streamType;
    }
    
    public String getDataType()
    {
        return dataType;
    }
    
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
    
    public Integer getPartitionCount()
    {
        return partitionCount;
    }
    
    public void setPartitionCount(Integer partitionCount)
    {
        this.partitionCount = partitionCount;
    }
    
    public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

    public List<Tag> getSysTags() {
        return sysTags;
    }

    public void setSysTags(List<Tag> sysTags) {
        this.sysTags = sysTags;
    }

    @Override
    public String toString()
    {
        return "StreamInfo [streamId=" + streamId + ", streamName=" + streamName + ", createTime=" + createTime
            + ", retentionPeriod=" + retentionPeriod + ", status=" + status + ", streamType=" + streamType
            + ", dataType=" + dataType + ", partitionCount=" + partitionCount + ", tags=" + tags + ", sysTags="
            + sysTags + ", autoScaleEnabled=" + autoScaleEnabled + ", autoScaleMinPartitionCount="
            + autoScaleMinPartitionCount + ", autoScaleMaxPartitionCount=" + autoScaleMaxPartitionCount + "]";
    }

    
}
