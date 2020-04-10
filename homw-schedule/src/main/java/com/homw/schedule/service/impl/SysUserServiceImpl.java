package com.homw.schedule.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homw.schedule.dao.SysUserDao;
import com.homw.schedule.entity.SysUserEntity;
import com.homw.schedule.service.SysUserRoleService;
import com.homw.schedule.service.SysUserService;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return sysUserDao.queryByUserName(username);
	}

	@Override
	public SysUserEntity queryObject(Long userId) {
		return sysUserDao.queryObject(userId);
	}

	@Override
	public List<SysUserEntity> queryList(Map<String, Object> map) {
		return sysUserDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		// sha256加密
		user.setPassword(new Sha256Hash(user.getPassword()).toHex());
		sysUserDao.save(user);

		List<Long> roleIdList = user.getRoleIdList();
		if (roleIdList == null || roleIdList.size() == 0) {
			roleIdList = new ArrayList<>();
			roleIdList.add(user.getRoleId());
		}
		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), roleIdList);
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword(null);
		} else {
			user.setPassword(new Sha256Hash(user.getPassword()).toHex());
		}
		sysUserDao.update(user);

		List<Long> roleIdList = user.getRoleIdList();
		if (roleIdList == null || roleIdList.size() == 0) {
			if (user.getRoleId() != null) {
				roleIdList = new ArrayList<>();
				roleIdList.add(user.getRoleId());
			}
		}
		// 保存用户与角色关系
		if (roleIdList != null && roleIdList.size() > 0) {
			sysUserRoleService.saveOrUpdate(user.getUserId(), roleIdList);
		}
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}
}
