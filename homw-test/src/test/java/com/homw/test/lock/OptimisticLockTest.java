package com.homw.test.lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.homw.test.bean.Constants;
import com.homw.test.lock.dao.OptimisticLockDao;
import com.homw.test.redis.RedisOperationCommand;
import com.homw.test.redis.RedisOperatorTest;
import com.homw.web.support.util.SpringContextUtil;

/**
 * 乐观锁测试
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class OptimisticLockTest {
	
	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;
	private OptimisticLockDao optimisticLockDao;
	
	@SuppressWarnings("rawtypes")
	public OptimisticLockTest() {
		redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate");
		optimisticLockDao = (OptimisticLockDao) SpringContextUtil.getBean("optimisticLockDao");
	}
	
	@Test
	public void test() {
		// 模拟并发修改数据
		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep((long) (Math.random() * 100));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Command<OptimisticLockEntity> command = new OptimisticLockCommand<OptimisticLockEntity>(
							new OptimisticLockForTest());
					// 修改数据，基于版本的乐观锁实现
					// exec返回null，则提交失败
					OptimisticLockEntity optimisticLock = command.exec();
					System.out.println("invoke success: " + optimisticLock);
				}
			}.start();
		}
		// wait
		while(true){}
	}
	
	class OptimisticLockForTest implements OptimisticLock<OptimisticLockEntity> {
		private Command<OptimisticLockEntity> redisCommand;
		
		public OptimisticLockForTest() {
			this.redisCommand = new RedisOperationCommand<OptimisticLockEntity>(
					new RedisOperatorTest(redisTemplate, optimisticLockDao), redisTemplate);
		}
		
		@Override
		public void update(OptimisticLockEntity t) {
			// 修改数据
			t.setLockName("lock-" + Thread.currentThread().getName());
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public void removeCache(OptimisticLockEntity t) {
			redisTemplate.delete(Constants.REDIS_OPTIMISTIC_LOCK_KEY + t.getLockId());
		}
		
		@Override
		public OptimisticLockEntity get() {
			return redisCommand.exec();
		}
		
		@Override
		public int commit(OptimisticLockEntity t) {
			t.setPreVersion(t.getLockVersion());
			// 版本号递增
			t.setLockVersion(t.getLockVersion() + 1);
			return optimisticLockDao.updateByVersion(t);
		}
	}
	
}
