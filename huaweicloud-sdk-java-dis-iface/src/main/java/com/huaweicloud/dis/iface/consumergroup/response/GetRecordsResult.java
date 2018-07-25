package com.huaweicloud.dis.iface.consumergroup.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huaweicloud.dis.iface.data.response.Record;

// Results: GW -> Client
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRecordsResult
{
    @JsonProperty("records")
    private List<Record> records;

    @JsonIgnore
    @JsonProperty("consumer_token")
    private String consumertoken;

    public String getConsumertoken()
    {
        return consumertoken;
    }

    public void setConsumertoken(String consumertoken)
    {
        this.consumertoken = consumertoken;
    }


    public List<Record> getRecords()
    {
        return records;
    }

    public void setRecords(List<Record> records)
    {
        this.records = records;
    }

    @Override
    public String toString()
    {
        return "GetRecordsResult{" +
                "records=" + records +
                ", consumertoken='" + consumertoken + '\'' +
                '}';
    }
}
