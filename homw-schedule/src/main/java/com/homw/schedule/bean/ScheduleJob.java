package com.homw.schedule.bean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.homw.schedule.entity.ScheduleEntity;
import com.homw.schedule.entity.ScheduleLogEntity;
import com.homw.schedule.service.ScheduleLogService;
import com.homw.web.support.util.SpringContextUtil;

/**
 * @description 定时任务
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public class ScheduleJob extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService service = Executors.newSingleThreadExecutor();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ScheduleEntity scheduleJob = (ScheduleEntity) context.getMergedJobDataMap().get(ScheduleEntity.JOB_PARAM_KEY);

		// 获取spring bean
		ScheduleLogService scheduleJobLogService = (ScheduleLogService) SpringContextUtil.getBean("scheduleLogService");

		// 数据库保存执行记录
		ScheduleLogEntity log = new ScheduleLogEntity();
		log.setJobId(scheduleJob.getJobId());
		log.setBeanName(scheduleJob.getBeanName());
		log.setMethodName(scheduleJob.getMethodName());
		log.setParams(scheduleJob.getParams());
		log.setCreateTime(new Date());

		// 任务开始时间
		long startTime = System.currentTimeMillis();

		try {
			// 执行任务
			logger.info("任务准备执行，任务ID：" + scheduleJob.getJobId());
			ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(),
					scheduleJob.getParams());
			Future<?> future = service.submit(task);

			future.get();

			// 任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int) times);
			// 任务状态 0：成功 1：失败
			log.setStatus(0);

			logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);

			// 任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int) times);

			// 任务状态 0：成功 1：失败
			log.setStatus(1);
			log.setError(StringUtils.substring(e.toString(), 0, 2000));
		} finally {
			scheduleJobLogService.save(log);
		}
	}
}
