package com.homw.test.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.homw.test.websocket.bean.WebSocketStatus;

/**
 * @description 用户登录后建立websocket连接，默认选择websocket连接； 如果浏览器不支持，则使用sockjs进行模拟连接。
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class WebSocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
	private static ConcurrentHashMap<String, WebSocketSession> sessionCache = new ConcurrentHashMap<>();

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 */
	public void sendMessage(TextMessage message) {
		log.debug("开始发送消息给在线用户，message:" + message);
		String sessionId = null;
		WebSocketSession session = null;
		for (Entry<String, WebSocketSession> entry : sessionCache.entrySet()) {
			sessionId = entry.getKey();
			session = entry.getValue();
			if (session != null) {
				sendMessageInternal(message, entry.getKey(), session);
			} else {
				log.info("发送消息失败，session为空，sessionId:" + sessionId + "，message:" + message);
				removeSessionCache(sessionId);
			}
		}
	}

	/**
	 * 给某个用户发送消息
	 * 
	 * @param message
	 * @param sessionId
	 */
	public void sendMessage(TextMessage message, String sessionId) {
		log.debug("开始发送消息，userId: " + sessionId + ", message: " + message);
		WebSocketSession session = sessionCache.get(sessionId);
		if (session != null) {
			sendMessageInternal(message, sessionId, session);
		} else {
			log.info("发送消息失败，未找到该用户，userId:" + sessionId + "，message:" + message);
		}
	}

	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);

		String payload = message.getPayload();
		Map<String, Long> paramMap = JSONObject.parseObject(payload, new TypeReference<Map<String, Long>>() {});
		if (paramMap == null) {
			log.info("消息格式不正确，message:" + message);
			return;
		}

		WebSocketStatus status = getSessionStatus(session);
		if (status != null) {
			Long bizId = paramMap.get("bizId");
			if (bizId != null) {
				status.setBizId(bizId);
				log.debug("session状态更新成功，bizId:" + bizId);
			}
		} else {
			log.info("该session状态不存在");
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);

		log.debug("websocket连接建立成功， 用户名：" + getUserName(session));
		sessionCache.put(session.getId(), session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		log.info("消息传输异常，连接关闭，用户名：" + getUserName(session));

		if (throwable != null) {
			log.warn(throwable.getMessage());
		}
		removeSessionCache(session);
		super.handleTransportError(session, throwable);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("websocket连接关闭");
		removeSessionCache(session);
		super.afterConnectionClosed(session, status);
	}

	/**
	 * 获取该session对应的userId
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Long getUserId(WebSocketSession session) {
		Object obj = session.getAttributes().get(WebSocketInterceptor.WEBSOCKET_STATUS);
		if (obj != null) {
			WebSocketStatus status = (WebSocketStatus) obj;
			return status.getUserId();
		}
		return null;
	}

	/**
	 * 获取该session对应的userName
	 * 
	 * @param session
	 * @return
	 */
	private static String getUserName(WebSocketSession session) {
		Object obj = session.getAttributes().get(WebSocketInterceptor.WEBSOCKET_STATUS);
		if (obj != null) {
			WebSocketStatus status = (WebSocketStatus) obj;
			return status.getUserName();
		}
		return null;
	}

	/**
	 * 获取session状态
	 * 
	 * @param session
	 * @return
	 */
	public static WebSocketStatus getSessionStatus(WebSocketSession session) {
		Object obj = session.getAttributes().get(WebSocketInterceptor.WEBSOCKET_STATUS);
		if (obj != null) {
			return (WebSocketStatus) obj;
		}
		return null;
	}

	/**
	 * 删除session缓存，释放连接
	 * 
	 * @param session
	 */
	public static void removeSessionCache(WebSocketSession session) {
		if (session != null) {
			disconnect(session);
			sessionCache.remove(session.getId());
		}
	}

	/**
	 * 删除session缓存，释放连接
	 * 
	 * @param sessionId
	 */
	public static void removeSessionCache(String sessionId) {
		WebSocketSession session = sessionCache.get(sessionId);
		if (session != null) {
			disconnect(session);
		}
		sessionCache.remove(sessionId);
	}

	/**
	 * 断开连接
	 * 
	 * @param session
	 */
	private static void disconnect(WebSocketSession session) {
		try {
			if (session.isOpen()) {
				session.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送消息，内部使用
	 * 
	 * @param message
	 * @param sessionId
	 * @param session
	 */
	private void sendMessageInternal(TextMessage message, String sessionId, WebSocketSession session) {
		try {
			if (session.isOpen()) {
				/**
				 * Resolve java.lang.IllegalStateException: The remote ENDPOINT was IN state
				 * [TEXT_PARTIAL_WRITING] which IS an invalid state FOR called method.
				 */
				synchronized (session) {
					session.sendMessage(message);
				}
			} else {
				log.info("发送消息失败，连接已关闭，sessionId:" + sessionId + "，message:" + message);
				removeSessionCache(session);
			}
		} catch (IOException e) {
			log.warn("发送消息异常，sessionId:" + sessionId + "，message:" + message);
			log.warn(e.getMessage());
		}
	}

	/**
	 * 获取session缓存
	 * 
	 * @return
	 */
	public static Map<String, WebSocketSession> getSessionCache() {
		return Collections.unmodifiableMap(sessionCache);
	}
}