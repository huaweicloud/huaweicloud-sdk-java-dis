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
import org.xerial.snappy.Snappy;


/**
 * Utils for fast compression/decompression of Snappy, using {@link Snappy}
 *
 */
public class SnappyUtils
{
    
    private static final Logger LOG = LoggerFactory.getLogger(SnappyUtils.class);
    
    /**
     * Compressing the input byte array.
     * 
     * @param input the input data
     * @return the compressed byte array
     * @throws IOException 
     * @since 1.3.0
     */
    public static byte[] compress(byte[] input) throws IOException
    {
        return Snappy.compress(input);
    }
    
    /**
     * Uncompressing the input byte array.
     * 
     * @param input
     * @return the uncompressed byte array
     * @throws IOException 
     * @since 1.3.0
     */
    public static byte[] uncompress(byte[] input) throws IOException
    {
        return Snappy.uncompress(input);
    }
    
}
