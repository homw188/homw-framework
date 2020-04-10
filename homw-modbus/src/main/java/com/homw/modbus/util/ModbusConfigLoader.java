package com.homw.modbus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.ModbusProtoType;
import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.handler.ModbusClientHandler;
import com.homw.modbus.handler.ModbusServerHandler;
import com.homw.modbus.handler.rtu.ModbusRTUServerHandler;
import com.homw.modbus.handler.tcp.ModbusTCPServerHandler;

/**
 * Modbus配置加载器
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusConfigLoader {
	private static String host = "127.0.0.1";
	private static int port = 8181;
	private static ModbusProtoType protoType = ModbusProtoType.RTU;
	private static ModbusClientHandler clientHandler;
	private static ModbusServerHandler serverHandler;

	public static final String DEFAULT_CONFIG_FILE = "modbus.properties";
	public static final String DEFAULT_CLIENT_HANDLER_CLASS = "com.homw.modbus.handler.ModbusDefaultClientHandler";
	public static final String DEFAULT_SERVER_RTU_HANDLER_CLASS = "com.homw.modbus.handler.rtu.ModbusRTUServerHandler";
	public static final String DEFAULT_SERVER_TCP_HANDLER_CLASS = "com.homw.modbus.handler.tcp.ModbusTCPServerHandler";

	private static final String SERVER_HOST_KEY = "modbus.server.host";
	private static final String SERVER_PORT_KEY = "modbus.server.port";
	private static final String PROTOCOL_KEY = "modbus.protocol.type";
	private static final String SERVER_HANDLER_KEY = "modbus.server.handlerClass";
	private static final String CLIENT_HANDLER_KEY = "modbus.client.handlerClas";

	private static Properties prop = new Properties();
	private static AtomicBoolean loaded = new AtomicBoolean(false);

	private static Logger log = LoggerFactory.getLogger(ModbusConfigLoader.class);

	/**
	 * 加载配置，可重复执行，已加载则忽略
	 * 
	 * @param serverMode
	 * @throws ModbusException
	 */
	public static void load(boolean serverMode) throws ModbusException {
		load(serverMode, DEFAULT_CONFIG_FILE);
	}

	/**
	 * 加载配置，可重复执行，已加载则忽略
	 * 
	 * @param serverMode        是否服务端模式
	 * @param cfgPropertiesFile 配置文件
	 * @throws ModbusException
	 */
	public static void load(boolean serverMode, String cfgPropertiesFile) throws ModbusException {
		if (loaded.compareAndSet(false, true)) {
			doLoad(cfgPropertiesFile);
		}

		synchronized (ModbusConfigLoader.class) {
			if (serverMode) {
				if (serverHandler == null) {
					parseServerConfig(prop);
				}
			} else {
				if (clientHandler == null) {
					parseClientConfig(prop);
				}
			}
		}
	}

	/**
	 * 执行加载逻辑
	 * 
	 * @param cfgPropertiesFile 配置文件
	 */
	private static void doLoad(String cfgPropertiesFile) {
		InputStream in = null;
		try {
			in = getConfigStream(cfgPropertiesFile);

			if (in != null) {
				prop.load(in);
				parseBaseConfig(prop);

				log.debug("Modbus基础配置加载完成");
			} else {
				log.warn("Modbus基础配置加载异常，未找到文件：" + cfgPropertiesFile);
			}
		} catch (IOException e) {
			log.warn("Modbus基础配置加载异常：" + e.getLocalizedMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("host=" + host + ", port=" + port + ", protocolType=" + protoType);
	}

	/**
	 * 获取配置流
	 * 
	 * @param cfgPropertiesFile 配置文件
	 * @return
	 */
	private static InputStream getConfigStream(String cfgPropertiesFile) {
		String cfg = null;
		InputStream in = null;

		if (cfgPropertiesFile == null || cfgPropertiesFile.trim().isEmpty()
				|| !cfgPropertiesFile.endsWith(".properties")) {
			log.warn("配置文件不合法：" + cfgPropertiesFile);
		} else {
			cfg = cfgPropertiesFile;
		}

		// *** class path下查找 ***//
		if (in == null && cfg != null) {
			in = ModbusConfigLoader.class.getClassLoader().getResourceAsStream(cfg);
		}
		if (in == null && cfg != null) {
			// 从root下查找
			String path = cfg.startsWith(File.separator) ? cfg : File.separator + cfg;
			in = ModbusConfigLoader.class.getResourceAsStream(path);
		}

		// *** 文件系统下查找 ***//
		if (in == null && cfg != null) {
			try {
				in = new FileInputStream(new File(cfg));
			} catch (FileNotFoundException e) {
				// log.warn("尝试文件系统方式加载失败，文件未找到：" + cfg);
			}
		}

		// *** 网络资源下查找 ***//
		if (in == null && cfg != null) {
			try {
				URL url = new URL(cfg);
				in = url.openStream();
			} catch (IOException e) {
				// log.warn("尝试网络资源方式加载失败：" + cfg);
			}
		}

		// *** 加载class path下默认配置 ***//
		if (in == null) {
			log.warn("加载指定配置文件失败：" + cfg);
			log.warn("加载默认配置文件：" + DEFAULT_CONFIG_FILE);
			in = getDefaultConfigStream();
		}
		return in;
	}

	/**
	 * 获取默认配置流
	 * 
	 * @return
	 */
	private static InputStream getDefaultConfigStream() {
		return ModbusConfigLoader.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
	}

	/**
	 * 解析配置
	 * 
	 * @param prop
	 */
	private static void parseBaseConfig(Properties prop) {
		host = prop.getProperty(SERVER_HOST_KEY);
		try {
			port = Integer.valueOf(prop.getProperty(SERVER_PORT_KEY));
		} catch (NumberFormatException e) {
			log.warn("Modbus端口解析异常：" + prop.getProperty(SERVER_PORT_KEY));
		}

		String typeStr = prop.getProperty(PROTOCOL_KEY);
		if (typeStr != null && !typeStr.trim().isEmpty()) {
			ModbusProtoType type = ModbusProtoType.valueOf(typeStr.toUpperCase());
			if (type != null) {
				protoType = type;
			} else {
				log.warn("Modbus协议类型解析异常：" + typeStr);
			}
		}
	}

	/**
	 * 解析服务端配置
	 * 
	 * @param prop
	 * @throws ModbusException 处理器与协议类型匹配检查异常
	 */
	@SuppressWarnings("rawtypes")
	private static void parseServerConfig(Properties prop) throws ModbusException {
		String serverHandlerClass = prop.getProperty(SERVER_HANDLER_KEY);
		if (serverHandlerClass == null || serverHandlerClass.trim().isEmpty()) {
			serverHandlerClass = getProtoType() == ModbusProtoType.RTU ? DEFAULT_SERVER_RTU_HANDLER_CLASS
					: DEFAULT_SERVER_TCP_HANDLER_CLASS;
		}

		try {
			Class serverClazz = Class.forName(serverHandlerClass);
			serverHandler = (ModbusServerHandler) serverClazz.newInstance();
		} catch (ClassNotFoundException e) {
			log.warn("Modbus服务端处理器类未找到：" + serverHandlerClass);
		} catch (InstantiationException | IllegalAccessException e) {
			log.warn("Modbus服务端处理器实例化异常：" + e.getLocalizedMessage());
		}

		if (serverHandler == null) {
			try {
				Class serverClazz = Class
						.forName(getProtoType() == ModbusProtoType.RTU ? DEFAULT_SERVER_RTU_HANDLER_CLASS
								: DEFAULT_SERVER_TCP_HANDLER_CLASS);
				serverHandler = (ModbusServerHandler) serverClazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (serverHandler != null) {
			if ((getProtoType() == ModbusProtoType.RTU && serverHandler instanceof ModbusTCPServerHandler)
					|| (getProtoType() == ModbusProtoType.TCP && serverHandler instanceof ModbusRTUServerHandler)) {
				throw new ModbusException("服务端处理器和协议类型不匹配，请检查配置");
			}
			log.debug("Modbus服务端配置加载完成");
			log.debug("handlerClass=" + serverHandler.getClass().getCanonicalName());
		}
	}

	/**
	 * 解析客户端配置
	 * 
	 * @param prop
	 */
	@SuppressWarnings("rawtypes")
	private static void parseClientConfig(Properties prop) {
		String clientHandlerClass = prop.getProperty(CLIENT_HANDLER_KEY);
		if (clientHandlerClass == null || clientHandlerClass.trim().isEmpty()) {
			clientHandlerClass = DEFAULT_CLIENT_HANDLER_CLASS;
		}

		try {
			Class clientClazz = Class.forName(clientHandlerClass);
			clientHandler = (ModbusClientHandler) clientClazz.newInstance();
		} catch (ClassNotFoundException e) {
			log.warn("Modbus客户端处理器类未找到：" + clientHandlerClass);
		} catch (InstantiationException | IllegalAccessException e) {
			log.warn("Modbus客户端处理器实例化异常：" + e.getLocalizedMessage());
		}

		if (clientHandler == null) {
			try {
				Class clientClazz = Class.forName(DEFAULT_CLIENT_HANDLER_CLASS);
				clientHandler = (ModbusClientHandler) clientClazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (clientHandler != null) {
			log.debug("Modbus客户端配置加载完成");
			log.debug("handlerClass=" + clientHandler.getClass().getCanonicalName());
		}
	}

	/**
	 * 获取远程服务主机
	 * 
	 * @return
	 */
	public static String getHost() {
		return host;
	}

	/**
	 * 获取远程服务端口
	 * 
	 * @return
	 */
	public static int getPort() {
		return port;
	}

	/**
	 * 获取协议类型
	 * 
	 * @return
	 */
	public static ModbusProtoType getProtoType() {
		return protoType;
	}

	/**
	 * 获取客户处理器
	 * 
	 * @return
	 */
	public synchronized static ModbusClientHandler getClientHandler() {
		return clientHandler;
	}

	/**
	 * 获取服务端处理器
	 * 
	 * @return
	 */
	public synchronized static ModbusServerHandler getServerHandler() {
		return serverHandler;
	}
}
