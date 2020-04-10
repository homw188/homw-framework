package com.homw.tool.service;

/**
 * 数据表更新服务
 * 
 * @author System
 * @email 
 * @date 2019-07-18 10:19:52
 */
public interface ITableUpdateService {
	/**
	 * 更新updateColumn至数据库
	 * @param tableName
	 * @param idColumn
	 * @param updateColumn
	 * @param fileName
	 * @throws Exception
	 */
	void update2Database(String tableName, String idColumn, String updateColumn, String fileName) throws Exception;
}
