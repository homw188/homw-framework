package com.homw.schedule.service;

import java.util.List;

/**
 * @description 角色与菜单对应关系
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
public interface SysRoleMenuService {
	
	void saveOrUpdate(Long roleId, List<Long> menuIdList);
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long roleId);
	
}
