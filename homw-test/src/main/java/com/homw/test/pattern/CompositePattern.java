package com.homw.test.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 组合模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class CompositePattern {

	public static void main(String[] args) {
		Composite root = new Composite("root");
		root.add(new Component("A"));
		root.add(new Component("B"));
		
		Composite level_1 = new Composite("X");
		level_1.add(new Component("XA"));
		level_1.add(new Component("XB"));
		
		root.add(level_1);
		
		Composite level_2 = new Composite("Y");
		level_2.add(new Component("YA"));
		level_2.add(new Component("YB"));
		
		level_1.add(level_2);
		
		root.display(1);
	}
	
	static class Component {
		String name;
		public Component(String name) {
			this.name = name;
		}
		void display(int depth) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < depth; i ++) {
				sb.append("-");
			}
			System.out.println(sb.append(name).toString());
		}
	}
	
	static class Composite extends Component {
		List<Component> children = new ArrayList<>();
		
		public Composite(String name) {
			super(name);
		}

		void add(Component component) {
			children.add(component);
		}
		void remove(Component component) {
			children.remove(component);
		}

		@Override
		void display(int depth) {
			super.display(depth);
			
			for (Component component : children) {
				component.display(depth + 2);
			}
		}
	}
	
}
