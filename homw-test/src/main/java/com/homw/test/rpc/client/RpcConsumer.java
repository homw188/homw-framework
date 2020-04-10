package com.homw.test.rpc.client;

import com.homw.test.rpc.api.service.RpcService;

/**
 * @description rpc接口使用者
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcConsumer {
	public static void main(String[] args) {
		RpcService service = RpcProxy.create(RpcService.class);
		// 本地调用
		System.out.println(service.hashCode());
		// rpc调用
		System.out.println(service.exec("Hello, world."));
	}
}
