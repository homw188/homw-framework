package com.homw.test.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.homw.test.activemq.service.RemindService;

/**
 * @description 自动提醒任务
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public class RemindJob implements Job {
	@Autowired
	private RemindService remindService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		remindService.remind("会员冻结:" + context.getTrigger().getKey());
	}
}
