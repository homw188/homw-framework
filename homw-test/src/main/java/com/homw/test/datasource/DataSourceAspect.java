package com.homw.test.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @description 数据源自动切换
 * @author Hom
 * @version 1.0
 * @since 2020-03-19
 */
@Aspect
@Component
public class DataSourceAspect {

	@Pointcut("execution(* com.homw.test.*.service. .*.*(..))")
	public void changeDataSourcePoint() {}
	
	/**
	 * 进入业务层方法之前执行数据源切换
	 * @param point
	 */
	@Before("changeDataSourcePoint()")
	public void before(JoinPoint point) {
		String methodName = point.getSignature().getName();
		if (isSlave(methodName)) {
			DynamicDataSourceHolder.chageSlave();
		} else {
			DynamicDataSourceHolder.changeMaster();
		}
	}
	
	/**
	 * 判断是否从库
	 * @param methodName
	 * @return
	 */
	private boolean isSlave(String methodName) {
		return StringUtils.startsWithAny(methodName, "query", "find", "get");
	}
}
