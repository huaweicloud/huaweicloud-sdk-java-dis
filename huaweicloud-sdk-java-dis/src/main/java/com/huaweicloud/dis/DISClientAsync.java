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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.core.builder.DefaultExecutorFactory;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.iface.app.request.ListAppsRequest;
import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.DescribeAppResult;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.huaweicloud.dis.iface.data.request.CommitCheckpointRequest;
import com.huaweicloud.dis.iface.data.request.DeleteCheckpointRequest;
import com.huaweicloud.dis.iface.data.request.GetCheckpointRequest;
import com.huaweicloud.dis.iface.data.request.GetPartitionCursorRequest;
import com.huaweicloud.dis.iface.data.request.GetRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.response.CommitCheckpointResult;
import com.huaweicloud.dis.iface.data.response.DeleteCheckpointResult;
import com.huaweicloud.dis.iface.data.response.GetCheckpointResult;
import com.huaweicloud.dis.iface.data.response.GetPartitionCursorResult;
import com.huaweicloud.dis.iface.data.response.GetRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.stream.request.CreateStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DeleteStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DescribeStreamRequest;
import com.huaweicloud.dis.iface.stream.request.ListStreamsRequest;
import com.huaweicloud.dis.iface.stream.request.UpdatePartitionCountRequest;
import com.huaweicloud.dis.iface.stream.response.CreateStreamResult;
import com.huaweicloud.dis.iface.stream.response.DeleteStreamResult;
import com.huaweicloud.dis.iface.stream.response.DescribeStreamResult;
import com.huaweicloud.dis.iface.stream.response.ListStreamsResult;
import com.huaweicloud.dis.iface.stream.response.UpdatePartitionCountResult;
import com.huaweicloud.dis.iface.transfertask.request.CreateTransferTaskRequest;
import com.huaweicloud.dis.iface.transfertask.request.DeleteTransferTaskRequest;
import com.huaweicloud.dis.iface.transfertask.request.DescribeTransferTaskRequest;
import com.huaweicloud.dis.iface.transfertask.request.ListTransferTasksRquest;
import com.huaweicloud.dis.iface.transfertask.request.UpdateTransferTaskRequest;
import com.huaweicloud.dis.iface.transfertask.response.CreateTransferTaskResult;
import com.huaweicloud.dis.iface.transfertask.response.DeleteTransferTaskResult;
import com.huaweicloud.dis.iface.transfertask.response.DescribeTransferTaskResult;
import com.huaweicloud.dis.iface.transfertask.response.ListTransferTasksResult;
import com.huaweicloud.dis.iface.transfertask.response.UpdateTransferTaskResult;

public class DISClientAsync extends DISClient implements DISAsync
{
    
    private static final Logger LOG = LoggerFactory.getLogger(DISClientAsync.class);

    private final ExecutorService executorService;
    
    public DISClientAsync(DISConfig disConfig) {
    	this(disConfig, null);
    }
    
    /**
     * 构造异步DIS客户端
     *
     * @param disConfig DIS客户端参数
     * @param executorService Instance of {@code ExecutorService}, default thread pool size is 50.
     * @see com.huaweicloud.dis.core.builder.ExecutorFactory
     */
    public DISClientAsync(DISConfig disConfig, ExecutorService executorService)
    {
        super(disConfig);
        this.executorService = executorService == null ? new DefaultExecutorFactory().newExecutor() : executorService;

    }
    
    @Override
    public Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsParam)
    {
        return putRecordsAsync(putRecordsParam, null);
    }
    
    @Override
    public Future<PutRecordsResult> putRecordsAsync(final PutRecordsRequest putRecordsParam,
        AsyncHandler<PutRecordsResult> asyncHandler)
    {
        return submit(putRecordsParam, asyncHandler, new InnerExecutor<PutRecordsRequest, PutRecordsResult>()
        {
            public PutRecordsResult innerExecute(PutRecordsRequest putRecordsParam) {
                return innerPutRecordsSupportingCache(putRecordsParam);
            }
        });
    }
    
    @Override
    public Future<GetPartitionCursorResult> getPartitionCursorAsync(GetPartitionCursorRequest getPartitionCursorParam)
    {
        return getPartitionCursorAsync(getPartitionCursorParam, null);
    }
    
    @Override
    public Future<GetPartitionCursorResult> getPartitionCursorAsync(GetPartitionCursorRequest getPartitionCursorParam,
        AsyncHandler<GetPartitionCursorResult> asyncHandler)
    {
        return submit(getPartitionCursorParam, asyncHandler, new InnerExecutor<GetPartitionCursorRequest, GetPartitionCursorResult>()
        {
            public GetPartitionCursorResult innerExecute(GetPartitionCursorRequest getPartitionCursorParam) {
                return innerGetPartitionCursor(getPartitionCursorParam);
            }
        });
    }
    
    @Override
    public Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam)
    {
        return getRecordsAsync(getRecordsParam, null);
    }
    
    @Override
    public Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam,
        AsyncHandler<GetRecordsResult> asyncHandler)
    {
        return submit(getRecordsParam, asyncHandler, getRecordsParam1 -> getRecords(getRecordsParam1));
    }
    
    
    @Override
    public Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest)
    {
        return describeStreamAsync(describeStreamRequest, null);
    }
    
    @Override
    public Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest,
        AsyncHandler<DescribeStreamResult> asyncHandler)
    {
        return submit(describeStreamRequest, asyncHandler, describeStreamRequest1 -> innerDescribeStream(describeStreamRequest1));
    }

    @Override
    public Future<CommitCheckpointResult> commitCheckpointAsync(CommitCheckpointRequest commitCheckpointRequest)
    {
        return commitCheckpointAsync(commitCheckpointRequest, null);
    }
    
    @Override
    public Future<CommitCheckpointResult> commitCheckpointAsync(CommitCheckpointRequest commitCheckpointRequest,
        AsyncHandler<CommitCheckpointResult> asyncHandler)
    {
        return submit(commitCheckpointRequest, asyncHandler, commitCheckpointRequest1 -> innerCommitCheckpoint(commitCheckpointRequest1));
    }
    
    @Override
    public Future<GetCheckpointResult> getCheckpointAsync(GetCheckpointRequest getCheckpointRequest)
    {
        return getCheckpointAsync(getCheckpointRequest, null);
    }
    
    @Override
    public Future<GetCheckpointResult> getCheckpointAsync(GetCheckpointRequest getCheckpointRequest,
        AsyncHandler<GetCheckpointResult> asyncHandler)
    {
        return submit(getCheckpointRequest, asyncHandler, getCheckpointRequest1 -> innerGetCheckpoint(getCheckpointRequest1));
    }
    
    @Override
    public Future<Void> createAppAsync(String appName)
    {
        return createAppAsync(appName, null);
    }
    
    @Override
    public Future<Void> createAppAsync(String appName, AsyncHandler<Void> asyncHandler)
    {
        return submit(appName, asyncHandler, new InnerExecutor<String, Void>()
        {
            public Void innerExecute(String appName)
            {
                innerCreateApp(appName);
                return null;
            }
        });
    }

    @Override
    public Future<Void> deleteAppAsync(String appName)
    {
        return deleteAppAsync(appName, null);
    }
    
    @Override
    public Future<Void> deleteAppAsync(String appName, AsyncHandler<Void> asyncHandler)
    {
        return submit(appName, asyncHandler, new InnerExecutor<String, Void>()
        {
            public Void innerExecute(String appName)
            {
                innerDeleteApp(appName);
                return null;
            }
        });
    }


    @Override
    public Future<DescribeAppResult> describeAppAsync(String appName)
    {
        return describeAppAsync(appName, null);
    }

    @Override
    public Future<DescribeAppResult> describeAppAsync(String appName, AsyncHandler<DescribeAppResult> asyncHandler)
    {
        return submit(appName, asyncHandler, new InnerExecutor<String, DescribeAppResult>()
        {
            public DescribeAppResult innerExecute(String appName)
            {
                return innerDescribeApp(appName);
            }
        });
    }


    @Override
    public Future<ListAppsResult> listAppsAsync(ListAppsRequest listAppsRequest)
    {
        return listAppsAsync(listAppsRequest,null);
    }

    @Override
    public Future<ListAppsResult> listAppsAsync(ListAppsRequest listAppsRequest, AsyncHandler<ListAppsResult> asyncHandler)
    {
        return submit(listAppsRequest, asyncHandler, new InnerExecutor<ListAppsRequest, ListAppsResult>()
        {
            public ListAppsResult innerExecute(ListAppsRequest listAppsRequest)
            {
                return innerListApps(listAppsRequest);
            }
        });
    }


    @Override
    public Future<UpdatePartitionCountResult> updatePartitionCountAsync(
        UpdatePartitionCountRequest updatePartitionCountRequest)
    {
        return updatePartitionCountAsync(updatePartitionCountRequest, null);
    }
    
    @Override
    public Future<UpdatePartitionCountResult> updatePartitionCountAsync(
        UpdatePartitionCountRequest updatePartitionCountRequest, AsyncHandler<UpdatePartitionCountResult> asyncHandler)
    {
        return submit(updatePartitionCountRequest,
            asyncHandler,
            new InnerExecutor<UpdatePartitionCountRequest, UpdatePartitionCountResult>()
            {
                public UpdatePartitionCountResult innerExecute(UpdatePartitionCountRequest updatePartitionCountRequest)
                {
                    return innerUpdatePartitionCount(updatePartitionCountRequest);
                }
            });
    }
    
    private <REQUEST, RESULT> Future<RESULT> submit(REQUEST request, AsyncHandler<RESULT> asyncHandler,
        InnerExecutor<REQUEST, RESULT> innerExecutor)
    {
        return executorService.submit(new java.util.concurrent.Callable<RESULT>()
        {
            @Override
            public RESULT call()
                throws Exception
            {
                RESULT result = null;
                
                try
                {
                    result = innerExecutor.innerExecute(request);
                }
                catch (Exception ex)
                {
                    if (asyncHandler != null)
                    {
                        asyncHandler.onError(ex);
                        return null;
                    } else {
                        // No Handler
                        throw ex;
                    }
                }
                
                if (asyncHandler != null)
                {
                    asyncHandler.onSuccess(result);
                }
                return result;
            }
        });
    }
 
    protected static interface InnerExecutor<REQUEST, RESULT>
    {
        /*
         * 异步方法需要执行的接口
         */
        RESULT innerExecute(REQUEST request);
    }
    
    protected static interface RetryExecutor<REQUEST>
    {
        /*
         * 需要重试的代码块
         */
        void doWithRetry(REQUEST request, int maxRetries, long maxDelay);
    }

	@Override
	public Future<CreateStreamResult> createStreamAsync(CreateStreamRequest createStreamRequest) {
		return createStreamAsync(createStreamRequest, null);
	}

	@Override
	public Future<CreateStreamResult> createStreamAsync(CreateStreamRequest createStreamRequest,
			AsyncHandler<CreateStreamResult> asyncHandler) {
		
        return submit(createStreamRequest, asyncHandler, new InnerExecutor<CreateStreamRequest, CreateStreamResult>()
        {
            public CreateStreamResult innerExecute(CreateStreamRequest createStreamRequest) {
                return innerCreateStream(createStreamRequest);
            };
        });
	}

	@Override
	public Future<DeleteStreamResult> deleteStreamAsync(DeleteStreamRequest deleteStreamRequest) {
		return deleteStreamAsync(deleteStreamRequest, null);
	}

	@Override
	public Future<DeleteStreamResult> deleteStreamAsync(DeleteStreamRequest deleteStreamRequest,
			AsyncHandler<DeleteStreamResult> asyncHandler) {
		return submit(deleteStreamRequest, asyncHandler, new InnerExecutor<DeleteStreamRequest, DeleteStreamResult>()
        {
            public DeleteStreamResult innerExecute(DeleteStreamRequest deleteStreamRequest) {
                return innerDeleteStream(deleteStreamRequest);
            };
        });
	}

	@Override
	public Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest) {
		return listStreamsAsync(listStreamsRequest, null);
	}

	@Override
	public Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest,
			AsyncHandler<ListStreamsResult> asyncHandler) {
		return submit(listStreamsRequest, asyncHandler, new InnerExecutor<ListStreamsRequest, ListStreamsResult>()
        {
            public ListStreamsResult innerExecute(ListStreamsRequest listStreamsRequest) {
                return innerListStreams(listStreamsRequest);
            };
        });
	}

	@Override
	public Future<DeleteCheckpointResult> deleteCheckpointAsync(DeleteCheckpointRequest deleteCheckpointRequest) {
		return deleteCheckpointAsync(deleteCheckpointRequest, null);
	}

	@Override
	public Future<DeleteCheckpointResult> deleteCheckpointAsync(DeleteCheckpointRequest deleteCheckpointRequest,
			AsyncHandler<DeleteCheckpointResult> asyncHandler) {
		return submit(deleteCheckpointRequest, asyncHandler, new InnerExecutor<DeleteCheckpointRequest, DeleteCheckpointResult>()
        {
            public DeleteCheckpointResult innerExecute(DeleteCheckpointRequest deleteCheckpointRequest) {
                return innerDeleteCheckpoint(deleteCheckpointRequest);
            }
        });
	}

	@Override
	public Future<ListStreamConsumingStateResult> listStreamConsumingStateAsync(
			ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
		return listStreamConsumingStateAsync(listStreamConsumingStateRequest, null);
	}

	@Override
	public Future<ListStreamConsumingStateResult> listStreamConsumingStateAsync(
			ListStreamConsumingStateRequest listStreamConsumingStateRequest,
			AsyncHandler<ListStreamConsumingStateResult> asyncHandler) {
		return submit(listStreamConsumingStateRequest, asyncHandler, new InnerExecutor<ListStreamConsumingStateRequest, ListStreamConsumingStateResult>()
        {
            public ListStreamConsumingStateResult innerExecute(ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
                return innerListStreamConsumingState(listStreamConsumingStateRequest);
            }
        });
	}

    @Override
    public void close()
    {
        if (executorService != null)
        {
            executorService.shutdown();
        }
    }
    
    @Override
    public Future<CreateTransferTaskResult> createTransferTaskAsync(CreateTransferTaskRequest createTransferTaskRequest) {
        return createTransferTaskAsync(createTransferTaskRequest, null);
    }
    
    @Override
    public Future<CreateTransferTaskResult> createTransferTaskAsync(CreateTransferTaskRequest createTransferTaskRequest,
        AsyncHandler<CreateTransferTaskResult> asyncHandler) {
        
        return submit(createTransferTaskRequest, asyncHandler, new InnerExecutor<CreateTransferTaskRequest, CreateTransferTaskResult>()
        {
            public CreateTransferTaskResult innerExecute(CreateTransferTaskRequest createTransferTaskRequest) {
                return innerCreateTransferTask(createTransferTaskRequest);
            }
        });
    }
    
    @Override
    public Future<UpdateTransferTaskResult> updateTransferTaskAsync(UpdateTransferTaskRequest updateTransferTaskRequest) {
        return updateTransferTaskAsync(updateTransferTaskRequest, null);
    }
    
    @Override
    public Future<UpdateTransferTaskResult> updateTransferTaskAsync(UpdateTransferTaskRequest updateTransferTaskRequest,
        AsyncHandler<UpdateTransferTaskResult> asyncHandler) {
        
        return submit(updateTransferTaskRequest, asyncHandler, new InnerExecutor<UpdateTransferTaskRequest, UpdateTransferTaskResult>()
        {
            public UpdateTransferTaskResult innerExecute(UpdateTransferTaskRequest updateTransferTaskRequest) {
                return innerUpdateTransferTask(updateTransferTaskRequest);
            }
        });
    }
    
    @Override
    public Future<DeleteTransferTaskResult> deleteTransferTaskAsync(DeleteTransferTaskRequest deleteTransferTaskRequest) {
        return deleteTransferTaskAsync(deleteTransferTaskRequest, null);
    }
    
    @Override
    public Future<DeleteTransferTaskResult> deleteTransferTaskAsync(DeleteTransferTaskRequest deleteTransferTaskRequest,
        AsyncHandler<DeleteTransferTaskResult> asyncHandler) {
        return submit(deleteTransferTaskRequest, asyncHandler, new InnerExecutor<DeleteTransferTaskRequest, DeleteTransferTaskResult>()
        {
            public DeleteTransferTaskResult innerExecute(DeleteTransferTaskRequest deleteTransferTaskRequest) {
                return innerDeleteTransferTask(deleteTransferTaskRequest);
            }
        });
    }
    
    @Override
    public Future<ListTransferTasksResult> listTransferTasksAsync(ListTransferTasksRquest listTransferTasksRequest) {
        return listTransferTasksAsync(listTransferTasksRequest, null);
    }
    
    @Override
    public Future<ListTransferTasksResult> listTransferTasksAsync(ListTransferTasksRquest listTransferTasksRequest,
        AsyncHandler<ListTransferTasksResult> asyncHandler) {
        return submit(listTransferTasksRequest, asyncHandler, new InnerExecutor<ListTransferTasksRquest, ListTransferTasksResult>()
        {
            public ListTransferTasksResult innerExecute(ListTransferTasksRquest listTransferTasksRequest) {
                return innerListTransferTasks(listTransferTasksRequest);
            }
        });
    }
    
    @Override
    public Future<DescribeTransferTaskResult> describeTransferTaskAsync(DescribeTransferTaskRequest describeTransferTaskRequest)
    {
        return describeTransferTaskAsync(describeTransferTaskRequest, null);
    }
    
    @Override
    public Future<DescribeTransferTaskResult> describeTransferTaskAsync(DescribeTransferTaskRequest describeTransferTaskRequest,
        AsyncHandler<DescribeTransferTaskResult> asyncHandler)
    {
        return submit(describeTransferTaskRequest, asyncHandler, new InnerExecutor<DescribeTransferTaskRequest, DescribeTransferTaskResult>()
        {
            public DescribeTransferTaskResult innerExecute(DescribeTransferTaskRequest describeTransferTaskRequest) {
                return innerDescribeTransferTask(describeTransferTaskRequest);
            }
        });
    }
}
