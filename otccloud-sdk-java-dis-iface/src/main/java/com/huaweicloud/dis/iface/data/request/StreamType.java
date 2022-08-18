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

package com.otccloud.dis.iface.data.request;

/**
 * 通道分片类型枚举
 */
public enum StreamType {

    COMMON("COMMON", 1), ADVANCED("ADVANCED", 5);

    private String type; // 流类型
    
    private int value; // 对应的BandWith的值
    
    private StreamType(String type, int value)
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

    @Override
    public String toString() {
        return this.type;
    }

    /**
     * 根据流通道类型获取枚举
     * 
     * @param type 流通道类型
     * @return StreamType 通道类型枚举
     */
    public static StreamType getEnumByType(String type)
    {
        if (null == type)
        {
            return StreamType.COMMON;
        }
        
        for (StreamType st : StreamType.values())
        {
            if (st.getType().equals(type))
            {
                return st;
            }
        }
        
        throw new IllegalArgumentException("Cannot create enum from type: " + type + ".");
    }
    
    /**
     * 根据流通道带宽类型获取枚举
     * 
     * @param value 流通道带宽
     * @return StreamType 通道类型枚举
     */
    public static StreamType getEnumByValue(int value)
    {
        for (StreamType st : StreamType.values())
        {
            if (st.getValue() == value)
            {
                return st;
            }
        }
        
        throw new IllegalArgumentException("Cannot create enum from value: " + value + ".");
    }
    
}