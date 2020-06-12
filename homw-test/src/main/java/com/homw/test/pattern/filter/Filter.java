package com.homw.test.pattern.filter;

/**
 * @description 过滤器接口
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public interface Filter {
	void action(Context<?> context) throws Exception;
}
