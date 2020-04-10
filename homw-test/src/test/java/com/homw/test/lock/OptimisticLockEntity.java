package com.homw.test.lock;

/**
 * 基于版本号机制的乐观锁实体
 * @author Hom
 * @version 1.0
 * @date 2018年8月8日
 */
public class OptimisticLockEntity
{
	private int lockId;
	private String lockName;
	private int lockVersion;
	
	/**
	 * 提交前的版本号
	 */
	private int preVersion;
	
	public int getLockId() {
		return lockId;
	}
	public void setLockId(int lockId) {
		this.lockId = lockId;
	}
	public String getLockName() {
		return lockName;
	}
	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	public int getLockVersion() {
		return lockVersion;
	}
	public void setLockVersion(int lockVersion) {
		this.lockVersion = lockVersion;
	}
	public int getPreVersion() {
		return preVersion;
	}
	public void setPreVersion(int preVersion) {
		this.preVersion = preVersion;
	}
	
	@Override
	public String toString()
	{
		return "OptimisticLockEntity[lockId:" + lockId + ", lockName:" + lockName + ", lockVersion:" + lockVersion + "]";
	}
	
}
