package com.homw.tool.application;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.homw.tool.config.SpringContextConfig;
import com.homw.tool.exception.ApplicationException;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description Application启动流程抽象
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public abstract class AbstractApplication implements Application {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public final void start(String[] args, Options options) {
		logger.info("Start application {}...", this.getClass().getSimpleName());

		configArgs(options);

		CommandLine params = null;
		try {
			params = parseArgs(args, options, false);
			validateArgs(params);
		} catch (Exception e) {
			logger.error("Arguments exception, please check your inputs.", e);
			printHint(options);
			System.exit(1);
		}

		Closeable ctx = startContiner();

		try {
			execute(params);
		} catch (Exception e) {
			wrapException(e);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (IOException e) {
					wrapException(e);
				}
			}
			completed();
		}
	}

	/**
	 * 打印应用列表
	 */
	public static void printAppList() {
		Map<String, String> appMap = ApplicationFactory.getAppMap();
		String[] appArr = new String[appMap.size()];
		appMap.keySet().toArray(appArr);
		Arrays.sort(appArr);
		System.out.println("available app list: ");
		for (String app : appArr) {
			System.out.println(" -a " + app);
		}
	}

	/**
	 * 创建命令行Options
	 * 
	 * @return
	 */
	public static Options buildCliOptions() {
		Options options = new Options();
		options.addOption(Option.builder("ls").longOpt("list").desc("list available application").build());
		options.addOption(Option.builder("a").longOpt("app").hasArg().desc("application name").build());
		return options;
	}

	/**
	 * 输入参数解析
	 * 
	 * @param args
	 * @param options
	 * @param stopAtNonOption
	 * @return
	 * @throws ParseException
	 */
	public static CommandLine parseArgs(String[] args, Options options, boolean stopAtNonOption) throws ParseException {
		return new DefaultParser().parse(options, args, stopAtNonOption);
	}

	private static HelpFormatter formatter = new HelpFormatter();

	/**
	 * 输出提示信息
	 * 
	 * @param options
	 */
	public static void printHint(Options options) {
		formatter.printHelp("-ls | -a [appName] [options]", options);
	}

	/**
	 * 启动spring容器
	 */
	protected Closeable startContiner() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringContextConfig.class);
		ctx.start();
		initContext(ctx);
		return ctx;
	}

	/**
	 * 初始化spring容器
	 * 
	 * @param ctx
	 */
	protected void initContext(ApplicationContext ctx) {
		SpringContextUtil springContextUtil = (SpringContextUtil) ctx.getBean("springContextUtil");
		springContextUtil.setApplicationContext(ctx);
	}

	/**
	 * 执行完成
	 */
	protected void completed() {
		logger.info("Application {} completed.", this.getClass().getSimpleName());
	}

	/**
	 * 执行异常
	 * 
	 * @param cause
	 */
	protected void wrapException(Throwable cause) {
		throw new ApplicationException(cause);
	}

	/**
	 * 配置参数
	 * 
	 * @param options
	 */
	protected abstract void configArgs(Options options);

	/**
	 * 校验参数
	 * 
	 * @param params
	 */
	protected abstract void validateArgs(CommandLine params);

	/**
	 * 执行
	 * 
	 * @param params
	 * @throws Exception
	 */
	protected abstract void execute(CommandLine params) throws Exception;
}
