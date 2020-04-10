package com.homw.test.rpc.server;

/**
 * @description rpc服务启动类
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcServerStarter {
	public static void main(String[] args) throws Exception {
		RpcServer server = new RpcServer();
		server.publish("com.homw.test.rpc.server.service.impl");
		server.start();
	}
}
