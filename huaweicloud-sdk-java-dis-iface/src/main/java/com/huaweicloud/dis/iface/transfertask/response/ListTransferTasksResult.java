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
package com.huaweicloud.dis.iface.transfertask.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListTransferTasksResult
{
    @JsonProperty("total_number")
    private int taskNumber;
    
    private List<TransferTaskResult> details;
    
    @JsonProperty("has_more_tasks")
    private Boolean hasMoreTasks;
    
    public int getTaskNumber()
    {
        return taskNumber;
    }
    
    public void setTaskNumber(int taskNumber)
    {
        this.taskNumber = taskNumber;
    }
    
    public List<TransferTaskResult> getDetails()
    {
        return details;
    }
    
    public void setDetails(List<TransferTaskResult> details)
    {
        this.details = details;
    }
    
    public Boolean getHasMoreTasks()
    {
        return hasMoreTasks;
    }
    
    public void setHasMoreTasks(Boolean hasMoreTasks)
    {
        this.hasMoreTasks = hasMoreTasks;
    }
}
