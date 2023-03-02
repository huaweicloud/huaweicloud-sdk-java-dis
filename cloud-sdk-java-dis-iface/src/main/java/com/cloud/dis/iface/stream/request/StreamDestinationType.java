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

package com.cloud.dis.iface.stream.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum StreamDestinationType
{
    DEFAULT("NOWHERE"), OBS("OBS"), MRS("MRS"), UQUERY("UQUERY"), DLI("DLI"), CLOUDTABLE("CLOUDTABLE"), OPENTSDB("OPENTSDB"), DWS("DWS");
    
    private String typeString;
    
    private StreamDestinationType(String typeString)
    {
        this.typeString = typeString;
    }
    
    @Override
    public String toString()
    {
        return typeString;
    }
    
    public static StreamDestinationType getByTypeString(String typeString)
    {
        for (StreamDestinationType streamDestinationType : values())
        {
            if (streamDestinationType.toString().equals(typeString))
            {
                return streamDestinationType;
            }
        }
        return null;
    }
}
