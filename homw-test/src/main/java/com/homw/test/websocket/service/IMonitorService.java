package com.homw.test.websocket.service;

import com.homw.test.websocket.bean.MonitorEvent;

/**
 * @description 监控接口
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public interface IMonitorService {
	/**
	 * 刷新统计数据
	 * @param event
	 */
	void refreshStat(MonitorEvent event);
	
	/**
	 * 刷新状态
	 * @param event
	 */
	void refreshStatus(MonitorEvent event);
	
	/**
	 * 刷新配置
	 * @param event
	 */
	void refreshConfig(MonitorEvent event);
}