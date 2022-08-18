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

package com.otccloud.dis.exception;

/**
 * 分区过期异常
 * @see com.otccloud.dis.Constants#HTTP_CODE_BAD_REQUEST
 * @see com.otccloud.dis.Constants#ERROR_CODE_PARTITION_IS_EXPIRED
 */
public class DISPartitionExpiredException extends DISClientException
{
    public DISPartitionExpiredException(String message)
    {
        super(message);
    }

    public DISPartitionExpiredException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DISPartitionExpiredException(Throwable cause)
    {
        super(cause);
    }
}
