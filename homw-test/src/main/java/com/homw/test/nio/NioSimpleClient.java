package com.homw.test.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @description NIO客户端简单实现版本
 * @author Hom
 * @version 1.0
 * @since 2020-10-15
 */
public class NioSimpleClient {

	public void start() throws Exception {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress("localhost", NioConfig.LISTEN_PORT));
		
		// 阻塞等待连接成功
		while(!channel.finishConnect()) {}
		System.out.println("client connected");
		
		System.out.println("please input your packet: ");
		Scanner scanner = new Scanner(System.in);
		try {
			while (scanner.hasNext()) {
				byte[] packet = scanner.next().getBytes();
				ByteBuffer buf = ByteBuffer.allocate(packet.length);
				buf.put(packet);
				// 切换读模式，用于发消息
				buf.flip();
				channel.write(buf);
			}
		} finally {
			scanner.close();
			channel.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new NioSimpleClient().start();
	}
}
