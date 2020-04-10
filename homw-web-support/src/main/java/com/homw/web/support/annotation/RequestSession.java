package com.homw.web.support.annotation;

import java.lang.annotation.*;

/**
 * @description 获取session参数注解
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestSession {
}
