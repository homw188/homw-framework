package com.homw.schedule.dao;

import java.util.List;

import com.homw.dao.support.BaseDao;
import com.homw.schedule.entity.SysUserRoleEntity;

/**
 * @description 用户与角色对应关系
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
public interface SysUserRoleDao extends BaseDao<SysUserRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);
}
