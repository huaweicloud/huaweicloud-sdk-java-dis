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

import com.huaweicloud.dis.iface.app.request.ListAppsRequest;
import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.DescribeAppResult;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;

public interface IAppService
{
    /**
     * 创建APP
     * @param appName App名称
     */
    void createApp(String appName);

    /**
     * 删除APP
     * @param appName App名称
     */
    void deleteApp(String appName);

    /**
     * <p>
     * 根据App名称查询App详情。
     * </p>
     *
     * @param appName App名称
     * @return app的描述信息
     */
    DescribeAppResult describeApp(String appName);

    /**
     * <p>
     * 获取用户所有app。
     * </p>
     *
     * @param listAppsRequest  listAppsRequestc参数
     * @return app的描述信息列表
     */
    ListAppsResult listApps(ListAppsRequest listAppsRequest);

    /**
     * <p>
     * 获取Stream consuming state。
     * </p>
     *
     * @param listStreamConsumingStateRequest
     * @return Stream consuming state
     */
    ListStreamConsumingStateResult listStreamConsumingState(ListStreamConsumingStateRequest listStreamConsumingStateRequest);
}
