package com.homw.tool.api.keda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @description 测试{@link SocketTool}在多线程环境下的表现
 * @author James
 * @version 1.0
 * @date 2020-01-17
 */
public class TestSocketTool {
	private static final String UTF_8 = "UTF-8";

	private static int concurrency = 10;
	private static boolean debug_enabled = true;
	private static CountDownLatch server_started_latch = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		final int server_port = 8888;
		final String request_prefix = "Thead-Client-";

		// 1.启动测试服，并监听客户端请求
		new Thread("Thread-Server-Acceptor") {
			public void run() {
				ServerSocket server = null;
				try {
					server = new ServerSocket(server_port, concurrency);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (debug_enabled) {
					System.out.println("服务端已启动，监听端口：" + server_port);
				}
				server_started_latch.countDown();
				
				try {
					while (true) {
						final Socket client = server.accept();
						if (debug_enabled) {
							System.out.println("服务端收到一个连接请求，远程地址：" + client.getInetAddress().getHostName() + "，端口："
									+ client.getPort());
						}

						new Thread("Thread-Server-Worker") {
							public void run() {
								try {
									client.setTcpNoDelay(true);
									sendResponse(client, request_prefix);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (server != null) {
						try {
							server.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();

		// 2.阻塞等待测试服就绪
		server_started_latch.await();
		if (debug_enabled) {
			System.out.println("开始启动客户端，并发数：" + concurrency);
		}

		// 3.模拟多线程环境下发送请求
		// final SocketTool tool = new SocketTool();
		final SocketToolThreadSafe tool = new SocketToolThreadSafe();
		int count = 1;
		while (count <= concurrency) {
			new Thread(request_prefix + count++) {
				public void run() {
					try {
						sendRequestSync(server_port, tool);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

		// 4.阻塞等待测试完成
		System.in.read();
	}

	/**
	 * 发送请求，阻塞等待服务端响应
	 * 
	 * @param server_port
	 * @param tool
	 * @throws Exception
	 */
	private static void sendRequestSync(final int server_port, final SocketToolThreadSafe tool) throws Exception {
		String thread_name = Thread.currentThread().getName();
		byte[] request = thread_name.getBytes(UTF_8);
		// 阻塞等待响应
		Object response = tool.SendData("localhost", server_port, request, 221);
		if (debug_enabled) {
			System.out.println("客户端[" + thread_name + "]收到响应：" + response);
		}
		if (response != null) {
			String result = response.toString();
			result = hexStringToString(result);
			// 校验指令收发
			if (!thread_name.equals(result)) {
				System.err.println("请求：" + thread_name + "，响应：" + result);
			}
		}
	}

	/**
	 * 处理客户端请求
	 * 
	 * @param client
	 * @param request_prefix
	 * @throws Exception
	 */
	private static void sendResponse(final Socket client, final String request_prefix) throws Exception {
		// 模拟处理消耗
		Thread.sleep((long) (Math.random() * 1000));

		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();

		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = in.read(buf)) > 0) {
			String request = new String(buf, 0, len, UTF_8);

			if (debug_enabled) {
				System.out.println("服务端收到请求：" + request);
			}

			if ("客户端".equals(request)) {
				// 连接成功
				out.write("cmd::Successful\n".getBytes(UTF_8));
			} else {
				if (request.contains(request_prefix)) {
					request = request.substring(request.indexOf(request_prefix));
				}
				out.write(request.getBytes(UTF_8));
			}
		}
	}

	/**
	 * 16进制字符串转字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String hexStringToString(String str) {
		if (str == null || str.equals("")) {
			return null;
		}
		str = str.replace(" ", "");
		byte[] words = new byte[str.length() / 2];
		for (int i = 0; i < words.length; i++) {
			try {
				words[i] = (byte) (0xff & Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			str = new String(words, UTF_8);
			new String();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}