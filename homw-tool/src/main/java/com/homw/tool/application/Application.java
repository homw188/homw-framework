package com.homw.tool.application;

import com.homw.tool.exception.ApplicationException;

/**
 * @description 应用接口
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public interface Application {
	/**
	 * 启动
	 * 
	 * @param args 输入参数
	 * @throws Exception
	 */
	void start(String[] args) throws ApplicationException;
}
