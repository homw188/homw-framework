package com.homw.robot.struct.packet;

import com.homw.robot.struct.JobStateCode;
import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Stamp;

import io.netty.buffer.ByteBuf;

/**
 * 机器人响应信息
 * 
 * @author Hom
 * @version 1.0
 */
public class Response extends MsgBody {
	public Response() {
		type = MsgType.TYPE_RESPONSE;
		len = 2 * 4 + 8;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		buf.writeInt(res_type.getValue());
		if (stamp == null) {
			logger.info("机器人响应命令ID为空");
			buf.writeZero(8);
		} else {
			buf.writeInt(stamp.getSecs());
			buf.writeInt(stamp.getNsecs());
		}
		buf.writeInt(state.getCode());
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		res_type = MsgType.getType(buf.readInt());

		stamp = new Stamp();
		stamp.setSecs(buf.readInt());
		stamp.setNsecs(buf.readInt());

		state = JobStateCode.getState(buf.readInt());
	}

	/**
	 * 任务类型，即接受该任务时的MsgType
	 */
	private MsgType res_type;

	/**
	 * 命令ID
	 */
	private Stamp stamp;

	/**
	 * 状态码
	 */
	private JobStateCode state;

	public Response(MsgType res_type, Stamp stamp, JobStateCode state) {
		this();
		this.res_type = res_type;
		this.stamp = stamp;
		this.state = state;
	}

	public MsgType getRes_type() {
		return res_type;
	}

	public void setRes_type(MsgType res_type) {
		this.res_type = res_type;
	}

	public Stamp getStamp() {
		return stamp;
	}

	public void setStamp(Stamp stamp) {
		this.stamp = stamp;
	}

	public JobStateCode getState() {
		return state;
	}

	public void setState(JobStateCode state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "(res_type=" + res_type + ",stamp=" + stamp + ",state=" + state + ")";
	}

}
