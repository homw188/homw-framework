package com.homw.robot.task;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.util.ProtocolConstant;

import io.netty.channel.Channel;

/**
 * Heart-Beat task.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotHeartTask implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(RobotHeartTask.class);

	/**
	 * 序列号，每次发送自动加1，达到最大值后置0
	 */
	private AtomicInteger seq;

	/**
	 * 服务端心跳计时器
	 */
	private AtomicLong timer;

	/**
	 * 心跳计数器
	 */
	private AtomicInteger counter;

	/**
	 * socket channel
	 */
	private volatile Channel channel;

	public RobotHeartTask() {
		seq = new AtomicInteger(0);
		timer = new AtomicLong(0);
		counter = new AtomicInteger(0);
	}

	@Override
	public void run() {
		if (timer.get() != 0) {
			long current = System.currentTimeMillis();
			if (current - timer.get() > ProtocolConstant.HEART_TIME_OUT) {
				logger.warn("心跳超时：" + ProtocolConstant.HEART_TIME_OUT + " 毫秒，连接断开");
				clear();

				channel.close();
			}
		} else {
			if (counter.get() > ProtocolConstant.HEART_TIME_OUT / ProtocolConstant.HEART_BEAT_RATIO) {
				logger.warn(
						"心跳超时：" + ProtocolConstant.HEART_TIME_OUT / ProtocolConstant.HEART_BEAT_RATIO + " 次未收到，连接断开");
				clear();

				channel.close();
			}
		}

		seq.compareAndSet(Integer.MAX_VALUE, 0);
		if (channel != null) {
			MsgPacket heart = MsgFactory.getHeartPacket(seq.incrementAndGet());
			channel.writeAndFlush(heart);
			counter.incrementAndGet();
		}
	}

	/**
	 * clear timer & counter status
	 */
	public void clear() {
		timer.set(0);
		counter.set(0);
	}

	/**
	 * set socket channel.
	 * 
	 * @param channel
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * set last heart time.
	 * 
	 * @param heartTime
	 */
	public void setTimer(long heartTime) {
		this.timer.set(heartTime);
	}
}