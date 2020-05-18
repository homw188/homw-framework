package com.homw.robot;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.homw.robot.handler.RobotChannelInitialer;
import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.packet.Cmd;
import com.homw.transport.netty.TcpClient;
import com.homw.transport.netty.session.Session;

/**
 * 机器人客户端测试
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotClientTest {

	@Test
	public void testClient() {
		TcpClient client = null;
		try {
			Session session = null;
			client = TcpClient.builder().host("localhost").port(8888).handler(new RobotChannelInitialer()).build();
			try {
				session = client.openSession(); // 建立TCP连接
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// 组装数据包
			MsgPacket cmd = MsgFactory.getCmdPacket(3, Arrays.copyOf("执行某任务".getBytes(), Cmd.size));
			// 发送数据包
			session.sendOriginal(cmd);
			System.out.println("client send:" + cmd);

			System.in.read();// wait
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}
}