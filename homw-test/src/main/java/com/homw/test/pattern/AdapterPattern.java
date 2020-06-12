package com.homw.test.pattern;

/**
 * @description 适配器模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class AdapterPattern {

	public static void main(String[] args) {
		Target target = new Adapter(new Adaptee());
		target.operate();
	}
	
	static class Adapter extends Target {
		// 被适配者
		Adaptee adaptee;
		
		public Adapter(Adaptee adaptee) {
			this.adaptee = adaptee;
		}
		
		@Override
		void operate() {
			adaptee.handle();
		}
	}
	
	static abstract class Target {
		void operate() {
			System.out.println("AdapterPattern.Target.operate()");
		}
	}
	
	static class Adaptee {
		void handle() {
			System.out.println("AdapterPattern.Adaptee.handle()");
		}
	}
}
