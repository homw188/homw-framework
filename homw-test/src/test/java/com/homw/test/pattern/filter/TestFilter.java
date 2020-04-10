package com.homw.test.pattern.filter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 装饰器结合责任链模式测试
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月2日
 *
 */
public class TestFilter {
	@Test
	public void test() throws Exception {
		final Filter filter = new MutableFilter(null);
		// 保证过滤器链顺序执行
		final Filter mutexfilter = new MutexFilter(filter);

		final Context<Message> ctx = new MessageContext(new Message(0, "initial message"));

		int taskCount = 100;
		final CountDownLatch exitLatch = new CountDownLatch(taskCount);
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < taskCount; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// filter.action(ctx);
						mutexfilter.action(ctx);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						exitLatch.countDown();
					}
				}
			});
		}

		exitLatch.await();
		executor.shutdown();
	}
}
