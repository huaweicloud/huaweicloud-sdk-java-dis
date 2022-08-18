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

package com.otccloud.dis.core.auth.signer.internal;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Utility methods that is used by the different Signer implementations.
 * This class is strictly internal and is subjected to change.
 */
public final class SignerUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyyMMdd").withZoneUTC();

    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss'Z'").withZoneUTC();

    /**
     * Returns a string representation of the given date time in yyyyMMdd
     * format. The date returned is in the UTC zone.
     * <p/>
     * For example, given a time "1416863450581", this method returns "20141124"
     */
    public static String formatDateStamp(long timeMilli) {
        return dateFormatter.print(timeMilli);
    }

    /**
     * Returns a string representation of the given date time in
     * yyyyMMdd'T'HHmmss'Z' format. The date returned is in the UTC zone.
     * <p/>
     * For example, given a time "1416863450581", this method returns
     * "20141124T211050Z"
     */
    public static String formatTimestamp(long timeMilli) {
        return timeFormatter.print(timeMilli);
    }
    
    /**
     * 根据日期字符串，获取一个时间的秒数.
     * <p>例如时间"20141124T211050Z"，返回毫秒数为"1416863450000"
     * 
     * @param signDate 日期字符串
     * @return 表示的自 1970 年 1 月 1 日 00:00:00 GMT 以来的数
     */
    public static long parseMillis(String signDate) {
        return timeFormatter.parseMillis(signDate);
    }
}