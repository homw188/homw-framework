package com.homw.schedule.service;

import java.util.List;

/**
 * @description 用户与角色对应关系
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
public interface SysUserRoleService {
	
	void saveOrUpdate(Long userId, List<Long> roleIdList);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);
	
	void delete(Long userId);
}
