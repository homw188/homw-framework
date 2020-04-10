package com.homw.modbus;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.handler.ModbusServerHandler;
import com.homw.modbus.handler.rtu.ModbusRTUChannelInitializer;
import com.homw.modbus.handler.tcp.ModbusTCPChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 基于RTU的Modbus服务端
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusServer {
	private final ModbusProtoType protocol;
	private final ChannelGroup clientChannels;

	private ServerBootstrap bootstrap;
	private Channel parentChannel;

	private AtomicBoolean handlerInited = new AtomicBoolean(false);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	private static Logger log = LoggerFactory.getLogger(ModbusServer.class);

	/**
	 * 构建服务端
	 * 
	 * @param protoType 协议类型
	 * @throws ModbusException 启动异常
	 */
	public ModbusServer(ModbusProtoType protoType) throws ModbusException {
		this.protocol = protoType;
		this.clientChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

		bootstrap();
	}

	/**
	 * 初始化，启动服务
	 * 
	 * @throws ModbusException
	 */
	private void bootstrap() throws ModbusException {
		try {
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();

			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
		} catch (Exception ex) {
			throw new ModbusException("服务启动异常", ex);
		}
	}

	/**
	 * 启动服务，绑定端口
	 * 
	 * @param port
	 * @throws ModbusException
	 */
	public synchronized void start(int port, ModbusServerHandler handler) throws ModbusException {
		if (parentChannel != null) {
			shutdown();
		}

		if (handlerInited.compareAndSet(false, true)) {
			if (protocol == ModbusProtoType.RTU) {
				bootstrap.childHandler(new ModbusRTUChannelInitializer(handler));
			} else {
				bootstrap.childHandler(new ModbusTCPChannelInitializer(handler));
			}
		}

		try {
			// 绑定端口，开始接入连接
			parentChannel = bootstrap.bind(port).sync().channel();

			log.info("服务已启动");

			parentChannel.closeFuture().addListener(new GenericFutureListener<ChannelFuture>() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					workerGroup.shutdownGracefully();
					bossGroup.shutdownGracefully();

					log.info("服务已停止");
				}
			});
		} catch (Exception e) {
			throw new ModbusException("服务绑定异常，监听端口：" + port, e);
		}
	}

	/**
	 * 关闭连接，释放资源
	 */
	public synchronized void shutdown() {
		if (parentChannel != null) {
			parentChannel.close().awaitUninterruptibly();
		}
		clientChannels.close().awaitUninterruptibly();
	}

	/**
	 * 获取所有客户端连接
	 * 
	 * @return
	 */
	public ChannelGroup getClientChannels() {
		return clientChannels;
	}

	/**
	 * 新增客户端连接
	 * 
	 * @param channel
	 */
	public void addClient(Channel channel) {
		clientChannels.add(channel);
	}

	/**
	 * 移除客户端连接
	 * 
	 * @param channel
	 */
	public void removeClient(Channel channel) {
		clientChannels.remove(channel);
	}
}
