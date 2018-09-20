package com.huaweicloud.dis.http;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;

public class HttpFutureAdapter<T> extends AbstractFutureAdapter<T, HttpResponse> implements Future<T> {
	private ResponseExtractor<T> responseExtractor;
	
	public HttpFutureAdapter(ResponseExtractor<T> responseExtractor) {
		this.responseExtractor = responseExtractor;
	}
	

	@Override
	protected T toT(HttpResponse innerT) {
		try {
			return responseExtractor.extractData(innerT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
