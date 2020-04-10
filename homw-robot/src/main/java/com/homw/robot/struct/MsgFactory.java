package com.homw.robot.struct;

import java.sql.Timestamp;

import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Pose2D;
import com.homw.robot.struct.base.Stamp;
import com.homw.robot.struct.base.Vector3f;
import com.homw.robot.struct.packet.BatteryData;
import com.homw.robot.struct.packet.Cmd;
import com.homw.robot.struct.packet.CmdCharge;
import com.homw.robot.struct.packet.CmdDock;
import com.homw.robot.struct.packet.CmdGo;
import com.homw.robot.struct.packet.CmdInspection;
import com.homw.robot.struct.packet.CmdJob;
import com.homw.robot.struct.packet.CmdJobs;
import com.homw.robot.struct.packet.CmdMove;
import com.homw.robot.struct.packet.CmdObserve;
import com.homw.robot.struct.packet.ControlMove;
import com.homw.robot.struct.packet.ControlPTZ;
import com.homw.robot.struct.packet.Heart;
import com.homw.robot.struct.packet.LaserData;
import com.homw.robot.struct.packet.MotorData;
import com.homw.robot.struct.packet.PathData;
import com.homw.robot.struct.packet.PoseData;
import com.homw.robot.struct.packet.Response;
import com.homw.robot.struct.packet.Set;
import com.homw.robot.struct.packet.SetPose;
import com.homw.robot.struct.packet.SetSpeed;
import com.homw.robot.struct.packet.SpeedData;
import com.homw.robot.struct.packet.StateData;
import com.homw.robot.util.ProtocolConstant;

/**
 * Message body factory.
 * 
 * @author James
 * @version 1.0
 *
 */
public class MsgFactory {
	private static Timestamp tempStamp = new Timestamp(0);

	/**
	 * create & return message body instance by type.
	 * 
	 * @param type message type.
	 * @return
	 */
	public static MsgBody getBody(MsgType type) {
		MsgBody body = null;

		switch (type) {
		case TYPE_BATTERY: {
			body = new BatteryData();
			break;
		}
		case TYPE_CMD: {
			body = new Cmd();
			break;
		}
		case TYPE_CMD_GO: {
			body = new CmdGo();
			break;
		}
		case TYPE_CMD_JOB: {
			body = new CmdJob();
			break;
		}
		case TYPE_CMD_JOBS: {
			body = new CmdJobs();
			break;
		}
		case TYPE_CMD_MOVE: {
			body = new CmdMove();
			break;
		}
		case TYPE_CMD_OBSERVE: {
			body = new CmdObserve();
			break;
		}
		case TYPE_HEART: {
			body = new Heart();
			break;
		}
		case TYPE_LASER: {
			body = new LaserData();
			break;
		}
		case TYPE_MOTOR: {
			body = new MotorData();
			break;
		}
		case TYPE_PATH: {
			body = new PathData();
			break;
		}
		case TYPE_POSE: {
			body = new PoseData();
			break;
		}
		case TYPE_RESPONSE: {
			body = new Response();
			break;
		}
		case TYPE_SET: {
			body = new Set();
			break;
		}
		case TYPE_SET_POSE: {
			body = new SetPose();
			break;
		}
		case TYPE_SET_SPEED: {
			body = new SetSpeed();
			break;
		}
		case TYPE_SPEED: {
			body = new SpeedData();
			break;
		}
		case TYPE_STATE: {
			body = new StateData();
			break;
		}
		case TYPE_CMD_CHARGE: {
			body = new CmdCharge();
			break;
		}
		case TYPE_CMD_DOCK: {
			body = new CmdDock();
			break;
		}
		case TYPE_CMD_INSPECTION: {
			body = new CmdInspection();
			break;
		}
		case TYPE_CONTROL_MOVE: {
			body = new ControlMove();
			break;
		}
		case TYPE_CONTROL_PTZ: {
			body = new ControlPTZ();
			break;
		}
		}
		return body;
	}

	/**
	 * create & return message head instance by type.
	 * 
	 * @param type message type.
	 * @return
	 */
	public static MsgHead getHead(MsgType type) {
		MsgHead head = new MsgHead();
		head.setType(type);
		head.setTime(getCurrentSystemStamp());

		return head;
	}

	/**
	 * get current time of {@link Stamp}.
	 * 
	 * @return
	 */
	public static Stamp getCurrentStamp() {
		tempStamp.setTime(System.currentTimeMillis());
		return new Stamp(tempStamp.getSeconds(), tempStamp.getNanos());
	}

	/**
	 * get current system time of {@link Stamp}.
	 * 
	 * @return
	 */
	public static Stamp getCurrentSystemStamp() {
		return new Stamp((int) (System.currentTimeMillis() / 1000.0),
				(int) ((System.currentTimeMillis() % 1000.0) * 1000000));
	}

	/**
	 * create & return message packet instance by type.
	 * 
	 * @param type message type.
	 * @return
	 */
	public static MsgPacket getPacket(MsgType type) {
		MsgPacket msg = new MsgPacket();

		MsgHead head = getHead(type);
		head.setHead(ProtocolConstant.DATA_FRAME_HEAD);

		msg.setHead(head);
		msg.setBody(getBody(type));

		msg.setEnd(ProtocolConstant.DATA_FRAME_END);

		return msg;
	}

	/**
	 * create & return message packet instance by type & robot id.
	 * 
	 * @param type
	 * @param robotId
	 * @return
	 */
	public static MsgPacket getPacket(MsgType type, int robotId) {
		MsgPacket msg = getPacket(type);
		msg.getHead().setRobot_id(robotId);

		return msg;
	}

	/**
	 * get heart-beat message packet.
	 * 
	 * @param seq
	 * @return
	 */
	public static MsgPacket getHeartPacket(int seq) {
		MsgPacket msg = getPacket(MsgType.TYPE_HEART, 0);
		((Heart) msg.getBody()).setSeq(seq);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of go command.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getCmdGoPacket(int robotId, Pose data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_GO, robotId);
		((CmdGo) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of command.
	 * 
	 * @param robotId
	 * @param str
	 * @return
	 */
	public static MsgPacket getCmdPacket(int robotId, byte[] str) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD, robotId);
		((Cmd) msg.getBody()).setStr(str);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of job command.
	 * 
	 * @param robotId
	 * @param cmd
	 * @return
	 */
	public static MsgPacket getCmdJobPacket(int robotId, int cmd) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_JOB, robotId);
		((CmdJob) msg.getBody()).setData(cmd);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of jobs command.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getCmdJobsPacket(int robotId, Pose[] data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_JOBS, robotId);
		((CmdJobs) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of move command.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getCmdMovePacket(int robotId, Pose2D data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_MOVE, robotId);
		((CmdMove) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of observe command.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getCmdObservePacket(int robotId, Vector3f[] data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_OBSERVE, robotId);
		((CmdObserve) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of settings.
	 * 
	 * @param robotId
	 * @param str
	 * @return
	 */
	public static MsgPacket getSetPacket(int robotId, byte[] str) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_SET, robotId);
		((Set) msg.getBody()).setStr(str);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of set pose.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getSetPosePacket(int robotId, Pose data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_SET_POSE, robotId);
		((SetPose) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of set speed.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getSetSpeedPacket(int robotId, Pose2D data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_SET_SPEED, robotId);
		((SetSpeed) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of dock cmd.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getCmdDockPacket(int robotId, int data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_DOCK, robotId);
		((CmdDock) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of inspection cmd.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getCmdInspectionPacket(int robotId, int[] data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_INSPECTION, robotId);
		((CmdInspection) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of move control.
	 * 
	 * @param robotId
	 * @param data
	 * @return
	 */
	public static MsgPacket getControlMovePacket(int robotId, Pose2D data) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CONTROL_MOVE, robotId);
		((ControlMove) msg.getBody()).setData(data);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of PTZ control.
	 * 
	 * @param robotId
	 * @param data
	 * @param mode
	 * @return
	 */
	public static MsgPacket getControlPtzPacket(int robotId, Pose data, byte mode) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CONTROL_PTZ, robotId);
		((ControlPTZ) msg.getBody()).setData(data);
		((ControlPTZ) msg.getBody()).setMode(mode);

		msg.refreshLength();
		return msg;
	}

	/**
	 * get message packet of charge cmd.
	 * 
	 * @param robotId
	 * @param data
	 * @param mode
	 * @return
	 */
	public static MsgPacket getCmdChargePacket(int robotId) {
		MsgPacket msg = MsgFactory.getPacket(MsgType.TYPE_CMD_CHARGE, robotId);
		msg.refreshLength();
		return msg;
	}

}
