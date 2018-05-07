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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBSDestinationDescriptorRequest
{
    /** taskName */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("task_name")
    private String transferTaskName;
    
    /** agency name */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("agency_name")
    private String agencyName;
    
    /** OBS file prefix */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("file_prefix")
    private String filePrefix;
    
    /** OBS partition format */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("partition_format")
    private String partitionFormat;
    
    /** obs Bucket path */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("obs_bucket_path")
    private String obsBucketPath;
    
    /** deliver time interval */
    @JsonDeserialize(using = ForceIntegerDeserializer.class)
    @JsonProperty("deliver_time_interval")
    private int deliverTimeInterval;
    
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("deliver_data_type")
    private String deliverDataType;
    
    /** 允许Firehose 重试的持续时间 (0–7200 秒) */
    @JsonProperty("retry_duration")
    private int retryDuration;
    
    public String getPartitionFormat()
    {
        return partitionFormat;
    }
    
    public void setPartitionFormat(String partitionFormat)
    {
        this.partitionFormat = partitionFormat;
    }
    
    public String getFilePrefix()
    {
        return filePrefix;
    }
    
    public void setFilePrefix(String filePrefix)
    {
        this.filePrefix = filePrefix;
    }
    
    public String getObsBucketPath()
    {
        return obsBucketPath;
    }
    
    public void setObsBucketPath(String obsBucketPath)
    {
        this.obsBucketPath = obsBucketPath;
    }
    
    public int getDeliverTimeInterval()
    {
        return deliverTimeInterval;
    }
    
    public void setDeliverTimeInterval(int deliverTimeInterval)
    {
        this.deliverTimeInterval = deliverTimeInterval;
    }
    
    public String getDeliverDataType()
    {
        return deliverDataType;
    }
    
    public void setDeliverDataType(String deliverDataType)
    {
        this.deliverDataType = deliverDataType;
    }
    
    public String getAgencyName()
    {
        return agencyName;
    }
    
    public void setAgencyName(String agencyName)
    {
        this.agencyName = agencyName;
    }
    
    public String getTransferTaskName()
    {
        return transferTaskName;
    }
    
    public void setTransferTaskName(String transferTaskName)
    {
        this.transferTaskName = transferTaskName;
    }
    
    public int getRetryDuration()
    {
        return retryDuration;
    }
    
    public void setRetryDuration(int retryDuration)
    {
        this.retryDuration = retryDuration;
    }
}
