package com.homw.test.pattern;

/**
 * @description 模板方法模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class TemplateMethodPattern {

	public static void main(String[] args) {
		AbstractClazz clazz = new ClazzA();
		clazz.templateMethod();
	}
	
	static abstract class AbstractClazz {
		protected abstract void partMethod();
		
		final void templateMethod() {
			System.out.println("TemplateMethodPattern.AbstractClazz.templateMethod()");
			partMethod();
		}
	}
	
	static class ClazzA extends AbstractClazz {
		@Override
		protected void partMethod() {
			System.out.println("TemplateMethodPattern.ClazzA.partMethod()");
		}
	}
}
