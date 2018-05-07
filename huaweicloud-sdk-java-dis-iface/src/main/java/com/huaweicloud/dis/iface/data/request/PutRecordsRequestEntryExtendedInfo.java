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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.huaweicloud.dis.iface.stream.request.ForceStringDeserializer;

/**
 * Created by z00382129 on 2017/9/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutRecordsRequestEntryExtendedInfo {

    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("file_name")
    private String fileName;

    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("deliver_data_id")
    private String deliverDataId;

    @JsonProperty("end_flag")
    private Boolean endFlag = false;

    @JsonProperty("sequence_number")
    private Long seqNum;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(Boolean endFlag) {
        this.endFlag = endFlag;
    }

    public Long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public String getDeliverDataId() {
        return deliverDataId;
    }

    public void setDeliverDataId(String deliverDataId) {
        this.deliverDataId = deliverDataId;
    }

    @Override
    public String toString()
    {
        return "PutRecordsRequestEntryExtendedInfo [fileName=" + fileName + ", deliverDataId=" + deliverDataId
            + ", endFlag=" + endFlag + ", seqNum=" + seqNum + "]";
    }


}
