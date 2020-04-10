package com.homw.schedule.dao;

import java.util.List;

import com.homw.dao.support.BaseDao;
import com.homw.schedule.entity.SysMenuEntity;

/**
 * @description 菜单管理
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
public interface SysMenuDao extends BaseDao<SysMenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
}
