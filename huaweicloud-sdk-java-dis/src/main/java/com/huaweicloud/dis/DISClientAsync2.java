package com.huaweicloud.dis;

import java.util.concurrent.Future;

import org.apache.http.HttpRequest;

import com.huaweicloud.dis.DISConfig.BodySerializeType;
import com.huaweicloud.dis.core.Request;
import com.huaweicloud.dis.core.handler.AsyncHandler;
import com.huaweicloud.dis.core.http.HttpMethodName;
import com.huaweicloud.dis.core.restresource.RecordResource;
import com.huaweicloud.dis.core.restresource.ResourcePathBuilder;
import com.huaweicloud.dis.http.AbstractCallbackAdapter;
import com.huaweicloud.dis.http.AbstractDISClient;
import com.huaweicloud.dis.http.AbstractFutureAdapter;
import com.huaweicloud.dis.iface.api.protobuf.ProtobufUtils;
import com.huaweicloud.dis.iface.data.request.GetRecordsRequest;
import com.huaweicloud.dis.iface.data.response.GetRecordsResult;

public class DISClientAsync2 extends AbstractDISClient{
	
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
    	public GetRecordsFuture() {
		}

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
}
