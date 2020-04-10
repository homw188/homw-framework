package com.homw.modbus.struct;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.exception.ModbusTimeoutException;
import com.homw.modbus.struct.rtu.ModbusRTUFrame;

/**
 * Modbus响应缓存，带超时检测，自适应回收
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月9日
 *
 */
public class ModbusResponseCache {
	private Thread idleCheckThread;
	private Runnable idleCheckTask;

	private volatile boolean pause = false;
	private Object lock = new Object();

	private static ModbusResponseCache instance;
	private Map<String, ModbusFrameWithTimeOut> cache;

	private static Logger log = LoggerFactory.getLogger(ModbusResponseCache.class);

	private ModbusResponseCache() {
		cache = new HashMap<String, ModbusFrameWithTimeOut>();
		idleCheckTask = new Runnable() {
			public void run() {
				while (!pause) {
					try {
						Thread.sleep(ModbusConstant.CACHE_CHECK_INTERVAL);
					} catch (InterruptedException e) {
						// e.printStackTrace();
						Thread.currentThread().interrupt();
					}

					if (pause) {
						break;
					}

					synchronized (lock) {
						int count = 0;
						for (Entry<String, ModbusFrameWithTimeOut> entry : cache.entrySet()) {
							long timeout = entry.getValue().getTimeout();
							if (timeout > System.currentTimeMillis()) {
								if (remove(entry.getKey()) != null) {
									count++;
								}
							}
						}
						log.info("清除过期数据帧：" + count + "个, 缓存大小：" + cache.size());
					}
				}
			}
		};
		startCheckThread();
	}

	/**
	 * 停止空闲检测线程
	 */
	private void shutdownCheckThread() {
		pause = true;
		if (idleCheckThread != null) {
			idleCheckThread.interrupt();
			try {
				idleCheckThread.join();
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			idleCheckThread = null;
		}

		log.info("Modbus缓存检测线程停止");
	}

	/**
	 * 启动空闲检测线程，实现空间自适应回收
	 */
	private void startCheckThread() {
		if (!pause) {
			return;
		}

		if (idleCheckThread != null) {
			shutdownCheckThread();
		}
		pause = false;

		idleCheckThread = new Thread(idleCheckTask, "Modbus-Cache-Check-Thread");
		idleCheckThread.start();

		log.info("Modbus缓存检测线程启动");
	}

	public static synchronized ModbusResponseCache getInstance() {
		if (instance == null) {
			instance = new ModbusResponseCache();
		}
		return instance;
	}

	/**
	 * 缓存响应数据包，过期则替换
	 * 
	 * @param key
	 * @param reponse
	 * @return {@link HashMap#put(Object, Object)}
	 */
	public ModbusFrame putIfAbsent(String key, ModbusFrame reponse) {
		log.trace("缓存大小：" + cache.size());
		synchronized (lock) {
			ModbusFrameWithTimeOut old = cache.get(key);
			if (old != null) {
				if (old.getTimeout() > System.currentTimeMillis()) {
					if (cache.remove(key) != null) {
						log.info("该key值已存在，替换。key=" + key + ", value=" + old.getFrame());
						old = null;
					}
				}
			}

			if (old == null) {
				long timeout = System.currentTimeMillis() + ModbusConstant.RESPONSE_ALIVE_TIME;
				ModbusFrameWithTimeOut wrapper = cache.put(key, new ModbusFrameWithTimeOut(reponse, timeout));
				return wrapper == null ? null : wrapper.getFrame();
			}
		}
		return null;
	}

	/**
	 * 删除数据包
	 * 
	 * @param key
	 * @return {@link HashMap#remove(Object)}
	 */
	public ModbusFrame remove(String key) {
		log.trace("缓存大小：" + cache.size());
		synchronized (lock) {
			ModbusFrameWithTimeOut wrapper = cache.remove(key);
			return wrapper == null ? null : wrapper.getFrame();
		}
	}

	/**
	 * 获取并删除数据帧，若过期，则抛{@code ModbusTimeoutException}异常
	 * 
	 * @param key
	 * @return {@link HashMap#get(Object)}
	 * @throws ModbusTimeoutException
	 */
	public ModbusFrame getAndRemove(String key) throws ModbusTimeoutException {
		log.trace("缓存大小：" + cache.size());
		synchronized (lock) {
			ModbusFrameWithTimeOut wrapper = cache.get(key);
			if (wrapper != null) {
				cache.remove(key);
				if (System.currentTimeMillis() < wrapper.getTimeout()) {
					return wrapper.getFrame();
				} else {
					throw new ModbusTimeoutException("该缓存已过期，最大存活时间：" + ModbusConstant.RESPONSE_ALIVE_TIME + "毫秒");
				}
			}
		}
		return null;
	}

	/**
	 * 清除缓存
	 */
	public void clear() {
		synchronized (lock) {
			cache.clear();
		}
		log.info("缓存已清除");
	}

	/**
	 * 销毁，释放空间资源，停止检测线程
	 */
	public synchronized static void dispose() {
		instance.clear();
		instance.shutdownCheckThread();
		instance = null;

		log.info("缓存已销毁");
	}

	/**
	 * 获取所有缓存数据
	 * 
	 * @return
	 */
	public Map<String, ModbusFrameWithTimeOut> getAll() {
		return Collections.unmodifiableMap(cache);
	}

	/**
	 * 带超时属性的响应数据包
	 * 
	 * @author Hom
	 * @version 1.0
	 * @since 2018年11月9日
	 *
	 */
	class ModbusFrameWithTimeOut {
		private ModbusFrame frame;
		private long timeout;

		public ModbusFrameWithTimeOut(ModbusFrame frame, long timeout) {
			this.frame = frame;
			this.timeout = timeout;
		}

		public ModbusFrame getFrame() {
			return frame;
		}

		public void setFrame(ModbusRTUFrame frame) {
			this.frame = frame;
		}

		public long getTimeout() {
			return timeout;
		}

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}
	}
}
