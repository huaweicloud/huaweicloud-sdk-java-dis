package io.github.dis.iface.app.response;

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
    private Long latestOffset;

    /**
     * <p>
     * partition data earliest offset
     * </p>
     */
    @JsonProperty("earliest_offset")
    private Long earliestOffset;

    @JsonProperty("checkpoint_type")
    private String checkpointType;
    
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


    public String getCheckpointType() {
        return checkpointType;
    }

    public void setCheckpointType(String checkpointType) {
        this.checkpointType = checkpointType;
    }

    public Long getLatestOffset() {
        return latestOffset;
    }

    public void setLatestOffset(Long latestOffset) {
        this.latestOffset = latestOffset;
    }

    public Long getEarliestOffset() {
        return earliestOffset;
    }

    public void setEarliestOffset(Long earliestOffset) {
        this.earliestOffset = earliestOffset;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
