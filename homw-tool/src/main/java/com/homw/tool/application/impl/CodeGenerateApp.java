package com.homw.tool.application.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.ICodeGenerateService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 代码生成器
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
@Controller
@Application("codeGenApp")
public class CodeGenerateApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length < 3) {
			throw new IllegalArgumentException("args must not null, and at least three items.");
		}
		Map<String, Object> params = new HashMap<>();

		int len = args.length;
		String fileName = args[len - 1] + ".zip";// 默认zip格式压缩
		File file = new File(fileName);
		// 检查文件是否存在
		if (file.exists()) {
			throw new IllegalArgumentException("output file [" + fileName + "] already existed.");
		}
		params.put("fileName", fileName);

		// 提取数据表参数
		String[] tableNames = new String[len - 2];
		System.arraycopy(args, 1, tableNames, 0, len - 2);
		params.put("tableNames", tableNames);
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " table1[ table2 talbe3...] saveAs");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String[] tableNames = (String[]) params.get("tableNames");
		String fileName = params.get("fileName").toString();

		// 生成代码
		ICodeGenerateService codeGenerateService = (ICodeGenerateService) SpringContextUtil
				.getBean("codeGenerateService");
		byte[] data = codeGenerateService.generatorCode(tableNames);

		// 存储代码压缩包
		IOUtils.write(data, new FileOutputStream(fileName));
	}
}