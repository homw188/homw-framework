package com.homw.tool.api.kede;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @description 山东科德水电表操作API
 * @author Hom
 * @version 1.0
 * @since 2020-07-08
 */
public interface KDSocketDll extends Library {
	// 加载动态库文件
	KDSocketDll INSTANCE = (KDSocketDll) Native.loadLibrary("KDSocket_x86.dll", KDSocketDll.class);

	/**
	 * 电表抄表
	 * @return 以“/”为分隔符，YES表示成功，NO表示失败，格式为：YES/第一户剩余或累计/第二户剩余或累计/第三户剩余或累计/第四户剩余或累
	 */
	String ReadAllELE(int SendStyle, int TimeLoad, String StrHostName, int i_PortNo, String addr, int SYLI, String IOT);

	/**
	 * 电表通断操作
	 */
	int SoloEleAction(int SendStyle, int TimeLoad, String StrHostName, int i_PortNo, String addr, int TD, String IOT);

	/**
	 * 远程控制水表阀门
	 * 
	 * @param SendStyle   发送方式，0：网络适配器，1：IOT模块
	 * @param TimeLoad    响应延时，单位毫秒，建议设置3000+
	 * @param StrHostName 接口服务ip
	 * @param i_PortNo    接口服务端口
	 * @param addr        电表地址，12位
	 * @param TD          通断类型，0：开，1：关
	 * @param IOT         物联网卡号，SendStyle为0则传任意值
	 * @return 整数型，0成功
	 */
	int WaterAction(int SendStyle, int TimeLoad, String StrHostName, int i_PortNo, String addr, int TD, String IOT);

	/**
	 * 远程实时读取水表状态
	 * @param KJ 口径大小，0：小口径，1：大口径
	 * @return 以“/”为分隔符，YES表示成功，NO表示失败，格式为：YES/累计/剩余/透支/次数/阀门状态（0：开阀，1：关阀）/电池状态（0：正常，1：欠压）
	 */
	String WaterReadZTCX(int SendStyle, int TimeLoad, String StrHostName, int i_PortNo, String addr, int KJ, String IOT);
}
