package com.homw.test.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @description <b>结构型模式：</b>组合模式
 * <p>依据树形结构来组合对象，用来表示部分以及整体层次</p>
 * 
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class CompositePattern {

	public static void main(String[] args) {
		Component root = new Component("root");
		root.add(new Component("A"));
		root.add(new Component("B"));
		
		Component level_1 = new Component("X");
		level_1.add(new Component("XA"));
		level_1.add(new Component("XB"));
		
		root.add(level_1);
		
		Component level_2 = new Component("Y");
		level_2.add(new Component("YA"));
		level_2.add(new Component("YB"));
		
		level_1.add(level_2);
		
		root.display(1);
	}
	
	static class Component {
		String name;
		List<Component> children;
		public Component(String name) {
			this.name = name;
			this.children = new ArrayList<>();
		}
		
		void add(Component component) {
			children.add(component);
		}
		void remove(Component component) {
			children.remove(component);
		}
		
		void display(int depth) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < depth; i ++) {
				sb.append("-");
			}
			System.out.println(sb.append(name).toString());
			
			for (Component component : children) {
				component.display(depth + 2);
			}
		}
	}
	
}
