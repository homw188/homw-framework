package com.homw.modbus;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.exception.ModbusConnectionException;
import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.exception.ModbusTimeoutException;
import com.homw.modbus.handler.ModbusClientHandler;
import com.homw.modbus.handler.rtu.ModbusRTUChannelInitializer;
import com.homw.modbus.handler.tcp.ModbusTCPChannelInitializer;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.ModbusResponseCache;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Modbus485客户端，兼容RTU和TCP
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusClient {
	private String host;
	private int port;
	private final ModbusProtoType protocol;

	private Channel channel;
	private EventLoopGroup workerGroup;
	private Bootstrap bootstrap;

	private ModbusClientHandler handler;

	private volatile boolean shutdown = false;
	private volatile boolean reconnecting = false;
	private volatile boolean forceDisconnected = false;

	private long responseTimer;

	private AtomicBoolean handlerInited = new AtomicBoolean(false);

	private ReentrantLock lock = new ReentrantLock();
	private static Logger log = LoggerFactory.getLogger(ModbusClient.class);

	/**
	 * <p>
	 * 构建客户端
	 * </p>
	 * 
	 * @param protoType 协议类型
	 * @throws ModbusException 启动异常
	 */
	public ModbusClient(ModbusProtoType protoType) throws ModbusException {
		bootstrap();

		this.shutdown = false;
		this.protocol = protoType;
	}

	/**
	 * <p>
	 * 初始化，启动客户端
	 * </p>
	 * 
	 * @param handler 客户端处理器 {@link ModbusClientHandler}
	 * @throws ModbusException 启动异常
	 */
	private void bootstrap() throws ModbusException {
		try {
			workerGroup = new NioEventLoopGroup();

			bootstrap = new Bootstrap();
			bootstrap.group(workerGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
		} catch (Exception ex) {
			throw new ModbusException("客户端启动异常", ex);
		}
	}

	/**
	 * <p>
	 * 重新建立连接，带延迟 {@link ModbusConstant.RECONNECT_INTERVAL}
	 * </p>
	 * 
	 * <p>
	 * 如果强制断开或正在重连，则该请求将被忽略
	 * </p>
	 */
	public void reconnectWithDelay() {
		if (forceDisconnected || reconnecting) {
			return;
		}
		reconnecting = true;

		workerGroup.schedule(new Runnable() {
			public void run() {
				log.info("尝试重连...");
				try {
					connect(host, port, null);
				} catch (ModbusConnectionException e) {
					// e.printStackTrace();
					log.warn("重连失败", e);
				}
				reconnecting = false;
			}
		}, ModbusConstant.RECONNECT_INTERVAL, TimeUnit.MILLISECONDS);
	}

	/**
	 * <p>
	 * 重新建立连接，立即执行
	 * </p>
	 * 
	 * <p>
	 * 如果强制断开或正在重连，则该请求将被忽略
	 * </p>
	 * 
	 * @throws ModbusConnectionException
	 */
	public void reconnectNow() throws ModbusConnectionException {
		if (forceDisconnected || reconnecting) {
			return;
		}
		reconnecting = true;
		connect(host, port, null);
		reconnecting = false;
	}

	/**
	 * <p>
	 * 建立连接
	 * </p>
	 * 
	 * @param host    服务主机
	 * @param port    服务监听端口
	 * @param handler 客户端处理器 {@link ModbusClientHandler}
	 * 
	 * @throws ModbusConnectionException 连接异常
	 */
	public void connect(String host, int port, ModbusClientHandler handler) throws ModbusConnectionException {
		if (isShutdown()) {
			log.warn("该客户端已销毁，请重新创建");
			return;
		}

		try {
			lock.lock();

			if (handlerInited.compareAndSet(false, true)) {
				this.handler = handler;

				if (protocol == ModbusProtoType.RTU) {
					bootstrap.handler(new ModbusRTUChannelInitializer(handler));
				} else {
					bootstrap.handler(new ModbusTCPChannelInitializer(handler));
				}
			}
			disconnect();

			this.host = host;
			this.port = port;
			this.forceDisconnected = false;

			try {
				ChannelFuture future = bootstrap.connect(host, port).sync();
				log.info("连接成功");

				channel = future.channel();
				channel.closeFuture().addListener(new GenericFutureListener<ChannelFuture>() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						log.info("连接断开");
					}
				});
			} catch (Exception e) {
				log.warn("连接失败, 服务地址：" + host + ", 端口：" + port);
				throw new ModbusConnectionException("建立连接失败", e);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 */
	public Channel getChannel() {
		try {
			lock.lock();
			return channel;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * <p>
	 * 强制断开连接
	 * </p>
	 * 
	 * <p>
	 * 客户端可通过重连 {@link ModbusClient#reconnectWithDelay()} 复用
	 * </p>
	 */
	public void disconnect() {
		forceDisconnected = true;
		try {
			lock.lock();
			if (channel != null && channel.isActive()) {
				channel.close().awaitUninterruptibly();
				log.info("连接已断开");
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * <p>
	 * 客户端销毁
	 * </p>
	 * 
	 * <p>
	 * 关闭连接，释放资源，客户端将不可再复用
	 * </p>
	 */
	public void shutdown() {
		shutdown = true;
		try {
			lock.lock();
			disconnect();
			if (protocol == ModbusProtoType.TCP) {
				ModbusResponseCache.dispose();
			}
			workerGroup.shutdownGracefully();
			handlerInited.set(false);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 客户端是否已销毁
	 * 
	 * @return
	 */
	public boolean isShutdown() {
		return shutdown;
	}

	/**
	 * <p>
	 * 发送数据包
	 * </p>
	 * <p>
	 * 该方法阻塞等待响应，直到超时异常
	 * </p>
	 * 
	 * @param request 请求数据包
	 * 
	 * @return 发送失败返 {@code null}
	 * 
	 * @throws ModbusConnectionException 连接异常
	 * @throws ModbusException           请求/响应数据格式不一致
	 * @throws ModbusTimeoutException    响应超时
	 */
	public ModbusFrame send(ModbusFrame request)
			throws ModbusConnectionException, ModbusException, ModbusTimeoutException {
		ModbusFrame response = null;
		try {
			lock.lock();
			if (channel == null || !channel.isActive()) {
				if (!reconnecting) {
					log.warn("发送失败，未连接服务端");
					log.warn("请求数据包：" + request);
				}
				throw new ModbusConnectionException("连接异常，连接未初始化或不可用");
			}

			if (channel.isWritable()) {
				if (protocol == ModbusProtoType.RTU && handler != null) {
					handler.clearResponseBuf();
				}

				// 响应至再次发送的时间间隔
				long interval = System.currentTimeMillis() - responseTimer;
				if (interval <= ModbusConstant.RESP_WITH_NEXT_SEND_INTERVAL) {
					try {
						Thread.sleep(ModbusConstant.RESP_WITH_NEXT_SEND_INTERVAL - interval);
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				}
				// 发送
				channel.writeAndFlush(request);

				response = waitForResponse(request);
				responseTimer = System.currentTimeMillis();

				// 请求/响应一致性校验
				if (response != null && ((request.getFuncCode() != response.getFuncCode())
						|| (request.getDevAddr() != response.getDevAddr()))) {
					throw new ModbusException("请求/响应数据格式不一致");
				}
			} else {
				log.warn("发送失败，写缓冲区已满");
				log.warn("请求数据包：" + request);
				throw new ModbusConnectionException("连接异常，连接不可写");
			}
		} finally {
			lock.unlock();
		}
		return response;
	}

	/**
	 * 等待响应数据包
	 * 
	 * @param request
	 * @return
	 * @throws ModbusTimeoutException 响应超时
	 */
	private ModbusFrame waitForResponse(ModbusFrame request) throws ModbusTimeoutException {
		ModbusFrame response = null;
		if (protocol == ModbusProtoType.RTU) {
			response = handler.syncResponse();
		} else if (protocol == ModbusProtoType.TCP) {
			response = handler.syncResponse(request.getModbusKey(), request.getModbusErrKey());
		}
		return response;
	}
}
