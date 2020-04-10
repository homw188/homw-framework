package com.homw.test.protobuf;

import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @description 基于protobuf序列化测试
 * @author Hom
 * @version 1.0
 * @since 2018年10月17日
 */
public class ProtobufTest 
{
	@Test
	public void test()
	{
		// 1、创建对象，并设置属性
		Message.proto_data.Builder builder = Message.proto_data.newBuilder();
		builder.setCode(1001);
		builder.setUserName("zhangsan");
		builder.setGender(true);
		
		// 2、序列化
		Message.proto_data src = builder.build();
		System.out.println("*** src ***\n" + src);
		
		System.out.println("byte size: " + src.toByteString().size() + "\n");
		try {
			// 3、反序列化
			Message.proto_data dest = Message.proto_data.parseFrom(src.toByteArray());
			System.out.println("*** dest ***\n" + dest);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
}
