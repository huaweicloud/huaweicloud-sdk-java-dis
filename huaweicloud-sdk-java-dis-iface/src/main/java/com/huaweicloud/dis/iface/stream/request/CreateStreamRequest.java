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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.stream.request.cloudtable.CloudtableDestinationDescriptorRequest;
import com.huaweicloud.dis.iface.stream.request.dws.DwsDestinationDescriptorRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStreamRequest
{
    /**
     * 普通通道类型
     */
    public static final String STREAM_TYPE_COMMON = "COMMON";
    
    /**
     * 高级通道类型
     */
    public static final String STREAM_TYPE_ADVANCED = "ADVANCED";
    
    /** -------------------- stream属性 ------------------- **/
    
    /**
     * 流名称 aws: [a-zA-Z0-9_.-]+ hws: [a-zA-Z0-9_-]+
     */
    // @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("stream_name")
    private String streamName;
    
    // @JsonDeserialize(using = ForceIntegerDeserializer.class)
    @JsonProperty("partition_count")
    private Integer partitionCount;
    
    @JsonProperty("auto_scale_enabled")
    private Boolean autoScaleEnabled;
    
    @JsonProperty("auto_scale_min_partition_count")
    private Integer autoScaleMinPartitionCount;
    
    @JsonProperty("auto_scale_max_partition_count")
    private Integer autoScaleMaxPartitionCount;
    
    /** -------------------- DIS属性 ------------------- **/
    
    /** OBS目的对象描述 */
    @JsonProperty("obs_destination_descriptor")
    private List<OBSDestinationDescriptorRequest> obsDestinationDescriptors;
    
    /** MRS目的对象描述 */
    @JsonProperty("mrs_destination_descriptor")
    private List<MRSDestinationDescriptorRequest> mrsDestinationDescriptors;
    
    /** Uquery目的对象描述 */
    @JsonProperty("uquery_destination_descriptor")
    private List<UqueryDestinationDescriptorRequest> uqueryDestinationDescriptors;
    
    /** dli目的对象描述:Uquery服务改名为dli，为兼容原接口，保留uquery_destination_descriptor */
    @JsonProperty("dli_destination_descriptor")
    private List<UqueryDestinationDescriptorRequest> dliDestinationDescriptors;
    
    /** cloudttable目的对象描述 */
    @JsonProperty("cloudtable_destination_descriptor")
    private List<CloudtableDestinationDescriptorRequest> cloudtableDestinationDescriptors;
    
    /** dws目的对象描述 */
    @JsonProperty("dws_destination_descriptor")
    private List<DwsDestinationDescriptorRequest> dwsDestinationDescriptors;
    
    /**
     * 流类型
     * 
     * @see {@link #STREAM_TYPE_COMMON} 对应带宽上限为1MB
     * @see {@link #STREAM_TYPE_ADVANCED} 对应带宽上限为5MB
     */
    // @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("stream_type")
    private String streamType;

    // 数据的类型，目前支持：BLOB、JSON、CSV格式
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
     * 数据的压缩类型，目前支持：不压缩, snappy, gzip, zip
     * </p>
     */
    @JsonProperty("compression_format")
    private String compressionFormat;


    /**
     * 数据保留时长，单位为小时，默认为24小时
     */
    // @JsonDeserialize(using = ForceIntegerDeserializer.class)
    @JsonProperty("data_duration")
    private Integer dataDuration;

    /**
     * <p>
     * CSV格式数据的描述，如delimiter
     * </p>
     */
    @JsonProperty("csv_properties")
    private CSVProperties csvProperties;

    /**
     * <p>
     * 通道标签列表
     * </p>
     */
    @JsonProperty("tags")
    private List<Tag> tags;
    
    
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
    
    public List<MRSDestinationDescriptorRequest> getMrsDestinationDescriptors()
    {
        return mrsDestinationDescriptors;
    }
    
    public void setMrsDestinationDescriptors(List<MRSDestinationDescriptorRequest> mrsDestinationDescriptors)
    {
        this.mrsDestinationDescriptors = mrsDestinationDescriptors;
    }
    
    public List<OBSDestinationDescriptorRequest> getObsDestinationDescriptors()
    {
        return obsDestinationDescriptors;
    }
    
    public void setObsDestinationDescriptors(List<OBSDestinationDescriptorRequest> obsDestinationDescriptors)
    {
        this.obsDestinationDescriptors = obsDestinationDescriptors;
    }
    
    public List<UqueryDestinationDescriptorRequest> getUqueryDestinationDescriptors()
    {
        return uqueryDestinationDescriptors;
    }
    
    public void setUqueryDestinationDescriptors(List<UqueryDestinationDescriptorRequest> uqueryDestinationDescriptors)
    {
        this.uqueryDestinationDescriptors = uqueryDestinationDescriptors;
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
    
    public List<CloudtableDestinationDescriptorRequest> getCloudtableDestinationDescriptors()
    {
        return cloudtableDestinationDescriptors;
    }
    
    public void setCloudtableDestinationDescriptors(
        List<CloudtableDestinationDescriptorRequest> cloudtableDestinationDescriptors)
    {
        this.cloudtableDestinationDescriptors = cloudtableDestinationDescriptors;
    }
    
    public List<DwsDestinationDescriptorRequest> getDwsDestinationDescriptors()
    {
        return dwsDestinationDescriptors;
    }
    
    public void setDwsDestinationDescriptors(List<DwsDestinationDescriptorRequest> dwsDestinationDescriptors)
    {
        this.dwsDestinationDescriptors = dwsDestinationDescriptors;
    }
    
    public List<UqueryDestinationDescriptorRequest> getDliDestinationDescriptors()
    {
        return dliDestinationDescriptors;
    }
    
    public void setDliDestinationDescriptors(List<UqueryDestinationDescriptorRequest> dliDestinationDescriptors)
    {
        this.dliDestinationDescriptors = dliDestinationDescriptors;
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
