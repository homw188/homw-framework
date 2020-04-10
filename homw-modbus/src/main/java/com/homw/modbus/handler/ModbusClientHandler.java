package com.homw.modbus.handler;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.ModbusClient;
import com.homw.modbus.ModbusProtoType;
import com.homw.modbus.exception.ModbusTimeoutException;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.ModbusResponseCache;
import com.homw.modbus.util.ModbusConfigLoader;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Modbus客户端处理器，兼容RTU和TCP
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
@Sharable
public abstract class ModbusClientHandler extends SimpleChannelInboundHandler<ModbusFrame> {
	protected ModbusClient client;
	private AtomicReference<ModbusFrame> responseBuf = new AtomicReference<ModbusFrame>(null);

	private static Logger log = LoggerFactory.getLogger(ModbusClientHandler.class);

	/**
	 * 设置客户端引用，在客户端连接 {@code ModbusClient#connect(String, int, ModbusClientHandler)}
	 * 前调用
	 * 
	 * @param client
	 */
	public void setClient(ModbusClient client) {
		this.client = client;
	}

	/**
	 * 同步阻塞获取响应结果
	 * 
	 * @param key    数据包唯一标识
	 * @param errKey 异常包标识
	 * @return
	 */
	public ModbusFrame syncResponse(String key, String errKey) throws ModbusTimeoutException {
		long timeoutTime = System.currentTimeMillis() + ModbusConstant.RESPONSE_TIMEOUT;
		ModbusFrame frame = null;
		do {
			frame = ModbusResponseCache.getInstance().getAndRemove(key);
			if (frame == null) {
				frame = ModbusResponseCache.getInstance().getAndRemove(errKey);
			}
		} while (frame == null && (timeoutTime - System.currentTimeMillis()) > 0);

		if (frame == null) {
			throw new ModbusTimeoutException("响应超时：" + ModbusConstant.RESPONSE_TIMEOUT + " 毫秒");
		}
		return frame;
	}

	/**
	 * 同步阻塞获取响应结果
	 * 
	 * @return
	 * @throws ModbusTimeoutException
	 */
	public ModbusFrame syncResponse() throws ModbusTimeoutException {
		long timeoutTime = System.currentTimeMillis() + ModbusConstant.RESPONSE_TIMEOUT;
		ModbusFrame frame = null;
		do {
			frame = responseBuf.get();
		} while (frame == null && (timeoutTime - System.currentTimeMillis()) > 0);

		if (frame == null) {
			throw new ModbusTimeoutException("响应超时：" + ModbusConstant.RESPONSE_TIMEOUT + " 毫秒");
		}
		return frame;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ModbusFrame response) throws Exception {
		if (ModbusConfigLoader.getProtoType() == ModbusProtoType.RTU) {
			responseBuf.set(response);
		} else if (ModbusConfigLoader.getProtoType() == ModbusProtoType.TCP) {
			ModbusResponseCache.getInstance().putIfAbsent(response.getModbusKey(), response);
		}
		handleResponse(response);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (client.isShutdown()) {
			super.channelInactive(ctx);
		} else {
			ctx.close();
			if (ModbusConfigLoader.getProtoType() == ModbusProtoType.TCP) {
				ModbusResponseCache.getInstance().clear();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.warn("响应处理异常:" + cause.getLocalizedMessage());

		if (client.isShutdown()) {
			super.exceptionCaught(ctx, cause);
		} else {
			ctx.close();
			if (ModbusConfigLoader.getProtoType() == ModbusProtoType.TCP) {
				ModbusResponseCache.getInstance().clear();
			}
		}
	}

	/**
	 * 清除响应缓存
	 * 
	 * @return 之前的缓存值
	 */
	public ModbusFrame clearResponseBuf() {
		return this.responseBuf.getAndSet(null);
	}

	/**
	 * 处理服务端响应
	 * 
	 * @param response
	 */
	protected abstract void handleResponse(ModbusFrame response);
}
