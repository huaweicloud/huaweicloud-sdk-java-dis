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

package com.cloud.dis.iface.stream;

import com.cloud.dis.iface.stream.request.CreateStreamRequest;
import com.cloud.dis.iface.stream.request.DeleteStreamRequest;
import com.cloud.dis.iface.stream.request.DescribeStreamRequest;
import com.cloud.dis.iface.stream.request.ListStreamsRequest;
import com.cloud.dis.iface.stream.request.UpdatePartitionCountRequest;
import com.cloud.dis.iface.stream.request.UpdateStreamRequest;
import com.cloud.dis.iface.stream.response.CreateStreamResult;
import com.cloud.dis.iface.stream.response.DeleteStreamResult;
import com.cloud.dis.iface.stream.response.DescribeStreamResult;
import com.cloud.dis.iface.stream.response.ListStreamsResult;
import com.cloud.dis.iface.stream.response.UpdatePartitionCountResult;
import com.cloud.dis.iface.stream.response.UpdateStreamResult;

/**
 * DIS服务的管理面接口
 * */
public interface IStreamService
{
    /**
     * 创建流
     * */
    CreateStreamResult createStream(CreateStreamRequest createStreamRequest);
    
    /**
     * 删除流
     * */
    DeleteStreamResult deleteStream(DeleteStreamRequest deleteStreamRequest);
    
    /**
     * 查询流清单
     * */
    ListStreamsResult listStreams(ListStreamsRequest listStreamsRequest);
    
    /**
     * <p>
     * 查询指定通道详情。
     * </p>
     * 
     * @param describeStreamRequest 查询通道详情的请求参数
     * @return 查询通道详情的响应结果
     */
    DescribeStreamResult describeStream(DescribeStreamRequest describeStreamRequest);
    
    /**
     * 更新流分片的扩缩容接口：
     * 被减少的分片，分片状态先置为deleted状态，只读。待达到该分片的有效期后（分片内没有数据），分片变为expired状态，不可读不可写。
     * 扩容分片时，会重用存在deleted状态的分片
     * 
     * deleted状态和expired状态的分片不参与计费。
     * */
    UpdatePartitionCountResult updatePartitionCount(UpdatePartitionCountRequest updatePartitionCountRequest);
    
    /**
     * 更新通道：当前支持更新通道的源数据类型和设置自动扩缩容
     */
    UpdateStreamResult updateStream(UpdateStreamRequest updateStreamRequest);
}
