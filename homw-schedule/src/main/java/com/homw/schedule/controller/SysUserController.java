package com.homw.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homw.common.bean.SystemMessage;
import com.homw.schedule.entity.SysUserEntity;
import com.homw.schedule.service.SysUserService;
import com.homw.schedule.shiro.ShiroUtil;
import com.homw.schedule.shiro.UserRealm;

/**
 * @description 系统用户
 * @author Hom
 * @version 1.0
 * @since 2020-04-01
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;

	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public SystemMessage list(Integer page, Integer limit, String username, String mobile, Integer status,
			String realname, Integer type) {
		Map<String, Object> map = new HashMap<>();
		// map.put("offset", (page - 1) * limit);
		// map.put("limit", limit);

		map.put("username", username);
		map.put("mobile", mobile);
		map.put("status", status);
		map.put("realname", realname);
		map.put("type", type);

		List<Long> roleIds = getSubRoleList();
		if (roleIds != null) {
			map.put("roleIds", roleIds);
		}

		if (getUserId() != UserRealm.ADMIN_ID) {
			map.put("userId", getUserId());
		}

		// 查询列表数据
		List<SysUserEntity> userList = sysUserService.queryList(map);
		// int total = sysUserService.queryTotal(map);
		// PageUtils pageUtil = new PageUtils(userList, total, limit, page);

		return SystemMessage.ok().put("list", userList);
	}

	@RequestMapping("/info")
	public SystemMessage info() {
		SysUserEntity user = getUser();
		if (user != null) {
			SysUserEntity temp = sysUserService.queryObject(user.getUserId());
			user.setRoleName(temp.getRoleName());
		}
		return SystemMessage.ok().put("user", user);
	}

	@RequestMapping("/password")
	public SystemMessage password(@RequestBody SysUserEntity user) {
		String password = new Sha256Hash(user.getPassword()).toHex();
		SysUserEntity dbUser = sysUserService.queryObject(getUserId());
		if (!dbUser.getPassword().equals(password)) {
			return SystemMessage.error("原密码不正确");
		}

		if (!StringUtils.isBlank(user.getNewPassword())) {
			String newPassword = new Sha256Hash(user.getNewPassword()).toHex();
			int count = sysUserService.updatePassword(getUserId(), password, newPassword);
			if (count == 0) {
				return SystemMessage.error("原密码不正确");
			}
			ShiroUtil.logout();
		}
		return SystemMessage.ok();
	}

	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public SystemMessage info(@PathVariable("userId") Long userId) {
		SysUserEntity user = sysUserService.queryObject(userId);
		// 获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		// user.setRoleIdList(roleIdList);
		if (roleIdList != null && roleIdList.size() > 0) {
			user.setRoleId(roleIdList.get(0));
		}
		return SystemMessage.ok().put("user", user);
	}

	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public SystemMessage save(@RequestBody SysUserEntity user) {
		if (StringUtils.isBlank(user.getUsername())) {
			return SystemMessage.error("用户名不能为空");
		}

		if (StringUtils.isBlank(user.getPassword())) {
			return SystemMessage.error("密码不能为空");
		}

		SysUserEntity dbUser = sysUserService.queryByUserName(user.getUsername());
		if (dbUser != null) {
			return SystemMessage.error("用户名重复，请重新输入");
		}
		sysUserService.save(user);
		return SystemMessage.ok();
	}

	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public SystemMessage update(@RequestBody SysUserEntity user) {
		if (StringUtils.isBlank(user.getUsername())) {
			return SystemMessage.error("用户名不能为空");
		}
		sysUserService.update(user);
		return SystemMessage.ok();
	}

	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public SystemMessage delete(@RequestBody Long[] userIds) {
		if (ArrayUtils.contains(userIds, 1L)) {
			return SystemMessage.error("系统管理员不能删除");
		}

		if (ArrayUtils.contains(userIds, getUserId())) {
			return SystemMessage.error("当前用户不能删除");
		}
		sysUserService.deleteBatch(userIds);
		return SystemMessage.ok();
	}
}
