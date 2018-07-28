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

package com.huaweicloud.dis.iface.app;

import com.huaweicloud.dis.iface.app.response.AppEntry;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;

/**
 * IAppService为开放到SDK的接口, IAppIdService为内部扩展接口不对SDK暴露
 */
public interface IAppIdService extends IAppService
{
    
    /**
     * <p>
     * 根据App名称查询App详情。
     * </p>
     * 
     * @param appName App名称
     * @return
     */
    AppEntry describeApp(String appName);

    /**
     * <p>
     * 获取用户所有app。
     * </p>
     *
     * @param projectId  projectId
     * @return
     */
    ListAppsResult listApps(String projectId);


}
