package com.homw.schedule.service;

import java.util.List;
import java.util.Map;

import com.homw.schedule.entity.SysRoleEntity;

/**
 * @description 角色
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
public interface SysRoleService {
	
	SysRoleEntity queryObject(Long roleId);
	
	List<SysRoleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysRoleEntity role);
	
	void update(SysRoleEntity role);
	
	void deleteBatch(Long[] roleIds);
}
