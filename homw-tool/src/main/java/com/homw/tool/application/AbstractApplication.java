package com.homw.tool.application;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
	public final void start(String[] args) throws ApplicationException {
		logger.info("Start application " + this.getClass().getSimpleName() + "...");

		Map<String, Object> params = null;
		try {
			params = parseArgs(args);
		} catch (Exception e) {
			logger.error("Arguments exception, please check your inputs.", e);
			printHint(args);
			System.exit(1);
		}

		Closeable ctx = preStart();

		try {
			execute(params);
		} catch (Exception e) {
			executeException(e);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (IOException e) {
					executeException(e);
				}
			}
			completed();
		}
	}

	/**
	 * 输入参数解析
	 * 
	 * @param args
	 * @return
	 */
	protected abstract Map<String, Object> parseArgs(String[] args);

	/**
	 * 输出提示信息
	 * 
	 * @param args
	 */
	protected void printHint(String[] args) {
		// TODO
	}

	/**
	 * 预启动，默认启动spring容器
	 */
	protected Closeable preStart() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
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
		SpringContextUtil springContextUtils = (SpringContextUtil) ctx.getBean("springContextUtil");
		springContextUtils.setApplicationContext(ctx);
	}

	/**
	 * 执行异常
	 * 
	 * @param cause
	 * @throws ApplicationException
	 */
	protected void executeException(Exception cause) throws ApplicationException {
		throw new ApplicationException(cause);
	}

	/**
	 * 执行
	 * 
	 * @param params 输入参数解析映射
	 * @throws Exception
	 */
	protected abstract void execute(Map<String, Object> params) throws Exception;

	/**
	 * 执行完成
	 */
	protected void completed() {
		logger.info("Application " + this.getClass().getSimpleName() + " completed.");
	}
}
