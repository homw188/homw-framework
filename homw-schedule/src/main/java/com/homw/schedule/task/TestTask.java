package com.homw.schedule.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.homw.common.exception.SystemException;

/**
 * @description 测试任务
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
@Component("testTask")
public class TestTask implements ScheduleTask {
	
	private static final Logger logger = LoggerFactory.getLogger(TestTask.class);
	
	@Override
	public void exec() throws SystemException {
		logger.info("测试任务开始执行...");
	}

	@Override
	public void exec(String args) throws SystemException {
		exec();
	}
}
