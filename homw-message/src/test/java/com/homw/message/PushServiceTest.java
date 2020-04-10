package com.homw.message;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.homw.common.util.Platform;
import com.homw.message.service.IPushMessageService;

/**
 * @description 推送服务测试
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-message.xml" })
public class PushServiceTest {

	@Autowired
	private IPushMessageService pushMessageService;

	@Test
	public void sendNotification() {
		try {
			Map<String, String> extraMap = new HashMap<String, String>();
			extraMap.put("key", "附带内容");
			System.setProperty(Platform.RUNTIME_ENV, Platform.PROD_ENV);
			pushMessageService.pushNotification("161a3797c894f2d7fe5", "JPush测试", extraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addTag() {
		try {
			pushMessageService.addTag("161a3797c894f2d7fe5", new String[] { "member" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sendNotificationWithTag() {
		try {
			Map<String, String> extraMap = new HashMap<String, String>();
			extraMap.put("key", "123");
			pushMessageService.sendNotificationWithTag("member", "标签组测试", extraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
