package com.homw.modbus;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.handler.ModbusClientHandler;
import com.homw.modbus.util.ModbusConfigLoader;

/**
 * <p>
 * Modbus485客户端创建工厂
 * </P>
 * 
 * <p>
 * 为适应串口通信特点，保证同一时刻仅有一个连接，避免总线混乱问题
 * </P>
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusClientFactory {
	private static ModbusClient serialClient;

	/**
	 * 创建Modbus客户端，并启动。加载默认配置
	 * 
	 * @return
	 * @throws ModbusException 启动异常
	 */
	public synchronized static ModbusClient create() throws ModbusException {
		if (serialClient != null) {
			if (!serialClient.isShutdown()) {
				serialClient.shutdown();
			}
			serialClient = null;
		}
		serialClient = create(ModbusConfigLoader.DEFAULT_CONFIG_FILE);
		return serialClient;
	}

	/**
	 * 创建Modbus客户端，并启动
	 * 
	 * @param cfgPropertiesFile 配置文件路径
	 * @return
	 * @throws ModbusException 启动异常
	 */
	public synchronized static ModbusClient create(String cfgPropertiesFile) throws ModbusException {
		if (serialClient != null) {
			if (!serialClient.isShutdown()) {
				serialClient.shutdown();
			}
			serialClient = null;
		}

		ModbusConfigLoader.load(false, cfgPropertiesFile);

		serialClient = new ModbusClient(ModbusConfigLoader.getProtoType());
		ModbusConfigLoader.getClientHandler().setClient(serialClient);
		serialClient.connect(ModbusConfigLoader.getHost(), ModbusConfigLoader.getPort(),
				ModbusConfigLoader.getClientHandler());
		return serialClient;
	}

	/**
	 * 创建Modbus客户端，并启动。忽略配置文件设置
	 * 
	 * @param host      服务ip
	 * @param port      服务监听端口
	 * @param protoType 协议类型 {@link ModbusProtoType}
	 * @param handler   客户端消息处理器
	 * @return
	 * @throws ModbusException 启动异常
	 */
	public synchronized static ModbusClient create(String host, int port, ModbusProtoType protoType,
			ModbusClientHandler handler) throws ModbusException {
		if (serialClient != null) {
			if (!serialClient.isShutdown()) {
				serialClient.shutdown();
			}
			serialClient = null;
		}

		serialClient = new ModbusClient(protoType);
		handler.setClient(serialClient);
		serialClient.connect(host, port, handler);
		return serialClient;
	}
}
