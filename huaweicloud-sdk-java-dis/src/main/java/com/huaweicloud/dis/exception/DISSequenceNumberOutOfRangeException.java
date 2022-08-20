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

package com.huaweicloud.dis.exception;

import com.huaweicloud.dis.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SequenceNumber超过范围异常
 * @see com.huaweicloud.dis.Constants#HTTP_CODE_BAD_REQUEST
 * @see com.huaweicloud.dis.Constants#ERROR_CODE_SEQUENCE_NUMBER_OUT_OF_RANGE
 */
public class DISSequenceNumberOutOfRangeException extends DISClientException
{
    private static Pattern pattern = Pattern.compile(".*\\[(\\d+)]\\[ should be within ]\\[(\\d+)]\\[ ]\\[(\\d+)].*");

    private Long earliestSequenceNumber = null;

    private Long latestSequenceNumber = null;

    private Long invalidSequenceNumber = null;

    public DISSequenceNumberOutOfRangeException(String message)
    {
        super(message);
        resolveSequenceNumber(message);
    }

    public DISSequenceNumberOutOfRangeException(String message, Throwable cause)
    {
        super(message, cause);
        resolveSequenceNumber(message);
    }

    public DISSequenceNumberOutOfRangeException(Throwable cause)
    {
        super(cause);
        resolveSequenceNumber(cause.getMessage());
    }

    public Long getEarliestSequenceNumber()
    {
        return earliestSequenceNumber;
    }

    public Long getLatestSequenceNumber()
    {
        return latestSequenceNumber;
    }

    public Long getInvalidSequenceNumber()
    {
        return invalidSequenceNumber;
    }

    private void resolveSequenceNumber(String message)
    {

        if (message != null && message.contains(Constants.ERROR_INFO_SEQUENCE_NUMBER_OUT_OF_RANGE))
        {
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches() && matcher.groupCount() == 3)
            {
                this.invalidSequenceNumber = Long.valueOf(matcher.group(1));
                this.earliestSequenceNumber = Long.valueOf(matcher.group(2));
                this.latestSequenceNumber = Long.valueOf(matcher.group(3));
            }
        }
    }
}
