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

package com.huaweicloud.dis;

import java.util.concurrent.Future;

import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import com.huaweicloud.dis.iface.stream.request.*;
import com.huaweicloud.dis.iface.stream.response.*;
import com.huaweicloud.dis.iface.transfertask.request.*;
import com.huaweicloud.dis.iface.transfertask.response.*;

/**
 * DIS服务的数据面异步接口
 */
public interface DISAsync extends DIS
{
    /**
     * <p>
     * 上传多条数据到DIS实例中。
     * </p>
     *
     * @param putRecordsParam 上传多条数据的请求参数
     * @return 包含批量上传数据操作响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsParam);
    
    /**
     * <p>
     * 上传多条数据到DIS实例中。
     * </p>
     *
     * @param putRecordsParam 上传多条数据的请求参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含批量上传数据操作响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsParam,
        AsyncHandler<PutRecordsResult> asyncHandler);
    
    /**
     * <p>
     * 用户获取迭代器，根据迭代器获取一次数据和下一个迭代器。
     * </p>
     *
     * @param getPartitionCursorParam 用户获取迭代器的请求参数
     * @return 包含获取迭代器响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetPartitionCursorResult> getPartitionCursorAsync(
        GetPartitionCursorRequest getPartitionCursorParam);
    
    /**
     * <p>
     * 用户获取迭代器，根据迭代器获取一次数据和下一个迭代器。
     * </p>
     *
     * @param getPartitionCursorParam 用户获取迭代器的请求参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含获取迭代器响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetPartitionCursorResult> getPartitionCursorAsync(
        GetPartitionCursorRequest getPartitionCursorParam, AsyncHandler<GetPartitionCursorResult> asyncHandler);
    
    /**
     * <p>
     * 从DIS实例中下载数据。
     * </p>
     *
     * @param getRecordsParam 下载数据的请求参数
     * @return 包含下载数据响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam);
    
    /**
     * <p>
     * 从DIS实例中下载数据。
     * </p>
     *
     * @param getRecordsParam 下载数据的请求参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含下载数据响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam,
        AsyncHandler<GetRecordsResult> asyncHandler);
    
    /**
     * <p>
     * 创建流。
     * </p>
     *
     * @param createStreamRequest 创建流的请求参数
     * @return 包含创建流响应结果的 JAVA Future 对象
     */
    Future<CreateStreamResult> createStreamAsync(CreateStreamRequest createStreamRequest);
    
    Future<CreateStreamResult> createStreamAsync(CreateStreamRequest createStreamRequest,
        AsyncHandler<CreateStreamResult> asyncHandler);
    
    /**
     * <p>
     * 删除流
     * </p>
     *
     * @param deleteStreamRequest 删除流的请求参数
     * @return 包含删除流响应结果的 JAVA Future 对象
     */
    Future<DeleteStreamResult> deleteStreamAsync(DeleteStreamRequest deleteStreamRequest);
    
    Future<DeleteStreamResult> deleteStreamAsync(DeleteStreamRequest deleteStreamRequest,
        AsyncHandler<DeleteStreamResult> asyncHandler);
    
    /**
     * <p>
     * 查询流列表
     * </p>
     *
     * @param listStreamsRequest 查询流列表的请求参数
     * @return 包含流列表响应结果的 JAVA Future 对象
     */
    Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest);
    
    Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest,
        AsyncHandler<ListStreamsResult> asyncHandler);
    
    /**
     * <p>
     * 查询指定通道详情。
     * </p>
     *
     * @param describeStreamRequest 查询通道详情的请求参数
     * @return 包含查询通道详情响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest);
    
    /**
     * <p>
     * 查询指定通道详情。
     * </p>
     *
     * @param describeStreamRequest 查询通道详情的请求参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含查询通道详情响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest,
        AsyncHandler<DescribeStreamResult> asyncHandler);
    
    /**
     * <p>
     * 变更分区数量
     * </p>
     *
     * @param updatePartitionCountRequest 变更分区数量请求体
     * @return 包含分区变更结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<UpdatePartitionCountResult> updatePartitionCountAsync(
        UpdatePartitionCountRequest updatePartitionCountRequest);
    
    /**
     * <p>
     * 变更分区数量
     * </p>
     *
     * @param updatePartitionCountRequest 变更分区数量请求体
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含分区变更结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<UpdatePartitionCountResult> updatePartitionCountAsync(
        UpdatePartitionCountRequest updatePartitionCountRequest, AsyncHandler<UpdatePartitionCountResult> asyncHandler);
    
    /**
     * <p>
     * 关闭线程池
     * </p>
     */
    public void close();
    
    /**
     * <p>
     * 创建转储任务。
     * </p>
     *
     * @param createTransferTaskRequest 创建转储任务的请求参数
     * @return 包含创建任务响应结果的 JAVA Future 对象
     */
    Future<CreateTransferTaskResult> createTransferTaskAsync(CreateTransferTaskRequest createTransferTaskRequest);
    
    Future<CreateTransferTaskResult> createTransferTaskAsync(CreateTransferTaskRequest createTransferTaskRequest,
        AsyncHandler<CreateTransferTaskResult> asyncHandler);
    
    /**
     * <p>
     * 更新转储任务。
     * </p>
     *
     * @param updateTransferTaskRequest 创建转储任务的请求参数
     * @return 包含创建任务响应结果的 JAVA Future 对象
     */
    Future<UpdateTransferTaskResult> updateTransferTaskAsync(UpdateTransferTaskRequest updateTransferTaskRequest);
    
    Future<UpdateTransferTaskResult> updateTransferTaskAsync(UpdateTransferTaskRequest updateTransferTaskRequest,
        AsyncHandler<UpdateTransferTaskResult> asyncHandler);
    
    /**
     * <p>
     * 删除转储任务
     * </p>
     *
     * @param deleteTransferTaskRequest 删除转储任务的请求参数
     * @return 包含删除转储任务响应结果的 JAVA Future 对象
     */
    Future<DeleteTransferTaskResult> deleteTransferTaskAsync(DeleteTransferTaskRequest deleteTransferTaskRequest);
    
    Future<DeleteTransferTaskResult> deleteTransferTaskAsync(DeleteTransferTaskRequest deleteTransferTaskRequest,
        AsyncHandler<DeleteTransferTaskResult> asyncHandler);
    
    /**
     * <p>
     * 查询转储任务列表
     * </p>
     *
     * @param listTransferTasksRequest 查询转储任务列表的请求参数
     * @return 包含转储任务列表响应结果的 JAVA Future 对象
     */
    Future<ListTransferTasksResult> listTransferTasksAsync(ListTransferTasksRquest listTransferTasksRequest);
    
    Future<ListTransferTasksResult> listTransferTasksAsync(ListTransferTasksRquest listTransferTasksRequest,
        AsyncHandler<ListTransferTasksResult> asyncHandler);
    
    /**
     * <p>
     * 查询指定转储任务详情。
     * </p>
     *
     * @param describeTransferTaskRequest 查询转储任务详情的请求参数
     * @return 包含查询转储任务详情响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<DescribeTransferTaskResult> describeTransferTaskAsync(
        DescribeTransferTaskRequest describeTransferTaskRequest);
    
    java.util.concurrent.Future<DescribeTransferTaskResult> describeTransferTaskAsync(
        DescribeTransferTaskRequest describeTransferTaskRequest, AsyncHandler<DescribeTransferTaskResult> asyncHandler);
}
