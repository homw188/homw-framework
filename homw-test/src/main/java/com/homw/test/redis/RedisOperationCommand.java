package com.homw.test.redis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.homw.test.bean.Constants;
import com.homw.test.lock.Command;

/**
 * redis操作命令实现
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 *
 * @param <T>
 */
public class RedisOperationCommand<T> implements Command<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisOperationCommand.class);
	
	private RedisOperator<T> operator;
	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;
	
	public RedisOperationCommand(RedisOperator<T> operator, 
			@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		this.operator = operator;
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T exec() {
		T t = null;
		try {
			t = operator.getFromRedis();
			if (t == null) {
				// 避免缓存穿透
				@SuppressWarnings("rawtypes")
				ValueOperations operations = redisTemplate.opsForValue();
				String emptyKey = Constants.REDIS_EMPTY_KEY + operator.getRedisKey();
				Object emptyVal = operations.get(emptyKey);
				if (emptyVal == null) {
					t = operator.getFromDB();
					if (t == null) {
						if (operations.setIfAbsent(emptyKey, Constants.REDIS_NULL_VALUE)) {
							redisTemplate.expire(emptyKey, Constants.REDIS_TIME_OUT, TimeUnit.SECONDS);
						}
					} else {
						operator.putInRedis(t);
					}
				}
			}
		} catch (Exception e) {
			logger.error("redis operation exception:", e);
		}
		return t;
	}

}
