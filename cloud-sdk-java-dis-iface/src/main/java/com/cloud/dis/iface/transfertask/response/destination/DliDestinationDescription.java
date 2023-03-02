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
package com.cloud.dis.iface.transfertask.response.destination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.cloud.dis.iface.stream.request.ForceStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DliDestinationDescription extends OBSDestinationDescription
{
    /**
     * <p>
     * Name of the DLI database to which data in the DIS stream will be dumped.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dli_database_name")
    private String dliDatabaseName;
    
    /**
     * <p>
     * Name of the DLI table to which data in the DIS stream will be dumped.Currently, only tables in DLI are supported.
     * Before selecting a DLI table, ensure that you have the permission to insert data into the table.
     * </p>
     */
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("dli_table_name")
    private String dliTableName;
    
    public String getDliDatabaseName()
    {
        return dliDatabaseName;
    }
    
    public void setDliDatabaseName(String dliDatabaseName)
    {
        this.dliDatabaseName = dliDatabaseName;
    }
    
    public String getDliTableName()
    {
        return dliTableName;
    }
    
    public void setDliTableName(String dliTableName)
    {
        this.dliTableName = dliTableName;
    }
}
