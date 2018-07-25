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
import com.huaweicloud.dis.iface.stream.request.StreamDestinationType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferTaskResult
{
    private StreamDestinationType type;

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
    
    public StreamDestinationType getType()
    {
        return type;
    }
    
    public void setType(StreamDestinationType type)
    {
        this.type = type;
    }
    
    public String getTransferTaskName()
    {
        return transferTaskName;
    }
    
    public void setTransferTaskName(String transferTaskName)
    {
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
}
