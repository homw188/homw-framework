package com.homw.test.pattern;

/**
 * @description <b>结构型模式：</b>适配器模式
 * <p>将一个类的接口<b>转换</b>成用户需要的接口</p>
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 * 
 * @see ProxyPattern
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
