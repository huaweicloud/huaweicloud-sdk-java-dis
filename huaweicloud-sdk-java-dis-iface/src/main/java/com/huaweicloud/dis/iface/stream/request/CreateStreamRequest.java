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

/**
 * 
 * @author s00313265
 *
 */
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
    
    // 数据的类型，目前支持：BLOB、JSON格式
    @JsonProperty("data_type")
    private String dataType;
    
    /**
     * 数据保留时长，单位为小时，默认为24小时
     */
    // @JsonDeserialize(using = ForceIntegerDeserializer.class)
    @JsonProperty("data_duration")
    private Integer dataDuration;
    
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

    public List<DwsDestinationDescriptorRequest> getDwsDestinationDescriptors() {
        return dwsDestinationDescriptors;
    }

    public void setDwsDestinationDescriptors(List<DwsDestinationDescriptorRequest> dwsDestinationDescriptors) {
        this.dwsDestinationDescriptors = dwsDestinationDescriptors;
    }

    public String getDataType()
    {
        return dataType;
    }
    
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
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
