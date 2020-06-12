package com.homw.test.pattern;

/**
 * @description 责任链模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-11
 */
public class ChainPattern {

	public static void main(String[] args) {
		Handler handlerA = new HandlerA();
		Handler handlerB = new HandlerB();
		Handler handlerC = new HandlerC();
		
		handlerA.setNext(handlerB);
		handlerB.setNext(handlerC);
		
		ContextEnum[] ctxs = new ContextEnum[] {ContextEnum.C, ContextEnum.A, ContextEnum.B};
		for (ContextEnum ctx : ctxs) {
			// 头节点开始
			handlerA.handle(ctx);
		}
	}
	
	static abstract class Handler {
		protected Handler next;
		// 非强关联
		public void setNext(Handler next) {
			this.next = next;
		}
		
		void handle(ContextEnum ctx) {
			// 防止末节点NPE
			if (next != null) {
				next.handle(ctx);
			}
		}
	}
	
	static class HandlerA extends Handler {
		@Override
		void handle(ContextEnum ctx) {
			// 责任判断
			if (ContextEnum.A == ctx) {
				System.out.println("ChainPattern.HandlerA.handle()");
			} else {
				// 链式调用
				super.handle(ctx);
			}
		}
	}
	
	static class HandlerB extends Handler {
		@Override
		void handle(ContextEnum ctx) {
			if (ContextEnum.B == ctx) {
				System.out.println("ChainPattern.HandlerB.handle()");
			} else {
				super.handle(ctx);
			}
		}
	}
	
	static class HandlerC extends Handler {
		@Override
		void handle(ContextEnum ctx) {
			if (ContextEnum.C == ctx) {
				System.out.println("ChainPattern.HandlerC.handle()");
			} else {
				super.handle(ctx);
			}
		}
	}
	
	static enum ContextEnum {
		A, B, C
	}
}
