package com.homw.test.mq;

import javax.jms.JMSException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.homw.test.activemq.start.SpringQueueConsumer;
import com.homw.test.activemq.start.SpringQueueProducer;
import com.homw.web.support.util.SpringContextUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml", "classpath:spring-context.xml" })
public class TestSpringJMS {
	@Test
	public void testProducer() {
		SpringQueueProducer sender = (SpringQueueProducer) SpringContextUtil.getBean("queueSender");
		sender.send();
		System.out.println("send message...");
	}

	@Test
	public void testConsumer() throws JMSException {
		SpringQueueConsumer receiver = (SpringQueueConsumer) SpringContextUtil.getBean("queueReceiver");
		receiver.receive();
	}
}