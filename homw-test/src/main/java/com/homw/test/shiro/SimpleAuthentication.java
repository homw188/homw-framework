package com.homw.test.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 基于shiro的简单身份认证
 * @author wang.hong
 * @version 1.0
 * @date 2018年7月16日
 *
 */
public class SimpleAuthentication
{
	public static void main(String[] args) 
	{
		// 1.获取SecurityManager工厂，此处用ini配置初始化
		Factory<SecurityManager> factory = 
				new IniSecurityManagerFactory("classpath:conf/shiro.ini");
		
		// 2.获取SecurtyManager实例，并绑定到SecurityUtils
		SecurityManager securityManager = factory.getInstance();
		System.out.println(securityManager);
		SecurityUtils.setSecurityManager(securityManager);
		
		// 3.获取Subject，并创建基于用户名和密码的token（用户身份/凭证）
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("admin", "123");
		
		// 4.登录，即身份认证
		subject.login(token);
		
		// 打印认证状态
		System.out.println("login status: " + subject.isAuthenticated());
		
		// 5.退出
		subject.logout();
	}
}
