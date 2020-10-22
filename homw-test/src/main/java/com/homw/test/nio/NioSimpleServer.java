package com.homw.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @description NIO服务端简单实现版本
 * @author Hom
 * @version 1.0
 * @since 2020-10-15
 */
public class NioSimpleServer {

	public final void start() throws Exception {
		Selector selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(new InetSocketAddress(NioConfig.LISTEN_PORT));
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("server started, listen port: " + NioConfig.LISTEN_PORT);
		
		try {
			while(selector.select() > 0) {
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while(keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();
					if (key.isAcceptable()) {
						processAccept(selector, key);
					} else if (key.isReadable()) {
						processRead(key);
					}
					keyIterator.remove();
				}
			}
		} finally {
			selector.close();
			serverChannel.close();
		}
	}

	protected void processAccept(Selector selector, SelectionKey key)
			throws IOException, ClosedChannelException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		if (channel != null) {
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		}
	}

	protected void processRead(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(NioConfig.BUF_SIZE);
		int len = 0;
		while((len = channel.read(buf)) > 0) {
			String packet = new String(buf.array(), 0, len);
			System.out.println("server received packet: " + packet);
		}
	}
	
	public static void main(String[] args) throws Exception {
		new NioSimpleServer().start();
	}
}
