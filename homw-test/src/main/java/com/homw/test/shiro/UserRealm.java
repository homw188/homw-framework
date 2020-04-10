package com.homw.test.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @description 用户登录认证、授权
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class UserRealm extends AuthorizingRealm {

	public static final int SUPER_ADMIN_ID = 1; // 超级管理员
	public static final int ADMIN_ID = 2; // 系统管理员

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		UserInfoEntity user = (UserInfoEntity) principals.getPrimaryPrincipal();
		Long userId = user.getUserId();

		List<String> permsList = null;
		if (userId == SUPER_ADMIN_ID) {
			// TODO 实际需查找所以菜单权限
			permsList = new ArrayList<>(2);
			permsList.add("sys:user:query");
			permsList.add("sys:user:add");
			permsList.add("sys:user:delete");
		} else {
			// TODO 实际需根据用户id查找权限
			permsList = new ArrayList<>(1);
			permsList.add("sys:user:query");
		}

		// 用户权限列表
		Set<String> permsSet = new HashSet<String>();
		for (String perms : permsList) {
			if (StringUtils.isBlank(perms)) {
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	@Override
	@SuppressWarnings("unused")
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		// TODO 实际需根据用户名查找用户信息
		UserInfoEntity user = new UserInfoEntity(1001L, username, username);

		// 登录认证
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}
		if (!password.equals(user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}
		return new SimpleAuthenticationInfo(user, password, getName());
	}

}
