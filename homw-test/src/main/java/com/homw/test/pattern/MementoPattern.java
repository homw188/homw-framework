package com.homw.test.pattern;

/**
 * @description 备忘录模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class MementoPattern {

	public static void main(String[] args) {
		Originator originator = new Originator();
		// 初始状态设置
		originator.setState(1);
		originator.show();
		
		// 保存备忘录
		Caretaker caretaker = new Caretaker();
		caretaker.setMemento(originator.createMemento());
		
		// 状态更新
		originator.setState(0);
		originator.show();
		
		// 还原备忘录
		originator.setMemento(caretaker.getMemento());
		originator.show();
	}
	
	static class Memento {
		int state;
		public Memento(int state) {
			this.state = state;
		}
		
		public int getState() {
			return state;
		}
	}
	
	static class Originator {
		int state;
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		
		/**
		 * 创建备忘录
		 * @return
		 */
		public Memento createMemento() {
			return new Memento(state);
		}
		
		/**
		 * 还原备忘录
		 * @param memento
		 */
		public void setMemento(Memento memento) {
			this.state = memento.getState();
		}
		
		public void show() {
			System.out.println("MementoPattern.Originator.show(), state=" + state);
		}
	}
	
	static class Caretaker {
		Memento memento;

		public Memento getMemento() {
			return memento;
		}
		public void setMemento(Memento memento) {
			this.memento = memento;
		}
	}
}
