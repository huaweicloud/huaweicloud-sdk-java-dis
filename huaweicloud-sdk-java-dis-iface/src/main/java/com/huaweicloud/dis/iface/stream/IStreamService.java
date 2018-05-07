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

package com.huaweicloud.dis.iface.stream;

import com.huaweicloud.dis.iface.stream.request.CreateStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DeleteStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DescribeStreamRequest;
import com.huaweicloud.dis.iface.stream.request.ListStreamsRequest;
import com.huaweicloud.dis.iface.stream.response.CreateStreamResult;
import com.huaweicloud.dis.iface.stream.response.DeleteStreamResult;
import com.huaweicloud.dis.iface.stream.response.DescribeStreamResult;
import com.huaweicloud.dis.iface.stream.response.ListStreamsResult;

/**
 * DIS服务的管理面接口
 * */
public interface IStreamService
{
    //管理
    CreateStreamResult createStream(CreateStreamRequest createStreamRequest);
    
    DeleteStreamResult deleteStream(DeleteStreamRequest deleteStreamRequest);
    
    //管理面
    ListStreamsResult listStreams(ListStreamsRequest listStreamsRequest);
    
    /**
     * 管理面API
     * <p>
     * 查询指定通道详情。
     * </p>
     * 
     * @param describeStreamRequest 查询通道详情的请求参数
     * @return 查询通道详情的响应结果
     */
    DescribeStreamResult describeStream(DescribeStreamRequest describeStreamRequest);
    
}
