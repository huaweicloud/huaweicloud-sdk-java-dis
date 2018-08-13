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

import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.iface.app.request.ListAppsRequest;
import com.huaweicloud.dis.iface.app.response.DescribeAppResult;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import com.huaweicloud.dis.iface.stream.request.DescribeStreamRequest;
import com.huaweicloud.dis.iface.stream.request.UpdatePartitionCountRequest;
import com.huaweicloud.dis.iface.stream.response.DescribeStreamResult;
import com.huaweicloud.dis.iface.stream.response.UpdatePartitionCountResult;

/**
 * DIS服务的数据面异步接口
 * */
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
    java.util.concurrent.Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsParam, AsyncHandler<PutRecordsResult> asyncHandler);
    
    /**
     * <p>
     * 用户获取迭代器，根据迭代器获取一次数据和下一个迭代器。
     * </p>
     * 
     * @param getPartitionCursorParam 用户获取迭代器的请求参数
     * @return 包含获取迭代器响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetPartitionCursorResult> getPartitionCursorAsync(GetPartitionCursorRequest getPartitionCursorParam);
    
    /**
     * <p>
     * 用户获取迭代器，根据迭代器获取一次数据和下一个迭代器。
     * </p>
     * 
     * @param getPartitionCursorParam 用户获取迭代器的请求参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含获取迭代器响应结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetPartitionCursorResult> getPartitionCursorAsync(GetPartitionCursorRequest getPartitionCursorParam, AsyncHandler<GetPartitionCursorResult> asyncHandler);
    
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
    java.util.concurrent.Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam, AsyncHandler<GetRecordsResult> asyncHandler);

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
    java.util.concurrent.Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest, AsyncHandler<DescribeStreamResult> asyncHandler);
    
    
    //所有的异步接口，只在同步接口正式对外发布的时候才处理，同步接口发布为异步的过程，各个接口的实现方式是一样的
    
//    java.util.concurrent.Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest);
//
//    java.util.concurrent.Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest, AsyncHandler<ListStreamsResult> asyncHandler);
//    
//    java.util.concurrent.Future<SplitShardResult> splitShardAsync(SplitShardRequest splitShardsRequest);
//
//    java.util.concurrent.Future<SplitShardResult> splitShardAsync(SplitShardRequest splitShardsRequest, AsyncHandler<SplitShardResult> asyncHandler);
//
//    java.util.concurrent.Future<MergeShardsResult> mergeShardsAsync(MergeShardsRequest mergeShardsRequest);
//    
//    java.util.concurrent.Future<MergeShardsResult> mergeShardsAsync(MergeShardsRequest mergeShardsRequest, AsyncHandler<MergeShardsResult> asyncHandler);
//    
//    java.util.concurrent.Future<AggregateRecordsResult> aggregateRecordsAsync(AggregateRecordsRequest aggregateRecordsParam);
//    
//    java.util.concurrent.Future<AggregateRecordsResult> aggregateRecordsAsync(AggregateRecordsRequest aggregateRecordsParam, AsyncHandler<AggregateRecordsResult> asyncHandler);
    
    /**
     * <p>
     * 上传小文件。
     * </p>
     * 
     * @param putFilesRequest 上传小文件的请求参数
     * @return 包含上传小文件结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<PutFilesResult> putFilesAsync(PutFilesRequest putFilesRequest);
    
    /**
     * <p>
     * 上传小文件。
     * </p>
     * 
     * @param putFilesRequest 上传小文件的请求参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含上传小文件结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<PutFilesResult> putFilesAsync(PutFilesRequest putFilesRequest,
        AsyncHandler<PutFilesResult> asyncHandler);

    /**
     * <p>
     * 提交Checkpoint
     * </p>
     *
     * @param commitCheckpointRequest 提交Checkpoint
     * @return 包含提交Checkpoint结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<CommitCheckpointResult> commitCheckpointAsync(
        CommitCheckpointRequest commitCheckpointRequest);
    
    /**
     * <p>
     * 提交Checkpoint
     * </p>
     *
     * @param commitCheckpointRequest 提交Checkpoint
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含提交Checkpoint结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<CommitCheckpointResult> commitCheckpointAsync(
        CommitCheckpointRequest commitCheckpointRequest, AsyncHandler<CommitCheckpointResult> asyncHandler);
    
    /**
     * <p>
     * 获取Checkpoint
     * </p>
     *
     * @param getCheckpointRequest 获取Checkpoint
     * @return 包含获取Checkpoint结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetCheckpointResult> getCheckpointAsync(GetCheckpointRequest getCheckpointRequest);
    
    /**
     * <p>
     * 获取Checkpoint
     * </p>
     *
     * @param getCheckpointRequest 获取Checkpoint
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含获取Checkpoint结果的 JAVA Future 对象
     */
    java.util.concurrent.Future<GetCheckpointResult> getCheckpointAsync(GetCheckpointRequest getCheckpointRequest,
        AsyncHandler<GetCheckpointResult> asyncHandler);

    /**
     * <p>
     * 创建App
     * </p>
     *
     * @param appName App名称
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明App创建成功
     */
    java.util.concurrent.Future<Void> createAppAsync(String appName);
    
    /**
     * <p>
     * 创建App
     * </p>
     *
     * @param appName App名称
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明App创建成功
     */
    java.util.concurrent.Future<Void> createAppAsync(String appName, AsyncHandler<Void> asyncHandler);
    
    /**
     * <p>
     * 删除App
     * </p>
     *
     * @param appName App名称
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明App删除成功
     */
    java.util.concurrent.Future<Void> deleteAppAsync(String appName);
    
    /**
     * <p>
     * 删除App
     * </p>
     *
     * @param appName App名称
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明App删除成功
     */
    java.util.concurrent.Future<Void> deleteAppAsync(String appName, AsyncHandler<Void> asyncHandler);


    /**
     * <p>
     * 描述App
     * </p>
     *
     * @param appName App名称
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明获取APP信息成功
     */
    java.util.concurrent.Future<DescribeAppResult> describeAppAsync(String appName);

    /**
     * <p>
     * 描述App
     * </p>
     *
     * @param appName App名称
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明获取APP信息成功
     */
    java.util.concurrent.Future<DescribeAppResult> describeAppAsync(String appName, AsyncHandler<DescribeAppResult> asyncHandler);


    /**
     * <p>
     * 获取App列表
     * </p>
     *
     * @param listAppsRequest list app的参数
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明获取APP信息列表成功
     */
    java.util.concurrent.Future<ListAppsResult> listAppAsync(ListAppsRequest listAppsRequest);

    /**
     * <p>
     * 获取App列表
     * </p>
     *
     * @param listAppsRequest list app的参数
     * @param asyncHandler 异步回调处理程序。 用户可以提供接口中回调方法的实现，以接收操作成功或失败的通知。
     * @return 包含没有返回类型的 JAVA Future 对象，如果get不报错则表明获取APP信息列表成功
     */
    java.util.concurrent.Future<ListAppsResult> listAppAsync(ListAppsRequest listAppsRequest, AsyncHandler<ListAppsResult> asyncHandler);


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
}
