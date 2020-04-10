package com.homw.test.pool;

/**
 * @description 对象池接口
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public interface ObjectPool<T> {
	/**
	 * 获取
	 * 
	 * @return
	 */
	public T take();

	/**
	 * 获取
	 * 
	 * @param params 构造参数
	 * @return
	 */
	public T take(Object[] params);

	/**
	 * 释放
	 * 
	 * @param obj
	 */
	public void release(T obj);

	/**
	 * 销毁
	 */
	public void destroy();
}
