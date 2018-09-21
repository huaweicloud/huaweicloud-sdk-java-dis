package com.huaweicloud.dis.iface.app.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartitionConsumingState {

    /**
     * <p>
     * 分区值。
     * </p>
     */
    @JsonProperty("partition_id")
    private String partitionId;

    /**
     * <p>
     * 分区序列号。分区序列号是每个记录的唯一标识符。
     * </p>
     */
    @JsonProperty("sequence_number")
    private String sequenceNumber;

    /**
     * <p>
     * 用户自定义元数据信息。
     * </p>
     */
    @JsonProperty("metadata")
    private String metadata;


    /**
     * <p>
     * partition data latest offset
     * </p>
     */
    @JsonProperty("latest_offset")
    private String latestOffset;

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getLatestOffset() {
        return latestOffset;
    }

    public void setLatestOffset(String latestOffset) {
        this.latestOffset = latestOffset;
    }
}
