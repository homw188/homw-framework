package com.homw.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homw.common.bean.SystemMessage;
import com.homw.common.exception.SystemException;
import com.homw.common.exception.SystemRuntimeException;
import com.homw.common.util.PageUtil;
import com.homw.schedule.entity.ScheduleEntity;
import com.homw.schedule.service.ScheduleService;

/**
 * @description 定时任务
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleJobService;
	
	/**
	 * 定时任务列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("schedule:list")
	public SystemMessage list(Integer page, Integer limit) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);

		// 查询列表数据
		List<ScheduleEntity> jobList = scheduleJobService.queryList(map);
		int total = scheduleJobService.queryTotal(map);

		PageUtil pageUtil = new PageUtil(jobList, total, limit, page);
		return SystemMessage.ok().put("page", pageUtil);
	}

	/**
	 * 定时任务信息
	 * @param jobId
	 * @return
	 */
	@RequestMapping("/info/{jobId}")
	@RequiresPermissions("schedule:info")
	public SystemMessage info(@PathVariable("jobId") Long jobId) {
		ScheduleEntity schedule = scheduleJobService.queryObject(jobId);
		return SystemMessage.ok().put("schedule", schedule);
	}

	/**
	 * 保存定时任务
	 * @param scheduleJob
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping("/save")
	@RequiresPermissions("schedule:save")
	public SystemMessage save(@RequestBody ScheduleEntity scheduleJob) {
		verifyForm(scheduleJob);
		scheduleJobService.save(scheduleJob);
		return SystemMessage.ok();
	}

	/**
	 * 修改定时任务
	 * @param scheduleJob
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping("/update")
	@RequiresPermissions("schedule:update")
	public SystemMessage update(@RequestBody ScheduleEntity scheduleJob) {
		verifyForm(scheduleJob);
		scheduleJobService.update(scheduleJob);
		return SystemMessage.ok();
	}

	/**
	 * 删除定时任务
	 * @param jobIds
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("schedule:delete")
	public SystemMessage delete(@RequestBody Long[] jobIds) {
		scheduleJobService.deleteBatch(jobIds);
		return SystemMessage.ok();
	}

	/**
	 * 立即执行任务
	 * @param jobIds
	 * @return
	 */
	@RequestMapping("/run")
	@RequiresPermissions("schedule:run")
	public SystemMessage run(@RequestBody Long[] jobIds) {
		scheduleJobService.run(jobIds);
		return SystemMessage.ok();
	}

	/**
	 * 暂停定时任务
	 * @param jobIds
	 * @return
	 */
	@RequestMapping("/pause")
	@RequiresPermissions("schedule:pause")
	public SystemMessage pause(@RequestBody Long[] jobIds) {
		scheduleJobService.pause(jobIds);
		return SystemMessage.ok();
	}

	/**
	 * 恢复定时任务
	 * @param jobIds
	 * @return
	 */
	@RequestMapping("/resume")
	@RequiresPermissions("schedule:resume")
	public SystemMessage resume(@RequestBody Long[] jobIds) {
		scheduleJobService.resume(jobIds);
		return SystemMessage.ok();
	}

	/**
	 * 参数校验
	 * @param scheduleJob
	 */
	private void verifyForm(ScheduleEntity scheduleJob) {
		if (StringUtils.isBlank(scheduleJob.getBeanName())) {
			throw new SystemRuntimeException("bean名称不能为空");
		}

		if (StringUtils.isBlank(scheduleJob.getMethodName())) {
			throw new SystemRuntimeException("方法名称不能为空");
		}

		if (StringUtils.isBlank(scheduleJob.getCronExpression())) {
			throw new SystemRuntimeException("cron表达式不能为空");
		}
	}
}
