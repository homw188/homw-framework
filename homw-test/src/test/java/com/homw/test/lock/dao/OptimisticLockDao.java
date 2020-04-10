package com.homw.test.lock.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.homw.test.lock.OptimisticLockEntity;

/**
 * 乐观锁表访问接口
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 */
@Repository
public interface OptimisticLockDao
{
	/**
	 * 查询列表
	 * @return
	 */
	public List<OptimisticLockEntity> list();
	
	/**
	 * 根据id查询
	 * @param lockId
	 * @return
	 */
	public OptimisticLockEntity getById(int lockId);
	
	/**
	 * 通过版本号更新
	 * @param lock
	 * @return
	 */
	public int updateByVersion(OptimisticLockEntity lock);
	
	/**
	 * 插入
	 * @param lock
	 * @return
	 */
	public int insert(OptimisticLockEntity lock);
}
