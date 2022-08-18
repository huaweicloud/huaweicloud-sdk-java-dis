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

package com.otccloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListStreamsRequest
{
    
    protected static final int STREAM_LIMITS = 10;
    
    /**
     * <p>
     * The maximum number of streams to list. Default value is 10.
     * </p>
     */
    @JsonProperty("limit")
    private Integer limit = STREAM_LIMITS;
    
    /**
     * <p>
     * The name of the stream to start the list with. Exclude this stream name.
     * </p>
     */
    @JsonProperty("start_stream_name")
    private String exclusiveStartStreamName;
    
    public Integer getLimit()
    {
        return limit;
    }
    
    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }
    
    public String getExclusiveStartStreamName()
    {
        return exclusiveStartStreamName;
    }
    
    public void setExclusiveStartStreamName(String exclusiveStartStreamName)
    {
        this.exclusiveStartStreamName = exclusiveStartStreamName;
    }

    @Override
    public String toString()
    {
        return "ListStreamsRequest [limit=" + limit + ", exclusiveStartStreamName=" + exclusiveStartStreamName + "]";
    }
    
}
