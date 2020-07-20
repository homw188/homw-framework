package com.homw.tool.api.kede;

import org.apache.commons.lang3.StringUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class KedeNettyClient {
	
	Channel channel;
	EventLoopGroup worker;
	KedeClientHandler clientHandler;

	public void connect(String ip, int port) {
		Bootstrap bootstap = new Bootstrap();
		worker = new NioEventLoopGroup();
		clientHandler = new KedeClientHandler();
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
							ch.pipeline().addLast("clientHandler", clientHandler);
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
		String result = "";
		boolean wait = true;
		
		// 发送最小间隔，避免线路阻塞
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		byte[] bytes = KedeProtocolUtil.hexStrToBytes(data);
		ByteBuf buffer = Unpooled.copiedBuffer(bytes);
		channel.writeAndFlush(buffer);

		// 等待返回数据
		int poll = 0;
		while (wait) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			poll++;
			
			result = clientHandler.getData();
			if (StringUtils.isNotEmpty(result) || (poll == timeout * 10)) {
				wait = false;
			}
		}
		return result;
	}
	
	public void close() {
		if (worker != null) worker.shutdownGracefully();
	}
    
}