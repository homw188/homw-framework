package com.homw.test.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 数据库连接检测任务
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public class ConnectionCheckTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectionCheckTask.class);
	
	private ConnectionPool pool;
	private volatile boolean running = true;
	public static final int VALIDATE_TIMEOUT = 3 * 60;

	public ConnectionCheckTask(ConnectionPool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(pool.checkPeriod);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			if (!running) {
				break;
			}

			scaleIdle();
			// checkValid();
			// System.out.println("[check]--active=" + pool.active.size() + ", idle=" +
			// pool.idle.size());
		}
	}

	/**
	 * 检测连接是否有效
	 */
	private void checkValid() {
		try {
			synchronized (pool) {
				Iterator<Connection> it = pool.idle.iterator();
				while (it.hasNext()) {
					Connection conn = it.next();
					if (!conn.isValid(VALIDATE_TIMEOUT)) {
						pool.idle.remove(conn);
					}
				}
			}
		} catch (SQLException e) {
			logger.warn("数据库连接检测异常：" + e.getMessage());
		}
	}

	/**
	 * 释放部分空闲连接
	 */
	private void scaleIdle() {
		synchronized (pool) {
			while (pool.idle.size() - pool.minIdleCount > 0) {
				// remove & close connection
				ConnectionPool.closeRealConnection(pool.idle.poll());
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
