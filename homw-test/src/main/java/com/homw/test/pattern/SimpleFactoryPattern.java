package com.homw.test.pattern;

/**
 * @description <b>创建型模式：</b>简单工厂模式
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class SimpleFactoryPattern {

	public static void main(String[] args) {
		Product product = ProductFactory.create(ProductTypeEnum.A);
		product.func();
		
		product = ProductFactory.create(ProductTypeEnum.B);
		product.func();
	}

	interface Product {
		void func();
	}

	static class ProductFactory {
		public static Product create(ProductTypeEnum type) {
			Product product = null;
			switch (type) {
				case A:
					product = new ProductA();
					break;
				case B:
					product = new ProductB();
					break;
			}
			return product;
		}
	}

	static class ProductA implements Product {
		@Override
		public void func() {
			System.out.println("SimpleFactoryPattern.ProductA.func()");
		}
	}

	static class ProductB implements Product {
		@Override
		public void func() {
			System.out.println("SimpleFactoryPattern.ProductB.func()");
		}
	}

	static enum ProductTypeEnum {
		A, B
	}
}
