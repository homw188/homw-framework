package com.homw.test.websocket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.homw.test.shiro.UserInfoEntity;
import com.homw.test.shiro.util.ShiroUtil;
import com.homw.test.websocket.bean.WebSocketStatus;

/**
 * @description websocket连接的拦截器有两种方式：
 * 一种是实现接口HandshakeInterceptor，实现beforeHandshake和afterHandshake函数；
 * 一种是继承HttpSessionHandshakeInterceptor，重载beforeHandshake和afterHandshake函数
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class WebSocketInterceptor implements HandshakeInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(WebSocketInterceptor.class);
	public static final String WEBSOCKET_STATUS = "_websocket_status";
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attrs) throws Exception {
    	UserInfoEntity user = ShiroUtil.getUserEntity();
		if (user != null) {
            // 使用已登录的user区分WebSocketHandler，以便定向发送消息
			WebSocketStatus status = new WebSocketStatus();
			status.setUserId(user.getUserId());
			status.setUserName(user.getUsername());
			attrs.put(WEBSOCKET_STATUS, status);
			
			/**
		    * 默认为false，要修改为true，否则无法建立websocket连接
		    * WebSocket connection to 'ws://..' failed: 
		    * Error during WebSocket handshake: Unexpected response code: 200
		    */
			return true;
    	}
		log.info("用户未登录，websocket连接建立失败");
    	return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}
	
}