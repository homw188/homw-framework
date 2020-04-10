package com.homw.tool.application.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.common.util.FileUtil;
import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.ITableUpdateService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 数据表更新应用
 * @author Hom
 * @version 1.0
 * @since 2019-07-18
 */
@Controller
@Application("tableUpdateApp")
public class TableUpdateApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length != 5) {
			throw new IllegalArgumentException("args must four items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", args[1]);
		params.put("idColumn", args[2]);
		params.put("updateColumn", args[3]);

		String fileName = args[4];
		File file = new File(fileName);
		// 检查文件是否存在
		if (!file.exists()) {
			throw new IllegalArgumentException("import file [" + fileName + "] not existed.");
		}

		// 导入文件格式校验
		if (!FileUtil.checkExtension(fileName, "xls", "xlsx")) {
			throw new IllegalArgumentException("import file [" + fileName + "] illegal excel format, not supported.");
		}
		params.put("fileName", fileName);
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " tableName idColumn updateColumn importFile");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String tableName = params.get("tableName").toString();
		String fileName = params.get("fileName").toString();
		String idColumn = params.get("idColumn").toString();
		String updateColumn = params.get("updateColumn").toString();

		ITableUpdateService updateService = (ITableUpdateService) SpringContextUtil.getBean("tableUpdateService");
		updateService.update2Database(tableName, idColumn, updateColumn, fileName);
	}
}