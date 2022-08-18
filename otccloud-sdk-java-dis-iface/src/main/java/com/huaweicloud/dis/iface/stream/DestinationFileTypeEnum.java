/*
 * Copyright 2002-2016 the original author or authors.
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
package com.otccloud.dis.iface.stream;

public enum DestinationFileTypeEnum
{
    TEXT("text", 1), PARQUET("parquet", 2), CARBON("carbon", 3);
    
    private String type; // 创建转储任务的转储文件格式：text parquet carbon等
    
    private int value; // 对应的转储文件格式的值
    
    // 构造方法
    private DestinationFileTypeEnum(String type, int value)
    {
        this.type = type;
        this.value = value;
    }
    
    public String getType()
    {
        return type;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public static DestinationFileTypeEnum getEnum(String type)
    {
        for (DestinationFileTypeEnum de : DestinationFileTypeEnum.values())
        {
            if (de.getType().equals(type))
            {
                return de;
            }
        }
        return null;
    }
    
    public static DestinationFileTypeEnum getEnumByValue(int value)
    {
        for (DestinationFileTypeEnum de : DestinationFileTypeEnum.values())
        {
            if (de.getValue() == value)
            {
                return de;
            }
        }
        return null;
    }
}
