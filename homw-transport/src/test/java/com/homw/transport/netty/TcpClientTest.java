package com.homw.transport.netty;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.homw.transport.netty.handler.ClientSessionHandler;
import com.homw.transport.netty.handler.HeartbeatHandler;
import com.homw.transport.netty.handler.ResultFutureHandler;
import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.session.ResultFuture;
import com.homw.transport.netty.session.Session;

import cn.hutool.core.util.RandomUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @description client test
 * @author Hom
 * @version 1.0
 * @since 2020-04-20
 */
public class TcpClientTest {

	@Test
	public void test() throws Exception {
		TcpClient client = TcpClient.builder().host("localhost").port(8888)
				.handler(new ChannelInitializer<SocketChannel>() {
					ObjectEncoder sharedEncoder = new ObjectEncoder();
					ClientSessionHandler sessionHandler = new ClientSessionHandler();
					ResultFutureHandler futureHandler = new ResultFutureHandler();
					HeartbeatHandler heartbeatHandler = new HeartbeatHandler(3, TimeUnit.SECONDS);

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("encoder", sharedEncoder);
						ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						ch.pipeline().addLast("sessionHandler", sessionHandler);
						ch.pipeline().addLast("futureHandler", futureHandler);
						ch.pipeline().addLast("heartbeatHandler", heartbeatHandler);
					}
				}).build();
		Session session = client.openSession();

		ResultFuture<Message> future = session.send("This message from client-" + RandomUtil.randomInt(10));
		System.out.println("Client recv messsage: " + future.get());

		// wait
		System.in.read();

		// release
		client.close();
	}
}
