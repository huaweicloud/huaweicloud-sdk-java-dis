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

package com.huaweicloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by s00348548 on 2017/11/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UqueryDestinationDescriptorRequest extends OBSDestinationDescriptorRequest
{
    /** Uquery databaseName */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("uquery_database_name")
    private String uqueryDatabaseName;
    
    /** Uquery tableName */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("uquery_table_name")
    private String uqueryTableName;

    
    public String getUqueryDatabaseName()
    {
        return uqueryDatabaseName;
    }
    
    public void setUqueryDatabaseName(String uqueryDatabaseName)
    {
        this.uqueryDatabaseName = uqueryDatabaseName;
    }
    
    public String getUqueryTableName()
    {
        return uqueryTableName;
    }
    
    public void setUqueryTableName(String uqueryTableName)
    {
        this.uqueryTableName = uqueryTableName;
    }
}
