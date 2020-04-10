package com.homw.test.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.homw.test.rpc.api.bean.RpcMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @description rpc调用代理
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcProxy {
	/**
	 * 监听端口
	 */
	private static int serverPort = 8888;

	/**
	 * 创建rpc调用接口代理
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T create(final Class<?> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (Object.class.equals(method.getDeclaringClass())) {
					return method.invoke(this, args);
				}
				return rpcInvoke(clazz, method, args);
			}
		});
	}

	/**
	 * 执行rpc调用
	 * 
	 * @param clazz
	 * @param method
	 * @param args
	 * @return
	 * @throws Exception
	 */
	protected static Object rpcInvoke(Class<?> clazz, Method method, Object[] args) throws Exception {
		final RpcClientHandler handler = new RpcClientHandler();
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(loopGroup).channel(NioSocketChannel.class)
					// 消息发送禁止延迟策略
					.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// 添加对象解码器
							pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
							// 添加对象编码器
							pipeline.addLast(new ObjectEncoder());
							// 添加自定义处理器
							pipeline.addLast(handler);
						}
					});
			ChannelFuture future = bootstrap.connect("localhost", serverPort).sync();

			// 发送rpc调用参数
			RpcMessage message = new RpcMessage();
			message.setClassName(clazz.getName());
			message.setMethodName(method.getName());
			message.setParams(args);
			message.setParamTypes(method.getParameterTypes());
			future.channel().writeAndFlush(message).sync();

			future.channel().closeFuture().sync();
		} finally {
			loopGroup.shutdownGracefully();
		}
		return handler.getResult();
	}
}
