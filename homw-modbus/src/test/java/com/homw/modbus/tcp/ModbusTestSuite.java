package com.homw.modbus.tcp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.homw.modbus.example.ModbusClientSingleton;
import com.homw.modbus.example.ModbusServerSingleton;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ModbusReadCoilsTest.class, ModbusWriteSingleRegisterTest.class,
		ModbusReadHoldingRegistersTest.class, ModbusReadDiscreteInputsTest.class, ModbusReadInputRegistersTest.class,
		ModbusWriteMultipleRegistersTest.class, ModbusWriteMultipleCoilsTest.class, ModbusWriteSingleCoilTest.class,
		ModbusClientSingleton.class })
public class ModbusTestSuite {
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		ModbusClientSingleton.getInstance().getModbusClient().shutdown();
		ModbusServerSingleton.getInstance().getModbusServer().shutdown();
	}
}
