package com.homw.test.rpc.server;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @description rpc服务
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcServer {
	/**
	 * 监听端口
	 */
	private int serverPort = 8888;
	/**
	 * rpc实现全限定名列表
	 */
	private List<String> providerList = new ArrayList<>();
	/**
	 * rpc接口实现映射表
	 */
	private Map<String, Object> providerMap = new HashMap<>();

	/**
	 * 启动服务
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(parentGroup, childGroup)
					// 设置已建立连接但未处理的连接队列大小
					.option(ChannelOption.SO_BACKLOG, 1024)
					// 开启心跳检测，保持长连接
					.childOption(ChannelOption.SO_KEEPALIVE, true).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// 添加对象解码器
							pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
							// 添加对象编码器
							pipeline.addLast(new ObjectEncoder());
							// 添加自定义处理器
							pipeline.addLast(new RpcServerHandler(providerMap));
						}
					});
			ChannelFuture future = bootstrap.bind(serverPort).sync();
			System.out.println("服务已启动，监听端口：" + serverPort);
			future.channel().closeFuture().sync();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}

	/**
	 * 发布rpc接口
	 * 
	 * @param packageName
	 * @throws Exception
	 */
	public void publish(String packageName) throws Exception {
		loadProviderList(packageName);
		registProvider();
	}

	/**
	 * 注册rpc接口
	 * 
	 * @throws Exception
	 */
	private void registProvider() throws Exception {
		if (providerList.isEmpty())
			return;
		for (String className : providerList) {
			Class<?> clazz = Class.forName(className);
			providerMap.put(clazz.getInterfaces()[0].getName(), clazz.newInstance());
		}
	}

	/**
	 * 加载rpc接口
	 * 
	 * @param packageName
	 */
	private void loadProviderList(String packageName) {
		URL resource = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
		if (resource == null)
			return;
		File dir = new File(resource.getFile());
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadProviderList(packageName + "." + file.getName());
			} else if (file.getName().endsWith(".class")) {
				providerList.add(packageName + "." + file.getName().replace(".class", ""));
			}
		}
	}
}
