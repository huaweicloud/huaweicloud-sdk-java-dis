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

/**
 * 指定消费组内消费者不存在异常
 * @see Constants#HTTP_CODE_BAD_REQUEST
 * @see Constants#ERROR_CODE_CONSUMER_MEMBER_NOT_EXIST
 */
public class DISConsumerMemberNotExistsException extends DISClientException
{
    public DISConsumerMemberNotExistsException(String message)
    {
        super(message);
    }

    public DISConsumerMemberNotExistsException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DISConsumerMemberNotExistsException(Throwable cause)
    {
        super(cause);
    }
}
