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

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.iface.app.request.ListAppsRequest;
import com.huaweicloud.dis.iface.app.response.DescribeAppResult;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;
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
import com.huaweicloud.dis.util.IOUtils;

public class DISClientAsync extends DISClient implements DISAsync
{
    
    private static final Logger LOG = LoggerFactory.getLogger(DISClientAsync.class);
    
    private static final int DEFAULT_THREAD_POOL_SIZE = 100;

    private final java.util.concurrent.ExecutorService executorService;
    
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
        
        this.executorService =
            (null == executorService) ? newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE) : executorService;
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
                return innerPutRecordsWithRetry(putRecordsParam);
            };
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
            };
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
        return submit(getRecordsParam, asyncHandler, new InnerExecutor<GetRecordsRequest, GetRecordsResult>()
        {
            public GetRecordsResult innerExecute(GetRecordsRequest getRecordsParam) {
                return innerGetRecords(getRecordsParam);
            };
        });
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
        return submit(describeStreamRequest, asyncHandler, new InnerExecutor<DescribeStreamRequest, DescribeStreamResult>()
        {
            public DescribeStreamResult innerExecute(DescribeStreamRequest describeStreamRequest) {
                return innerDescribeStream(describeStreamRequest);
            };
        });
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
        return submit(commitCheckpointRequest,
            asyncHandler,
            new InnerExecutor<CommitCheckpointRequest, CommitCheckpointResult>()
            {
                public CommitCheckpointResult innerExecute(CommitCheckpointRequest commitCheckpointRequest)
                {
                    return innerCommitCheckpoint(commitCheckpointRequest);
                }
            });
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
        return submit(getCheckpointRequest, asyncHandler, new InnerExecutor<GetCheckpointRequest, GetCheckpointResult>()
        {
            public GetCheckpointResult innerExecute(GetCheckpointRequest getCheckpointRequest)
            {
                return innerGetCheckpoint(getCheckpointRequest);
            }
        });
    }

    @Override
    public Future<DeleteCheckpointResult> deleteCheckpointAsync(DeleteCheckpointRequest deleteCheckpointRequest) {
        return deleteCheckpointAsync(deleteCheckpointRequest,null);
    }

    @Override
    public Future<DeleteCheckpointResult> deleteCheckpointAsync(DeleteCheckpointRequest deleteCheckpointRequest, AsyncHandler<DeleteCheckpointResult> asyncHandler) {
        return submit(deleteCheckpointRequest, asyncHandler, new InnerExecutor<DeleteCheckpointRequest, DeleteCheckpointResult>()
        {
            public DeleteCheckpointResult innerExecute(DeleteCheckpointRequest deleteCheckpointRequest)
            {
                return innerDeleteCheckpoint(deleteCheckpointRequest);
            }
        });
    }

    @Override
    public Future<ListStreamConsumingStateResult> listStreamConsumingStateAsync(ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
        return listStreamConsumingStateAsync(listStreamConsumingStateRequest,null);
    }

    @Override
    public Future<ListStreamConsumingStateResult> listStreamConsumingStateAsync(ListStreamConsumingStateRequest listStreamConsumingStateRequest, AsyncHandler<ListStreamConsumingStateResult> asyncHandler) {
        return submit(listStreamConsumingStateRequest, asyncHandler, new InnerExecutor<ListStreamConsumingStateRequest, ListStreamConsumingStateResult>()
        {
            public ListStreamConsumingStateResult innerExecute(ListStreamConsumingStateRequest listStreamConsumingStateRequest)
            {
                return innerListStreamConsumingState(listStreamConsumingStateRequest);
            }
        });
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
    
//    
//    @Override
//    public Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest)
//    {
//        return listStreamsAsync(listStreamsRequest, null);
//    }
//    
//    @Override
//    public Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest,
//        AsyncHandler<ListStreamsResult> asyncHandler)
//    {
//        return submit(listStreamsRequest, asyncHandler, new InnerExecutor<ListStreamsRequest, ListStreamsResult>()
//        {
//            public ListStreamsResult innerExecute(ListStreamsRequest listStreamsRequest) {
//                return innerListStreams(listStreamsRequest);
//            };
//        });
//    }
//    
//    @Override
//    public Future<SplitShardResult> splitShardAsync(SplitShardRequest splitShardsRequest)
//    {
//        return splitShardAsync(splitShardsRequest, null);
//    }
//    
//    @Override
//    public Future<SplitShardResult> splitShardAsync(SplitShardRequest splitShardsRequest,
//        AsyncHandler<SplitShardResult> asyncHandler)
//    {
//        return submit(splitShardsRequest, asyncHandler, new InnerExecutor<SplitShardRequest, SplitShardResult>()
//        {
//            public SplitShardResult innerExecute(SplitShardRequest splitShardsRequest) {
//                return splitShard(splitShardsRequest);
//            };
//        });
//    }
//    
//    @Override
//    public Future<MergeShardsResult> mergeShardsAsync(MergeShardsRequest mergeShardsRequest)
//    {
//        return mergeShardsAsync(mergeShardsRequest, null);
//    }
//    
//    @Override
//    public Future<MergeShardsResult> mergeShardsAsync(MergeShardsRequest mergeShardsRequest,
//        AsyncHandler<MergeShardsResult> asyncHandler)
//    {
//        return submit(mergeShardsRequest, asyncHandler, new InnerExecutor<MergeShardsRequest, MergeShardsResult>()
//        {
//            public MergeShardsResult innerExecute(MergeShardsRequest mergeShardsRequest) {
//                return mergeShards(mergeShardsRequest);
//            };
//        });
//    }
//    
//    @Override
//    public Future<AggregateRecordsResult> aggregateRecordsAsync(AggregateRecordsRequest aggregateRecordsParam)
//    {
//        return aggregateRecordsAsync(aggregateRecordsParam, null);
//    }
//    
//    @Override
//    public Future<AggregateRecordsResult> aggregateRecordsAsync(AggregateRecordsRequest aggregateRecordsParam,
//        AsyncHandler<AggregateRecordsResult> asyncHandler)
//    {
//        return submit(aggregateRecordsParam, asyncHandler, new InnerExecutor<AggregateRecordsRequest, AggregateRecordsResult>()
//        {
//            public AggregateRecordsResult innerExecute(AggregateRecordsRequest mergeShardsRequest) {
//                return innerAggregateRecords(aggregateRecordsParam);
//            };
//        });
//    }
    
    @Override
    public Future<PutFilesResult> putFilesAsync(PutFilesRequest putFilesRequest)
    {
        return putFilesAsync(putFilesRequest, null);
    }
    
    @Override
    public Future<PutFilesResult> putFilesAsync(PutFilesRequest putFilesRequest,
        AsyncHandler<PutFilesResult> asyncHandler)
    {
        // 文件名称校验
        if (!IOUtils.isValidFileName(putFilesRequest.getFileName()))
        {
            throw new RuntimeException("Invalid file name.");
        }
        
        return submit(putFilesRequest, asyncHandler, new InnerExecutor<PutFilesRequest, PutFilesResult>()
        {
            public PutFilesResult innerExecute(PutFilesRequest putFilesRequest) {
                
                String DELIVER_DATA_ID = UUID.randomUUID().toString().replace("-", "");
                DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
                describeStreamRequest.setStreamName(putFilesRequest.getStreamName());
                DescribeStreamResult describeStreamResult = describeStream(describeStreamRequest);
                innerReadFiles(putFilesRequest, DELIVER_DATA_ID, StreamType.getEnumByType(describeStreamResult.getStreamType()).getValue());
                
                // 异步返回，文件转储到OBS成功之后执行回调函数
                QueryFileState queryFileState = new QueryFileState();
                queryFileState.setStreamName(putFilesRequest.getStreamName());
                queryFileState.setFileName(putFilesRequest.getFileName());
                queryFileState.setDeliverDataId(String.valueOf(DELIVER_DATA_ID));
                
                boolean isSuccessful = false;
                while (true)
                {
                    // 查询当前文件转储状态（获取文件状态接口不进行重试）
                    FileUploadResult fileUploadResult = innerGetFileUploadResult(queryFileState);
                    if (FileUploadResult.STATE_IN_OBS.equals(fileUploadResult.getState()))
                    {
                        isSuccessful = true;
                        break; // 文件转储已经完成，返回
                    }

                    if(FileUploadResult.STATE_CANCELLED.equals(fileUploadResult.getState()))
                    {
                        //可能流已经被删除了
                        LOG.error("Failed to upload file {}, {}", putFilesRequest.getFilePath(), fileUploadResult);
                        break;
                    }
                    
                    LOG.info("Wait for file {} transferring completed, {}",
                        putFilesRequest.getFilePath(),
                        fileUploadResult);
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e)
                    {
                        LOG.error(e.getMessage(), e);;
                    }
                }
                
                // TODO 响应信息待优化
                PutFilesResult putFilesResult = new PutFilesResult();
                putFilesResult.setSuccessful(isSuccessful);
                return putFilesResult;
            };
        });
    }
    
    private void innerReadFiles(PutFilesRequest putFilesRequest, String deliverDataId, int bandwith)
    {
        final int READ_BYTES_PER_TIME = 300 * 1024;
        
        IOUtils.readFileByBytes(putFilesRequest.getFilePath(), READ_BYTES_PER_TIME, bandwith, new IOUtils.IOHandler<ByteBuffer>()
        {
            
            @Override
            public void doInIO(ByteBuffer byteBuffer, int seqNum)
            {
                innerPutFiles(putFilesRequest, byteBuffer, deliverDataId, seqNum, false);
            }

            @Override
            public void doLastIO(ByteBuffer byteBuffer, int seqNum)
            {
                innerPutFiles(putFilesRequest, byteBuffer, deliverDataId, seqNum, true);
            }
        });
        
    }
    
    private void innerPutFiles(PutFilesRequest putFilesRequest, ByteBuffer byteBuffer, String deliverDataId, long seqNum,
        boolean endFlag)
    {
        PutRecordsRequest putRecordsRequest = getPutRecordsRequest(putFilesRequest, byteBuffer, deliverDataId, seqNum, endFlag);
        
        doWithRetry(putRecordsRequest, 3, 1000, new RetryExecutor<PutRecordsRequest>(){

            @Override
            public void doWithRetry(PutRecordsRequest request, int maxRetries, long maxDelay)
            {
                PutRecordsResult putRecordsResult = innerPutRecordsWithRetry(putRecordsRequest);
                
                if (putRecordsResult.getFailedRecordCount().intValue() > 0)
                {
                    final String TRAFFIC_CONTROL_EXCEED_ERROR_CODE = "DIS.4303";
                    if (putRecordsResult.getRecords().get(0).getErrorCode().equals(TRAFFIC_CONTROL_EXCEED_ERROR_CODE))
                    {
                        // 客户端有流量限制的情况下，超过流控限制，线程休眠1秒
                        LOG.warn("Traffic control limit exceeded.");
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                    throw new RuntimeException(
                        "Fail to put records, errorCode: " + putRecordsResult.getRecords().get(0).getErrorCode()
                            + ", errorMessage: " + putRecordsResult.getRecords().get(0).getErrorMessage());
                }
            }
            
        });
    }
    
    private PutRecordsRequest getPutRecordsRequest(PutFilesRequest putFilesRequest, ByteBuffer byteBuffer,
        String dELIVER_DATA_ID, long seqNum, boolean endFlag)
    {
        
        PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
        putRecordsRequestEntry.setData(byteBuffer);
        putRecordsRequestEntry.setPartitionKey(putFilesRequest.getPartitionKey());
        putRecordsRequestEntry.setExplicitHashKey(putFilesRequest.getExplicitHashKey());
        
        PutRecordsRequestEntryExtendedInfo extendedInfo = new PutRecordsRequestEntryExtendedInfo();
        
        if (StringUtils.isNullOrEmpty(putFilesRequest.getFileName()))
        {
            putFilesRequest.setFileName(putFilesRequest.getFileName().toString().replace("\\", "/").replace(":", ""));
        }
        extendedInfo.setFileName(putFilesRequest.getFileName());
        extendedInfo.setDeliverDataId(String.valueOf(dELIVER_DATA_ID));
        extendedInfo.setSeqNum(seqNum);
        extendedInfo.setEndFlag(endFlag); // 文件末尾
        putRecordsRequestEntry.setExtendedInfo(extendedInfo);
        
        List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<PutRecordsRequestEntry>();
        putRecordsRequestEntryList.add(putRecordsRequestEntry);
        
        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setRecords(putRecordsRequestEntryList);
        putRecordsRequest.setStreamName(putFilesRequest.getStreamName());
        
        return putRecordsRequest;
    }
    
    private <REQUEST> void doWithRetry(REQUEST request, int maxRetries, long maxDelay,
        RetryExecutor<REQUEST> retryExecutor)
    {
        int count = 0;
        while (true)
        {
            try
            {
                retryExecutor.doWithRetry(request, maxRetries, maxDelay);
                break;
            }
            catch (Exception e)
            {
                if (++count <= maxRetries)
                {
                    try
                    {
                        Thread.sleep(maxDelay);
                    }
                    catch (InterruptedException e1)
                    {
                        LOG.error(e1.getMessage(), e1);
                    }
                }
                else
                {
                    throw e;
                }
            }
        }
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
                    }
                    throw ex;
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
    
}
