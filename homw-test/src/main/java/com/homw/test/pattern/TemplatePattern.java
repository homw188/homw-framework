package com.homw.test.pattern;

/**
 * @description <b>行为型模式：</b>模板方法模式
 * <p>定义一个逻辑骨架， 子类延伸实现</p>
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 * 
 * @see StrategyPattern
 */
public class TemplatePattern {

	public static void main(String[] args) {
		AbstractClazz clazz = new ClazzA();
		clazz.templateMethod();
	}
	
	static abstract class AbstractClazz {
		protected abstract void partMethod();
		
		final void templateMethod() {
			System.out.println("TemplatePattern.AbstractClazz.templateMethod()");
			partMethod();
		}
	}
	
	static class ClazzA extends AbstractClazz {
		@Override
		protected void partMethod() {
			System.out.println("TemplatePattern.ClazzA.partMethod()");
		}
	}
}
