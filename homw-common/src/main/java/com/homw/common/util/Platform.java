package com.homw.common.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * @description 系统运行时工具类
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class Platform {
	
	public static final String RUNTIME_ENV = "runtime_env";
	public static final String PROD_ENV = "prod";
	
	public static boolean isWindow() {
		String osName = getSystemProperty("os.name");
		return osName != null && osName.startsWith("Windows");
	}

	public static boolean is32BitJvm() {
		String jvm = getSystemProperty("java.vm.name");
		return jvm == null || !jvm.contains("64");
	}

	public static String getJavaVersion() {
		return getSystemProperty("java.version");
	}

	public static String getUserHome() {
		return getSystemProperty("user.home");
	}
	
	public static String getOSName() {
		return getSystemProperty("os.name");
	}
	
	public static String getOSVersion() {
		return getSystemProperty("os.version");
	}
	
	public static String getOSArch() {
		return getSystemProperty("os.arch", "");
	}
	
	public static String getJVMName() {
		return getSystemProperty("java.vm.name");
	}
	
	public static String getJVMRuntimeVersion() {
		return getSystemProperty("java.runtime.version");
	}
	
	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}
	
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
	
	public static int getCPUCores() {
		return Runtime.getRuntime().availableProcessors();
	}
	
	public static String getNetworkInterface() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces != null && interfaces.hasMoreElements()) {
				NetworkInterface ni = interfaces.nextElement();
				if (ni != null) {
					return ni.getDisplayName();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isIDEMode() {
		String classPath = System.getProperty("java.class.path");
		return classPath != null && classPath.contains("target") && classPath.contains("classes");
	}
	
	public static boolean isProdEnv() {
		String runtimeEnv = System.getProperty(RUNTIME_ENV);
		return runtimeEnv != null && runtimeEnv.equals(PROD_ENV);
	}
	
	public static String getSystemProperty(String key) {
		return getSystemProperty(key, null);
	}
	
	public static String getSystemProperty(String key, String defaultValue) {
		String value = System.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		return value.trim();
	}
	
}