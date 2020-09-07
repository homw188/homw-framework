package com.homw.test.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description 插件
 * @author Hom
 * @version 1.0
 * @since 2020-09-07
 */
public class Plugin implements InvocationHandler {

	private Object target;
	private Interceptor interceptor;

	public Plugin(Object target, Interceptor interceptor) {
		this.target = target;
		this.interceptor = interceptor;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return interceptor.process(new Invocation(target, method, args));
	}

	/**
	 * 通过拦截器包装代理对象
	 * @param target
	 * @param interceptor
	 * @return
	 */
	public static Object wrap(Object target, Interceptor interceptor) {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
				new Plugin(target, interceptor));
	}
}
