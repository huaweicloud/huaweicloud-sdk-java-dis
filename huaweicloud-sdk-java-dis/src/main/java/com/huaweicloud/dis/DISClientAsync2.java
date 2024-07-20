package com.huaweicloud.dis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import com.huaweicloud.dis.iface.transfertask.request.*;
import com.huaweicloud.dis.iface.transfertask.response.*;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huaweicloud.dis.DISConfig.BodySerializeType;
import com.huaweicloud.dis.core.DISCredentials;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.builder.DefaultExecutorFactory;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.restresource.CursorResource;
import com.huaweicloud.dis.core.restresource.RecordResource;
import com.huaweicloud.dis.core.restresource.ResourcePathBuilder;
import com.huaweicloud.dis.core.restresource.StreamResource;
import com.huaweicloud.dis.core.restresource.TransferTaskResource;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.http.AbstractCallbackAdapter;
import com.huaweicloud.dis.http.AbstractFutureAdapter;
import com.huaweicloud.dis.iface.api.protobuf.ProtobufUtils;
import com.huaweicloud.dis.iface.data.request.GetPartitionCursorRequest;
import com.huaweicloud.dis.iface.data.request.GetRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequestEntry;
import com.huaweicloud.dis.iface.data.response.GetPartitionCursorResult;
import com.huaweicloud.dis.iface.data.response.GetRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;
import com.huaweicloud.dis.iface.stream.request.CreateStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DeleteStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DescribeStreamRequest;
import com.huaweicloud.dis.iface.stream.request.ListStreamsRequest;
import com.huaweicloud.dis.iface.stream.request.UpdatePartitionCountRequest;
import com.huaweicloud.dis.iface.stream.request.UpdateStreamRequest;
import com.huaweicloud.dis.iface.stream.response.CreateStreamResult;
import com.huaweicloud.dis.iface.stream.response.DeleteStreamResult;
import com.huaweicloud.dis.iface.stream.response.DescribeStreamResult;
import com.huaweicloud.dis.iface.stream.response.ListStreamsResult;
import com.huaweicloud.dis.iface.stream.response.UpdatePartitionCountResult;
import com.huaweicloud.dis.iface.stream.response.UpdateStreamResult;
import com.huaweicloud.dis.util.Utils;

public class DISClientAsync2 extends AbstractDISClientAsync implements DISAsync{
	
	private static final Logger LOG = LoggerFactory.getLogger(DISClientAsync2.class);
	
	protected ExecutorService executorService;
	
	/**
     * 构造异步DIS客户端
     *
     * @param disConfig DIS客户端参数
     */
    public DISClientAsync2(DISConfig disConfig)
    {
        super(disConfig);
    }
    
    @Deprecated
    public DISClientAsync2(DISConfig disConfig, ExecutorService executorService) {
		super(disConfig);
		this.executorService = executorService == null ? new DefaultExecutorFactory().newExecutor() : executorService;
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
				finalResult = DISClientAsync2.this.decorateRecords(innerT);
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
			return DISClientAsync2.this.decorateRecords(result);
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
		
    	PutRecordsTrafficLimitRetryFuture trafficLimitRetryFuture = new PutRecordsTrafficLimitRetryFuture(request, asyncHandler, putRecordsParam);
    	
		PutRecordsTrafficLimitRetryCallback trafficLimitRetryCallback = null;
    	if(asyncHandler != null) {
    		trafficLimitRetryCallback = new PutRecordsTrafficLimitRetryCallback(asyncHandler, trafficLimitRetryFuture, 0);
    	}
    	
    	Future<PutRecordsResult> putRecordsFuture = innerPutRecordsAsync(putRecordsParam, request, trafficLimitRetryCallback);
    	trafficLimitRetryFuture.setInnerFuture(putRecordsFuture);
    	
    	return trafficLimitRetryFuture;
	}
	
	private Future<PutRecordsResult> innerPutRecordsAsync(PutRecordsRequest putRecordsParam,
			Request<HttpRequest> request, PutRecordsTrafficLimitRetryCallback trafficLimitRetryCallback){
    	
    	if(BodySerializeType.protobuf.equals(disConfig.getBodySerializeType())){
    		request.getHeaders().remove("Content-Type");
            request.addHeader("Content-Type", "application/x-protobuf; charset=utf-8");
            
            com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsRequest protoRequest = ProtobufUtils.toProtobufPutRecordsRequest(putRecordsParam);
            
            PutRecordsFuture putRecordsFuture = new PutRecordsFuture();
            
            PutRecordsCallback putRecordsCallback = null;
            if(trafficLimitRetryCallback != null) {
            	putRecordsCallback = new PutRecordsCallback(trafficLimitRetryCallback, putRecordsFuture);
            }
            
            Future<com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult> putRecordsProtobufFuture = requestAsync(protoRequest.toByteArray(), request, com.huaweicloud.dis.iface.api.protobuf.Message.PutRecordsResult.class, putRecordsCallback);
            putRecordsFuture.setInnerFuture(putRecordsProtobufFuture);
            
            return putRecordsFuture;
        }else{
			return requestAsync(putRecordsParam, request, PutRecordsResult.class, trafficLimitRetryCallback);
        }
    	
	}

	private static class PutRecordsTrafficLimitRetryCallback implements AsyncHandler<PutRecordsResult>{
		private final int retryIndex;
		private final AsyncHandler<PutRecordsResult> innerAsyncHandler;
		protected final Future<PutRecordsResult> futureAdapter;
		
		public PutRecordsTrafficLimitRetryCallback(AsyncHandler<PutRecordsResult> innerAsyncHandler,
				Future<PutRecordsResult> futureAdapter, int retryIndex) {
			this.innerAsyncHandler = innerAsyncHandler;
			this.futureAdapter = futureAdapter;
			this.retryIndex = retryIndex;
		}

		@Override
		public void onSuccess(PutRecordsResult result) throws Exception {
			PutRecordsTrafficLimitRetryFuture future = (PutRecordsTrafficLimitRetryFuture) this.futureAdapter;
			
			try {
				PutRecordsResult mergedResult = future.mergeRetryHandle(result, true, retryIndex);
				if(mergedResult == null) {
					return;
				}else {
					innerAsyncHandler.onSuccess(result);
				}
			}catch(Exception e) {
				onError(e);
			}
		}
		
		@Override
		public void onError(Exception exception) throws Exception {
			PutRecordsTrafficLimitRetryFuture future = (PutRecordsTrafficLimitRetryFuture) this.futureAdapter;
			
			PutRecordsResult exRes = future.mergeException(exception, retryIndex);
			if(exRes == null) {
				innerAsyncHandler.onError(exception);
			}else {
				innerAsyncHandler.onSuccess(exRes);
			}
		}
	}
	
	private class PutRecordsTrafficLimitRetryFuture implements Future<PutRecordsResult> {
		protected volatile Future<PutRecordsResult> innerFuture;
		
		//为了避免future.get和callback的各种并发情况下的重复重试，使用该锁和计数进行控制
		private final AtomicInteger retryCount = new AtomicInteger();
		private final ReentrantLock retryLock = new ReentrantLock();
		
		private final AtomicBoolean finished = new AtomicBoolean();
		
		private final AtomicInteger retryMergeIndex = new AtomicInteger(-1);
		
		private final AsyncHandler<PutRecordsResult> asyncHandler;
		private final Request<HttpRequest> request;
		private final PutRecordsRequest putRecordsParam;
		
		private final AtomicReference<PutRecordsResult> putRecordsResultRef = new AtomicReference<>();
		
		private volatile Integer[] retryRecordIndex = null;
		
		public void setInnerFuture(Future<PutRecordsResult> innerFuture) {
			this.innerFuture = innerFuture;
		}
		
		public PutRecordsTrafficLimitRetryFuture(Request<HttpRequest> request,
				AsyncHandler<PutRecordsResult> asyncHandler, PutRecordsRequest putRecordsParam) {
			this.request = request;
			this.asyncHandler = asyncHandler;
			this.putRecordsParam = putRecordsParam;
		}

		public PutRecordsResult getNewestResult() {
			return this.putRecordsResultRef.get();
		}
		
		public PutRecordsResult mergeException(Exception exception, int retryIndex) {
			retryMergeIndex.compareAndSet(retryIndex-1, retryIndex);
			finished.set(true);
			return getNewestResult();
		}
		
		public PutRecordsResult mergeRetryHandle(PutRecordsResult putRecordsResult, boolean tryLock, int retryIndex) {
			//处理重试的数据合并
			List<Integer> retryIndexTemp = new ArrayList<>();
            List<PutRecordsRequestEntry> retryRecordEntrys = new ArrayList<>();
			synchronized (this) {
				if(retryMergeIndex.compareAndSet(retryIndex-1, retryIndex)) {
					mergeResult(putRecordsResult, retryIndex, retryRecordEntrys, retryIndexTemp);
				}
				
				if(finished.get()) {
					return getNewestResult();
				}
			}
            
			
			if(tryLock) {
				if(!retryLock.tryLock()) {
					return null;
				}
			}else {
				retryLock.lock();
			}
            
			try {
				if(retryIndex != retryCount.get()){
					return null;
				}
				
				retryRecordIndex = retryIndexTemp.size() > 0 ? retryIndexTemp.toArray(new Integer[retryIndexTemp.size()])
	                    : new Integer[0];
	            
	            int tmpRetryIndex = retryCount.incrementAndGet();
	            
	            PutRecordsRequest retryPutRecordsRequest = new PutRecordsRequest();
	            retryPutRecordsRequest.setStreamName(putRecordsParam.getStreamName());
                retryPutRecordsRequest.setStreamId(putRecordsParam.getStreamId());
	            retryPutRecordsRequest.setRecords(retryRecordEntrys);
	            
	    		PutRecordsTrafficLimitRetryCallback trafficLimitRetryCallback = null;
	        	if(asyncHandler != null) {
	        		trafficLimitRetryCallback = new PutRecordsTrafficLimitRetryCallback(asyncHandler, this, tmpRetryIndex);
	        	}
	        	LOG.warn("traffic limit retry [{}] [{}] [{}]", putRecordsParam.getStreamName(), this.hashCode(), retryIndex);
	            Future<PutRecordsResult> recordRetryFuture = innerPutRecordsAsync(retryPutRecordsRequest, request, trafficLimitRetryCallback);
	            this.setInnerFuture(recordRetryFuture);
	            
	            return null;
			}finally {
				retryLock.unlock();
			}
		}
		
		private void mergeResult(PutRecordsResult putRecordsResult, int retryIndex,
				List<PutRecordsRequestEntry> retryRecordEntrys, List<Integer> retryIndexTemp) {
			this.putRecordsResultRef.compareAndSet(null, putRecordsResult);
			
			int currentFailed = putRecordsResult.getFailedRecordCount().get();
			if(retryIndex == 0 && currentFailed == 0 || disConfig.getRecordsRetries() == 0) {
				finished.set(true);
				return;
			}
			
			boolean isCanRetry = retryIndex < disConfig.getRecordsRetries();
            
			int curSuccessCount = 0;
            // 对每条结果分析，更新结果数据
            for (int i = 0; i < putRecordsResult.getRecords().size(); i++)
            {
            	PutRecordsResultEntry putRecordsResultEntry = putRecordsResult.getRecords().get(i);
            	
            	// 获取重试数据在原始数据中的下标位置
                int originalIndex = retryRecordIndex == null ? i : retryRecordIndex[i];
                 
                if (!StringUtils.isNullOrEmpty(putRecordsResultEntry.getErrorCode()))
                {
                    // 只对指定异常(如流控与服务端内核异常)进行重试
                    if (isCanRetry && isRecordsRetriableErrorCode(putRecordsResultEntry.getErrorCode()))
                    {
                        retryIndexTemp.add(originalIndex);
                        retryRecordEntrys.add(putRecordsParam.getRecords().get(originalIndex));
                    }
                }else {
                	curSuccessCount++;
                }
                
                if(retryIndex != 0) {
                	this.putRecordsResultRef.get().getRecords().set(originalIndex, putRecordsResultEntry);
                }
            }
            
            if(retryIndex != 0 && curSuccessCount > 0) {
            	this.putRecordsResultRef.get().getFailedRecordCount().addAndGet(-curSuccessCount);
            }
            
            if(retryRecordEntrys.isEmpty()) {
            	finished.set(true);
            }
		}

		@Override
		public PutRecordsResult get() throws InterruptedException, ExecutionException {
			retryLock.lock();
			int getThreadRetryIndex = retryCount.get();
			
			try {
				PutRecordsResult putRecordsResult = innerFuture.get();
				
				PutRecordsResult mergedPutRecordsResult = mergeRetryHandle(putRecordsResult, false, getThreadRetryIndex);
				
				if(mergedPutRecordsResult == null) {
					return this.get();
				}else {
					return mergedPutRecordsResult;
				}
			}catch(InterruptedException | ExecutionException e) {
				if(getThreadRetryIndex == 0) {
					PutRecordsResult exRes = mergeException(e, getThreadRetryIndex);
					if(exRes != null) {
						return exRes;
					}
				}
				
				throw e;
			}
			finally {
				retryLock.unlock();
			}
		}
		
		@Override
		public PutRecordsResult get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException, TimeoutException {
			retryLock.lock();
			int getThreadRetryIndex = retryCount.get();
			
			try {
				PutRecordsResult putRecordsResult = innerFuture.get(timeout, unit);
				
				PutRecordsResult mergedPutRecordsResult = mergeRetryHandle(putRecordsResult, false, getThreadRetryIndex);
				
				if(mergedPutRecordsResult == null) {
					return this.get(timeout, unit);
				}else {
					return mergedPutRecordsResult;
				}
			}catch(InterruptedException | ExecutionException e) {
				if(getThreadRetryIndex == 0) {
					PutRecordsResult exRes = mergeException(e, getThreadRetryIndex);
					if(exRes != null) {
						return exRes;
					}
				}
				
				throw e;
			}
			finally {
				retryLock.unlock();
			}
		}
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return innerFuture.cancel(mayInterruptIfRunning);
		}

		@Override
		public boolean isCancelled() {
			return innerFuture.isCancelled();
		}

		@Override
		public boolean isDone() {
			return innerFuture.isDone();
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

        // change to shardId format
        describeStreamRequest.setStartPartitionId(Utils.getShardIdFromPartitionId(describeStreamRequest.getStartPartitionId()));
    	Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
    			ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, describeStreamRequest.getStreamName()))
                .build());
        
        return requestAsync(describeStreamRequest, request, DescribeStreamResult.class, asyncHandler);
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
    public UpdateStreamResult updateStream(UpdateStreamRequest updateStreamRequest)
    {
        return null;
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
	public void updateCredentials(DISCredentials credentials)
	{
		super.innerUpdateCredentials(credentials);
	}
    
    @Override
    public Future<CreateTransferTaskResult> createTransferTaskAsync(CreateTransferTaskRequest createTransferTaskRequest) {
        return createTransferTaskAsync(createTransferTaskRequest, null);
    }
    
    @Override
    public Future<CreateTransferTaskResult> createTransferTaskAsync(CreateTransferTaskRequest createTransferTaskRequest,
        AsyncHandler<CreateTransferTaskResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.POST, disConfig.getManagerEndpoint(),
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, createTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, null))
                .build());
        
        return requestAsync(createTransferTaskRequest, request, CreateTransferTaskResult.class, asyncHandler);
    }
    
    @Override
    public Future<UpdateTransferTaskResult> updateTransferTaskAsync(UpdateTransferTaskRequest updateTransferTaskRequest) {
        return updateTransferTaskAsync(updateTransferTaskRequest, null);
    }
    
    @Override
    public Future<UpdateTransferTaskResult> updateTransferTaskAsync(UpdateTransferTaskRequest updateTransferTaskRequest,
        AsyncHandler<UpdateTransferTaskResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.POST, disConfig.getManagerEndpoint(),
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, updateTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, null))
                .build());
        
        return requestAsync(updateTransferTaskRequest, request, UpdateTransferTaskResult.class, asyncHandler);
    }
    
    @Override
    public Future<DeleteTransferTaskResult> deleteTransferTaskAsync(DeleteTransferTaskRequest deleteTransferTaskRequest) {
        return deleteTransferTaskAsync(deleteTransferTaskRequest, null);
    }
    
    @Override
    public Future<DeleteTransferTaskResult> deleteTransferTaskAsync(DeleteTransferTaskRequest deleteTransferTaskRequest,
        AsyncHandler<DeleteTransferTaskResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.DELETE, disConfig.getManagerEndpoint(),
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, deleteTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, deleteTransferTaskRequest.getTransferTaskName()))
                .build());
        
        return requestAsync(deleteTransferTaskRequest, request, DeleteTransferTaskResult.class, asyncHandler);
    }
    
    @Override
    public Future<ListTransferTasksResult> listTransferTasksAsync(ListTransferTasksRquest listTransferTasksRequest) {
        return listTransferTasksAsync(listTransferTasksRequest, null);
    }
    
    @Override
    public Future<ListTransferTasksResult> listTransferTasksAsync(ListTransferTasksRquest listTransferTasksRequest,
        AsyncHandler<ListTransferTasksResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, listTransferTasksRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, null))
                .build());
        
        return requestAsync(listTransferTasksRequest, request, ListTransferTasksResult.class, asyncHandler);
    }
    
    
    @Override
    public Future<DescribeTransferTaskResult> describeTransferTaskAsync(DescribeTransferTaskRequest describeTransferTaskRequest) {
        return describeTransferTaskAsync(describeTransferTaskRequest, null);
    }
    
    @Override
    public Future<DescribeTransferTaskResult> describeTransferTaskAsync(DescribeTransferTaskRequest describeTransferTaskRequest,
        AsyncHandler<DescribeTransferTaskResult> asyncHandler) {
        Request<HttpRequest> request = buildRequest(HttpMethodName.GET, disConfig.getManagerEndpoint(),
            ResourcePathBuilder.standard()
                .withProjectId(disConfig.getProjectId())
                .withResource(new StreamResource(null, describeTransferTaskRequest.getStreamName()))
                .withResource(new TransferTaskResource(null, describeTransferTaskRequest.getTransferTaskName()))
                .build());
        
        return requestAsync(describeTransferTaskRequest, request, DescribeTransferTaskResult.class, asyncHandler);
    }

	@Override
	public BatchTransferTaskResult batchTransferTask(BatchTransferTaskRequest batchTransferTaskRequest) {
		return null;
	}
    
}
