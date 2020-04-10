package com.homw.schedule.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.homw.common.exception.SystemRuntimeException;
import com.homw.schedule.bean.ScheduleJob;
import com.homw.schedule.bean.ScheduleStatus;
import com.homw.schedule.entity.ScheduleEntity;

/**
 * @description 定时任务工具类
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public class ScheduleUtil {
    private final static String JOB_NAME = "TASK_";
    
    /**
     * 获取触发器key
     * @param jobId
     * @return
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }
    
    /**
     * 获取jobKey
     * @param jobId
     * @return
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     * @param scheduler
     * @param jobId
     * @return
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("获取定时任务CronTrigger出现异常", e);
        }
    }

    /**
     * 创建定时任务
     * @param scheduler
     * @param schedule
     */
    public static void createSchedule(Scheduler scheduler, ScheduleEntity schedule) {
        try {
        	//构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(schedule.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCronExpression());

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(schedule.getJobId())).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleEntity.JOB_PARAM_KEY, schedule);
            
            scheduler.scheduleJob(jobDetail, trigger);
            
            //暂停任务
            if(schedule.getStatus() == ScheduleStatus.PAUSE.getValue()){
            	pause(scheduler, schedule.getJobId());
            }
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("创建定时任务失败", e);
        }
    }
    
    /**
     * 更新定时任务
     * @param scheduler
     * @param schedule
     */
    public static void updateSchedule(Scheduler scheduler, ScheduleEntity schedule) {
        try {
            TriggerKey triggerKey = getTriggerKey(schedule.getJobId());

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCronExpression());

            CronTrigger trigger = getCronTrigger(scheduler, schedule.getJobId());
            
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            
            //参数
            trigger.getJobDataMap().put(ScheduleEntity.JOB_PARAM_KEY, schedule);
            
            scheduler.rescheduleJob(triggerKey, trigger);
            
            //暂停任务
            if(schedule.getStatus() == ScheduleStatus.PAUSE.getValue()){
            	pause(scheduler, schedule.getJobId());
            }
            
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("更新定时任务失败", e);
        }
    }

    /**
     * 立即执行任务
     * @param scheduler
     * @param schedule
     */
    public static void run(Scheduler scheduler, ScheduleEntity schedule) {
        try {
        	//参数
        	JobDataMap dataMap = new JobDataMap();
        	dataMap.put(ScheduleEntity.JOB_PARAM_KEY, schedule);
        	
            scheduler.triggerJob(getJobKey(schedule.getJobId()), dataMap);
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     * @param scheduler
     * @param jobId
     */
    public static void pause(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     * @param scheduler
     * @param jobId
     */
    public static void resume(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     * @param scheduler
     * @param jobId
     */
    public static void deleteSchedule(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new SystemRuntimeException("删除定时任务失败", e);
        }
    }
}
