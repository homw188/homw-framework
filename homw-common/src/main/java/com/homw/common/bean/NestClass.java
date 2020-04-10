package com.homw.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 泛型嵌套类型序列化支持
 * @author Hom
 * @version 1.0
 * @date 2020-03-17
 */
public class NestClass {
	private Class<?> root;
	private NestClass key;
	private NestClass val;

	private static Map<String, Class<?>> primitiveClasses = new HashMap<String, Class<?>>();
	static {
		primitiveClasses.put("byte", byte.class);
		primitiveClasses.put("short", short.class);
		primitiveClasses.put("char", char.class);
		primitiveClasses.put("int", int.class);
		primitiveClasses.put("float", float.class);
		primitiveClasses.put("double", double.class);
		primitiveClasses.put("long", long.class);
		primitiveClasses.put("boolean", boolean.class);
	}

	public NestClass() {
	}

	public NestClass(String className) {
		try {
			this.root = Class.forName(className);
		} catch (ClassNotFoundException e) {
			this.root = primitiveClasses.get(className);
		}
	}

	public NestClass(Class<?> baseClass) {
		this.root = baseClass;
	}

	public Class<?> getRootClass() {
		return root;
	}

	public void setRootClass(Class<?> root) {
		this.root = root;
	}

	public NestClass getKeyClass() {
		return key;
	}

	public void setKeyClass(NestClass key) {
		this.key = key;
	}

	public NestClass getValClass() {
		return val;
	}

	public void setValClass(NestClass valClass) {
		this.val = valClass;
	}

}
