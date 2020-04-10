package com.homw.test.pool;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.common.util.Platform;
import com.homw.common.util.ReflectUtil;

/**
 * @description 数据库连接池
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public class ConnectionPool implements ObjectPool<Connection> {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
	
	/**
	 * 连接检测周期
	 */
	protected int checkPeriod;

	/**
	 * 连接检测任务
	 */
	protected ConnectionCheckTask checkTask;

	/**
	 * 空闲连接链表
	 */
	protected LinkedList<Connection> idle;

	/**
	 * 活动连接链表
	 */
	protected LinkedList<Connection> active;

	/**
	 * 最小空闲连接数
	 */
	protected int minIdleCount;

	/**
	 * 最大连接数
	 */
	protected int maxConnectionCount;

	/**
	 * 连接状态缓存
	 */
	protected Map<Connection, Long> connStatusCache;

	/**
	 * 等待连接超时
	 */
	protected int timeout = 100;

	protected boolean debugMode = false; // 用于调试

	public ConnectionPool() {
		this(10, 100, 10 * 60 * 1000);
	}

	/**
	 * 构建数据库连接池
	 * 
	 * @param minIdleCount       最小空闲连接数
	 * @param maxConnectionCount 最大连接数
	 */
	public ConnectionPool(int minIdleCount, int maxConnectionCount) {
		this(minIdleCount, maxConnectionCount, 10 * 60 * 1000);
	}

	/**
	 * 构建数据库连接池
	 * 
	 * @param minIdleCount       最小空闲连接数
	 * @param maxConnectionCount 最大连接数
	 * @param checkPeriod        连接检测周期
	 */
	public ConnectionPool(int minIdleCount, int maxConnectionCount, int checkPeriod) {
		this.checkPeriod = checkPeriod;
		this.minIdleCount = minIdleCount;
		this.maxConnectionCount = maxConnectionCount;

		idle = new LinkedList<Connection>();
		active = new LinkedList<Connection>();
		connStatusCache = new HashMap<Connection, Long>();

		addShutdownHook();
		// 非开发模式，数据库不允许debug
		if (!Platform.isIDEMode())
			debugMode = false;
	}

	/**
	 * 当Java虚拟机退出的时候，销毁连接池
	 */
	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				ConnectionPool.this.destroy();
			}
		});
	}

	@Override
	public synchronized Connection take() {
		Connection conn = idle.poll();
		if (conn == null) {
			if (active.size() + idle.size() < maxConnectionCount) {
				conn = getConnection();
				active.offer(conn);
				// System.out.println("[create]--active=" + active.size() + ", idle=" + idle.size());
			} else {
				logger.info("当前无空闲连接，等待" + timeout + "毫秒将重新获取");
				try {
					this.wait(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				conn = take();
			}
		} else {
			// System.out.println("[hit]--active=" + active.size() + ", idle=" + idle.size());
			active.offer(conn);
		}

		if (debugMode) {
			System.out.println(conn + "-" + active.size() + "-" + idle.size());
			new Throwable().printStackTrace();
		}

		// *** 连接状态检测 ***//
		if ((System.currentTimeMillis() - connStatusCache.get(conn)) > checkPeriod) {
			try {
				if (!conn.isValid(ConnectionCheckTask.VALIDATE_TIMEOUT)) {
					active.remove(conn);
					connStatusCache.remove(conn);
					closeRealConnection(conn);

					conn = take();
				} else {
					connStatusCache.put(conn, System.currentTimeMillis());// update time
				}
			} catch (SQLException e) {
				logger.warn("数据库连接状态检测异常：" + e.getMessage());
			}
		}
		return conn;
	}

	@Override
	public Connection take(Object[] params) {
		return take();
	}

	@Override
	public synchronized void release(Connection obj) {
		if (obj != null) {
			if (active.remove(obj)) {
				idle.offer(obj);
				this.notifyAll();
				if (debugMode) {
					System.out.println(
							"[release]--connection=" + obj + ", active=" + active.size() + ", idle=" + idle.size());
				} else {
//					System.out.println("[release]--active=" + active.size() + ", idle=" + idle.size());
				}

				// *** 重置数据库连接的自动提交状态 ***//
				try {
					if (!obj.getAutoCommit()) {
						obj.setAutoCommit(true);
					}
				} catch (SQLException e) {
					logger.warn("重置数据库连接的自动提交状态异常：" + e.getMessage());
				}
			} else {
				connStatusCache.remove(obj);
				closeRealConnection(obj);
				logger.warn("数据库连接：[" + obj + "] 状态转换（active->idle）失败");
			}
			startCheckTask();
		} else {
			logger.warn("释放空数据库连接");
		}
	}

	/**
	 * 启动连接检测任务
	 */
	private synchronized void startCheckTask() {
		if (checkTask == null) {
			checkTask = new ConnectionCheckTask(this);
			new Thread(checkTask).start();
		}
	}

	/**
	 * 停止连接检测任务
	 */
	private synchronized void stopCheckTask() {
		if (checkTask != null) {
			checkTask.setRunning(false);
			checkTask = null;
		}
	}

	/**
	 * 获取连接池对象
	 * 
	 * @param mustSecond
	 * @return
	 */
	protected Connection getConnection() {
		Connection conn = null;

		try {
			Class.forName("driverName");
			conn = DriverManager.getConnection("url", "user", "pwd");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 数据库连接代理
		conn = ConnectionProxy.getProxy(conn);
		connStatusCache.put(conn, System.currentTimeMillis());
		return conn;
	}

	/**
	 * 关闭真实数据库连接
	 * 
	 * @param conn
	 */
	@SuppressWarnings("rawtypes")
	public static void closeRealConnection(Connection conn) {
		if (conn == null) {
			return;
		}

		try {
			Class clazz = conn.getClass();
			if (conn instanceof Proxy) {
				Field hf = ReflectUtil.getAccessibleField(clazz, "h");
				if (hf != null) {
					if (hf.get(conn) instanceof ConnectionProxy) {
						ConnectionProxy proxy = (ConnectionProxy) hf.get(conn);
						proxy.getTarget().close();
					}
				}
			} else {
				conn.close();
			}
		} catch (Exception e) {
			logger.warn("数据库连接关闭异常：" + e.getMessage());
		}
	}

	@Override
	public synchronized void destroy() {
		stopCheckTask();

		for (Connection conn : active) {
			closeRealConnection(conn);
		}

		for (Connection conn : idle) {
			closeRealConnection(conn);
		}

		active.clear();
		idle.clear();
		connStatusCache.clear();
	}

}
