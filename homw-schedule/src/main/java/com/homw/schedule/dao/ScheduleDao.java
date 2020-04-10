package com.homw.schedule.dao;

import java.util.Map;

import com.homw.dao.support.BaseDao;
import com.homw.schedule.entity.ScheduleEntity;

/**
 * @description 定时任务
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public interface ScheduleDao extends BaseDao<ScheduleEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
