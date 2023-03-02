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
public class UpdateStreamRequest
{
    /**
     * 更新类别 UpdateStreamTypeEnum
     * 
     * "DATA_TYPE"
     * "AUTO_SCALE"
     * */
    @JsonProperty("update_type")
    private String updateType; 
    
    /**
     * <p>
     * 通道名称.
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * <p>
     * 指定修改后的源数据类型.
     * </p>
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * <p>
     * 指定修改后的源数据schema.
     * </p>
     */
    @JsonProperty("data_schema")
    private String dataSchema;
    
    /**
     * 设置是否自动扩缩容
     * */
    @JsonProperty("auto_scale_enabled")
    private Boolean autoScaleEnabled;
    
    @JsonProperty("auto_scale_min_partition_count")
    private Integer autoScaleMinPartitionCount;
    
    @JsonProperty("auto_scale_max_partition_count")
    private Integer autoScaleMaxPartitionCount;
    
    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    public String getUpdateType()
    {
        return updateType;
    }

    public void setUpdateType(String updateType)
    {
        this.updateType = updateType;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDataSchema()
    {
        return dataSchema;
    }

    public void setDataSchema(String dataSchema)
    {
        this.dataSchema = dataSchema;
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

    @Override
    public String toString()
    {
        return "UpdateStreamRequest [updateType=" + updateType + ", streamName=" + streamName + ", dataType="
            + dataType + ", dataSchema=" + dataSchema + ", autoScaleEnabled=" + autoScaleEnabled
            + ", autoScaleMinPartitionCount=" + autoScaleMinPartitionCount + ", autoScaleMaxPartitionCount="
            + autoScaleMaxPartitionCount + "]";
    }
    
    public static enum UpdateStreamTypeEnum{
        DATA_TYPE, AUTO_SCALE
    }
}
