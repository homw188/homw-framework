package com.homw.test.pattern.filter;

/**
 * @description 执行环境
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public interface Context<T> {
	T get();

	void set(T ctx);
}
