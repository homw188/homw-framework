package com.homw.test.quartz.demo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @description 简单Quartz任务测试
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public class SimpleJob implements Job {
	private static AtomicInteger counter = new AtomicInteger(0);
	private static Scheduler scheduler;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int count = context.getJobDetail().getJobDataMap().getInt("counter");
		context.getJobDetail().getJobDataMap().put("counter", ++count);

		System.out.println("start-[" + Thread.currentThread().getName() + "], count: " + count);
		try {
			Thread.sleep((long) (Math.random() * 100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (count == 10 && scheduler != null) {
			try {
				scheduler.standby();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end-[" + Thread.currentThread().getName() + "]");
	}

	public static void main(String[] args) throws SchedulerException, IOException {
		launch();
	}

	public static void launch() throws SchedulerException, IOException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();

		scheduler.start();

		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("job-1", "job.group")
				.usingJobData("counter", 0).storeDurably(true).build();

		JobDetail jobDetail2 = JobBuilder.newJob(SimpleJob.class).withIdentity("job-2", "job.group")
				.usingJobData("counter", 0).build();

		SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-1", "trigger.group").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(500))
				.build();

		SimpleTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger-2", "trigger.group").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(800))
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
		// scheduler.scheduleJob(jobDetail, trigger2);

		System.in.read();

		scheduler.shutdown();
	}
}
