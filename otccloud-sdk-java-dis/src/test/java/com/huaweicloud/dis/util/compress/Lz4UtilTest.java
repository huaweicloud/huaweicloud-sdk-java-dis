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

package com.otccloud.dis.util.compress;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class Lz4UtilTest {

    @Test
    public void testCompressByte() throws IOException {
        String input =
            "Hello, I'm compressing using Lz4!";
        byte[] compressed = Lz4Util.compressByte(input.getBytes("UTF-8"));
        byte[] decompressed = Lz4Util.decompressByte(compressed, input.length());

        String result = new String(decompressed, "UTF-8");

        Assert.assertEquals(input, result);
    }

    @Test
    public void testDecompressorByte() throws IOException {
        String input =
            "Hello, I'm decompressing using Lz4!";
        byte[] compressed = Lz4Util.compressByte(input.getBytes("UTF-8"));
        byte[] decompressed = Lz4Util.decompressByte(compressed, input.length());

        String result = new String(decompressed, "UTF-8");

        Assert.assertEquals(input, result);
    }

}
