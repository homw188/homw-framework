package com.homw.test.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 观察者模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class ObserverPattern {

	public static void main(String[] args) {
		Subject subject = new Subject();
		subject.attach(new ObserverA());
		subject.attach(new ObserverB());
		
		subject.setState(1);
		subject.stateChanged();
	}
	
	static class Subject {
		// 被观察状态
		int state;
		// 观察者集合
		List<Observer> observers = new ArrayList<>();
		
		public void attach(Observer observer) {
			observers.add(observer);
		}
		
		public void detach(Observer observer) {
			observers.remove(observer);
		}
		
		/**
		 * 状态更新，通知所有观察者
		 */
		void stateChanged() {
			for (Observer observer : observers) {
				observer.update(this);
			}
		}
		
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
	}
	
	interface Observer {
		void update(Subject subject);
	}
	
	static class ObserverA implements Observer {
		@Override
		public void update(Subject subject) {
			System.out.println("ObserverPattern.ObserverA.update(), state=" + subject.getState());
		}
	}
	
	static class ObserverB implements Observer {
		@Override
		public void update(Subject subject) {
			System.out.println("ObserverPattern.ObserverB.update(), state=" + subject.getState());
		}
	}
}
