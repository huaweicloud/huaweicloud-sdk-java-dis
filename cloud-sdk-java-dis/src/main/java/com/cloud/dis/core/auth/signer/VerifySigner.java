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

package com.cloud.dis.core.auth.signer;

import com.cloud.dis.core.Request;
import com.cloud.dis.core.auth.credentials.Credentials;

import java.util.Properties;

/**
 * 签名需要提供校验方法，校验{@code Request}头部的签名信息是否合理.
 * 
 * @author l00307761
 * @version v1.0.0
 */
public interface VerifySigner extends RegionSigner, ServiceSigner, Signer {
    
    /**
     * 校验{@code Request}头部的签名信息.
     * <p>方法从{@code Request}的头部获取{@code SignerConstants.AUTHORIZATION}参数与{@code Request}生成新的签名进行比较，如果相同则
     * 返回<CODE>true</CODE>,否则返回<CODE>false</CODE>.
     * <p>如果{@code Request}的头部没有{@code SignerConstants.AUTHORIZATION}参数，则返回<CODE>false</CODE>
     * 
     * 
     * @param request 需要校验的请求
     * @param credentials AK/SK信息
     * @return 如果校验成功返回<CODE>true</CODE>,否则返回<CODE>false</CODE>
     * @since v1.0.0
     */
    boolean verify(Request<?> request, Credentials credentials);

    /**
     * 校验{@code Request}头部的签名信息.
     * <p>方法从{@code Request}的头部获取{@code SignerConstants.AUTHORIZATION}参数与{@code Request}生成新的签名进行比较，如果相同则
     * 返回<CODE>true</CODE>,否则返回<CODE>false</CODE>.
     * <p>如果{@code Request}的头部没有{@code SignerConstants.AUTHORIZATION}参数，则返回<CODE>false</CODE>
     *
     *
     * @param request 需要校验的请求
     * @param credentials AK/SK信息
     * @param prop 一些配置信息
     * @return 如果校验成功返回<CODE>true</CODE>,否则返回<CODE>false</CODE>
     * @since v1.0.0
     */
    boolean verify(Request<?> request, Credentials credentials, Properties prop);
}