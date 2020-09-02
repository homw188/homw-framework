package com.homw.test.pattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description <b>结构型模式：</b>代理模式
 * <p>通过代理以实现对目标对象的<b>访问控制</b></p>
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-06-11
 * 
 * @see AdapterPattern
 */
public class ProxyPattern {

	public static void main(String[] args) {
		final Subject target = new SubjectA();
		// 静态代理
		StaticProxy staticProxy = new StaticProxy(target);
		staticProxy.operation();
		
		System.out.println("===============================");

		// 动态代理
		Subject dynamicProxy = (Subject) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { Subject.class }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Object result = method.invoke(target, args);
						System.out.println("ProxyPattern.main(...).new InvocationHandler() {...}.invoke()");
						return result;
					}
				});
		dynamicProxy.operation();
	}

	interface Subject {
		void operation();
	}

	static class SubjectA implements Subject {
		@Override
		public void operation() {
			System.out.println("ProxyPattern.SubjectA.operation()");
		}
	}

	static class StaticProxy implements Subject {
		Subject target;

		public StaticProxy(Subject target) {
			this.target = target;
		}

		@Override
		public void operation() {
			if (target != null) {
				target.operation();
			}
			System.out.println("ProxyPattern.Proxy.operation()");
		}
	}

}
