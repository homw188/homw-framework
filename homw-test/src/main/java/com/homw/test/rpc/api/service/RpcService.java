package com.homw.test.rpc.api.service;

/**
 * @description rpc调用接口
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public interface RpcService {
	/**
	 * 执行某业务
	 * @param arg
	 * @return
	 */
    String exec(String arg);
}
