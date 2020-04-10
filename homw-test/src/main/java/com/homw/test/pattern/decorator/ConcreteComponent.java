package com.homw.test.pattern.decorator;

/**
 * @description 被装饰组件的默认实现
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class ConcreteComponent implements Component {
	@Override
	public void operation() {
		System.out.println("ConcreteComponent.operation()");
	}
}
