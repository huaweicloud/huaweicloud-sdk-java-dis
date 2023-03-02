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

package com.cloud.dis.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Utilities for converting objects to strings.
 */
public class StringUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    public static final String COMMA_SEPARATOR = ",";

    public static final Charset UTF8 = Charset.forName(DEFAULT_ENCODING);

    public static Integer toInteger(StringBuilder value) {
        return Integer.parseInt(value.toString());
    }

    public static String toString(StringBuilder value) {
        return value.toString();
    }

    public static Boolean toBoolean(StringBuilder value) {
        return Boolean.getBoolean(value.toString());
    }

    public static String fromInteger(Integer value) {
        return Integer.toString(value);
    }

    public static String fromLong(Long value) {
        return Long.toString(value);
    }

    public static String fromString(String value) {
        return value;
    }

    public static String fromBoolean(Boolean value) {
        return Boolean.toString(value);
    }

    public static String fromBigInteger(BigInteger value) {
        return value.toString();
    }

    public static String fromBigDecimal(BigDecimal value) {
        return value.toString();
    }


    public static BigInteger toBigInteger(String s) {
        return new BigInteger(s);
    }

    public static BigDecimal toBigDecimal(String s) {
        return new BigDecimal(s);
    }

    public static String fromFloat(Float value) {
        return Float.toString(value);
    }

    /**
     * Converts the specified date to an ISO 8601 timestamp string and returns
     * it.
     *
     * @param value
     *            The date to format as an ISO 8601 timestamp string.
     *
     * @return An ISO 8601 timestamp string created from the specified date.
     */
    public static String fromDate(Date value) {
        return DateUtils.formatISO8601Date(value);
    }

    /**
     * Returns the string representation of the specified double.
     *
     * @param d
     *            The double to represent as a string.
     *
     * @return The string representation of the specified double.
     */
    public static String fromDouble(Double d) {
        return Double.toString(d);
    }

    /**
     * Returns the string representation of the specified Byte.
     *
     * @param b
     *            The Byte to represent as a string.
     *
     * @return The string representation of the specified Byte.
     */
    public static String fromByte(Byte b) {
        return Byte.toString(b);
    }

    /**
     * Base64 encodes the data in the specified byte buffer (from the current
     * position to the buffer's limit) and returns it as a base64 encoded
     * string.
     *
     * @param byteBuffer
     *            The data to base64 encode and return as a string; must not be
     *            null.
     *
     * @return The base64 encoded contents of the specified byte buffer.
     */
    public static String fromByteBuffer(ByteBuffer byteBuffer) {
        return Base64.encodeAsString(BinaryUtils.copyBytesFrom(byteBuffer));
    }

    public static String replace( String originalString, String partToMatch, String replacement ) {
        StringBuffer buffer = new StringBuffer( originalString.length() );
        buffer.append( originalString );

        int indexOf = buffer.indexOf( partToMatch );
        while ( indexOf != -1 ) {
            buffer = buffer.replace( indexOf, indexOf + partToMatch.length(), replacement );
            indexOf = buffer.indexOf( partToMatch );
        }

        return buffer.toString();
    }

    /**
     * Joins the strings in parts with joiner between each string
     * @param joiner the string to insert between the strings in parts
     * @param parts the parts to join
     * @return String after joining the parts string
     */
    public static String join(String joiner, String... parts) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            builder.append(parts[i]);
            if (i < parts.length - 1) {
                builder.append(joiner);
            }
        }
        return builder.toString();
    }

    /**
     * A null-safe trim method. If the input string is null, returns null;
     * otherwise returns a trimmed version of the input.
     * 
     * @param value The input string.
     * @return String after being trimmed.
     */
    public static String trim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    /**
     * Judge a String is null or empty.
     * 
     * @param value The input string.
     * @return true if the given value is either null or the empty string
     */
    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.isEmpty();
    }
}