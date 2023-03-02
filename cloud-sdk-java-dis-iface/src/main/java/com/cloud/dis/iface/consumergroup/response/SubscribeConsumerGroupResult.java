package com.cloud.dis.iface.consumergroup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscribeConsumerGroupResult extends ConsumerGroupResult
{
    @Override
    public String toString()
    {
        return "SubscribeConsumerGroupResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
