package com.homw.test.websocket.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;
import com.homw.common.bean.SystemMessage;
import com.homw.test.websocket.WebSocketHandler;
import com.homw.test.websocket.bean.MonitorEvent;
import com.homw.test.websocket.bean.MonitorEventLogEntity;
import com.homw.test.websocket.bean.WebSocketStatus;
import com.homw.test.websocket.service.IMonitorService;

/**
 * @description 监控服务实现
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Service("monitorService")
public class MonitorServiceImpl implements IMonitorService {

	private static final Logger log = LoggerFactory.getLogger(MonitorServiceImpl.class);
	private ExecutorService executor = Executors.newCachedThreadPool();

	@Override
	public void refreshStat(MonitorEvent event) {
		new AbstractMonitorHandler() {
			@Override
			void process(WebSocketSession session, WebSocketStatus status, Long bizId) {
				// TODO 需封装业务数据
				SystemMessage message = SystemMessage.ok();
				message.put("refreshType", "stat");
				sendMessage(session, message);
			}

			@Override
			boolean beforeHandle(MonitorEvent event) {
				return true;
			}
		}.handle(event);
	}

	@Override
	public void refreshStatus(MonitorEvent event) {
		new AbstractMonitorHandler() {
			@Override
			void process(WebSocketSession session, WebSocketStatus status, Long bizId) {
				// TODO 需封装业务数据
				SystemMessage message = SystemMessage.ok();
				message.put("refreshType", "status");
				sendMessage(session, message);

				message.clear();

				// TODO 需封装业务数据
				message = SystemMessage.error();
				message.put("refreshType", "alarm");
				sendMessage(session, message);
			}

			@Override
			boolean beforeHandle(MonitorEvent event) {
				try {
					// save(alarm);
				} catch (Exception e1) {
					MonitorEventLogEntity eventLog = new MonitorEventLogEntity();
					setBasicEventLogStatus(eventLog, event, null);
					saveEventLog(eventLog, "ignore", "alarm save fail, " + e1.toString());
					return false;
				}
				return true;
			}
		}.handle(event);
	}

	@Override
	public void refreshConfig(MonitorEvent event) {
		new AbstractMonitorHandler() {
			@Override
			void process(WebSocketSession session, WebSocketStatus status, Long bizId) {
				// TODO 需封装业务数据
				SystemMessage message = SystemMessage.ok();
				message.put("refreshType", "config");
				sendMessage(session, message);
			}

			@Override
			boolean beforeHandle(MonitorEvent event) {
				return true;
			}
		}.handle(event);
	}

	/**
	 * 存储事件日志
	 *
	 * @param log    日志
	 * @param action 执行结果（eg. success, ignore, fail...）
	 * @param remark 备注
	 */
	private void saveEventLog(MonitorEventLogEntity log, String action, String remark) {
		if (StringUtils.isEmpty(action)) {
			action = "empty";
		}
		log.setAction(action);

		if (!StringUtils.isEmpty(remark)) {
			if (remark.length() >= 500) {
				remark = remark.substring(0, 490) + "...";
			}
			log.setRemark(remark);
		}
		// TODO save(log);
	}

	/**
	 * 当会话缓存为空时处理
	 *
	 * @param event
	 */
	private void onSessionCacheEmpty(MonitorEvent event) {
		MonitorEventLogEntity eventLog = new MonitorEventLogEntity();
		setBasicEventLogStatus(eventLog, event, null);
		saveEventLog(eventLog, "ignore", "session cache is empty");
	}

	/**
	 * 设置日志的基本状态
	 *
	 * @param log     日志
	 * @param event   事件
	 * @param session 会话状态
	 */
	private void setBasicEventLogStatus(MonitorEventLogEntity log, MonitorEvent event, WebSocketStatus session) {
		if (session != null) {
			log.setBizId(session.getBizId());
			log.setUserId(session.getUserId());
		}
		log.setCreateTime(event.getTimestamp());
		log.setEvent(event.getMonitorType().name());

		Object obj = event.getSource();
		String source = null;
		if (obj != null) {
			if (obj instanceof String) {
				source = obj.toString();
			} else {
				source = obj.getClass().getName();
			}

			if (source.length() >= 100) {
				source = source.substring(0, 90) + "...";
			}
			log.setSource(source);
		}
	}

	/**
	 * 发送消息
	 *
	 * @param session 会话
	 * @param message 消息
	 */
	private void sendMessage(WebSocketSession session, SystemMessage message) {
		getSocketHandler().sendMessage(new TextMessage(JSONObject.toJSONString(message)), session.getId());
	}

	/**
	 * 异常处理
	 *
	 * @param session  会话
	 * @param ex
	 * @param eventLog 事件日志
	 */
	private void onException(WebSocketSession session, Exception ex, MonitorEventLogEntity eventLog) {
		WebSocketStatus status = WebSocketHandler.getSessionStatus(session);
		try {
			sendMessage(session, SystemMessage.error(ex.toString()));
		} finally {
			log.error("消息推送异常, status: " + status, ex);
			saveEventLog(eventLog, "fail", ex.toString());
		}
	}

	/**
	 * 检查会话状态
	 *
	 * @param session 当前会话
	 * @throws IllegalStateException 状态异常
	 */
	private void checkSessionState(WebSocketSession session) throws IllegalStateException {
		WebSocketStatus status = WebSocketHandler.getSessionStatus(session);
		Long lotId = status.getBizId();
		if (lotId == null) {
			WebSocketHandler.removeSessionCache(session);
			throw new IllegalStateException("当前会话未绑定业务");
		}
	}

	@Bean
	WebSocketHandler getSocketHandler() {
		return new WebSocketHandler();
	}

	abstract class AbstractMonitorHandler {
		/**
		 * 监控处理
		 * 
		 * @param event
		 */
		void handle(final MonitorEvent event) {
			// 预处理
			if (!beforeHandle(event))
				return;

			Map<String, WebSocketSession> sessionCache = WebSocketHandler.getSessionCache();
			if (sessionCache.isEmpty()) {
				onSessionCacheEmpty(event);
				return;
			}

			for (final Entry<String, WebSocketSession> entry : sessionCache.entrySet()) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						long startTimeMillis = System.currentTimeMillis();

						WebSocketSession session = entry.getValue();
						WebSocketStatus status = WebSocketHandler.getSessionStatus(session);

						MonitorEventLogEntity eventLog = new MonitorEventLogEntity();
						setBasicEventLogStatus(eventLog, event, status);

						if (status != null) {
							try {
								checkSessionState(session);

								Long bizId = status.getBizId();
								if (event.getBizId() != null && !event.getBizId().equals(bizId)) {
									saveEventLog(eventLog, "ignore", "biz not match, " + event.getBizId());
									return;
								}

								// 业务处理
								process(session, status, bizId);

								long handleTime = System.currentTimeMillis() - startTimeMillis;
								saveEventLog(eventLog, "success", "handleTime:" + handleTime + "ms");
							} catch (Exception e) {
								onException(session, e, eventLog);
							}
						} else {
							saveEventLog(eventLog, "ignore", "not found session status");
						}
					}
				});
			}
		}

		/**
		 * 预处理
		 * 
		 * @param event
		 * @return
		 */
		abstract boolean beforeHandle(MonitorEvent event);

		/**
		 * 业务处理，如发送消息
		 * 
		 * @param session
		 * @param status  会话状态
		 * @param bizId   业务id
		 */
		abstract void process(WebSocketSession session, WebSocketStatus status, Long bizId);
	}
}
