package com.huaweicloud.dis.iface.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteCheckpointRequest {

    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("partition_id")
    private String partitionId;

    @JsonProperty("checkpoint_type")
    private String checkpointType;

    @JsonProperty("app_name")
    private String appName;

    @JsonProperty("stream_id")
    private String streamId;

    public String getStreamName()
    {
        return streamName;
    }

    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    public String getPartitionId()
    {
        return partitionId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId = partitionId;
    }

    public String getCheckpointType()
    {
        return checkpointType;
    }

    public void setCheckpointType(String checkpointType)
    {
        this.checkpointType = checkpointType;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    @Override
    public String toString() {
        return "DeleteCheckpointRequest{" +
            "streamName='" + streamName + '\'' +
            ", partitionId='" + partitionId + '\'' +
            ", checkpointType='" + checkpointType + '\'' +
            ", appName='" + appName + '\'' +
            ", streamId='" + streamId + '\'' +
            '}';
    }
}
