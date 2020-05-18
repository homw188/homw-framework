package com.homw.transport.netty;

import org.junit.Test;

import com.homw.transport.netty.handler.ResultFutureHandler;
import com.homw.transport.netty.handler.ServerSessionHandler;
import com.homw.transport.netty.message.Message;
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
			ResultFutureHandler futureHandler = new ResultFutureHandler();
			ServerSessionHandler sessionHandler = new ServerSessionHandler();
			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("encoder", sharedEncoder);
				ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
				ch.pipeline().addLast("sessionHandler", sessionHandler);
				ch.pipeline().addLast("futureHandler", futureHandler);
				ch.pipeline().addLast("serverHandler", new SimpleChannelInboundHandler<Message>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
						System.out.println("Server recv messsage: " + msg);
						msg.setPayload(msg.getPayload() + " <-- back from server");
						
						// echo
						Session session = SessionManager.getInstance().getSession(Session.genSessionId(ctx));
						if (session != null) {
							session.send(msg);
						} else {
							// not found session
							ctx.channel().writeAndFlush(msg);
						}
						
						// broadcast
						//SessionManager.getInstance().broadcast(msg);
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
