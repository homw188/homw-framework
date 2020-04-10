package com.homw.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.common.bean.NestClass;

/**
 * @description 反射工具类
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public class ReflectUtil {

	private static final String SETTER_PREFIX = "set";
	private static final String GETTER_PREFIX = "get";
	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	/**
	 * 调用Getter方法，支持多级（如：对象名.对象名.方法）
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")) {
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
			object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。 支持多级（如：对象名.对象名.方法）
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i = 0; i < names.length; i++) {
			if (i < names.length - 1) {
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
			} else {
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invokeMethodByName(object, setterMethodName, new Object[] { value });
			}
		}
	}

	/**
	 * 直接读取对象属性值
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);
		Validate.notNull(field, "Could not find field [{0}] on target [{1}]", fieldName, obj);

		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 直接设置对象属性值
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);
		Validate.notNull(field, "Could not find field [{0}] on target [{1}]", fieldName, obj);

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		Validate.notNull(method, "Could not find method [{0}] on target [{1}]", methodName, obj);
		
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 只匹配方法名，如果有多个同名函数调用第一个
	 */
	public static Object invokeMethodByName(Object obj, String methodName, Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		Validate.notNull(method, "Could not find method [{0}] on target [{1}]", methodName, obj);

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取可访问的Field，自动向上查找
	 */
	public static Field getAccessibleField(Object obj, String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {
				continue;
			}
		}
		return null;
	}

	/**
	 * 获取可访问的Method，自动向上查找
	 */
	public static Method getAccessibleMethod(Object obj, String methodName, Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				continue;
			}
		}
		return null;
	}

	/**
	 * 仅方法名匹配，获取可访问的Method，自动向上查找
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 获取Class定义中声明的泛型参数的类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class<?> clazz) {
		return (Class<T>) getClassGenricType(clazz, 0);
	}

	/**
	 * 获取Class定义中声明的泛型参数的类型. 如无法找到, 返回Object.class
	 */
	public static Class<?> getClassGenricType(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class<?>) params[index];
	}

	/**
	 * 获取应用Class，屏蔽动态代理Class
	 * @param obj
	 * @return
	 */
	public static Class<?> getUserClass(Object obj) {
		Validate.notNull(obj, "Instance must not be null");

		Class<?> clazz = obj.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 获取方法返回值嵌套Class
	 */
	public static NestClass getNestReturnType(Method method) {
		NestClass root = new NestClass();
		Class<?> retType = method.getReturnType();
		root.setRootClass(retType);

		Type type = method.getGenericReturnType();
		if (type instanceof ParameterizedType) {
			if (Map.class.isAssignableFrom(retType)) {
				Type[] typeArr = ((ParameterizedType) type).getActualTypeArguments();
				root.setKeyClass(getNestParameterizedType(typeArr[0]));
				root.setValClass(getNestParameterizedType(typeArr[1]));
			} else {
				root.setKeyClass(getNestParameterizedType(method.getGenericReturnType()));
			}
		}
		return root;
	}

	/**
	 * 获取方法参数Class
	 */
	public static NestClass[] getNestParamterType(Method method) {
		Type[] typeArr = method.getGenericParameterTypes();
		NestClass[] classes = new NestClass[typeArr.length];
		for (int i = 0; i < typeArr.length; i++) {
			NestClass param = new NestClass();
			if (typeArr[i] instanceof ParameterizedType) {
				Class<?> paramType = (Class<?>) ((ParameterizedType) typeArr[i]).getRawType();
				param.setRootClass(paramType);
				if (Map.class.isAssignableFrom(paramType)) {
					Type[] actualArr = ((ParameterizedType) typeArr[i]).getActualTypeArguments();
					param.setKeyClass(getNestParameterizedType(actualArr[0]));
					param.setValClass(getNestParameterizedType(actualArr[1]));
				} else {
					param.setKeyClass(getNestParameterizedType(method.getGenericReturnType()));
				}
			} else {
				param.setRootClass((Class<?>) typeArr[i]);
			}
			classes[i] = param;
		}
		return classes;
	}

	/**
	 * 获取参数化的Class
	 */
	private static NestClass getNestParameterizedType(Type type) {
		NestClass root = null;
		if (type instanceof ParameterizedType) {
			Type[] typeArr = ((ParameterizedType) type).getActualTypeArguments();
			for (Type actualType : typeArr) {
				if (actualType instanceof ParameterizedType) {
					root = new NestClass();
					root.setRootClass((Class<?>) ((ParameterizedType) actualType).getRawType());
					root.setKeyClass(getNestParameterizedType(actualType));
				} else {
					root = new NestClass((Class<?>) actualType);
				}
			}
		} else {
			root = new NestClass((Class<?>) type);
		}
		return root;
	}
}
