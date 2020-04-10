package com.homw.test.rpc.http;

import java.sql.Timestamp;

import com.homw.common.bean.NestClass;

/**
 * @description rpc接口参数
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public class RpcParam {
	private String className;
	private String method;
	private NestClass[] paramTypes;
	private Object[] args;

	private Class<?>[] paramClasses;

	public RpcParam() {
	}

	public RpcParam(String className, String method, NestClass[] paramTypes, Object[] args) {
		this.className = className;
		this.method = method;
		this.paramTypes = paramTypes;
		this.args = args;

		setParamClasses(paramTypes);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public NestClass[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(NestClass[] paramTypes) {
		this.paramTypes = paramTypes;
		setParamClasses(paramTypes);
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Class<?>[] getParamClasses() {
		return paramClasses;
	}

	/**
	 * 设置参数类别
	 * 
	 * @param paramTypes
	 */
	private void setParamClasses(NestClass[] paramTypes) {
		if (paramTypes != null) {
			this.paramClasses = new Class[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramClasses[i] = paramTypes[i].getRootClass();
			}
		}
	}

	/**
	 * 参数内容适配其类型
	 */
	public void adapterArgsType() {
		if (args != null && paramClasses != null) {
			for (int i = 0; i < args.length; i++) {
				if (Timestamp.class.isAssignableFrom(paramClasses[i])) {
					args[i] = Timestamp.valueOf(args[i].toString());
				}
			}
		}
	}

}
