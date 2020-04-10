package com.homw.schedule.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homw.schedule.bean.ScheduleStatus;
import com.homw.schedule.dao.ScheduleDao;
import com.homw.schedule.entity.ScheduleEntity;
import com.homw.schedule.service.ScheduleService;
import com.homw.schedule.util.ScheduleUtil;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
    private Scheduler scheduler;
	@Autowired
	private ScheduleDao schedulerDao;
	
	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init(){
		List<ScheduleEntity> scheduleList = schedulerDao.queryList(new HashMap<String, Object>());
		for(ScheduleEntity schedule : scheduleList){
			CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler, schedule.getJobId());
            //如果不存在，则创建
            if(cronTrigger == null) {
                ScheduleUtil.createSchedule(scheduler, schedule);
            }else {
                ScheduleUtil.updateSchedule(scheduler, schedule);
            }
		}
	}
	
	@Override
	public ScheduleEntity queryObject(Long jobId) {
		return schedulerDao.queryObject(jobId);
	}

	@Override
	public List<ScheduleEntity> queryList(Map<String, Object> map) {
		return schedulerDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return schedulerDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(ScheduleEntity schedule) {
		schedule.setCreateTime(new Date());
		schedule.setStatus(ScheduleStatus.NORMAL.getValue());
        schedulerDao.save(schedule);
        
        ScheduleUtil.createSchedule(scheduler, schedule);
    }
	
	@Override
	@Transactional
	public void update(ScheduleEntity schedule) {
        ScheduleUtil.updateSchedule(scheduler, schedule);
                
        schedulerDao.update(schedule);
    }

	@Override
	@Transactional
    public void deleteBatch(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtil.deleteSchedule(scheduler, jobId);
    	}
    	
    	//删除数据
    	schedulerDao.deleteBatch(jobIds);
	}

	@Override
    public int updateBatch(Long[] jobIds, int status){
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", jobIds);
    	map.put("status", status);
    	return schedulerDao.updateBatch(map);
    }
    
	@Override
	@Transactional
    public void run(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtil.run(scheduler, queryObject(jobId));
    	}
    }

	@Override
	@Transactional
    public void pause(Long[] jobIds) {
        for(Long jobId : jobIds){
    		ScheduleUtil.pause(scheduler, jobId);
    	}
        
    	updateBatch(jobIds, ScheduleStatus.PAUSE.getValue());
    }

	@Override
	@Transactional
    public void resume(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtil.resume(scheduler, jobId);
    	}

    	updateBatch(jobIds, ScheduleStatus.NORMAL.getValue());
    }
    
}
