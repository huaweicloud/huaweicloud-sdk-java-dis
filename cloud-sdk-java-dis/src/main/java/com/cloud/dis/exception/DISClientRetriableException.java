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

import com.cloud.dis.core.Request;
import com.cloud.dis.http.AbstractDISClient;

/**
 * @see AbstractDISClient#isRetriableSendException(Throwable, Request)
 */
public class DISClientRetriableException extends DISClientException
{
    public DISClientRetriableException(String message)
    {
        super(message);
    }

    public DISClientRetriableException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DISClientRetriableException(Throwable cause)
    {
        super(cause);
    }
}
