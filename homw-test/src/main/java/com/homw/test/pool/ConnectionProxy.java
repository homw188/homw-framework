package com.homw.test.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @description 数据库连接代理，接管（释放连接到池）close方法
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public class ConnectionProxy implements InvocationHandler {
	/**
	 * 代理对象
	 */
	private Connection target;

	@SuppressWarnings("rawtypes")
	protected static Class[] proxyInter = new Class[] { Connection.class };

	public ConnectionProxy(Connection target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ("close".equals(method.getName())) {
			// TODO
			// release to connection pool.
			return null;
		} else {
			if ("equals".equals(method.getName())) {
				args[0] = target;
			}
			try {
				return method.invoke(target, args);
			} catch (InvocationTargetException e) {
				throw e.getCause();
			}
		}
	}

	/**
	 * 获取动态代理对象
	 * 
	 * @param conn
	 * @return
	 */
	public static Connection getProxy(Connection conn) {
		return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), proxyInter,
				new ConnectionProxy(conn));
	}

	/**
	 * 获取代理对象
	 * 
	 * @return
	 */
	public Connection getTarget() {
		return target;
	}

}
