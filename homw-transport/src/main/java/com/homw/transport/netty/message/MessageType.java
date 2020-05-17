package com.homw.transport.netty.message;

/**
 * @description 消息类型
 * @author Hom
 * @version 1.0
 * @since 2020-05-17
 */
public interface MessageType {
	byte NORMAL = 0x0; // 00000000
	byte HEARTBEAT = 0x2 << 2; // 00001000
}