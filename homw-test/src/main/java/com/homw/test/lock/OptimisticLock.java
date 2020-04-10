package com.homw.test.lock;

/**
 * 乐观锁接口
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 *
 * @param <T>
 */
public interface OptimisticLock<T>
{
	/**
	 * 查询数据
	 * @return
	 */
	public T get();
	
	/**
	 * 更新状态
	 * @param t
	 */
	public void update(T t);
	
	/**
	 * 提交数据
	 * @param t
	 * @return
	 */
	public int commit(T t);
	
	/**
	 * 移除缓存
	 */
	public void removeCache(T t);
}
