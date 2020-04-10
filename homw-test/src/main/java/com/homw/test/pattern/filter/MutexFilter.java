package com.homw.test.pattern.filter;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @description 线程安全的过滤器，保证顺序执行
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class MutexFilter extends AbstractFilter {
	private final ReentrantLock lock = new ReentrantLock();

	public MutexFilter(Filter next) {
		super(next);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void action(Context context) throws Exception {
		try {
			lock.lock();
			System.out.println(Thread.currentThread().getName() + "#before:" + context.get());
			super.action(context);
			System.out.println(Thread.currentThread().getName() + "#after:" + context.get());
		} finally {
			lock.unlock();
		}
	}

}
