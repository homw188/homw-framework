package com.homw.tool.application;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @description 应用工厂
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public class ApplicationFactory {
	
	private static Map<String, String> appMap = new HashMap<>();
	
	private ApplicationFactory() {}

	/**
	 * 创建应用
	 * 
	 * @param appKey
	 * @return
	 * @throws Exception
	 */
	public static Application create(String appKey) throws Exception {
		String className = appMap.get(appKey);
		if (className == null) {
			throw new IllegalArgumentException("application [" + appKey + "] not found.");
		}

		try {
			Class<?> clazz = Class.forName(className);
			if (Application.class.isAssignableFrom(clazz)) {
				return (Application) clazz.newInstance();
			} else {
				throw new IllegalArgumentException("application [" + appKey + "] not supported.");
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("class [" + className + "] not found.", e);
		}
	}
	
	/**
	 * 初始化注册应用
	 * @param packageName
	 * @throws Exception
	 */
	public static void init(String packageName) throws Exception {
		String path = packageName.replaceAll("\\.", "/");
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String protocol = resource.getProtocol();
			if ("file".equals(protocol)) {
				File dir = new File(resource.getFile());
				for (File file : dir.listFiles()) {
					if (file.isDirectory()) {
						init(packageName + "." + file.getName());
					} else if (file.getName().endsWith(".class")) {
						String className = packageName + "." + file.getName().replace(".class", "");
						Class<?> clazz = Class.forName(className);
						com.homw.tool.annotation.Application appAnnotaion = clazz.getAnnotation(
								com.homw.tool.annotation.Application.class);
						if (appAnnotaion != null) {
							appMap.put(appAnnotaion.value(), className);
						}
					}
				}
			} else if ("jar".equals(protocol)) {
				String jarpath = resource.getPath();
				jarpath = jarpath.replace("file:/", "");
				jarpath = jarpath.substring(0, jarpath.indexOf("!"));
				
				JarFile jarFile = new JarFile(jarpath);
				try {
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String entryName = entry.getName();
						if (entryName.startsWith(path) && entryName.endsWith(".class")) {
							String className = entryName.replace('/', '.');
							className = className.substring(0, className.length() - 6);
							Class<?> clazz = Class.forName(className);
							com.homw.tool.annotation.Application appAnnotaion = clazz
									.getAnnotation(com.homw.tool.annotation.Application.class);
							if (appAnnotaion != null) {
								appMap.put(appAnnotaion.value(), className);
							}
						}
					} 
				} finally {
					jarFile.close();
				}
			}
		}
	}

	public static Map<String, String> getAppMap() {
		return Collections.unmodifiableMap(appMap);
	}
}