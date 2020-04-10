package com.homw.test.pattern.decorator;

/**
 * @description 装饰器类
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public abstract class Decorator implements Component {
	protected Component component;

	public Decorator(Component component) {
		this.component = component;
	}
}
