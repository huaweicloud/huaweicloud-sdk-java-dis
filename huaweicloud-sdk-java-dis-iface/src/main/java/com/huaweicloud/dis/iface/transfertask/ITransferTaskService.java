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
package com.huaweicloud.dis.iface.transfertask;

import com.huaweicloud.dis.iface.transfertask.request.*;
import com.huaweicloud.dis.iface.transfertask.response.*;

/**
 * DIS服务管理面转储任务的相关接口
 */
public interface ITransferTaskService
{
    /**
     * <p>
     * 创建转储任务。
     * </p>
     */
    CreateTransferTaskResult createTransferTask(CreateTransferTaskRequest createTransferTaskRequest);
    
    /**
     * <p>
     * 更新转储任务。
     * </p>
     */
    UpdateTransferTaskResult updateTransferTask(UpdateTransferTaskRequest updateTransferTaskRequest);

    
    /**
     * <p>
     * 删除转储任务。
     * </p>
     */
    DeleteTransferTaskResult deleteTransferTask(DeleteTransferTaskRequest deleteTransferTaskRequest);
    
    /**
     * <p>
     * 查询转储任务清单。
     * </p>
     */
    ListTransferTasksResult listTransferTasks(ListTransferTasksRquest listTransferTasksRquest);
    
    /**
     * <p>
     * 查询指定转储任务的详情。
     * </p>
     */
    DescribeTransferTaskResult describeTransferTask(DescribeTransferTaskRequest describeTransferTaskRequest);
}
