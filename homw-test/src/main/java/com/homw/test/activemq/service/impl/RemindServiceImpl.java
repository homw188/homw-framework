package com.homw.test.activemq.service.impl;

import org.springframework.stereotype.Service;

import com.homw.test.activemq.service.RemindService;

@Service("remindService")
public class RemindServiceImpl implements RemindService
{
	@Override
	public boolean remind(String message)
	{
		System.out.println("已触发提示信息：" + message);
		return true;
	}

}
