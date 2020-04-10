package com.homw.test.websocket.bean;

import org.springframework.context.ApplicationEvent;

/**
 * @description 监控事件
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class MonitorEvent extends ApplicationEvent
{
	private static final long serialVersionUID = 1L;
	
	private MonitorType monitorType;
	private Long bizId;
	
	public MonitorEvent(Object source, MonitorType monitorType) {
		super(source);
		this.monitorType = monitorType;
	}
	
	public MonitorEvent(Object source, MonitorType monitorType, Long bizId) {
		super(source);
		this.monitorType = monitorType;
		this.bizId = bizId;
	}
	
	public MonitorType getMonitorType() {
		return monitorType;
	}

	public Long getBizId() {
		return bizId;
	}
	
}