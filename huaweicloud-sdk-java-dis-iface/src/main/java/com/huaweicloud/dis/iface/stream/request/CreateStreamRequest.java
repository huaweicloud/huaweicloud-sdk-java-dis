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
import com.huaweicloud.dis.iface.data.request.StreamType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStreamRequest
{
    /**
     * 普通通道类型
     */
    public static final String STREAM_TYPE_COMMON = StreamType.COMMON.getType();
    
    /**
     * 高级通道类型
     */
    public static final String STREAM_TYPE_ADVANCED = StreamType.ADVANCED.getType();
    
    /**
     * 流名称，必填。校验规则 [a-zA-Z0-9_-]+
     */
    //aws: [a-zA-Z0-9_.-]+ hws: [a-zA-Z0-9_-]+
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * 流类型
     * 
     * @see {@link #STREAM_TYPE_COMMON} 对应带宽上限为1MB
     * @see {@link #STREAM_TYPE_ADVANCED} 对应带宽上限为5MB
     */
    @JsonProperty("stream_type")
    private String streamType;
    
    /**
     * 分区数量，必填。正整数，但不应大于用户剩余分区配额
     * */
    @JsonProperty("partition_count")
    private Integer partitionCount;
    
    /**
     * 数据保留时长，单位为小时，默认为24小时
     */
    @JsonProperty("data_duration")
    private Integer dataDuration;
    
    /**
     * 是否启用自动扩缩容，默认关闭
     * */
    @JsonProperty("auto_scale_enabled")
    private Boolean autoScaleEnabled;
    
    /**
     * 当自动扩缩容启用时，自动缩容的最小分片数
     * */
    @JsonProperty("auto_scale_min_partition_count")
    private Integer autoScaleMinPartitionCount;
    
    /**
     * 当自动扩缩容启用时，自动扩容的最大分片数
     * */
    @JsonProperty("auto_scale_max_partition_count")
    private Integer autoScaleMaxPartitionCount;
    
    
    /**
     * 数据的类型，目前支持："BLOB"、"JSON"、"CSV"
     * */ 
    @JsonProperty("data_type")
    private String dataType;

    /**
     * <p>
     * 用户JOSN、CSV格式数据schema,选填，用avro shema描述
     * </p>
     */
    @JsonProperty("data_schema")
    private String dataSchema;

    /**
     * <p>
     * CSV格式数据的描述，选填。如delimiter
     * </p>
     */
    @JsonProperty("csv_properties")
    private CSVProperties csvProperties;
    
    /**
     * <p>
     * 数据的压缩类型，选填。目前支持：不压缩, snappy, gzip, zip
     * </p>
     */
    @JsonProperty("compression_format")
    private String compressionFormat;

    /**
     * <p>
     * 通道标签列表
     * </p>
     */
    @JsonProperty("tags")
    private List<Tag> tags;
    /**
     * <p>
     * 企业项目标签列表
     * </p>
     */
    @JsonProperty("sys_tags")
    private List<Tag> sysTags;
    /**
     * <p>
     * 通道是否在独享集群上创建,选填
     * </p>
     * true: 在独享集群上创建；false: 在共享集群上创建
     */
    @JsonProperty("is_private")
    private boolean isPrivate;
    
    /**
     * 在独享集群上创建的通道对应的独享集群ID
     */
    @JsonProperty("private_cluster_id")
    private String privateClusterId;
    
    /**
     * 在独享集群上创建的通道对应的集群Topic名称
     */
    @JsonProperty("private_cluster_topic_name")
    private String privateClusterTopicName;
    
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

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public String getStreamName()
    {
        return this.streamName;
    }
    
    public CreateStreamRequest withStreamName(String streamName)
    {
        setStreamName(streamName);
        return this;
    }
    
    public void setPartitionCount(Integer partitionCount)
    {
        this.partitionCount = partitionCount;
    }
    
    public Integer getPartitionCount()
    {
        return this.partitionCount;
    }
    
    public CreateStreamRequest withShardCount(Integer shardCount)
    {
        setPartitionCount(shardCount);
        return this;
    }
    
    public String getStreamType()
    {
        return streamType;
    }
    
    public void setStreamType(String streamType)
    {
        this.streamType = streamType;
    }
    
    public Integer getDataDuration()
    {
        return dataDuration;
    }
    
    public void setDataDuration(Integer dataDuration)
    {
        this.dataDuration = dataDuration;
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

    public List<Tag> getSysTags() {
        return sysTags;
    }

    public void setSysTags(List<Tag> sysTags) {
        this.sysTags = sysTags;
    }

    public String getCompressionFormat() {
        return compressionFormat;
    }

    public void setCompressionFormat(String compressionFormat) {
        this.compressionFormat = compressionFormat;
    }

    public CSVProperties getCsvProperties() {
        return csvProperties;
    }

    public void setCsvProperties(CSVProperties csvProperties) {
        this.csvProperties = csvProperties;
    }
    
	public boolean isPrivate()
    {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate)
    {
        this.isPrivate = isPrivate;
    }

    public String getPrivateClusterId()
    {
        return privateClusterId;
    }

    public void setPrivateClusterId(String privateClusterId)
    {
        this.privateClusterId = privateClusterId;
    }

    public String getPrivateClusterTopicName()
    {
        return privateClusterTopicName;
    }

    public void setPrivateClusterTopicName(String privateClusterTopicName)
    {
        this.privateClusterTopicName = privateClusterTopicName;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getStreamName() != null)
            sb.append("StreamName: ").append(getStreamName()).append(",");
        if (getPartitionCount() != null)
            sb.append("ShardCount: ").append(getPartitionCount());
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int hashCode = 1;
        
        hashCode = prime * hashCode + ((getStreamName() == null) ? 0 : getStreamName().hashCode());
        hashCode = prime * hashCode + ((getPartitionCount() == null) ? 0 : getPartitionCount().hashCode());
        return hashCode;
    }
}
