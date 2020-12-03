package com.homw.tool.api.keda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketTool {

	private OutputStream os;
	// private InputStream in;
	public Socket socket;
	String backHexStr = "";
	Protocol protocal = new Protocol();

	private volatile boolean isExit = false;
	
	private InputStream ConnectServer(String serverIP, int serverPort) {
		InputStream in = null;
		// boolean _obj = false;
		BufferedReader br = null;
		try {
			socket = new Socket(serverIP, serverPort);
			os = socket.getOutputStream();
			String sendStr = "客户端";
			byte[] bs = sendStr.getBytes("UTF-8");
			os.write(bs);
			in = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			String info = null;

			while ((info = br.readLine()) != null) {
				if (info.contains("cmd::Successful")) {
					// _obj = true;
					break;
				}
			}
			System.out.println("连接成功.....");
		} catch (IOException e) {
			e.printStackTrace();
			// _obj = false;
		} finally {
			// if(in!=null) {
			// try {
			// in.close();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
		}
		return in;
	}

	/**
	 * 向通讯程序发送指令
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @param sendContent
	 * @param tag
	 *            （223 集中式电表 221 单表 255水表)
	 * @return
	 */
	public Object sendData(String serverIP, int serverPort, byte[] sendContent, int headTag) {
		long t1;
		isExit = false;
		// final String backHexStr = "";
		final InputStream in = ConnectServer(serverIP, serverPort);
		if (in == null)
			return false;

		byte[] headbyte = new byte[4];

		headbyte[0] = (byte) 0xCC;
		headbyte[1] = (byte) 0xCC;
		headbyte[2] = (byte) 0;
		headbyte[3] = (byte) headTag;

		byte[] zlbyte = new byte[sendContent.length + headbyte.length];// 前导字节+指令字节组合
		System.arraycopy(headbyte, 0, zlbyte, 0, headbyte.length);
		System.arraycopy(sendContent, 0, zlbyte, headbyte.length, sendContent.length);
		try {
			os.write(zlbyte);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		t1 = System.currentTimeMillis(); // 发送时间
		new Thread() {
			byte[] buffer = new byte[256];
			int receiveLength = -1;

			public void run() {
				while (!isExit) {
					//System.out.println("线程AA在执行..................");
					try {
						while ((receiveLength = in.read(buffer)) != -1) {
							//System.out.println("线程BB在执行..................");
							// 输出获取到所有字节 16进制
							backHexStr = CommonTool.bytes2Hex(buffer);
							backHexStr = backHexStr.substring(0, receiveLength * 2);
							break;
						}
						isExit = true;
						// break;
					} catch (IOException e) {
						e.printStackTrace();
						isExit = true;
					}
				}
				//System.out.println("获取返回指令.......");

			}
		}.start();

		// 防止read阻塞 最长8秒 无任何返回直接退出
		while (System.currentTimeMillis() - t1 <= 8 * 1000 && !isExit) {
			try {
				Thread.sleep(10);
				if (isExit)
					break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isExit = true;
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return backHexStr;
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	public OutputStream getOs() {
		return os;
	}
}