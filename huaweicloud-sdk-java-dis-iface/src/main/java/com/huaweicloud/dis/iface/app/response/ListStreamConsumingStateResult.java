package com.huaweicloud.dis.iface.app.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListStreamConsumingStateResult {

    @JsonProperty("has_more")
    private Boolean hasMore;
    /**
     * <p>
     * 流名称。
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;

    /**
     * <p>
     * app名称。
     * </p>
     */
    @JsonProperty("app_name")
    private String appName;

    /**
     * <p>
     * partition consuming state list
     * </p>
     */
    @JsonProperty("partition_consuming_states")
    private List<PartitionConsumingState> partitionConsumingStates;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public List<PartitionConsumingState> getPartitionConsumingStates() {
        return partitionConsumingStates;
    }

    public void setPartitionConsumingStates(List<PartitionConsumingState> partitionConsumingStates) {
        this.partitionConsumingStates = partitionConsumingStates;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }
}
