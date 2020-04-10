package com.homw.test.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.test.pattern.command.Command;

/**
 * 乐观锁命令实现
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 *
 * @param <T>
 */
public class OptimisticLockCommand<T> implements Command<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(OptimisticLockCommand.class);
	
	private static int repeat = 10; // 自旋重试次数
	private OptimisticLock<T> lock;
	
	public OptimisticLockCommand(OptimisticLock<T> lock) {
		this.lock = lock;
	}
	
	@Override
	public T exec() {
		int i = 0;
		while (i < repeat) {
			try {
				T t = lock.get();
				if (t == null) {
					return null;
				}
				lock.update(t);
				
				int num = lock.commit(t);
				if (num > 0) {
					lock.removeCache(t);
					return t;
				}
			} catch (Exception e) {
				logger.error("optimistic lock exception:", e);
			}
			i++;
		}
		return null;
	}
	
}
