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
	
	private String tag = "Test-Tag";
	private String regId = "161a3797c894f2d7fe5";

	@Test
	public void sendNotification() {
		Map<String, String> extraMap = new HashMap<String, String>();
		extraMap.put("extra_key", "附加值");
		System.setProperty(Platform.RUNTIME_ENV, Platform.PROD_ENV);
		pushMessageService.pushNotification(regId, "JPush Reg.ID测试", extraMap);
	}

	@Test
	public void addTag() {
		pushMessageService.addTag(regId, new String[] { tag });
	}

	@Test
	public void sendNotificationWithTag() {
		Map<String, String> extraMap = new HashMap<String, String>();
		extraMap.put("extra_key", "附加值");
		System.setProperty(Platform.RUNTIME_ENV, Platform.PROD_ENV);
		pushMessageService.sendNotificationWithTag(tag, "Jpush Tag测试", extraMap);
	}
}
