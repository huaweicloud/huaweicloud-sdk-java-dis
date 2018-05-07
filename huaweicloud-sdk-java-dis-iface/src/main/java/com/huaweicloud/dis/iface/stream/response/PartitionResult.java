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

package com.huaweicloud.dis.iface.stream.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kienluu on 7/19/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartitionResult
{
    /**
     * <p>
     * 分区的当前状态。可能是以下某种状态：
     * </p>
     * <ul>
     * <li>
     * <p>
     * CREATING - 创建中。
     * </p>
     * </li>
     * <li>
     * <p>
     * ACTIVE - 运行中。
     * </p>
     * </li>
     * <li>
     * <li>
     * <p>
     * DELETED - 已删除。
     * </p>
     * </li>
     * <li>
     * <p>
     * EXPIRED - 已过期。
     * </p>
     * </li>
     * </ul>
     */
    @JsonProperty("status")
    private String status;

    /**
     * <p>
     * 分区的唯一标识符。
     * </p>
     */
    @JsonProperty("partition_id")
    private String partitionId;

    /**
     * <p>
     * 分区的可能哈希键值范围。
     * </p>
     */
    @JsonProperty("hash_range")
    private String hashRange;

    /**
     * <p>
     * 父分区。
     * </p>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("parent_partitions")
    private String parentPartitionIds;

    /**
     * <p>
     * 分区的可能序列号范围。
     * </p>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sequence_number_range")
    private String sequenceNumberRange;

    public String getSequenceNumberRange()
    {
        return sequenceNumberRange;
    }

    public void setSequenceNumberRange(String sequenceNumberRange)
    {
        this.sequenceNumberRange = sequenceNumberRange;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getParentPartitionIds()
    {
        return parentPartitionIds;
    }

    public void setParentPartitionIds(String parentPartitionIds)
    {
        this.parentPartitionIds = parentPartitionIds;
    }

    public String getPartitionId()
    {
        return partitionId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId = partitionId;
    }

    public String getHashRange()
    {
        return hashRange;
    }

    public void setHashRange(String hashRange)
    {
        this.hashRange = hashRange;
    }

    @Override
    public String toString()
    {
        return "PartitionResult{" +
                "partitionId='" + partitionId + '\'' +
                ", hashRange='" + hashRange + '\'' +
                ", status='" + status + '\'' +
                ", parentPartitionIds='" + parentPartitionIds + '\'' +
//                ", sequenceNumberRange='" + sequenceNumberRange + '\'' +
                '}';
    }
}
