package com.homw.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homw.common.bean.SystemMessage;
import com.homw.common.util.PageUtil;
import com.homw.schedule.entity.ScheduleLogEntity;
import com.homw.schedule.service.ScheduleLogService;

/**
 * @description 定时任务日志
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
@RestController
@RequestMapping("/schedule/log")
public class ScheduleLogController {
	@Autowired
	private ScheduleLogService scheduleJobLogService;

	/**
	 * 定时任务日志列表
	 * @param page
	 * @param limit
	 * @param jobId
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("schedule:log")
	public SystemMessage list(Integer page, Integer limit, Integer jobId) {
		Map<String, Object> map = new HashMap<>();
		map.put("jobId", jobId);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);

		// 查询列表数据
		List<ScheduleLogEntity> jobList = scheduleJobLogService.queryList(map);
		int total = scheduleJobLogService.queryTotal(map);

		PageUtil pageUtil = new PageUtil(jobList, total, limit, page);
		return SystemMessage.ok().put("page", pageUtil);
	}

	/**
	 * 定时任务日志信息
	 * @param logId
	 * @return
	 */
	@RequestMapping("/info/{logId}")
	@RequiresPermissions("schedule:log")
	public SystemMessage info(@PathVariable("logId") Long logId) {
		ScheduleLogEntity log = scheduleJobLogService.queryObject(logId);
		return SystemMessage.ok().put("log", log);
	}
}
