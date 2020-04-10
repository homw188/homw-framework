package com.homw.common.util;

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
		String osName = System.getProperty("os.name");
		return osName != null && osName.startsWith("Windows");
	}

	public static boolean is32BitJvm() {
		String jvm = System.getProperty("java.vm.name");
		return jvm == null || !jvm.contains("64");
	}

	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}

	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	public static boolean isIDEMode() {
		String classPath = System.getProperty("java.class.path");
		return classPath != null && classPath.contains("target") && classPath.contains("classes");
	}
	
	public static boolean isProdEnv() {
		String runtimeEnv = System.getProperty(RUNTIME_ENV);
		return runtimeEnv != null && runtimeEnv.equals(PROD_ENV);
	}
}