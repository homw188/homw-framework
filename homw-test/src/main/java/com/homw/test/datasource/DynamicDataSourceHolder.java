package com.homw.test.datasource;

/**
 * @description 记录当前线程的数据源key<br>
 * 	由于DynamicDataSource是单例的，线程不安全，所以采用ThreadLocal保证线程安全
 * @author Hom
 * @version 1.0
 * @since 2020-03-19
 */
public class DynamicDataSourceHolder {

	public static final String MASTER = "master";
	public static final String SLAVE = "slave";
	private static final ThreadLocal<String> holder = new ThreadLocal<>();
	
	public static void putDataSourceKey(String key) {
		holder.set(key);
	}
	
	public static String getDataSourceKey() {
		return holder.get();
	}
	
	public static void changeMaster() {
		putDataSourceKey(MASTER);
	}
	
	public static void chageSlave() {
		putDataSourceKey(SLAVE);
	}
	
	public static boolean isMaster() {
		return getDataSourceKey() != null && MASTER.equals(getDataSourceKey());
	}
}
