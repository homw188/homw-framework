package com.homw.transport.netty.session;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homw.transport.netty.ResultFuture;
import com.homw.transport.netty.message.Message;

/**
 * @description 会话管理器
 * @author Hom
 * @version 1.0
 * @since 2020-05-17
 */
public class SessionManager {

	private static SessionManager instance = new SessionManager();
	private Map<String, Session> sessionCache = Maps.newConcurrentMap();
	
	public static SessionManager getInstance() {
		return instance;
	}
	
	public void addSession(Session session) {
		if (session != null) {
			sessionCache.put(session.getSessionId(), session);
		}
	}
	
	public void removeSession(String sessionId) {
		sessionCache.remove(sessionId);
	}
	
	public List<ResultFuture<Message>> broadcast(Object data) {
		List<ResultFuture<Message>> futureList = Lists.newArrayList();
		for (Session session : sessionCache.values()) {
			ResultFuture<Message> future = session.send(data);
			futureList.add(future);
		}
		return futureList;
	}
	
	public Session getSession(String sessionId) {
		return sessionCache.get(sessionId);
	}
	
	public Map<String, Session> getSessionMap() {
		return Collections.unmodifiableMap(sessionCache);
	}
}
