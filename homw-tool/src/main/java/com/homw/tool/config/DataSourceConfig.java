package com.homw.tool.config;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.homw.tool.annotation.DataSourceCondition;

@Configuration
@Conditional(DataSourceCondition.class)
@ImportResource("classpath:spring-jdbc.xml")
public class DataSourceConfig {
}
