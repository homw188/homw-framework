package com.homw.test.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class SimpleRealm implements Realm 
{
	@Override
	public String getName() 
	{
		return "Simple Realm";
	}

	@Override
	public boolean supports(AuthenticationToken token) 
	{
		return token instanceof UsernamePasswordToken;
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) 
			throws AuthenticationException 
	{
		System.out.println("enter getAuthenticationInfo method.");
		
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());
		
		if (!"admin".equals(username))
		{
			throw new UnknownAccountException();
		}
		
		if (!"123".equals(password))
		{
			throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(username, password, getName());
	}

}
