package com.homw.test.redis;

/**
 * redis操作接口
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 *
 * @param <T>
 */
public interface RedisOperator<T> 
{
	/**
	 * 从数据库查询
	 * @return
	 */
	public T getFromDB();
	
	/**
	 * 从redis缓存查询
	 * @return
	 */
	public T getFromRedis();
	
	/**
	 * 生成redis缓存
	 * @param t
	 */
	public void putInRedis(T t);
	
	/**
	 * 获取redis缓存key
	 * @return
	 */
	public String getRedisKey();
}
