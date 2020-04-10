package com.homw.tool.application.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.common.util.FileUtil;
import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.IDeviceInfoService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 设备导入应用
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
@Controller
@Application("deviceImportApp")
public class DeviceImportApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length != 3) {
			throw new IllegalArgumentException("args must three items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", args[1]);

		String fileName = args[2];
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
		logger.error("Usage:\t" + args[0] + " tableName importFile");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String tableName = params.get("tableName").toString();
		String fileName = params.get("fileName").toString();

		IDeviceInfoService deviceInfoService = (IDeviceInfoService) SpringContextUtil.getBean("deviceInfoService");
		deviceInfoService.import2Database(tableName, fileName);
	}

}
