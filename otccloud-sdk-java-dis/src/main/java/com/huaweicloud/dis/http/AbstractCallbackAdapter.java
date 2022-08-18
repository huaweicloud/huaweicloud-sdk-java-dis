package com.otccloud.dis.http;

import com.otccloud.dis.core.handler.AsyncHandler;

public abstract class AbstractCallbackAdapter<InnerT, T> implements AsyncHandler<InnerT>{
	private AsyncHandler<T> innerAsyncHandler;
	
	protected AbstractFutureAdapter<T, InnerT> futureAdapter;
	
	public AbstractCallbackAdapter(AsyncHandler<T> innerAsyncHandler) {
		this.innerAsyncHandler = innerAsyncHandler;
	}

	public AbstractCallbackAdapter(AsyncHandler<T> innerAsyncHandler, AbstractFutureAdapter<T, InnerT> futureAdapter) {
		this.innerAsyncHandler = innerAsyncHandler;
		this.futureAdapter = futureAdapter;
	}
	
	@Override
	public void onError(Exception exception) throws Exception {
		innerAsyncHandler.onError(exception);
	}

	@Override
	public void onSuccess(InnerT result) throws Exception {
		T t = null;
		try {
			if(futureAdapter != null) {
				t = futureAdapter.getT(result);
			}else {
				t = toInnerT(result);
			}
		}catch(Exception e) {
			this.onError(e);
			return;
		}
		
		innerAsyncHandler.onSuccess(t);	
	}

	protected abstract T toInnerT(InnerT result);
	
}
