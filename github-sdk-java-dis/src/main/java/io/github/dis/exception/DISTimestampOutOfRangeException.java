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

package io.github.dis.exception;

import io.github.dis.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间戳超过范围异常
 * @see Constants#HTTP_CODE_BAD_REQUEST
 * @see Constants#ERROR_CODE_REQUEST_ERROR
 * @see Constants#ERROR_INFO_TIMESTAMP_IS_EXPIRED
 */
public class DISTimestampOutOfRangeException extends DISClientException
{
    private static Pattern pattern = Pattern.compile(".*timestamp should be larger than (\\d+).*");

    private Long earliestTimestamp = null;

    public DISTimestampOutOfRangeException(String message)
    {
        super(message);
        resolveTimestamp(message);
    }

    public DISTimestampOutOfRangeException(String message, Throwable cause)
    {
        super(message, cause);
        resolveTimestamp(message);
    }

    public DISTimestampOutOfRangeException(Throwable cause)
    {
        super(cause);
        resolveTimestamp(cause.getMessage());
    }

    public Long getEarliestTimestamp()
    {
        return earliestTimestamp;
    }

    private void resolveTimestamp(String message)
    {
        // {"errorCode":"DIS.4300","message":"Request error. [timestamp is expired, timestamp should be larger than 1545728813434]"}
        if (message != null && message.contains(Constants.ERROR_INFO_TIMESTAMP_IS_EXPIRED))
        {
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches() && matcher.groupCount() == 1)
            {
                this.earliestTimestamp = Long.valueOf(matcher.group(1));
            }
        }
    }
}
