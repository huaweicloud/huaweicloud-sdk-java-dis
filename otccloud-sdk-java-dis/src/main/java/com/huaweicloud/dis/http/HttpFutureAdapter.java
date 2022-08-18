package com.otccloud.dis.http;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;

import com.otccloud.dis.exception.DISClientException;

public class HttpFutureAdapter<T> extends AbstractFutureAdapter<T, HttpResponse> implements Future<T> {
	private ResponseExtractor<T> responseExtractor;
	private ResponseErrorHandler errorHandler;
	
	public HttpFutureAdapter(ResponseExtractor<T> responseExtractor, ResponseErrorHandler errorHandler) {
		this.responseExtractor = responseExtractor;
		this.errorHandler = errorHandler;
	}
	

	@Override
	protected T toT(HttpResponse innerT) {
		try {
			if (errorHandler.hasError(innerT))
	        {
	            errorHandler.handleError(innerT);
	        }
	        
	        if (responseExtractor != null)
	        {
	            return responseExtractor.extractData(innerT);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			throw new DISClientException(e);
		}
	}

}
