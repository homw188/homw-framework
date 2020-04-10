package com.homw.test.websocket.bean;

import java.io.Serializable;

/**
 * @description 监控日志
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class MonitorEventLogEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	// 主键
	private Long id;
	// 业务id
	private Long bizId;
	// 会话状态(用户)
	private Long userId;
	// 事件源
	private String source;
	// 事件类型
	private String event;
	// 执行结果
	private String action;
	// 备注
	private String remark;
	// 事件触发时间
	private Long createTime;

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return source;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEvent() {
		return event;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAction() {
		return action;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getCreateTime() {
		return createTime;
	}
}
