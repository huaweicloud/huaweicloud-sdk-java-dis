package com.huaweicloud.dis.iface.consumergroup.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCGRecordsRequest extends ConsumerGroupRequest
{
    @Override
    public String toString()
    {
        return "GetCGRecordsRequest{" +
                ", projectId='" + projectId + '\'' +
                ", streamName='" + streamName + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
