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

package com.huaweicloud.dis.iface.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutRecordsRequestExtended
{
    
    /**
     * <p>
     * 文件名称。
     * </p>
     */
    @JsonProperty("file_name")
    private String fileName;
    
    /**
     * <p>
     * 文件唯一标识符。
     * </p>
     */
    @JsonProperty("md5")
    private String md5;
    
    /**
     * <p>
     * 是否文件末尾，如果为 true 则表示单个文件上传结束。
     * </p>
     */
    @JsonProperty("deliver_end_flag")
    private Boolean deliverEndFlag = false;
    
    /**
     * <p>
     * 本次文件上传序列号。
     * </p>
     */
    @JsonProperty("sequence_number")
    private Integer sequenceNumber;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public Boolean getDeliverEndFlag()
    {
        return deliverEndFlag;
    }

    public void setDeliverEndFlag(Boolean deliverEndFlag)
    {
        this.deliverEndFlag = deliverEndFlag;
    }

    public Integer getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString()
    {
        return "PutFilesRequestExtended [fileName=" + fileName + ", md5=" + md5 + ", deliverEndFlag=" + deliverEndFlag
            + ", sequenceNumber=" + sequenceNumber + "]";
    }
    
}
