package com.homw.modbus;

import org.junit.Assert;
import org.junit.Test;

import com.homw.modbus.exception.ModbusException;

/**
 * Modbus服务端测试
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusServerTest {
	@Test
	public void test() throws ModbusException {
		ModbusServer server = ModbusServerFactory.create("http://localhost:8088/examples/modbus.properties");
		Assert.assertNotNull(server);
		while (true);
	}
}