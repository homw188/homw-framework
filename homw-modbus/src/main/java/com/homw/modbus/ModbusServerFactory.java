package com.homw.modbus;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.util.ModbusConfigLoader;

/**
 * Modbus服务端工厂
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusServerFactory {
	/**
	 * 创建Modbus服务端，并启动
	 * 
	 * @return
	 * @throws ModbusException 启动异常
	 */
	public static ModbusServer create() throws ModbusException {
		return create(ModbusConfigLoader.DEFAULT_CONFIG_FILE);
	}

	/**
	 * 创建Modbus服务端，并启动
	 * 
	 * @param cfgPropertiesFile 配置文件
	 * @return
	 * @throws ModbusException 启动异常
	 */
	public static ModbusServer create(String cfgPropertiesFile) throws ModbusException {
		ModbusConfigLoader.load(true, cfgPropertiesFile);

		ModbusServer server = new ModbusServer(ModbusConfigLoader.getProtoType());
		ModbusConfigLoader.getServerHandler().setServer(server);
		server.start(ModbusConfigLoader.getPort(), ModbusConfigLoader.getServerHandler());
		return server;
	}
}
