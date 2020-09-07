package com.homw.test.plugin;

/**
 * @description 拦截器
 * @author Hom
 * @version 1.0
 * @since 2020-09-07
 */
public interface Interceptor {

	/**
	 * 拦截器业务处理
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	Object process(Invocation invocation) throws Exception;

	/**
	 * 将拦截器组装到目标对象
	 * @param target
	 * @return
	 */
	Object plugin(Object target);
}
