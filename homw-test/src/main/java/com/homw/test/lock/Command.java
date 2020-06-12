package com.homw.test.lock;

/**
 * 命令接口
 * 
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 *
 * @param <T>
 */
public interface Command<T> {
	/**
	 * 执行命令
	 * 
	 * @return
	 */
	public T exec();
}
