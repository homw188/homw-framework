package com.homw.tool.api.keda;

import org.junit.Test;

import com.homw.tool.api.dashi.DashiDoorApi;

public class DashiApiTest {

	@Test
	public void test() throws Exception {
		String ip = "172.16.42.16";
		String port = "0";
		String addr = "0090C285CF5A";
		String readno = "3";

		Integer readNo = Integer.valueOf(readno);
		readNo = (int) Math.pow(2, readNo);
		addr = addr.substring(6);

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("55").append("04").append(addr).append("2D").append("0001").append("0").append(readNo)
				.append("00");
		byte[] sumBytes = DashiDoorApi.checkSum(DashiDoorApi.hex2Bytes(strBuf.toString()), 2);
		strBuf.append(DashiDoorApi.bytes2Hex(sumBytes).toUpperCase());
		System.out.println("checksum: " + strBuf.toString());

		byte[] data = DashiDoorApi.hex2Bytes(strBuf.toString());
		System.out.println("send packet: " + DashiDoorApi.bytes2Hex(data));

		DashiDoorApi.send(ip, Integer.valueOf(port), data);
		System.out.println("open door success.");
	}
}
