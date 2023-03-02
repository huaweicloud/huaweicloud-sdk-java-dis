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

package com.cloud.dis.iface.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutRecordsResultEntry
{
    /**
     * <p>
     * 分区ID。
     * </p>
     */
    @JsonProperty("partition_id")
    private String partitionId;
    
    /**
     * <p>
     * 序列号。序列号是每个记录的唯一标识符。序列号由DIS在数据生产者调用 <code>PutRecord</code> 操作以添加数据到DIS数据通道时DIS服务自动分配的。
     * 同一分区键的序列号通常会随时间变化增加。
     * </p>
     */
    @JsonProperty("sequence_number")
    private String sequenceNumber;
    
    /**
     * <p>
     * 错误码。
     * </p>
     */
    @JsonProperty("error_code")
    private String errorCode;
    
    /**
     * <p>
     * 错误消息。
     * </p>
     */
    @JsonProperty("error_message")
    private String errorMessage;

    public String getPartitionId()
    {
        return partitionId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId = partitionId;
    }

    public String getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString()
    {
        return "PutRecordsResultEntry [partitionId=" + partitionId + ", sequenceNumber=" + sequenceNumber + ", errorCode="
            + errorCode + ", errorMessage=" + errorMessage +  "]";
    }
    
}
