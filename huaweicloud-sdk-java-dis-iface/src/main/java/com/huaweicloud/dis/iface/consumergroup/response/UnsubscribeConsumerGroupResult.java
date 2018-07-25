package com.huaweicloud.dis.iface.consumergroup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnsubscribeConsumerGroupResult extends ConsumerGroupResult
{
    @Override
    public String toString()
    {
        return "UnsubscribeConsumerGroupResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
