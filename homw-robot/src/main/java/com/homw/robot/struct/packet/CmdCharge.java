package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 充电命令
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdCharge extends MsgBody {
	public CmdCharge() {
		type = MsgType.TYPE_CMD_CHARGE;
		len = 0;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		// TODO Auto-generated method stub

	}

}
