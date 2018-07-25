package com.huaweicloud.dis.iface.consumergroup.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// GW -> CG_Global
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscribeConsumerGroupRequest extends ConsumerGroupRequest
{
    @JsonProperty("topic_name")
    protected String topicName;

    public String getTopicName()
    {
        return topicName;
    }

    public void setTopicName(String topicName)
    {
        this.topicName = topicName;
    }

    @Override
    public String toString()
    {
        return "SubscribeConsumerGroupRequest{" +
                "projectId='" + projectId + '\'' +
                ", streamName='" + streamName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
