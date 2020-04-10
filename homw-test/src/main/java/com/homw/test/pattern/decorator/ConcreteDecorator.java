package com.homw.test.pattern.decorator;

/**
 * @description 装饰器类的实现
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class ConcreteDecorator extends Decorator {
	public ConcreteDecorator(Component component) {
		super(component);
	}

	@Override
	public void operation() {
		component.operation();
		System.out.println("ConcreteDecorator.operation()");
	}
}
