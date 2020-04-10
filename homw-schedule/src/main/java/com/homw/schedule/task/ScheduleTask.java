package com.homw.schedule.task;

import com.homw.common.exception.SystemException;

/**
 * @description 定时任务执行接口
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
public interface ScheduleTask {
	
	void exec() throws SystemException;
	
	void exec(String args) throws SystemException;
}
