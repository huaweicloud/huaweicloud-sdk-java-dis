package com.huaweicloud.dis.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import com.huaweicloud.dis.core.handler.AsyncHandler;

public class HttpFutureCallbackAdapter<T> implements FutureCallback<HttpResponse>{

	private AsyncHandler<T> asyncHandler;
	private ResponseExtractor<T> responseExtractor;
	private HttpFutureAdapter<T> httpFutureAdapter;
	
	public HttpFutureCallbackAdapter(AsyncHandler<T> asyncHandler, ResponseExtractor<T> responseExtractor, HttpFutureAdapter<T> httpFutureAdapter) {
		this.asyncHandler = asyncHandler;
		this.responseExtractor = responseExtractor;
		this.httpFutureAdapter = httpFutureAdapter;
	}
	
	@Override
	public void completed(HttpResponse result) {
		T t = null;
		if(httpFutureAdapter != null) {
			t = httpFutureAdapter.getT(result);
		}else {
			try {
				t = responseExtractor.extractData(result);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
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
