package com.homw.test.pattern.decorator;

import org.junit.Test;

/**
 * 装饰器模式测试
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月2日
 *
 */
public class TestDecorator {
	@Test
	public void test() {
		// 1.创建被装饰对象
		Component component = new ConcreteComponent();
		// 2.通过构造参数（component）创建装饰器对象
		Decorator decorator = new ConcreteDecorator(component);
		// 3.调用装饰后的对象
		decorator.operation();
	}
}
