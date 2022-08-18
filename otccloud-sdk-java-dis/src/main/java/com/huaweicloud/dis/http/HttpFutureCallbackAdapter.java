package com.otccloud.dis.http;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import com.otccloud.dis.core.handler.AsyncHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpFutureCallbackAdapter<T> implements FutureCallback<HttpResponse>{

	private static final Logger LOG = LoggerFactory.getLogger(HttpFutureCallbackAdapter.class);

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

		try {
			asyncHandler.onSuccess(t);
		} catch (Exception e) {
			failed(e);
		}
	}

	@Override
	public void failed(Exception ex) {
		try {
			asyncHandler.onError(ex);
		} catch (Exception e) {
			// 无需将异常抛出
			LOG.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void cancelled() {
		throw new UnsupportedOperationException();
	}
	
}
