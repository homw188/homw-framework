package com.homw.test.pattern;

/**
 * @description 策略模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-11
 */
public class StrategyPattern {

	public static void main(String[] args) {
		Context ctx = new Context(new StrategyA());
		ctx.action();
		
		ctx = new Context(new StrategyB());
		ctx.action();
	}
	
	static abstract class Strategy {
		abstract void algorithm();
	}
	
	static class StrategyA extends Strategy {
		@Override
		void algorithm() {
			System.out.println("StrategyPattern.StrategyA.algorithm()");
		}
	}
	
	static class StrategyB extends Strategy {
		@Override
		void algorithm() {
			System.out.println("StrategyPattern.StrategyB.algorithm()");
		}
	}
	
	static class Context {
		// 依赖于抽象策略，可替换不同算法实现
		Strategy strategy;
		public Context(Strategy strategy) {
			this.strategy = strategy;
		}
		
		void action() {
			strategy.algorithm();
		}
	}
}
