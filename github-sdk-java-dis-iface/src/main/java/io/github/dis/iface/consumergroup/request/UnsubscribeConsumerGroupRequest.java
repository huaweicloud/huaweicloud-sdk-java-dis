package io.github.dis.iface.consumergroup.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnsubscribeConsumerGroupRequest extends ConsumerGroupRequest
{
    @Override
    public String toString()
    {
        return "UnsubscribeConsumerGroupRequest{" +
                "projectId='" + projectId + '\'' +
                ", streamName='" + streamName + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
