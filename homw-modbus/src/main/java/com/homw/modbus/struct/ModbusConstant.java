package com.homw.modbus.struct;

/**
 * Modbus相关常量
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusConstant {
	public static final int ADU_MAX_LENGTH = 260;
	public static final int MBAP_LENGTH = 7;
	public static final int RTU_MIN_FRAME_LENGTH = 5;

	// maximum of 1968 bits
	public static final int REQUEST_COILS_MAX_COUNT = 246;
	// maximum of 2000 bits
	public static final int RESPONSE_COILS_MAX_COUNT = 250;
	// maximum of 125 registers
	public static final int REGISTERS_MAX_COUNT = 125;

	public static final int RECONNECT_INTERVAL = 10 * 1000;

	public static final int RESPONSE_BUF = 100;
	/**
	 * 避免总线混乱，确保串口服务器无响应，该值不宜设置过小
	 */
	public static final int RESPONSE_TIMEOUT = 5 * 1000;
	public static final int RESPONSE_ALIVE_TIME = 60 * 1000;
	public static final int CACHE_CHECK_INTERVAL = 10 * 60 * 1000;

	/**
	 * 响应至再次发送间隔必须大于11毫秒
	 */
	public static final int RESP_WITH_NEXT_SEND_INTERVAL = 15;

	public static final int ERROR_OFFSET = 0x80;
	/**
	 * 模拟量地址偏移量
	 */
	public static final int ANALOG_ADDR_OFFSET = 40000;

	public static final short DEFAULT_PROTOCOL_IDENTIFIER = 0;
	public static final short DEFAULT_UNIT_IDENTIFIER = 255;
	public static final int TRANSACTION_IDENTIFIER_MAX = 100;
}