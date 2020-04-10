package com.homw.schedule.service;

import java.util.List;
import java.util.Map;

import com.homw.schedule.entity.ScheduleEntity;

/**
 * @description 定时任务
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public interface ScheduleService {

	/**
	 * 根据ID，查询定时任务
	 * @param jobId
	 * @return
	 */
	ScheduleEntity queryObject(Long jobId);

	/**
	 * 查询定时任务列表
	 * @param map
	 * @return
	 */
	List<ScheduleEntity> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 * @param map
	 * @return
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存定时任务
	 * @param scheduleJob
	 */
	void save(ScheduleEntity scheduleJob);

	/**
	 * 更新定时任务
	 * @param scheduleJob
	 */
	void update(ScheduleEntity scheduleJob);

	/**
	 * 批量删除定时任务
	 * @param jobIds
	 */
	void deleteBatch(Long[] jobIds);

	/**
	 * 批量更新定时任务状态
	 * @param jobIds
	 * @param status
	 * @return
	 */
	int updateBatch(Long[] jobIds, int status);

	/**
	 * 立即执行
	 * @param jobIds
	 */
	void run(Long[] jobIds);

	/**
	 * 暂停运行
	 * @param jobIds
	 */
	void pause(Long[] jobIds);

	/**
	 * 恢复运行
	 * @param jobIds
	 */
	void resume(Long[] jobIds);
}
