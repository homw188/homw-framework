package com.homw.test.rpc.server.service.impl;

import com.homw.test.rpc.api.service.RpcService;

/**
 * @description rpc接口实现
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcServiceImpl implements RpcService {
	@Override
	public String exec(String arg) {
		return "rpc: " + arg;
	}
}
