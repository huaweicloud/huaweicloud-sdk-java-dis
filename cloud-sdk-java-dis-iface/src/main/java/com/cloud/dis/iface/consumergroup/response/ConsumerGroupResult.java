package com.cloud.dis.iface.consumergroup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.cloud.dis.iface.consumergroup.ResultStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerGroupResult
{
    @JsonProperty("status")
    protected ResultStatus status = ResultStatus.SUCCESS;

    @JsonProperty("message")
    protected String message;

    public ResultStatus getStatus()
    {
        return status;
    }

    public void setStatus(ResultStatus status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
