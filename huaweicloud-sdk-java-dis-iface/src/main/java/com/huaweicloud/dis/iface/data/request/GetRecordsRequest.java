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

package com.huaweicloud.dis.iface.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRecordsRequest
{
    /**
     * <p>
     * 迭代器。
     * </p>
     */
    @JsonProperty("partition-cursor")
    private String partitionCursor;
    
    /**
     * <p>
     * 每个请求获取 Record 的最大数量。
     * </p>
     * @deprecated limit已不生效
     */
    @JsonProperty("limit")
    private Integer limit;

    /**
     * <p>
     *     每个请求获取记录的最大字节数。
     * </p>
     * <p>
     *     <b>注意：</b>该值如果小于分区中单条记录的大小，会导致一直无法获取到记录。
     * </p>
     */
    @JsonProperty("max_fetch_bytes")
    private Long maxFetchBytes;

    //分组ID
    @JsonProperty("appName")
    private String appName;

    public String getPartitionCursor()
    {
        return partitionCursor;
    }

    public void setPartitionCursor(String partitionCursor)
    {
        this.partitionCursor = partitionCursor;
    }

    @Deprecated
    public Integer getLimit()
    {
        return limit;
    }
    
    @Deprecated
    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }

    public Long getMaxFetchBytes() {
        return maxFetchBytes;
    }

    public void setMaxFetchBytes(Long maxFetchBytes) {
        this.maxFetchBytes = maxFetchBytes;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString()
    {
        return "GetRecordsParam [shardIterator=" + partitionCursor + ", limit=" + limit + ", maxFetchBytes=" + maxFetchBytes  + "]";
    }    

}
