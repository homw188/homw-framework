package com.homw.common.util;

import org.junit.Test;

public class PlatformTest {

	@Test
	public void test() {
		System.out.println("is window os: " + Platform.isWindow());
		System.out.println("is 32-bit jvm: " + Platform.is32BitJvm());
		System.out.println("is ide mode: " + Platform.isIDEMode());
		System.setProperty(Platform.RUNTIME_ENV, Platform.PROD_ENV);
		System.out.println("is prod env: " + Platform.isProdEnv());
		
		System.out.println("==============================================");
		
		System.out.println("OS: " + Platform.getOSName() + " " + Platform.getOSVersion() + " " + Platform.getOSArch());
		System.out.println("CPU: " + Platform.getCPUCores() + " cores");
		System.out.println("JVM: " + Platform.getJVMName() + " " + Platform.getJVMRuntimeVersion());
		System.out.println("Memory: " + Platform.getTotalMemory() + " bytes (Max: " + Platform.getMaxMemory() + " bytes)");
		System.out.println("Network: " + Platform.getNetworkInterface());
	}
}
