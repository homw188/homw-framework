package com.homw.transport.netty;

import org.junit.Test;

import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.session.ClientSessionHandler;
import com.homw.transport.netty.session.Session;

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
					
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("encoder", sharedEncoder);
						ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						ch.pipeline().addLast("sessionHandler", sessionHandler);
					}
				}).build();
		Session session = client.openSession();

		ResultFuture<Message> future = session.send("This message from client");
		System.out.println(future.get());

		// wait
		System.in.read();

		// release
		client.close();
	}
}
