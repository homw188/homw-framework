package com.homw.test.rpc.http;

import java.lang.reflect.Field;

import com.homw.common.util.ReflectUtil;

/**
 * @description rpc异常包装器
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public class RpcExceptionWrapper {
	public static final String EXCEPTION_FIELD = "ServiceException";
	private Exception serviceException;

	public RpcExceptionWrapper() {
	}

	public RpcExceptionWrapper(Exception e) {
		Field messageField = ReflectUtil.getAccessibleField(e.getClass(), "detailMessage");
		try {
			messageField.set(e, "RPC异常: " + e.getMessage());
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
		this.serviceException = e;
	}

	public Exception getServiceException() {
		return serviceException;
	}

	public void setServiceException(Exception serviceException) {
		this.serviceException = serviceException;
	}

}
