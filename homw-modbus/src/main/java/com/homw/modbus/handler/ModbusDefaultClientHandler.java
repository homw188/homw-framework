package com.homw.modbus.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.struct.ModbusFrame;

/**
 * Modbus客户端处理器默认实现
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月14日
 *
 */
public class ModbusDefaultClientHandler extends ModbusClientHandler {
	private static Logger log = LoggerFactory.getLogger(ModbusDefaultClientHandler.class);

	@Override
	public void handleResponse(ModbusFrame frame) {
		log.debug("客户端收到数据包:" + frame);
	}
}
