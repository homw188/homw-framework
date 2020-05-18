package com.homw.transport.netty.session;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description 结果异步等待
 * @author Hom
 * @version 1.0
 * @since 2020-05-17
 */
public class ResultFuture<T> implements java.util.concurrent.Future<T> {

	private T result;
	private CountDownLatch latch = new CountDownLatch(1);

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return result != null;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		latch.await();
		return result;
	}

	@Override
	public T get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (latch.await(timeout, unit)) {
			return result;
		}
		return null;
	}
	
	public void set(T result) {
		this.result = result;
		latch.countDown();
	}

}
