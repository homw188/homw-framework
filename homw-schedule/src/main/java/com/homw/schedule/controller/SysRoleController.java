package com.homw.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homw.common.bean.SystemMessage;
import com.homw.schedule.entity.SysRoleEntity;
import com.homw.schedule.service.SysRoleMenuService;
import com.homw.schedule.service.SysRoleService;
import com.homw.web.support.bean.PageBean;

/**
 * @description 角色管理
 * @author Hom
 * @version 1.0
 * @since 2020-04-01
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public SystemMessage list(Integer page, Integer limit) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);

		// 查询列表数据
		List<SysRoleEntity> list = sysRoleService.queryList(map);
		int total = sysRoleService.queryTotal(map);
		PageBean pageBean = new PageBean(list, total, limit, page);

		return SystemMessage.ok().put("page", pageBean);
	}

	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public SystemMessage select() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> roleIds = getSubRoleList();
		if (roleIds != null) {
			map.put("roleIds", roleIds);
		}
		// 查询列表数据
		List<SysRoleEntity> list = sysRoleService.queryList(map);
		return SystemMessage.ok().put("list", list);
	}

	@RequestMapping("/select/tree")
	@RequiresPermissions("sys:role:select")
	public SystemMessage selectTree() {
		// 查询列表数据
		List<SysRoleEntity> roleList = sysRoleService.queryList(new HashMap<String, Object>());

		// 添加顶级菜单
		SysRoleEntity root = new SysRoleEntity();
		root.setRoleId(0L);
		root.setRoleName("一级角色");
		root.setParentId(-1L);
		root.setOpen(true);
		roleList.add(root);

		return SystemMessage.ok().put("roleList", roleList);
	}

	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public SystemMessage info(@PathVariable("roleId") Long roleId) {
		SysRoleEntity role = sysRoleService.queryObject(roleId);

		// 查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		return SystemMessage.ok().put("role", role);
	}

	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public SystemMessage save(@RequestBody SysRoleEntity role) {
		if (StringUtils.isBlank(role.getRoleName())) {
			return SystemMessage.error("角色名称不能为空");
		}
		sysRoleService.save(role);
		return SystemMessage.ok();
	}

	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public SystemMessage update(@RequestBody SysRoleEntity role) {
		if (StringUtils.isBlank(role.getRoleName())) {
			return SystemMessage.error("角色名称不能为空");
		}
		sysRoleService.update(role);
		return SystemMessage.ok();
	}

	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public SystemMessage delete(@RequestBody Long[] roleIds) {
		sysRoleService.deleteBatch(roleIds);
		return SystemMessage.ok();
	}
}
