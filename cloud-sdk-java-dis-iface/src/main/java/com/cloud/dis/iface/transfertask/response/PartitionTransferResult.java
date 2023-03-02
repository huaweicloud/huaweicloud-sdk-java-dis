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
package com.cloud.dis.iface.transfertask.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartitionTransferResult {
    private String partitionId;

    /**
     * <p>
     * The transfer state of the delivery task.
     * </p>
     */
    @JsonProperty("state")
    private String state;

    /**
     * <p>
     * The lastest transfer timeStamp of the partition.
     * </p>
     */
    @JsonProperty("last_transfer_timestamp")
    private long lastTransferTimeStamp;

    /**
     * <p>
     * The lastest partition transfer offsets of the partition.
     * </p>
     */
    @JsonProperty("last_transfer_offset")
    private long lastTransferOffset;

    /**
     * <p>
     * The discard data number of the partition.
     * </p>
     */
    private long discard;

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getLastTransferTimeStamp() {
        return lastTransferTimeStamp;
    }

    public void setLastTransferTimeStamp(long lastTransferTimeStamp) {
        this.lastTransferTimeStamp = lastTransferTimeStamp;
    }

    public long getLastTransferOffset() {
        return lastTransferOffset;
    }

    public void setLastTransferOffset(long lastTransferOffset) {
        this.lastTransferOffset = lastTransferOffset;
    }

    public long getDiscard() {
        return discard;
    }

    public void setDiscard(long discard) {
        this.discard = discard;
    }
}
