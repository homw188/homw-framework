package com.homw.dao.support.util;

import java.util.Collections;

import org.apache.commons.lang3.Validate;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

/**
 * @description 基于redis的分布式锁实现
 * @author Hom
 * @version 1.0
 * @since 2020-04-17
 */
public class RedisLockUtil {

	// NX -> SET IF NOT EXIST，key不存在，则执行set操作；key已存在，则不执行
	private static final String SET_IF_NOT_EXIST = "NX";
	// PX 需要给key加过期设置，具体时间由第五个参数决定
	private static final String SET_WITH_EXPIRE_TIME = "PX";

	private static final String LOCK_SUCCESS = "OK";
	private static final Long RELEASE_SUCCESS = 1L;

	private static final int MAX_RETRY_NUM = 3;
	private static final long RETRY_INTERVAL = 3000L;
	
	private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then "
			+ "return redis.call('del', KEYS[1]) else return 0 end";

	/**
	 * 尝试获取分布式锁
	 * 
	 * @param lockName   锁名称
	 * @param requestId  请求标识
	 * @param expireTime 超期时间
	 * @return
	 */
	public static boolean tryLock(String lockName, String requestId, int expireTime) throws InterruptedException {
		Validate.notBlank(lockName, "lockName param must not blank");
		Validate.notBlank(requestId, "requestId param must not blank");

		Jedis jedis = null;
		boolean locked = false;
		int tryNum = MAX_RETRY_NUM;
		try {
			jedis = JedisUtil.getResource();
			while (tryNum > 0) {
				String result = jedis.set(lockName, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
				if (LOCK_SUCCESS.equals(result)) {
					locked = true;
					return true;
				} else {
					Thread.sleep(RETRY_INTERVAL);
				}
				tryNum--;
			}
		} finally {
			if (locked || (!locked && tryNum == 0)) {
				JedisUtil.returnResource(jedis);
			}
		}
		return false;
	}

	/**
	 * 释放分布式锁
	 * 
	 * @param lockName  锁名称
	 * @param requestId 请求标识
	 * @return
	 */
	public static boolean releaseLock(String lockName, String requestId) {
		Validate.notBlank(lockName, "lockName param must not blank");
		Validate.notBlank(requestId, "requestId param must not blank");

		Object result = JedisUtil.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockName),
				Collections.singletonList(requestId));
		if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查分布式锁的持有情况
	 * 
	 * @param lockName  锁名称
	 * @param requestId 请求标识
	 * @return
	 */
	public boolean checkLockHold(String lockName, String requestId) {
		Validate.notBlank(lockName, "lockName param must not blank");
		Validate.notBlank(requestId, "requestId param must not blank");

		String result = JedisUtil.get(lockName);
		if (!StringUtils.isEmpty(result) && result.equals(requestId)) {
			return true;
		}
		return false;
	}

}