package com.huaweicloud.dis.iface.app.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListStreamConsumingStateRequest {

    protected static final int PARTITION_LIMITS = 100;

    /**
     * <p>
     * 从该分区值开始返回分区列表，返回的分区列表不包括此分区。
     * </p>
     */
    @JsonProperty("start_partition_id")
    private String startPartitionId;

    /**
     * <p>
     * 单次请求返回的最大分区数。最小值是1，最大值是1000；默认值是100。
     * </p>
     */
    private int limit = PARTITION_LIMITS;

    @JsonProperty("app_name")
    private String appName;

    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("checkpoint_type")
    private String checkpointType;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getStartPartitionId() {
        return startPartitionId;
    }

    public void setStartPartitionId(String startPartitionId) {
        this.startPartitionId = startPartitionId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getCheckpointType() {
        return checkpointType;
    }

    public void setCheckpointType(String checkpointType) {
        this.checkpointType = checkpointType;
    }
}
