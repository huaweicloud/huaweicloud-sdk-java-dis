package com.huaweicloud.dis;

import java.util.concurrent.Future;

import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.huaweicloud.dis.iface.data.request.*;
import com.huaweicloud.dis.iface.data.response.*;
import org.apache.http.HttpRequest;

import com.huaweicloud.dis.DISConfig.BodySerializeType;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.restresource.AppsResource;
import com.huaweicloud.dis.core.restresource.CheckPointResource;
import com.huaweicloud.dis.core.restresource.CursorResource;
import com.huaweicloud.dis.core.restresource.RecordResource;
import com.huaweicloud.dis.core.restresource.ResourcePathBuilder;
import com.huaweicloud.dis.core.restresource.StreamResource;
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

public class DISClientAsync2 extends AbstractDISClientAsync implements DISAsync{
	
	/**
     * 构造异步DIS客户端
     * 
     * @param disConfig DIS客户端参数
     * @param executorService Instance of {@code ExecutorService}, default thread pool size is 50.
     * @see com.huaweicloud.dis.core.builder.ExecutorFactory
     */
    public DISClientAsync2(DISConfig disConfig)
    {
        super(disConfig);
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

	@Override
	public Future<PutFilesResult> putFilesAsync(PutFilesRequest putFilesRequest,
			AsyncHandler<PutFilesResult> asyncHandler) {
		
		// TODO Auto-generated method stub
		return null;
	}

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
}
