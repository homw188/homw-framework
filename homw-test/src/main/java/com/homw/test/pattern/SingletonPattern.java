package com.homw.test.pattern;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description <b>创建型模式：</b>单例模式
 * <p>保证一个类仅有一个实例，并提供一个访问它的全局访问点</p>
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class SingletonPattern {

	public static void main(String[] args) {
		Runnable task = new Runnable() {
			AtomicInteger singleton = new AtomicInteger();
			@Override
			public void run() {
				int instance = Singleton.getInstance().hashCode();
				if (!singleton.compareAndSet(0, instance) && singleton.get() != instance) {
					System.out.println("singleton broken, prev=" + singleton.get() + ", cur=" + instance);
				}
			}
		};
		for (int i = 0; i < 100; i++) {
			new Thread(task, "Thead-" + i).start();
		}
	}

	static class Singleton {
		private static volatile Singleton instance;

		private Singleton() {}

		public static Singleton getInstance() {
			if (instance == null) {
				synchronized (Singleton.class) {
					if (instance == null) {
						instance = new Singleton();
					}
				}
			}
			return instance;
		}
	}
}
