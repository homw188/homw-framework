package com.homw.test.pattern;

/**
 * @description 装饰器模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-11
 */
public class DecoratorPattern {

	public static void main(String[] args) {
		Decorator decoratorA = new DecoratorA();
		decoratorA.setComponent(new ComponentA());

		Decorator decoratorB = new DecoratorB();
		decoratorB.setComponent(decoratorA);
		// DecoratorB -> DecoratorA -> ComponentA
		decoratorB.operate();
	}

	interface Component {
		void operate();
	}

	/** 
	 * 实现{@code Component}，支持嵌套装饰
	 */
	static abstract class Decorator implements Component {
		protected Component component;

		public void setComponent(Component component) {
			this.component = component;
		}

		/**
		 * 子类可扩展
		 */
		@Override
		public void operate() {
			if (component != null) {
				component.operate();
			}
		}
	}

	static class DecoratorA extends Decorator {
		@Override
		public void operate() {
			super.operate();
			System.out.println("DecoratorPattern.DecoratorA.operate()");
		}
	}

	static class DecoratorB extends Decorator {
		@Override
		public void operate() {
			super.operate();
			System.out.println("DecoratorPattern.DecoratorB.operate()");
		}
	}

	static class ComponentA implements Component {
		@Override
		public void operate() {
			System.out.println("DecoratorPattern.ComponentA.operate()");
		}
	}
}
