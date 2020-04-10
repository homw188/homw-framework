package com.homw.modbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.exception.ModbusException;

/**
 * Modbus启动类
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月16日
 *
 */
public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws ModbusException {
		if (args == null || args.length < 1) {
			log.error("至少需要一个参数，参数格式：server|client [config]");
			System.exit(1);
		}
		String mode = args[0];

		String cfg = null;
		if (args.length == 2) {
			cfg = args[1];
		}

		if ("server".equals(mode)) {
			if (cfg == null) {
				ModbusServerFactory.create();
			} else {
				ModbusServerFactory.create(cfg);
			}
		} else if ("client".equals(mode)) {
			if (cfg == null) {
				ModbusClientFactory.create();
			} else {
				ModbusClientFactory.create(cfg);
			}
		} else {
			log.error("第一个参数（运行模式）：" + mode + " 格式不对，'server'或'client'");
			System.exit(1);
		}
	}
}
