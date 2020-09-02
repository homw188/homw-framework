package com.homw.test.pattern;

/**
 * @description <b>创建型模式：</b>工厂模式
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class FactoryPattern {

	public static void main(String[] args) {
		Product product = new FactoryA().create();
		product.func();
		
		product = new FactoryB().create();
		product.func();
	}

	interface Product {
		void func();
	}
	
	interface Factory {
		Product create();
	}

	static class FactoryA implements Factory {
		@Override
		public Product create() {
			return new ProductA();
		}
	}
	
	static class FactoryB implements Factory {
		@Override
		public Product create() {
			return new ProductB();
		}
	}

	static class ProductA implements Product {
		@Override
		public void func() {
			System.out.println("FactoryPattern.ProductA.func()");
		}
	}

	static class ProductB implements Product {
		@Override
		public void func() {
			System.out.println("FactoryPattern.ProductB.func()");
		}
	}
}
