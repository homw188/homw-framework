package com.homw.test.bean;

/**
 * @description 响应数据
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public class ResponseData
{
	private Object data;
	private boolean success;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean result) {
		this.success = result;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
