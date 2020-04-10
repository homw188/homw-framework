package com.homw.schedule.service;

import java.util.List;
import java.util.Map;

import com.homw.schedule.entity.ScheduleLogEntity;

/**
 * @description 定时任务日志
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public interface ScheduleLogService {

	/**
	 * 根据ID，查询定时任务日志
	 * @param jobId
	 * @return
	 */
	ScheduleLogEntity queryObject(Long jobId);

	/**
	 * 查询定时任务日志列表
	 * @param map
	 * @return
	 */
	List<ScheduleLogEntity> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 * @param map
	 * @return
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存定时任务日志
	 * @param log
	 */
	void save(ScheduleLogEntity log);

}
