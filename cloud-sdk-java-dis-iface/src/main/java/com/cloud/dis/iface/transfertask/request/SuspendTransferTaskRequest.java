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
package com.cloud.dis.iface.transfertask.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuspendTransferTaskRequest
{
    /**
     * <p>
     * The name of the delivery stream.
     * </p>
     */
    @JsonProperty("stream_name")
    private String streamName;
    
    /**
     * <p>
     * The name of the transfer task.
     * </p>
     */
    @JsonProperty("task_name")
    private String transferTaskName;
    
    /**
     * <p>
     * suspend or restart the transfer task.
     * </p>
     */
    @JsonProperty("suspend")
    private boolean suspend;
    
    public String getTransferTaskName()
    {
        return transferTaskName;
    }
    
    public void setTransferTaskName(String transferTaskName)
    {
        this.transferTaskName = transferTaskName;
    }
    
    public String getStreamName()
    {
        return streamName;
    }
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public boolean isSuspend()
    {
        return suspend;
    }
    
    public void setSuspend(boolean suspend)
    {
        suspend = suspend;
    }
    
    @Override
    public String toString()
    {
        return "SuspendTransferTaskRequest{" + "streamName='" + streamName + '\'' + ", taskName='" + transferTaskName
            + '\'' + '}';
    }
}
