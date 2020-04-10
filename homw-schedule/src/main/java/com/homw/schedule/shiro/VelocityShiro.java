package com.homw.schedule.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @description Shiro权限标签(Velocity版)
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public class VelocityShiro {

	/**
	 * 是否拥有该权限
	 * @param permission 权限标识
	 * @return true：是 false：否
	 */
	public boolean hasPermission(String permission) {
		Subject subject = SecurityUtils.getSubject();
		return subject != null && subject.isPermitted(permission);
	}
}
