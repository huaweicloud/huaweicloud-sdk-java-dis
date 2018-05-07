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

package com.huaweicloud.dis.iface.stream.request.cloudtable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by s00348548 on 2018/1/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaField
{
    @JsonProperty("column_family_name")
    private String columnFamilyName;
    
    @JsonProperty("column_name")
    private String qualifierName;
    
    @JsonProperty("name")
    private String name;
    
    private String format;
    
    private String value;
    
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
