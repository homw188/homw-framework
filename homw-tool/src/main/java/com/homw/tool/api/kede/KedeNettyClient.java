package com.homw.tool.api.kede;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class KedeNettyClient {
	private KedeClientHandler clientHandler = new KedeClientHandler();

	private ChannelFuture channelFuture;
	private Bootstrap clientBootstap;
	private EventLoopGroup worker;
	private boolean btn = true;

	public String startClient(String ip, int port, int timeout, String write) {
		String fdata = "";
		try {
			clientBootstap = new Bootstrap();
			worker = new NioEventLoopGroup();
			clientBootstap.group(worker).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(ip, port))
					.handler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new ByteArrayDecoder());
							ch.pipeline().addLast(new ByteArrayEncoder());
							ch.pipeline().addLast("ClientHandler", clientHandler);
						}
					});

			channelFuture = clientBootstap.connect(ip, port);
			writeData(write);

			// 计算时间；
			int poll = 0;
			while (btn) {
				try {
					Thread.sleep(1 * 100); // 设置暂停的时间 5 秒
					poll++;
					
					fdata = clientHandler.getData();
					if (fdata != null && !fdata.isEmpty()) {
						btn = false;
					}
					
					if (poll == timeout * 10) {
						btn = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally {
			// 销毁客户端；
			worker.shutdownGracefully();
		}
		return fdata;
	}
    
	public void writeData(String write) {
		try {
			byte[] g = KedeDataProtocolUtil.hexStringToByte(write);
			ByteBuf buffer = Unpooled.copiedBuffer(g);
			Thread.sleep(1000); // 这里联休眠连续发送或失败
			channelFuture.channel().writeAndFlush(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}