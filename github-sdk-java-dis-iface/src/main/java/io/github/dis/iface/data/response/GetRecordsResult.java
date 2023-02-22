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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRecordsResult
{
    /**
     * <p>
     * 下载的数据列表。
     * </p>
     */
    @JsonProperty("records")
    private List<Record> records;
    
    /**
     * <p>
     * 下一个迭代器。
     * </p>
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("next_partition_cursor")
    private String nextPartitionCursor;
    
    @JsonIgnore
    @JsonProperty("millis_behind_latest")
    private Long millisBehindLatest;

    public List<Record> getRecords()
    {
        return records;
    }

    public void setRecords(List<Record> records)
    {
        this.records = records;
    }

    public String getNextPartitionCursor()
    {
        return nextPartitionCursor;
    }

    public void setNextPartitionCursor(String nextPartitionCursor)
    {
        this.nextPartitionCursor = nextPartitionCursor;
    }

    public Long getMillisBehindLatest()
    {
        return millisBehindLatest;
    }

    public void setMillisBehindLatest(Long millisBehindLatest)
    {
        this.millisBehindLatest = millisBehindLatest;
    }

    @Override
    public String toString()
    {
        return "RecordsResponse [records=" + records + ", nextPartitionCursor=" + nextPartitionCursor
            + ", millisBehindLatest=" + millisBehindLatest + "]";
    }
    
}
