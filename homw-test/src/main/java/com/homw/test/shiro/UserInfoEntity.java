package com.homw.test.shiro;

import java.io.Serializable;

public class UserInfoEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String username;
	private transient String password;
	
	public UserInfoEntity(Long userId, String username, String password) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
}
