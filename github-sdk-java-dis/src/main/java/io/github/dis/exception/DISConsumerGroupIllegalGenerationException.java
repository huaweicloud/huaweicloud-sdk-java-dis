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
 * 指定消费组客户端和服务端版本号不一致，无法执行CheckPoint提交等操作
 * @see Constants#HTTP_CODE_BAD_REQUEST
 * @see Constants#ERROR_CODE_CONSUMER_GROUP_ILLEGAL_GENERATION
 */
public class DISConsumerGroupIllegalGenerationException extends DISClientException
{
    public DISConsumerGroupIllegalGenerationException(String message)
    {
        super(message);
    }

    public DISConsumerGroupIllegalGenerationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DISConsumerGroupIllegalGenerationException(Throwable cause)
    {
        super(cause);
    }
}
