package com.homw.transport.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @description 基于ssh2实现远程命令执行
 * @author Hom
 * @version 1.0
 * @since 2020-04-17
 */
public class Ssh2Client {
	private String host;
	private String username;
	private String password;
	private static final String DEFAULT_CHARSET = "UTF-8";

	public Ssh2Client(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	/**
	 * 登录ssh2 server，获取连接
	 * 
	 * @return
	 */
	public Connection login() {
		try {
			Connection conn = new Connection(host);
			conn.connect();
			conn.authenticateWithPassword(username, password);
			return conn;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行命令
	 * 
	 * @param cmd
	 * @return
	 */
	public String execCommand(String cmd) {
		String result = "";
		Connection ssh2Conn = login();
		if (ssh2Conn != null) {
			Session session = null;
			try {
				session = ssh2Conn.openSession();
				session.execCommand(cmd);
				result = process(session.getStdout(), DEFAULT_CHARSET);
				if (StringUtils.isBlank(result)) {
					result = process(session.getStderr(), DEFAULT_CHARSET);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				ssh2Conn.close();
				if (session != null) {
					session.close();
				}
			}
		}
		return result;
	}

	/**
	 * 处理命令执行结果
	 * 
	 * @param in
	 * @param charset
	 * @return
	 */
	private String process(InputStream in, String charset) {
		StringBuilder resultBuf = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new StreamGobbler(in), charset));
			String line = null;
			while ((line = reader.readLine()) != null) {
				resultBuf.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultBuf.toString();
	}
}