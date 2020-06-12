package com.homw.tool.api.keda;

import static org.junit.Assert.*;

import org.junit.Test;

public class KedaApiTest {
	@Test
	public void testFormat() {
		String txadd = String.format("%014d", Long.parseLong("1709002216")); // 14位
		assertEquals("00001709002216", txadd);
		assertEquals(Integer.parseInt(txadd.substring(12), 16), 22);
	}

	@Test
	public void testReadWater() {
		String msg = KDZTService.getSingleInstance().readWaterValue("172.18.18.2", 8002, 1709002216);
		System.out.println("readWaterValue value is: " + msg);
	}

	@Test
	public void testReadWaterSate() {
		String msg = KDZTService.getSingleInstance().readOnOffState_Water("172.18.18.2", 8002, 1709002216);
		System.out.println("readWaterOnOffState value is: " + msg);
	}

	@Test
	public void testWaterOnOff() {
		String msg = KDZTService.getSingleInstance().waterOnOff("172.18.18.2", 8002, 1709002216, "55");// 55开阀 99关阀
		System.out.println("waterOnOff value is: " + msg);
	}

	// ************* 电表 *************//
	
	@Test
	public void testReadElec() {
		String msg = KDZTService.getSingleInstance().readPowerValue("172.18.18.2", 8002, 1, 7);
		System.out.println("readElecValue value is: " + msg);
	}

	@Test
	public void testReadElecSate() {
		String msg = KDZTService.getSingleInstance().readOnOffState("172.18.18.2", 8002, 1, 1);
		System.out.println("readElecOnOffState value is: " + msg);
	}

	@Test
	public void testElecOnOff() {
		String msg = KDZTService.getSingleInstance().eleOnOff("172.18.18.2", 8002, 1, 1, 1);// 0断 1通
		System.out.println("eleOnOff value is: " + msg);
	}
}