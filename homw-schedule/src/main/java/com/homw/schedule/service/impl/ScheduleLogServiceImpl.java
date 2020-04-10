package com.homw.schedule.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.schedule.dao.ScheduleLogDao;
import com.homw.schedule.entity.ScheduleLogEntity;
import com.homw.schedule.service.ScheduleLogService;

@Service("scheduleLogService")
public class ScheduleLogServiceImpl implements ScheduleLogService {
	@Autowired
	private ScheduleLogDao scheduleLogDao;
	
	@Override
	public ScheduleLogEntity queryObject(Long jobId) {
		return scheduleLogDao.queryObject(jobId);
	}

	@Override
	public List<ScheduleLogEntity> queryList(Map<String, Object> map) {
		return scheduleLogDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return scheduleLogDao.queryTotal(map);
	}

	@Override
	public void save(ScheduleLogEntity log) {
		scheduleLogDao.save(log);
	}

}
