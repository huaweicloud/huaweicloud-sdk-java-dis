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

package com.cloud.dis.exception;

import com.cloud.dis.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通道不存在异常
 * @see Constants#HTTP_CODE_BAD_REQUEST
 * @see Constants#ERROR_CODE_STREAM_NOT_EXISTS
 */
public class DISStreamNotExistsException extends DISClientException
{
    private static Pattern pattern = Pattern.compile(".*Stream does not exist. \\[(.+?)].*");

    private String streamName = null;

    public DISStreamNotExistsException(String message)
    {
        super(message);
        resolveStreamName(message);
    }

    public DISStreamNotExistsException(String message, Throwable cause)
    {
        super(message, cause);
        resolveStreamName(message);
    }

    public DISStreamNotExistsException(Throwable cause)
    {
        super(cause);
        resolveStreamName(cause.getMessage());
    }

    public String getStreamName()
    {
        return streamName;
    }

    private void resolveStreamName(String message)
    {
        // message example: {"errorCode":"DIS.4301","message":"Stream does not exist. [dis-lR5q111][3e83253eb4a24c65a8533eb3da85e070]"}
        if (message != null && message.contains(Constants.ERROR_INFO_STREAM_NOT_EXISTS))
        {
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches() && matcher.groupCount() == 1)
            {
                this.streamName = matcher.group(1);
            }
        }
    }
}
