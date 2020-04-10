package com.homw.tool.service;

import java.util.List;
import java.util.Map;

/**
 * @description 代码生成器
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public interface ICodeGenerateService {
	List<Map<String, Object>> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	Map<String, String> queryTable(String tableName);

	List<Map<String, String>> queryColumns(String tableName);

	byte[] generatorCode(String[] tableNames);
}
