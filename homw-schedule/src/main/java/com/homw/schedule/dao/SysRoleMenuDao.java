package com.homw.schedule.dao;

import java.util.List;

import com.homw.dao.support.BaseDao;
import com.homw.schedule.entity.SysRoleMenuEntity;

/**
 * @description 角色与菜单对应关系
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
public interface SysRoleMenuDao extends BaseDao<SysRoleMenuEntity> {
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long roleId);
}
