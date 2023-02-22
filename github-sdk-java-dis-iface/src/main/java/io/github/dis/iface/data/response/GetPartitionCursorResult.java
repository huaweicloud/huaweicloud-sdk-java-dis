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

package io.github.dis.iface.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPartitionCursorResult
{
    /**
     * <p>
     * 迭代器。
     * </p>
     */
    @JsonProperty("partition_cursor")
    private String partitionCursor;

    public String getPartitionCursor()
    {
        return partitionCursor;
    }

    public void setPartitionCursor(String partitionCursor)
    {
        this.partitionCursor = partitionCursor;
    }

    @Override
    public String toString()
    {
        return "StringResponse [partitionCursor=" + partitionCursor + "]";
    }
    
    
}
