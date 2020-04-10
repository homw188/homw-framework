package com.homw.test.rpc.api.bean;

import java.io.Serializable;

/**
 * @description rpc调用参数类型
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcMessage implements Serializable {
	private static final long serialVersionUID = 5420264031263140990L;
	
	private String className;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
