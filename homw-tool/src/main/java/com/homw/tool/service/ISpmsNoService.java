package com.homw.tool.service;

/**
 * spmsNo服务
 * 
 * @author System
 * @email 
 * @date 2019-05-20 15:13:52
 */
public interface ISpmsNoService {
	/**
	 * 导入spms至数据库
	 * @param tableName
	 * @param idColumn
	 * @param fileName
	 * @throws Exception
	 */
	void import2Database(String tableName, String idColumn, String fileName) throws Exception;
}
