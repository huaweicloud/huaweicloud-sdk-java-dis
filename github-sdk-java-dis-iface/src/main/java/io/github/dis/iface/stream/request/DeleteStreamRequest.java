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

package io.github.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteStreamRequest
{
    /**-------------------- Stream属性 -------------------**/
    
    /**
     * 流名称
     * aws:   [a-zA-Z0-9_.-]+
     * hws:   [a-zA-Z0-9_-]+
     * */
    @JsonProperty("stream_name")
    private String streamName;
    
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }
    
    public String getStreamName()
    {
        return this.streamName;
    }
    
    public DeleteStreamRequest withStreamName(String streamName)
    {
        setStreamName(streamName);
        return this;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getStreamName() != null)
            sb.append("StreamName: ").append(getStreamName());
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int hashCode = 1;
        
        hashCode = prime * hashCode + ((getStreamName() == null) ? 0 : getStreamName().hashCode());
        return hashCode;
    }
    
}
