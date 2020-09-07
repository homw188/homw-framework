package com.homw.test.plugin;

import org.junit.Test;

/**
 * @description 插件测试
 * @author Hom
 * @version 1.0
 * @since 2020-09-07
 */
public class PluginTest {
	
	@Test
	public void test() {
		// InterceptorB->InterceptorA->TargetA
		InterceptorChain chain = new InterceptorChain();
		Interceptor interceptor = new InterceptorA();
		chain.add(interceptor);
		interceptor = new InterceptorB();
		chain.add(interceptor);
		
		Target target = new TargetA();
		target = (Target) chain.pluginAll(target);
		
		String ret = target.exec("do something...");
		System.out.println(ret);
	}
	
	class InterceptorA implements Interceptor {

		@Override
		public Object process(Invocation invocation) throws Exception {
			System.out.println("PluginTest.InterceptorA.process()");
			return invocation.invoke();
		}

		@Override
		public Object plugin(Object target) {
			return Plugin.wrap(target, this);
		}
	}
	
	class InterceptorB implements Interceptor {

		@Override
		public Object process(Invocation invocation) throws Exception {
			System.out.println("PluginTest.InterceptorB.process()");
			return invocation.invoke();
		}

		@Override
		public Object plugin(Object target) {
			return Plugin.wrap(target, this);
		}
	}
	
	interface Target {
		String exec(String cmd);
	}
	
	class TargetA implements Target {
		public String exec(String cmd) {
			return cmd;
		}
	}
}
