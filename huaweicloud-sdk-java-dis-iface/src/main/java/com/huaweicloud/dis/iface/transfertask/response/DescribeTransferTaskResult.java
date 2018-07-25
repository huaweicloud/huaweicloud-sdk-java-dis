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
package com.huaweicloud.dis.iface.transfertask.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.transfertask.response.destination.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DescribeTransferTaskResult
{
    /**
     * <p>
     * The name of the delivery stream.
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * <p>
     * The name of the delivery task.
     * </p>
     */
    @JsonProperty("task_name")
    private String transferTaskName;

    
    /**
     * <p>
     * The create time of the transfer task.
     * </p>
     */
    @JsonProperty("create_time")
    private long createTime;
    
    /**
     * <p>
     * The lastest transfer timeStamp of the transfer task.
     * </p>
     */
    @JsonProperty("last_transfer_timestamp")
    private long lastTransferTimeStamp;
    
    /**
     * <p>
     * The lastest partition transfer offsets of the transfer task.
     * </p>
     */
    @JsonProperty("last_partitions_transfer_offset")
    private String lastPartitionsTransferOffset;
    
    /**
     * <p>
     * description of a task that dump data from DIS to OBS.
     * </p>
     */
    @JsonProperty("obs_destination_description")
    private OBSDestinationDescription obsDestinationDescription;
    
    /**
     * <p>
     * description of a task that dump data from DIS to MRS.
     * </p>
     */
    @JsonProperty("mrs_destination_description")
    private MRSDestinationDescription mrsDestinationDescription;
    
    /**
     * <p>
     * description of a task that dump data from DIS to DLI.
     * </p>
     */
    @JsonProperty("dli_destination_description")
    private DliDestinationDescription dliDestinationDescription;
    
    /**
     * <p>
     * description of a task that dump data from DIS to DWS.
     * </p>
     */
    @JsonProperty("dws_destination_description")
    private DwsDestinationDescription dwsDestinationDescription;
    
    /**
     * <p>
     * description of a task that dump data from DIS to CloudTable.
     * </p>
     */
    @JsonProperty("cloudtable_destination_description")
    private CloudtableDestinationDescription cloudtableDestinationDescription;
    
    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    public String getTransferTaskName() {
        return transferTaskName;
    }

    public void setTransferTaskName(String transferTaskName) {
        this.transferTaskName = transferTaskName;
    }

    public long getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }
    
    public long getLastTransferTimeStamp()
    {
        return lastTransferTimeStamp;
    }
    
    public void setLastTransferTimeStamp(long lastTransferTimeStamp)
    {
        this.lastTransferTimeStamp = lastTransferTimeStamp;
    }
    
    public String getLastPartitionsTransferOffset()
    {
        return lastPartitionsTransferOffset;
    }
    
    public void setLastPartitionsTransferOffset(String lastPartitionsTransferOffset)
    {
        this.lastPartitionsTransferOffset = lastPartitionsTransferOffset;
    }
    
    public OBSDestinationDescription getObsDestinationDescription()
    {
        return obsDestinationDescription;
    }
    
    public void setObsDestinationDescription(OBSDestinationDescription obsDestinationDescription)
    {
        this.obsDestinationDescription = obsDestinationDescription;
    }
    
    public MRSDestinationDescription getMrsDestinationDescription()
    {
        return mrsDestinationDescription;
    }
    
    public void setMrsDestinationDescription(MRSDestinationDescription mrsDestinationDescription)
    {
        this.mrsDestinationDescription = mrsDestinationDescription;
    }
    
    public DliDestinationDescription getDliDestinationDescription()
    {
        return dliDestinationDescription;
    }
    
    public void setDliDestinationDescription(DliDestinationDescription dliDestinationDescription)
    {
        this.dliDestinationDescription = dliDestinationDescription;
    }
    
    public DwsDestinationDescription getDwsDestinationDescription()
    {
        return dwsDestinationDescription;
    }
    
    public void setDwsDestinationDescription(DwsDestinationDescription dwsDestinationDescription)
    {
        this.dwsDestinationDescription = dwsDestinationDescription;
    }
    
    public CloudtableDestinationDescription getCloudtableDestinationDescription()
    {
        return cloudtableDestinationDescription;
    }
    
    public void setCloudtableDestinationDescription(CloudtableDestinationDescription cloudtableDestinationDescription)
    {
        this.cloudtableDestinationDescription = cloudtableDestinationDescription;
    }
}
