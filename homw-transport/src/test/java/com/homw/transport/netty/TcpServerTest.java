package com.homw.transport.netty;

import org.junit.Test;

import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.session.ServerSessionHandler;
import com.homw.transport.netty.session.Session;
import com.homw.transport.netty.session.SessionManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @description server test
 * @author Hom
 * @version 1.0
 * @since 2020-04-20
 */
public class TcpServerTest {

	@Test
	public void test() throws Exception {
		TcpServer server = TcpServer.builder().port(8888).handler(new ChannelInitializer<SocketChannel>() {
			ObjectEncoder sharedEncoder = new ObjectEncoder();
			ServerSessionHandler sessionHandler = new ServerSessionHandler();
			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("encoder", sharedEncoder);
				ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
				ch.pipeline().addLast("sessionHandler", sessionHandler);
				ch.pipeline().addLast("serverHandler", new SimpleChannelInboundHandler<Message>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
						Session session = SessionManager.getInstance().getSession(Session.genSessionId(ctx));
						msg.setSessionId(session.getSessionId());
						msg.setPayload(msg.getPayload() + "--> back from server");
						session.send(msg);
					}
				});
			}
		}).build();

		// start sever
		server.start();

		// wait
		System.in.read();

		// release
		server.shutdown();
	}
}
