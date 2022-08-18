package com.otccloud.dis.http;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFutureAdapter<T, InnerT> implements Future<T>{
	
	protected volatile Future<InnerT> innerFuture;
	
	protected T finalT;
	
	public AbstractFutureAdapter() {
	}
	
	public AbstractFutureAdapter(Future<InnerT> innerFuture) {
		this.innerFuture = innerFuture;
	}
	
	public void setInnerFuture(Future<InnerT> innerFuture) {
		this.innerFuture = innerFuture;
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

	@Override
	public T get() throws InterruptedException, ExecutionException {
		if(finalT != null) {
			return finalT;
		}
		InnerT innerT = innerFuture.get();
		return getT(innerT);
	}

	public synchronized T getT(InnerT innerT) {
		if(finalT == null) {
			finalT = toT(innerT);
		}
		
		return finalT;
	}
	
	protected abstract T toT(InnerT innerT);
	
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if(finalT != null) {
			return finalT;
		}
		InnerT innerT = innerFuture.get(timeout, unit);
		return getT(innerT);
		
	}
	
}
