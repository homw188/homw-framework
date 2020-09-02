package com.homw.test.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @description <b>行为型模式：</b>调停者模式
 * <p>用来降低多个对象和类之间的通信复杂性</p>
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class MediatorPattern {

	public static void main(String[] args) {
		Mediator mediator = new MediatorA();
		
		Colleague colleagueA = new ColleagueA(mediator);
		Colleague colleagueB = new ColleagueB(mediator);
		
		mediator.add(colleagueA);
		mediator.add(colleagueB);
		
		colleagueA.send("Hello, from A");
		colleagueB.send("Hello, from B");
	}
	
	static abstract class Mediator {
		List<Colleague> colleagues = new ArrayList<>();
		void add(Colleague colleague) {
			colleagues.add(colleague);
		}
		abstract void send(String message, Colleague colleague);
	}
	
	static abstract class Colleague {
		Mediator mediator;
		public Colleague(Mediator mediator) {
			this.mediator = mediator;
		}
		
		void send(String message) {
			mediator.send(message, this);
		}
		
		abstract void back(String message);
	}
	
	static class MediatorA extends Mediator {
		@Override
		void send(String message, Colleague colleague) {
			for (Colleague other : colleagues) {
				if (other != null && !other.equals(colleague)) {
					other.back(message);
				}
			}
		}
	}
	
	static class ColleagueA extends Colleague {

		public ColleagueA(Mediator mediator) {
			super(mediator);
		}

		@Override
		void back(String message) {
			System.out.println("MediatorPattern.ColleagueA.back(), message=" + message);
		}
	}
	
	static class ColleagueB extends Colleague {

		public ColleagueB(Mediator mediator) {
			super(mediator);
		}

		@Override
		void back(String message) {
			System.out.println("MediatorPattern.ColleagueB.back(), message=" + message);
		}
	}
}
