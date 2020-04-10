package com.homw.common.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 对象创建工厂
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class ObjectFactory {
	private static final Logger logger = LoggerFactory.getLogger(ObjectFactory.class);

	/**
	 * 创建对象
	 * 
	 * @param clazz
	 * @param params 如果null，将无参构造对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T getInstance(Class<? extends T> clazz, Object[] params) {
		try {
			if (params != null && params.length > 0) {
				int i = 0;
				Class[] types = new Class[params.length];
				for (Object o : params) {
					types[i] = o.getClass();
					i++;
				}

				try {
					Constructor<? extends T> c = clazz.getConstructor(types);
					return c.newInstance(params);
				} catch (NoSuchMethodException e) {
					logger.warn("[对象工厂]未找到 " + clazz.getName() + " 的构造方法，参数列表：" + Arrays.asList(types));
				}
			}
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}