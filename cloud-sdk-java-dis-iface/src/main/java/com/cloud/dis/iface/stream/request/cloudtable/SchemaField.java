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

package com.cloud.dis.iface.stream.request.cloudtable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaField
{
    /**
     * <p>
     * Name of the HBase column family to which data will be dumped.
     * </p>
     */
    @JsonProperty("column_family_name")
    private String columnFamilyName;
    
    /**
     * <p>
     * Name of the HBase column to which data will be dumped.
     * </p>
     */
    @JsonProperty("column_name")
    private String qualifierName;
    
    /**
     * <p>
     * The name of the schema field.
     * </p>
     */
    @JsonProperty("name")
    private String name;
    
    /**
     * <p>
     * The format of the schema field.
     * </p>
     */
    private String format;
    
    /**
     * <p>
     * The value of the schema field.
     * </p>
     */
    private String value;
    
    /**
     * <p>
     * The type of the schema field.
     * </p>
     */
    private String type;
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getColumnFamilyName()
    {
        return columnFamilyName;
    }
    
    public void setColumnFamilyName(String columnFamilyName)
    {
        this.columnFamilyName = columnFamilyName;
    }
    
    public String getQualifierName()
    {
        return qualifierName;
    }
    
    public void setQualifierName(String qualifierName)
    {
        this.qualifierName = qualifierName;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getFormat()
    {
        return format;
    }
    
    public void setFormat(String format)
    {
        this.format = format;
    }
}
