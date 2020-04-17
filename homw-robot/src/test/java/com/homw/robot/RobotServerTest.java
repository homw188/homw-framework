package com.homw.robot;

import com.homw.robot.codec.RobotMsgDecoder;
import com.homw.robot.codec.RobotMsgEncoder;
import com.homw.robot.handler.RobotServerMsgHandler;
import com.homw.robot.util.ProtocolConstant;
import com.homw.transport.netty.NettyServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.junit.Test;

import java.io.IOException;

/**
 * 机器人服务端测试
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotServerTest {
	
	@Test
	public void testServer() {
		NettyServer server = null;
		try {
			server = new NettyServer(8888, new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("messageDecoder", new RobotMsgDecoder(ProtocolConstant.MAX_FRAME_LENGTH, 2, 2));
					ch.pipeline().addLast("messageEncoder", new RobotMsgEncoder());
					ch.pipeline().addLast("msgPacketHandler", new RobotServerMsgHandler());
				}
			});
			server.start();

			System.in.read();// wait
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.shutdown();
			}
		}
	}

}