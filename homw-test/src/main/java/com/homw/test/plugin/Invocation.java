package com.homw.test.plugin;

import java.lang.reflect.Method;

/**
 * @description 目标调用对象参数封装
 * @author Hom
 * @version 1.0
 * @since 2020-09-07
 */
public class Invocation {

	private Object target;
	private Method method;
	private Object[] args;

	public Invocation() {
	}

	public Invocation(Object target, Method method, Object[] args) {
		this.target = target;
		this.method = method;
		this.args = args;
	}

	/**
	 * 目标对象方法调用
	 * @return
	 * @throws Exception
	 */
	public Object invoke() throws Exception {
		return method.invoke(target, args);
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
