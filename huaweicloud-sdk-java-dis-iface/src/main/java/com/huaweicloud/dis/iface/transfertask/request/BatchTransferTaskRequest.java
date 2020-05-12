package com.huaweicloud.dis.iface.transfertask.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.transfertask.TransferTaskActionEnum;
import com.huaweicloud.dis.iface.transfertask.model.BatchTransferTask;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchTransferTaskRequest {
    /**
     * 转储任务操作，目前支持：<br/>
     * 1. start：启动转储任务<br/>
     * 2. stop：停止转储任务
     */
    @JsonProperty("action")
    private String action;

    /**
     * 转储任务所属通道的名称。
     */
    @JsonProperty("stream_name")
    private String streamName;

    /**
     * 待操作的转储任务列表。
     */
    @JsonProperty("tasks")
    private List<BatchTransferTask> transferTasks;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public List<BatchTransferTask> getTransferTasks() {
        return transferTasks;
    }

    public void setTransferTasks(List<BatchTransferTask> transferTasks) {
        this.transferTasks = transferTasks;
    }
}
