package com.homw.robot.util;

/**
 * Constants about protocol.
 * 
 * @author Hom
 * @version 1.0
 */
public final class ProtocolConstant
{
	/**
	 * 客户端重连时间（毫秒）
	 */
	public static final long RE_CONNECT_TIME = 1000 * 30;
	
	/**
	 * socket写数据超时（毫秒）
	 */
	public static final long WRITE_TIME_OUT = 1000 * 30;
	
	/**
	 * socket读数据超时（毫秒）
	 */
	public static final long READ_TIME_OUT = 1000 * 60 * 30;
	
	/**
	 * socket读数据超时（毫秒）
	 */
	public static final long TRACKER_READ_TIME_OUT = 1000 * 5;
	
	/**
	 * TCP连接超时（毫秒）
	 */
	public static final int CONNECT_TIME_OUT = 1000 * 10;

	/**
	 * socket接收缓存大小（KB）
	 */
	public static final int RECEIVE_BUFFER = 1024 * 1024;
	
	/**
	 * socket发送缓存大小（KB）
	 */
	public static final int SEND_BUFFER = 1024 * 1024;
	
	/**
	 * 数据帧头
	 */
	public static final byte[] PC_FRAME_HEAD = new byte[] { 'V', 'K', 'P', 'C' };
	
	/**
	 * 数据帧头
	 */
	public static final byte[] TR_FRAME_HEAD = new byte[] { 'V', 'K', 'T', 'R' };
	
	/**
	 * 心跳包频率 机器人接口1/30HZ，平台-接口1/60 Hz（毫秒）
	 */
	public static final long HEART_BEAT_RATIO = 1000 * 60;
	
	/**
	 * 心跳超时（毫秒）
	 */
	public static final long HEART_TIME_OUT = 1000 * 60 * 10;
	
	/**
	 * 会话队列大小
	 */
	public static final int SESSION_QUEUE_SIZE = 1024;
	
	/**
	 * 数据帧头
	 */
	public static final byte[] DATA_FRAME_HEAD = new byte[] { (byte) 0xAA, (byte) 0xBB };
	
	/**
	 * 数据帧尾
	 */
	public static final byte[] DATA_FRAME_END = new byte[] { (byte) 0xDD, (byte) 0xEE, (byte) 0xDD, (byte) 0xEE };
	
	/**
	 * 最大数据帧长度（byte）
	 */
	public static final int MAX_FRAME_LENGTH = 1024 * 1024;
}
