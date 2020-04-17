package com.homw.robot;

import com.homw.robot.handler.RobotChannelInitialer;
import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.packet.Cmd;
import com.homw.transport.netty.NettyClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * 机器人客户端测试
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotClientTest {

	@Test
	public void testClient() {
		NettyClient client = null;
		try {
			client = new NettyClient("localhost", 8888, new RobotChannelInitialer());
			try {
				client.connect();// 建立TCP连接
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// 组装数据包
			MsgPacket cmd = MsgFactory.getCmdPacket(3, Arrays.copyOf("执行某任务".getBytes(), Cmd.size));
			// 发送数据包
			client.sendMessage(cmd);
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