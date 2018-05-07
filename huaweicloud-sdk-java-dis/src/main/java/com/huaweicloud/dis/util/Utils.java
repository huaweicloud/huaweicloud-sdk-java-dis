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

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils
{
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    
    private static final String UTF_8 = "UTF-8";
    
    public static byte[] encodingBytes(String value)
    {
        if (value == null)
        {
            return null;
        }
        byte[] bytes = null;
        try
        {
            bytes = value.getBytes(UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error(e.getMessage(), e);
        }
        
        return bytes;
    }
    
    public static boolean isValidEndpoint(String endpoint)
    {
        String endpointPattern = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+(:[1-9][0-9]{0,4})?([\\w-./?%&=]*)?$";
        
        Pattern pattern = Pattern.compile(endpointPattern);
        return pattern.matcher(endpoint).matches();
    }
}
