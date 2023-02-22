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

package io.github.dis.core;

/**
 * Base exception class for any errors that occur while attempting to use an 
 * client to make service calls to  Web Services.
 */
public class ClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ClientException with the specified message, and root
     * cause.
     *
     * @param message An error message describing why this exception was thrown.
     * @param t       The underlying cause of this exception.
     */
    public ClientException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * Creates a new ClientException with the specified message.
     *
     * @param message An error message describing why this exception was thrown.
     */
    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable t) {
        super(t);
    }

	/**
	 * Returns a hint as to whether it makes sense to retry upon this exception.
	 * Default is true, but subclass may override.
	 * 
	 * @return {@code true} retryable {@code false} not retryable
	 */
    public boolean isRetryable() {
        return true;
    }
}