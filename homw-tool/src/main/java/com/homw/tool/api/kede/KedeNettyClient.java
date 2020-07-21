package com.homw.tool.api.kede;

import java.util.concurrent.TimeUnit;

import com.homw.transport.netty.session.ResultFuture;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @description 科德通信客户端
 * @author Hom
 * @version 1.0
 * @since 2020-07-21
 */
public class KedeNettyClient {
	
	private Channel channel;
	private EventLoopGroup worker;
	public static final AttributeKey<ResultFuture<String>> DATA_KEY = AttributeKey.valueOf("data_key");

	public void connect(String ip, int port) {
		Bootstrap bootstap = new Bootstrap();
		worker = new NioEventLoopGroup();
		try {
			bootstap.group(worker)
					// 连接超时
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1500)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast("decoder", new ByteArrayDecoder());
							ch.pipeline().addLast("encoder", new ByteArrayEncoder());
							ch.pipeline().addLast("clientHandler", new KedeClientHandler());
						}
					});

			// 等待建立连接
			ChannelFuture future = bootstap.connect(ip, port).sync();
			if (future.cause() != null) {
				throw future.cause();
			}
			channel = future.channel();
		} catch (Throwable e) {
			e.printStackTrace();
		} 
	}
	
	public String send(String data, int timeout) {
		// 发送最小间隔，避免线路阻塞
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		channel.writeAndFlush(KedeProtocolUtil.hexStrToBytes(data));
		
		Attribute<ResultFuture<String>> attr = channel.attr(DATA_KEY);
		ResultFuture<String> future = new ResultFuture<String>();
		attr.set(future);
		try {
			return future.get(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void close() {
		if (worker != null) worker.shutdownGracefully();
	}
    
}