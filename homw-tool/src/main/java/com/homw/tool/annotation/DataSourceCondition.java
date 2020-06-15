package com.homw.tool.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.homw.tool.application.Application;
import com.homw.tool.application.ApplicationFactory;

/**
 * @description 判断是否导入数据源配置
 * @author Hom
 * @version 1.0
 * @since 2020-06-15
 */
public class DataSourceCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Application app = ApplicationFactory.getActive();
		com.homw.tool.annotation.Application appAnnotaion = app.getClass()
				.getAnnotation(com.homw.tool.annotation.Application.class);
		return appAnnotaion.importDataSource();
	}
}
