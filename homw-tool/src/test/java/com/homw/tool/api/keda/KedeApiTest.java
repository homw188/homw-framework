package com.homw.tool.api.keda;

import org.junit.Test;

import com.homw.tool.api.kede.KedeMeterUtil;

public class KedeApiTest {
	@Test
	public void testReadWater() {
		String msg = KedeMeterUtil.readWaterData("172.31.253.145", 10001, "000000052047", 5);
		System.out.println("readWaterValue value is: " + msg);
	}
	
	@Test
	public void testWaterOnOff() {
		String msg = KedeMeterUtil.pullWaterSwitch("172.31.253.145", 10001, "000000052047", 0, 10);
		System.out.println("waterOnOff value is: " + msg);
	}

	@Test
	public void testReadElec() {
		String msg = KedeMeterUtil.readElecData("172.31.253.145", 10001, "000014330403", 5);
		System.out.println("readElecValue value is: " + msg);
	}

	@Test
	public void testElecOnOff() {
		String msg = KedeMeterUtil.pullElecSwitch("172.16.252.3", 10001, "000010745109", 0, 10);
		System.out.println("eleOnOff value is: " + msg);
	}
}