package com.homw.tool.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author psiot
 * @email psiot@psiot.com.cn
 * @date 2019-04-16 10:44:07
 */
public class LightlampsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// 主键
	private Long id;
	//灯具名称
	private String lampsName;
	//灯具图标 
	private String lampsIcon;
	//灯具目标显示图片
	private String lampsViewicon;
	//上传时间
	@JSONField(serialize=false)
	private Date uptime;
	//是否启用
	private String isValid;
	
	private String lampsType;
	public String getLampsType() {
		return lampsType;
	}
	public void setLampsType(String lampsType) {
		this.lampsType = lampsType;
	}
	
	private String lampsKey;
	public String getLampsKey() {
		return lampsKey;
	}
	public void setLampsKey(String lampsKey) {
		this.lampsKey = lampsKey;
	}
	
	private String isVisible;
	public String getIsVisible() {
		return isVisible;
	}
	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * 设置： 主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取： 主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：灯具名称
	 */
	public void setLampsName(String lampsName) {
		this.lampsName = lampsName;
	}
	/**
	 * 获取：灯具名称
	 */
	public String getLampsName() {
		return lampsName;
	}
	/**
	 * 设置：灯具图标 
	 */
	public void setLampsIcon(String lampsIcon) {
		this.lampsIcon = lampsIcon;
	}
	/**
	 * 获取：灯具图标 
	 */
	public String getLampsIcon() {
		return lampsIcon;
	}
	/**
	 * 设置：上传时间
	 */
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	/**
	 * 获取：上传时间
	 */
	public Date getUptime() {
		return uptime;
	}
	/**
	 * 设置：是否启用
	 */
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	/**
	 * 获取：是否启用
	 */
	public String getIsValid() {
		return isValid;
	}
	public String getLampsViewicon() {
		return lampsViewicon;
	}
	public void setLampsViewicon(String lampsViewicon) {
		this.lampsViewicon = lampsViewicon;
	}
	
}
