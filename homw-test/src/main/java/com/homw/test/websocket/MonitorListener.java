package com.homw.test.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.homw.test.websocket.bean.MonitorEvent;
import com.homw.test.websocket.bean.MonitorType;
import com.homw.test.websocket.service.IMonitorService;

/**
 * @description 监控监听器
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Component
public class MonitorListener implements ApplicationListener<MonitorEvent> {
	@Autowired
	private IMonitorService monitorService;
	
	@Override
	public void onApplicationEvent(MonitorEvent event) {
		if (event.getMonitorType() == MonitorType.STAT) {
			monitorService.refreshStat(event);
		} else if (event.getMonitorType() == MonitorType.ALARM
				|| event.getMonitorType() == MonitorType.STATUS) {
			monitorService.refreshStatus(event);
		} else if (event.getMonitorType() == MonitorType.CONFIG) {
			monitorService.refreshConfig(event);
		} 
	}
}