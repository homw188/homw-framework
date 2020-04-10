package com.homw.test.activemq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homw.test.activemq.bean.UserInfo;
import com.homw.test.activemq.service.PushService;
import com.homw.test.bean.ResponseData;

/**
 * @description 消息推送接口
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Controller
@RequestMapping("/push")
public class UserPushController {
	
	@Autowired
	@Qualifier("pushUserService")
	private PushService pushUserService;

	@ResponseBody
	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public Object pushUser(UserInfo user) {
		ResponseData data = new ResponseData();
		try {
			pushUserService.push(user);

			data.setSuccess(true);
			data.setData(user);
		} catch (Exception e) {
			data.setSuccess(false);
			data.setData(e.getMessage());

			e.printStackTrace();
		}
		return data;
	}
}
