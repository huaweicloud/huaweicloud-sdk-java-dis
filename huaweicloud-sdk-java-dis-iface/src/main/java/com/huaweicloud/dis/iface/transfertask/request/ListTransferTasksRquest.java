/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huaweicloud.dis.iface.transfertask.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListTransferTasksRquest
{
    protected static final int TASK_LIMITS = 10;
    
    /**
     * <p>
     * The maximum number of tasks to list. Default value is 10.
     * </p>
     */
    @JsonProperty("limit")
    private Integer limit = TASK_LIMITS;
    
    /**
     * <p>
     * The name of the delivery stream.
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * <p>
     * The name of the task to start the list with. Exclude this task name.
     * </p>
     */
    @JsonProperty("start_task_name")
    private String exclusiveStartTaskName;
    
    public Integer getLimit()
    {
        return limit;
    }
    
    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }
    
    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public String getExclusiveStartTaskName()
    {
        return exclusiveStartTaskName;
    }
    
    public void setExclusiveStartTaskName(String exclusiveStartTaskName)
    {
        this.exclusiveStartTaskName = exclusiveStartTaskName;
    }
    
    @Override
    public String toString()
    {
        return "ListTransferTasksRequest [limit=" + limit + ", streamName=" + streamName + ", exclusiveStartTaskName="
            + exclusiveStartTaskName + "]";
    }
}
