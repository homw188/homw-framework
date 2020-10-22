package com.homw.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description NIO服务器多线程Reactor实现版本
 * @author Hom
 * @version 1.0
 * @since 2020-10-15
 */
public class NioReactorServer {

	private Reactor[] reactors;
	private Selector[] selectors;
	private AtomicInteger next = new AtomicInteger(0);
	private ExecutorService threadPool = Executors.newFixedThreadPool(NioConfig.THREAD_POOL_SIZE);
	
	public NioReactorServer() throws Exception {
		selectors = new Selector[NioConfig.REACTOR_SIZE];
		reactors = new Reactor[NioConfig.REACTOR_SIZE];
		Selector selector = null;
		for (int i = 0; i < NioConfig.REACTOR_SIZE; i++) {
			selector = Selector.open();
			selectors[i] = selector;
			// 关联selector用于轮询I/O事件
			reactors[i] = new Reactor(selector);
		}
		
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(new InetSocketAddress(NioConfig.LISTEN_PORT));
		// 注册I/O事件，并绑定处理器
		SelectionKey skey = serverChannel.register(selectors[0], SelectionKey.OP_ACCEPT);
		skey.attach(new RecvHandler(skey));
	}
	
	public void start() {
		// 启动reactor线程
		for (Reactor reactor : reactors) {
			new Thread(reactor).start();
		}
	}
	
	class Reactor implements Runnable {
		Selector selector;
		public Reactor(Selector selector) {
			this.selector = selector;
		}
		
		@Override
		public void run() {
			// 响应线程中断
			while(!Thread.interrupted()) {
				try {
					selector.select();
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectedKeys.iterator();
					while (it.hasNext()) {
						// I/O事件分发
						dispatch(it.next());
					}
					selectedKeys.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		void dispatch(SelectionKey skey) {
			Runnable handler = (Runnable) skey.attachment();
			if (handler != null) {
				handler.run();
			}
		}
	}
	
	class RecvHandler implements Runnable {
		SelectionKey skey;
		
		public RecvHandler(SelectionKey skey) {
			this.skey = skey;
		}
		
		@Override
		public void run() {
			ServerSocketChannel serverChannel = (ServerSocketChannel) skey.channel();
			try {
				SocketChannel channel = serverChannel.accept();
				channel.configureBlocking(false);
				// 轮询selector绑定
				Selector selector = selectors[next.get()];
				skey = channel.register(selector, 0);
				
				// 注册I/O事件，并绑定处理器 
				EchoHandler handler = new EchoHandler(skey, channel);
				skey.attach(handler);
				skey.interestOps(SelectionKey.OP_READ);
				
				// 唤醒阻塞方法：selector#select()
				selector.wakeup();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (next.incrementAndGet() >= selectors.length) {
				next.set(0);
			}
		}
	}
	
	class EchoHandler implements Runnable {
		final SelectionKey skey;
		final SocketChannel channel;
		
		static final int READING = 0, SENDING = 1;
		int state = READING;
		final ByteBuffer buf = ByteBuffer.allocate(NioConfig.BUF_SIZE);
		
		public EchoHandler(SelectionKey skey, SocketChannel channel) {
			this.skey = skey;
			this.channel = channel;
		}
		
		@Override
		public void run() {
			// 提交异步处理，避免reactor线程阻塞
			threadPool.execute(new AsyncTask());
		}
		
		synchronized void echo() throws IOException {
			if (state == READING) {
				int len = 0;
				while ((len = channel.read(buf)) > 0) {
					System.out.println("server received packet: " + new String(buf.array(), 0, len));
				}
				// 切换为读模式，用于发消息
				buf.flip();
				skey.interestOps(SelectionKey.OP_WRITE);
				state = SENDING;
			} else if (state == SENDING) {
				channel.write(buf);
				// 切换为写模式，用于收消息
				buf.clear();
				skey.interestOps(SelectionKey.OP_READ);
				state = READING;
			}
		}
		
		class AsyncTask implements Runnable {
			@Override
			public void run() {
				try {
					echo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new NioReactorServer().start();
	}
}
