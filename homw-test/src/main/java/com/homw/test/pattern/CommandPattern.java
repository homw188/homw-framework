package com.homw.test.pattern;

/**
 * @description <b>行为型模式：</b>命令模式
 * @author Hom
 * @version 1.0
 * @since 2020-06-11
 * 
 * @see StrategyPattern
 */
public class CommandPattern {

	public static void main(String[] args) {
		Receiver receiver = new Receiver();
		Command command = new CommandA(receiver);
		
		Invoker invoker = new Invoker();
		// 设置命令
		invoker.setCommand(command);
		// 执行命令
		invoker.exec();
	}
	
	static abstract class Command {
		protected Receiver receiver;
		// 强关联
		public Command(Receiver receiver) {
			this.receiver = receiver;
		}
		
		abstract void execute();
	}
	
	/**
	 * 命令请求执行器
	 */
	static class Receiver {
		void action() {
			System.out.println("CommandPattern.Receiver.action()");
		}
	}
	
	static class CommandA extends Command {
		
		public CommandA(Receiver receiver) {
			super(receiver);
		}
		
		@Override
		void execute() {
			receiver.action();
		}
	}
	
	static class Invoker {
		Command command;
		
		public void setCommand(Command command) {
			this.command = command;
		}
		
		void exec() {
			command.execute();
		}
	}
}
