package com.huaweicloud.dis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig.BodySerializeType;
import com.huaweicloud.dis.core.DefaultRequest;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.restresource.AppsResource;
import com.huaweicloud.dis.core.restresource.CheckPointResource;
import com.huaweicloud.dis.core.restresource.CursorResource;
import com.huaweicloud.dis.core.restresource.FileResource;
import com.huaweicloud.dis.core.restresource.RecordResource;
import com.huaweicloud.dis.core.restresource.ResourcePathBuilder;
import com.huaweicloud.dis.core.restresource.StateResource;
import com.huaweicloud.dis.core.restresource.StreamResource;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.http.AbstractCallbackAdapter;
import com.huaweicloud.dis.http.AbstractFutureAdapter;
import com.huaweicloud.dis.iface.api.protobuf.ProtobufUtils;
import com.huaweicloud.dis.iface.app.request.CreateAppRequest;
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
import com.huaweicloud.dis.util.ExponentialBackOff;
import com.huaweicloud.dis.util.IOUtils;

public class DISClientAsync extends AbstractDISClientAsync implements DISAsync{
	
	private static final Logger LOG = LoggerFactory.getLogger(DISClientAsync.class);
	
	protected ExecutorService executorService;
	
	/**
     * 构造异步DIS客户端
     * 
     * @param disConfig DIS客户端参数
     */
    public DISClientAsync(DISConfig disConfig)
    {
        super(disConfig);
    }
    
    @Deprecated
    public DISClientAsync(DISConfig disConfig, ExecutorService executorService) {
    	super(disConfig);
    	this.executorService = executorService == null ? Executors.newFixedThreadPool(100) : executorService;
	}

	public Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam,
        AsyncHandler<GetRecordsResult> asyncHandler)
    {
    	Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new RecordResource(null))
                .build());
        
    	GetRecordsDecorateFuture getRecordsDecorateFuture = new GetRecordsDecorateFuture();
    	
    	AsyncHandler<GetRecordsResult> getRecordsDecorateCallback = null;
    	if(asyncHandler != null) {
    		getRecordsDecorateCallback = new GetRecordsDecorateCallback(asyncHandler, getRecordsDecorateFuture);
    	}
    	
    	Future<GetRecordsResult> result;
        if(BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())){            
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");
            
            AsyncHandler<com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult> finalAsyncHandler = null;
            
            GetRecordsFuture getRecordsFuture = new GetRecordsFuture();
            
            if(getRecordsDecorateCallback != null) {
            	finalAsyncHandler = new GetRecordsCallback(getRecordsDecorateCallback, getRecordsFuture);
            }
            
            Future<com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult> getRecordsProtobufFuture = requestAsync(getRecordsParam, request, com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult.class, finalAsyncHandler);
            getRecordsFuture.setInnerFuture(getRecordsProtobufFuture);
            
            result = getRecordsFuture;
        }else{
        	result = requestAsync(getRecordsParam, request, GetRecordsResult.class, getRecordsDecorateCallback);
        }
        
        getRecordsDecorateFuture.setInnerFuture(result);
        return getRecordsDecorateFuture;
    }
	
    private class GetRecordsDecorateFuture extends AbstractFutureAdapter<GetRecordsResult, GetRecordsResult> implements Future<GetRecordsResult>{
		private GetRecordsResult finalResult = null;
    	
    	public GetRecordsDecorateFuture() {
    		super();
    	}

		@Override
		protected GetRecordsResult toT(GetRecordsResult innerT) {
			if(finalResult == null) {
				finalResult = DISClientAsync.this.decorateRecords(innerT);
			}
			return finalResult;
		}
    }
    
    private static class GetRecordsFuture extends AbstractFutureAdapter<GetRecordsResult, com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult> implements Future<GetRecordsResult>{
		@Override
		protected GetRecordsResult toT(com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult innerT) {
			return ProtobufUtils.toGetRecordsResult(innerT);
		}
    }
    
    private class GetRecordsDecorateCallback extends AbstractCallbackAdapter<GetRecordsResult, GetRecordsResult> implements AsyncHandler<GetRecordsResult>{
    	public GetRecordsDecorateCallback(AsyncHandler<GetRecordsResult> innerAsyncHandler,
				AbstractFutureAdapter<GetRecordsResult, GetRecordsResult> futureAdapter) {
			super(innerAsyncHandler, futureAdapter);
		}

		@Override
		protected GetRecordsResult toInnerT(GetRecordsResult result) {
			return DISClientAsync.this.decorateRecords(result);
		}
    	
    }
    
    private static class GetRecordsCallback extends AbstractCallbackAdapter<com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult, GetRecordsResult> implements AsyncHandler<com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult>{
		public GetRecordsCallback(AsyncHandler<GetRecordsResult> innerAsyncHandler, GetRecordsFuture getRecordsFuture) {
			super(innerAsyncHandler, getRecordsFuture);
		}

		@Override
		protected GetRecordsResult toInnerT(com.huaweicloud.dis.iface.api.protobuf.Message.GetRecordsResult result) {
			GetRecordsResult getRecordsResult = ProtobufUtils.toGetRecordsResult(result);
			return getRecordsResult;
		}
    }

    private static class PutRecordsFuture extends AbstractFutureAdapter<PutRecordsResult, com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult> implements Future<PutRecordsResult>{

		@Override
		protected PutRecordsResult toT(com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult innerT) {
			return ProtobufUtils.toPutRecordsResult(innerT);
		}
    	
    }
    
    private static class PutRecordsCallback extends AbstractCallbackAdapter<com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult, PutRecordsResult> implements AsyncHandler<com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult>{

		public PutRecordsCallback(AsyncHandler<PutRecordsResult> innerAsyncHandler,
				AbstractFutureAdapter<PutRecordsResult, com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult> futureAdapter) {
			super(innerAsyncHandler, futureAdapter);
		}

		@Override
		protected PutRecordsResult toInnerT(com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult result) {
			return ProtobufUtils.toPutRecordsResult(result);
		}
    	
    }
    
	@Override
	public Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsParam) {
		return putRecordsAsync(putRecordsParam, null);
	}

	@Override
	public Future<PutRecordsResult> putRecordsAsync(PutRecordsRequest putRecordsParam,
			AsyncHandler<PutRecordsResult> asyncHandler) {
		putRecordsParam = decorateRecords(putRecordsParam);
        
    	Request<HttpRequest> request = buildRequest(HttpMethodName.POST, disConfig.getEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new RecordResource(null))
                .build());
		
        if(BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())){            
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");
            
            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest protoRequest = ProtobufUtils.toProtobufPutRecordsRequest(putRecordsParam);
            
            PutRecordsFuture putRecordsFuture = new PutRecordsFuture();
            
            PutRecordsCallback putRecordsCallback = null;
            if(asyncHandler != null) {
            	putRecordsCallback = new PutRecordsCallback(asyncHandler, putRecordsFuture);
            }
            
            Future<com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult> putRecordsProtobufFuture = requestAsync(protoRequest.toByteArray(), request, com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult.class, putRecordsCallback);            
            putRecordsFuture.setInnerFuture(putRecordsProtobufFuture);
            
            return putRecordsFuture;
        }else{
            return requestAsync(putRecordsParam, request, PutRecordsResult.class, asyncHandler);
        }
	}

	@Override
	public Future<GetPartitionCursorResult> getPartitionCursorAsync(GetPartitionCursorRequest getPartitionCursorParam) {
		return getPartitionCursorAsync(getPartitionCursorParam, null);
	}

	@Override
	public Future<GetPartitionCursorResult> getPartitionCursorAsync(GetPartitionCursorRequest getPartitionCursorParam,
			AsyncHandler<GetPartitionCursorResult> asyncHandler) {
    	Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new CursorResource(null))
                .build());
    	
        return requestAsync(getPartitionCursorParam, request, GetPartitionCursorResult.class, asyncHandler);
	}

	@Override
	public Future<GetRecordsResult> getRecordsAsync(GetRecordsRequest getRecordsParam) {
		return getRecordsAsync(getRecordsParam, null);
	}

	@Override
	public Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest) {
		return describeStreamAsync(describeStreamRequest, null);
	}

	@Override
	public Future<DescribeStreamResult> describeStreamAsync(DescribeStreamRequest describeStreamRequest,
			AsyncHandler<DescribeStreamResult> asyncHandler) {
    	Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, describeStreamRequest.getStreamName()))
                .build());
        
        return requestAsync(describeStreamRequest, request, DescribeStreamResult.class, asyncHandler);
	}

	@Override
	public Future<PutFilesResult> putFilesAsync(PutFilesRequest putFilesRequest) {
		return putFilesAsync(putFilesRequest, null);
	}

	//---------------------------------------- putfilesAsync -----------------------------------------
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
    
    protected final FileUploadResult innerGetFileUploadResult(QueryFileState queryFileState)
    {
        Request<HttpRequest> request = new DefaultRequest<>(Constants.SERVICENAME);
        request.setHttpMethod(HttpMethodName.GET);
        
        final String resourcePath = ResourcePathBuilder.standard()
            .withProjectId(disConfig.getProjectId())
            .withResource(new FileResource(queryFileState.getDeliverDataId()))
            .withResource(new StateResource(null))
            .build();
        request.setResourcePath(resourcePath);
        setEndpoint(request, disConfig.getEndpoint());
        
        return request(queryFileState, request, FileUploadResult.class);
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
    
    protected ReentrantLock recordsRetryLock = new ReentrantLock();
    protected PutRecordsResult innerPutRecordsWithRetry(PutRecordsRequest putRecordsParam)
    {
        PutRecordsResult putRecordsResult = null;
        PutRecordsResultEntry[] putRecordsResultEntryList = null;
        Integer[] retryIndex = null;
        PutRecordsRequest retryPutRecordsRequest = putRecordsParam;
        
        int retryCount = -1;
        int currentFailed = 0;
        ExponentialBackOff backOff = null;
        try
        {
            do
            {
                retryCount++;
                if (retryCount > 0)
                {
                    // 等待一段时间再发起重试
                    if (backOff == null)
                    {
                        recordsRetryLock.lock();
                        LOG.trace("Put records retry lock.");
                        backOff = new ExponentialBackOff(ExponentialBackOff.DEFAULT_INITIAL_INTERVAL,
                            ExponentialBackOff.DEFAULT_MULTIPLIER, disConfig.getBackOffMaxIntervalMs(),
                            ExponentialBackOff.DEFAULT_MAX_ELAPSED_TIME);
                    }
                    
                    if (putRecordsResult != null && currentFailed != putRecordsResult.getRecords().size())
                    {
                        // 部分失败则重置退避时间
                        backOff.resetCurrentInterval();
                    }
                    
                    long sleepMs = backOff.getNextBackOff();
                    
                    if (retryPutRecordsRequest.getRecords().size() > 0)
                    {
                        LOG.debug(
                            "Put {} records but {} failed, will re-try after backoff {} ms, current retry count is {}.",
                            putRecordsResult != null ? putRecordsResult.getRecords().size()
                                : putRecordsParam.getRecords().size(),
                            currentFailed,
                            sleepMs,
                            retryCount);
                    }
                    
                    backOff.backOff(sleepMs);
                }
                
                try
                {
                    putRecordsResult = putRecords(retryPutRecordsRequest);
                }
                catch (Throwable t)
                {
                    if (putRecordsResultEntryList != null)
                    {
                        LOG.error(t.getMessage(), t);
                        break;
                    }
                    throw t;
                }
                
                if (putRecordsResult != null)
                {
                    currentFailed = putRecordsResult.getFailedRecordCount().get();
                    
                    if (putRecordsResultEntryList == null && currentFailed == 0 || disConfig.getRecordsRetries() == 0)
                    {
                        // 第一次发送全部成功或者不需要重试，则直接返回结果
                        return putRecordsResult;
                    }
                    
                    if (putRecordsResultEntryList == null)
                    {
                        // 存在发送失败的情况，需要重试，则使用数组来汇总每次请求后的结果。
                        putRecordsResultEntryList = new PutRecordsResultEntry[putRecordsParam.getRecords().size()];
                    }
                    
                    // 需要重试发送数据的原始下标
                    List<Integer> retryIndexTemp = new ArrayList<>(currentFailed);
                    
                    if (currentFailed > 0)
                    {
                        // 初始化重试发送的数据请求
                        retryPutRecordsRequest = new PutRecordsRequest();
                        retryPutRecordsRequest.setStreamName(putRecordsParam.getStreamName());
                        retryPutRecordsRequest.setRecords(new ArrayList<>(currentFailed));
                    }
                    
                    // 对每条结果分析，更新结果数据
                    for (int i = 0; i < putRecordsResult.getRecords().size(); i++)
                    {
                        // 获取重试数据在原始数据中的下标位置
                        int originalIndex = retryIndex == null ? i : retryIndex[i];
                        PutRecordsResultEntry putRecordsResultEntry = putRecordsResult.getRecords().get(i);
                        // 对所有异常进行重试 && "DIS.4303".equals(putRecordsResultEntry.getErrorCode())
                        if (!StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode()))
                        {
                            retryIndexTemp.add(originalIndex);
                            retryPutRecordsRequest.getRecords().add(putRecordsParam.getRecords().get(originalIndex));
                        }
                        putRecordsResultEntryList[originalIndex] = putRecordsResultEntry;
                    }
                    retryIndex = retryIndexTemp.size() > 0 ? retryIndexTemp.toArray(new Integer[retryIndexTemp.size()])
                        : new Integer[0];
                }
            } while ((retryIndex == null || retryIndex.length > 0) && retryCount < disConfig.getRecordsRetries());
        }
        finally
        {
            if (retryCount > 0)
            {
                recordsRetryLock.unlock();
                LOG.trace("Put records retry unlock.");
            }
        }
        putRecordsResult = new PutRecordsResult();
        if (retryIndex == null)
        {
            // 不可能存在此情况，完全没有发送出去会直接抛出异常
            putRecordsResult.setFailedRecordCount(new AtomicInteger(putRecordsParam.getRecords().size()));
        }
        else
        {
            putRecordsResult.setFailedRecordCount(new AtomicInteger(retryIndex.length));
            putRecordsResult.setRecords(Arrays.asList(putRecordsResultEntryList));
        }
        
        return putRecordsResult;
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

	//---------------------------------------- putfilesAsync -----------------------------------------
    
	@Override
	public Future<CommitCheckpointResult> commitCheckpointAsync(CommitCheckpointRequest commitCheckpointRequest) {
		return commitCheckpointAsync(commitCheckpointRequest, null);
	}

	@Override
	public Future<CommitCheckpointResult> commitCheckpointAsync(CommitCheckpointRequest commitCheckpointRequest,
			AsyncHandler<CommitCheckpointResult> asyncHandler) {
    	Request<HttpRequest> request = buildRequest(HttpMethodName.POST, disConfig.getEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new CheckPointResource(null))
                .build());
		
        return requestAsync(commitCheckpointRequest, request, CommitCheckpointResult.class, asyncHandler);
	}

	@Override
	public Future<GetCheckpointResult> getCheckpointAsync(GetCheckpointRequest getCheckpointRequest) {
		return getCheckpointAsync(getCheckpointRequest, null);
	}

	@Override
	public Future<GetCheckpointResult> getCheckpointAsync(GetCheckpointRequest getCheckpointRequest,
			AsyncHandler<GetCheckpointResult> asyncHandler) {
    	Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new CheckPointResource(null))
                .build());
		
        return requestAsync(getCheckpointRequest, request, GetCheckpointResult.class, asyncHandler);
	}

	@Override
	public Future<Void> createAppAsync(String appName) {
		return createAppAsync(appName, null);
	}

	@Override
	public Future<Void> createAppAsync(String appName, AsyncHandler<Void> asyncHandler) {
    	Request<HttpRequest> request = buildRequest(HttpMethodName.POST, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(null))
                .build());
        
        CreateAppRequest createAppIdRequest = new CreateAppRequest();
        createAppIdRequest.setAppName(appName);
        return requestAsync(createAppIdRequest, request, null, asyncHandler);
	}

	@Override
	public Future<Void> deleteAppAsync(String appName) {
		return deleteAppAsync(appName, null);
	}

	@Override
	public Future<Void> deleteAppAsync(String appName, AsyncHandler<Void> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.DELETE, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(appName))
                .build());
		
		return requestAsync(null, request, null, asyncHandler);
	}

	@Override
	public Future<DescribeAppResult> describeAppAsync(String appName) {
		return describeAppAsync(appName, null);
	}

	@Override
	public Future<DescribeAppResult> describeAppAsync(String appName, AsyncHandler<DescribeAppResult> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(appName))
                .build());
		
        return requestAsync(null, request, DescribeAppResult.class, asyncHandler);
	}

	@Override
	public Future<ListAppsResult> listAppsAsync(ListAppsRequest listAppsRequest) {
		return listAppsAsync(listAppsRequest, null);
	}

	@Override
	public Future<ListAppsResult> listAppsAsync(ListAppsRequest listAppsRequest,
			AsyncHandler<ListAppsResult> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new AppsResource(null))
                .build());
        
        return requestAsync(listAppsRequest, request, ListAppsResult.class, asyncHandler);
	}

	@Override
	public Future<UpdatePartitionCountResult> updatePartitionCountAsync(
			UpdatePartitionCountRequest updatePartitionCountRequest) {
		return updatePartitionCountAsync(updatePartitionCountRequest, null);
	}

	@Override
	public Future<UpdatePartitionCountResult> updatePartitionCountAsync(
			UpdatePartitionCountRequest updatePartitionCountRequest,
			AsyncHandler<UpdatePartitionCountResult> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.PUT, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, updatePartitionCountRequest.getStreamName()))
                .build());
        
        return requestAsync(updatePartitionCountRequest, request, UpdatePartitionCountResult.class, asyncHandler);
	}

	@Override
	public Future<CreateStreamResult> createStreamAsync(CreateStreamRequest createStreamRequest) {
		return createStreamAsync(createStreamRequest, null);
	}

	@Override
	public Future<CreateStreamResult> createStreamAsync(CreateStreamRequest createStreamRequest,
			AsyncHandler<CreateStreamResult> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.POST, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, null))
                .build());
        
        return requestAsync(createStreamRequest, request, CreateStreamResult.class, asyncHandler);
	}

	@Override
	public Future<DeleteStreamResult> deleteStreamAsync(DeleteStreamRequest deleteStreamRequest) {
		return deleteStreamAsync(deleteStreamRequest, null);
	}

	@Override
	public Future<DeleteStreamResult> deleteStreamAsync(DeleteStreamRequest deleteStreamRequest,
			AsyncHandler<DeleteStreamResult> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.DELETE, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, deleteStreamRequest.getStreamName()))
                .build());
        
        return requestAsync(deleteStreamRequest, request, DeleteStreamResult.class, asyncHandler);
	}

	@Override
	public Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest) {
		return listStreamsAsync(listStreamsRequest, null);
	}

	@Override
	public Future<ListStreamsResult> listStreamsAsync(ListStreamsRequest listStreamsRequest,
			AsyncHandler<ListStreamsResult> asyncHandler) {
		Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, null))
                .build());
        
        return requestAsync(listStreamsRequest, request, ListStreamsResult.class, asyncHandler);
	}

    @Override
    public Future<DeleteCheckpointResult> deleteCheckpointAsync(DeleteCheckpointRequest deleteCheckpointRequest) {
        return deleteCheckpointAsync(deleteCheckpointRequest,null);
    }

    @Override
    public Future<DeleteCheckpointResult> deleteCheckpointAsync(DeleteCheckpointRequest deleteCheckpointRequest, AsyncHandler<DeleteCheckpointResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.DELETE, disConfig.getEndpoint(),
                ResourcePathBuilder.standard()
                        .withProjectId(disConfig.getProjectId())
                        .withResource(new CheckPointResource(null))
                        .build());
        return requestAsync(deleteCheckpointRequest, request, DeleteCheckpointResult.class, asyncHandler);
    }

    @Override
    public Future<ListStreamConsumingStateResult> listStreamConsumingStateAsync(ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
        return listStreamConsumingStateAsync(listStreamConsumingStateRequest,null);
    }

    @Override
    public Future<ListStreamConsumingStateResult> listStreamConsumingStateAsync(ListStreamConsumingStateRequest listStreamConsumingStateRequest, AsyncHandler<ListStreamConsumingStateResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getEndpoint(),
                ResourcePathBuilder.standard()
                        .withProjectId(disConfig.getProjectId())
                        .withResource(new AppsResource(listStreamConsumingStateRequest.getAppName()))
                        .withResource(new StreamResource(listStreamConsumingStateRequest.getStreamName()))
                        .build());
        return requestAsync(listStreamConsumingStateRequest, request, ListStreamConsumingStateResult.class, asyncHandler);
    }

}
