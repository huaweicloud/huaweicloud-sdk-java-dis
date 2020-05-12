package com.huaweicloud.dis.iface.transfertask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchTransferTask {
    /**
     * 转储任务ID
     */
    @Length(min = 19, max = 19, message = "Parameter `id` is invalid.")
    @NotBlank(message = "Parameter `id` cannot be blank.")
    @JsonProperty("id")
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
