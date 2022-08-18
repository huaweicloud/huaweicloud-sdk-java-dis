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
 * 指定消费组处于rebalance中，无法执行CheckPoint提交等操作
 * @see com.otccloud.dis.Constants#HTTP_CODE_BAD_REQUEST
 * @see com.otccloud.dis.Constants#ERROR_CODE_CONSUMER_GROUP_REBALANCE_IN_PROGRESS
 */
public class DISConsumerGroupRebalanceInProgressException extends DISClientException
{
    public DISConsumerGroupRebalanceInProgressException(String message)
    {
        super(message);
    }

    public DISConsumerGroupRebalanceInProgressException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DISConsumerGroupRebalanceInProgressException(Throwable cause)
    {
        super(cause);
    }
}
