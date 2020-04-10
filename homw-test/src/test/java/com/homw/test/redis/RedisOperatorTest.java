package com.homw.test.redis;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homw.test.bean.Constants;
import com.homw.test.lock.OptimisticLockEntity;
import com.homw.test.lock.dao.OptimisticLockDao;

/**
 * @description redis执行器测试
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class RedisOperatorTest implements RedisOperator<OptimisticLockEntity> {
	
	private int lockId = 1;
	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;
	private OptimisticLockDao optimisticLockDao;
	
	@SuppressWarnings("rawtypes")
	public RedisOperatorTest(RedisTemplate redisTemplate, OptimisticLockDao optimisticLockDao) {
		this.redisTemplate = redisTemplate;
		this.optimisticLockDao = optimisticLockDao;
	}
	
	@Override
	public OptimisticLockEntity getFromDB() {
		return optimisticLockDao.getById(lockId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public OptimisticLockEntity getFromRedis() {
		HashOperations operations = redisTemplate.opsForHash();
		Map map = operations.entries(getRedisKey());
		if (map == null || map.size() == 0) {
			return null;
		}
		return new ObjectMapper().convertValue(map, OptimisticLockEntity.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void putInRedis(OptimisticLockEntity t) {
		@SuppressWarnings("rawtypes")
		HashOperations operations = redisTemplate.opsForHash();
		Map<String, String> map = new ObjectMapper().convertValue(t, new TypeReference<Map<String, String>>() {});
		operations.putAll(getRedisKey(), map);
	}

	@Override
	public String getRedisKey() {
		return Constants.REDIS_OPTIMISTIC_LOCK_KEY + lockId;
	}

}
