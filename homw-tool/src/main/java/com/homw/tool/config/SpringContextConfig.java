package com.homw.tool.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.homw.tool", useDefaultFilters = true)
public class SpringContextConfig {
}
