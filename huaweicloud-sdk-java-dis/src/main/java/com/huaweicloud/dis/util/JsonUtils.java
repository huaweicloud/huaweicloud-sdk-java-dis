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

package com.huaweicloud.dis.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON处理工具类
 *
 */
public class JsonUtils
{
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static
    {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 遇到空Bean不报错
    }

    /*
     * 自定义属性命名策略：首字母转小写
     */
    public static final PropertyNamingStrategy CASE_FIRST_WORD_TO_LOWER_CASE = new MyPropertyNameingStrategy();

    /**
     * json转换为java对象
     *
     * @param <T>
     *            转换为的java对象
     * @param json
     *            json字符串
     * @param typeReference
     *            jackjson自定义的类型
     * @return 返回Java对象
     */
    public static <T> T jsonToObj(String json, TypeReference<T> typeReference)
    {
        mapper.setPropertyNamingStrategy(new DefaultPropertyNameingStrategy());
        try
        {
            return mapper.readValue(json, typeReference);
        }
        catch (IOException e)
        {
            logger.error("Fail to read value of JSON. " + e);
        }

        return null;
    }

    /**
     * json转换为java对象
     *
     * @param <T>
     *            要转换的对象
     * @param json
     *            字符串
     * @param valueType
     *            对象的class
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObj(String json, Class<T> valueType)
    {
        if (valueType.isAssignableFrom(String.class))
        {
            return (T)json;
        }
        mapper.setPropertyNamingStrategy(new DefaultPropertyNameingStrategy());
        try
        {
            return mapper.readValue(json, valueType);
        }
        catch (IOException e)
        {
            logger.error("Fail to read value of JSON. " + e);
        }

        return null;
    }

    /**
     * json转换为java对象
     *
     * @param <T>
     *            要转换的对象
     * @param json
     *            字符串
     * @param valueType
     *            对象的class
     * @param pns
     *            自定义的属性命名策略
     * @return 返回对象
     */
    public static <T> T jsonToObj(String json, Class<T> valueType,
                                  PropertyNamingStrategy pns)
    {
        mapper.setPropertyNamingStrategy(pns);
        try
        {
            return mapper.readValue(json, valueType);
        }
        catch (IOException e)
        {
            logger.error("Fail to read value of JSON. " + e);
        }

        return null;
    }

    /**
     * java对象转换为json字符串
     *
     * @param object
     *            Java对象
     * @return 返回字符串
     */
    public static String objToJson(Object object)
    {
        mapper.setPropertyNamingStrategy(new DefaultPropertyNameingStrategy());
        try
        {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            logger.error("Fail to write value as String. " + e);
        }

        return null;
    }

    /**
     * java对象转换为json字符串
     *
     * @param object
     *            Java对象
     * @param pns
     *            自定义的属性命名策略
     * @return 返回字符串
     */
    public static String objToJson(Object object, PropertyNamingStrategy pns)
    {
        mapper.setPropertyNamingStrategy(pns);
        try
        {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            logger.error("Fail to write value as String. " + e);
        }

        return null;
    }

    private static class MyPropertyNameingStrategy extends
            PropertyNamingStrategyBase
    {
        private static final long serialVersionUID = 1L;

        @Override
        public String translate(String input)
        {
            if (input == null || input.length() == 0)
            {
                return input; // garbage in, garbage out
            }
            // Replace first lower-case letter with upper-case equivalent
            char c = input.charAt(0);
            if (Character.isLowerCase(c))
            {
                return input;
            }
            StringBuilder sb = new StringBuilder(input);
            sb.setCharAt(0, Character.toLowerCase(c));
            return sb.toString();
        }
    }

    private static class DefaultPropertyNameingStrategy extends
            PropertyNamingStrategyBase
    {
        private static final long serialVersionUID = 1L;

        @Override
        public String translate(String input)
        {
            return input;
        }
    }

}

