package com.homw.tool.application.impl;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Controller;

import com.homw.common.util.FileUtil;
import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.ISpmsNoService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description spmsNo导入应用
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 * 
 * @see {@link TableUpdateApp}
 */
@Controller
@Deprecated
@Application("spmsNoImportApp")
public class SpmsNoImportApp extends AbstractApplication {
	
	@Override
	protected void configArgs(Options options) {
		options.addOption(Option.builder("t").longOpt("table").hasArg().required().desc("data table name").build());
		options.addOption(Option.builder("k").longOpt("key-column").hasArg().required().desc("primary key column").build());
		options.addOption(Option.builder("f").longOpt("file").hasArg().required().desc("import file path").build());
	}
	
	@Override
	protected void validateArgs(CommandLine params) {
		String fileName = params.getOptionValue("f");
		File file = new File(fileName);
		// 检查文件是否存在
		if (!file.exists()) {
			throw new IllegalArgumentException("import file [" + fileName + "] not existed.");
		}

		// 导入文件格式校验
		if (!FileUtil.checkExtension(fileName, "xls", "xlsx")) {
			throw new IllegalArgumentException("import file [" + fileName + "] illegal excel format, not supported.");
		}
	}
	
	@Override
	protected void execute(CommandLine params) throws Exception {
		String tableName = params.getOptionValue("t");
		String fileName = params.getOptionValue("f");
		String idColumn = params.getOptionValue("k");

		ISpmsNoService spmsNoService = (ISpmsNoService) SpringContextUtil.getBean("spmsNoService");
		spmsNoService.import2Database(tableName, idColumn, fileName);
	}
}