package com.huaweicloud.dis.http;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import com.huaweicloud.dis.core.handler.AsyncHandler;

public class HttpFutureCallbackAdapter<T> implements FutureCallback<HttpResponse>{

	private AsyncHandler<T> asyncHandler;
	private HttpFutureAdapter<T> httpFutureAdapter;
	
	private ResponseExtractor<T> responseExtractor;
	private ResponseErrorHandler errorHandler;
	
	public HttpFutureCallbackAdapter(AsyncHandler<T> asyncHandler, ResponseExtractor<T> responseExtractor, HttpFutureAdapter<T> httpFutureAdapter, ResponseErrorHandler errorHandler) {
		this.asyncHandler = asyncHandler;
		this.responseExtractor = responseExtractor;
		this.httpFutureAdapter = httpFutureAdapter;
		this.errorHandler = errorHandler;
	}
	
	@Override
	public void completed(HttpResponse result) {
		T t = null;
		try {
			if (errorHandler.hasError(result))
	        {
	            errorHandler.handleError(result);
	        }
			
			if(httpFutureAdapter != null) {
				t = httpFutureAdapter.getT(result);
			}else {
				t = responseExtractor.extractData(result);
			}
		}catch(Exception e) {
			failed(e);
			return;
		}
		
		asyncHandler.onSuccess(t);
	}

	@Override
	public void failed(Exception ex) {
		asyncHandler.onError(ex);
	}

	@Override
	public void cancelled() {
		throw new UnsupportedOperationException();
	}
	
}
