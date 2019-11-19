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

package com.huaweicloud.dis.core.handler;
/**
 * SDK异步接口回调函数接口
 */
public interface AsyncHandler<RESULT> {

	/**
	 * Invoked after an asynchronous request 
	 * @param exception 异常对象
	 * @throws Exception in case of I/O errors
	 */
	void onError(Exception exception) throws Exception;

	/**
	 * Invoked after an asynchronous request has completed successfully. Callers
	 * have access to the original request object and the returned response
	 * object.
	 *
	 * @param result
	 *            The successful result of the executed operation.
	 * @throws Exception in case of I/O errors
	 */
	void onSuccess(RESULT result) throws Exception;

}