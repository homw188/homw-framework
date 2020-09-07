package com.homw.test.plugin;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @description 拦截器链
 * @author Hom
 * @version 1.0
 * @since 2020-09-07
 */
public class InterceptorChain {

	private List<Interceptor> chain = Lists.newArrayList();

	public void add(Interceptor interceptor) {
		chain.add(interceptor);
	}

	/**
	 * 组装所有拦截器
	 * @param target
	 * @return
	 */
	public Object pluginAll(Object target) {
		for (Interceptor interceptor : chain) {
			target = interceptor.plugin(target);
		}
		return target;
	}

	public List<Interceptor> getChain() {
		return Collections.unmodifiableList(chain);
	}
}
