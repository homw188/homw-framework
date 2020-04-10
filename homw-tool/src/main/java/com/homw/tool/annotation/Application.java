package com.homw.tool.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 应用注解
 * @author Hom
 * @version 1.0
 * @since 2020-03-19
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Application {
	/**
	 * 应用名称，用于注册Key
	 * @return
	 */
	String value() default "defaultApp";
}
