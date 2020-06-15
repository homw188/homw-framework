package com.homw.tool.application.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.ICodeGenService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 代码生成器应用
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
@Controller
@Application("codeGenApp")
public class CodeGenApp extends AbstractApplication {

	@Override
	protected void configArgs(Options options) {
		options.addOption(Option.builder("t").longOpt("tables").hasArg().required()
				.desc("table names, separated by comma").build());
		options.addOption(Option.builder("f").longOpt("file").hasArg().required().desc("save file path").build());
	}

	@Override
	protected void validateArgs(CommandLine params) {
		String fileName = params.getOptionValue("f");
		fileName += ".zip";// 默认zip格式压缩
		File file = new File(fileName);
		// 检查文件是否存在
		if (file.exists()) {
			throw new IllegalArgumentException("output file [" + fileName + "] already existed.");
		}
	}

	@Override
	protected void execute(CommandLine params) throws Exception {
		String[] tableNames = params.getOptionValues("t");
		String fileName = params.getOptionValue("f");
		fileName += ".zip";// 默认zip格式压缩

		// 生成代码
		ICodeGenService codeGenerateService = (ICodeGenService) SpringContextUtil.getBean("codeGenerateService");
		byte[] data = codeGenerateService.generatorCode(tableNames);

		// 存储代码压缩包
		IOUtils.write(data, new FileOutputStream(fileName));
	}
}