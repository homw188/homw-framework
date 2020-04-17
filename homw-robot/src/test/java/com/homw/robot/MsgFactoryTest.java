package com.homw.robot;

import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.*;
import com.homw.robot.struct.packet.*;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;

/**
 * 数据包工厂测试
 * 
 * @author Hom
 * @version 1.0
 */
public class MsgFactoryTest {
	private MsgPacket msg;

	@Test
	public void testHeartPacket() {
		msg = MsgFactory.getHeartPacket(0);
	}

	@Test
	public void testBatteryPacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_BATTERY);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		BatteryData body = (BatteryData) msg.getBody();
		body.setPower(95.5f);
		body.setTime(30);
		body.setVoltage(220);

		State state = new State();
		state.setStateCode(0);

		byte[] str = Arrays.copyOf("无需充电".getBytes(), State.size);
		state.setStr(str);

		body.setData(state);

		// 刷新数据包长度
		msg.refreshLength();
	}

	@Test
	public void testCmdPacket() {
		msg = MsgFactory.getCmdPacket(3, Arrays.copyOf("执行某任务".getBytes(), Cmd.size));
	}

	@Test
	public void testCmdGoPacket() {
		Pose data = new Pose();
		data.setPosition(new Vector3f(1, 2, 3));
		data.setOrientation(new Quaternion(1, 2, 3, 5));

		msg = MsgFactory.getCmdGoPacket(3, data);
	}

	@Test
	public void testCmdJobPacket() {
		msg = MsgFactory.getCmdJobPacket(3, 101);
	}

	@Test
	public void testCmdJobsPacket() {
		Pose data = new Pose();
		data.setPosition(new Vector3f(1, 2, 3));
		data.setOrientation(new Quaternion(1, 2, 3, 5));

		msg = MsgFactory.getCmdJobsPacket(3, new Pose[] { data });
	}

	@Test
	public void testCmdMovePacket() {
		msg = MsgFactory.getCmdMovePacket(3, new Pose2D(1, 2, 3));
	}

	@Test
	public void testCmdDockPacket() {
		msg = MsgFactory.getCmdDockPacket(3, 1);
	}

	@Test
	public void testCmdInspectionPacket() {
		msg = MsgFactory.getCmdInspectionPacket(3, new int[] { 1, 3, 4, 11, 8 });
	}

	@Test
	public void testControlMovePacket() {
		msg = MsgFactory.getControlMovePacket(3, new Pose2D(1, 2, 3));
	}

	@Test
	public void testControlPtzPacket() {
		Pose data = new Pose();
		data.setPosition(new Vector3f(1, 2, 3));
		data.setOrientation(new Quaternion(1, 2, 3, 5));

		msg = MsgFactory.getControlPtzPacket(3, data, (byte) 'a');
	}

	@Test
	public void testCmdObservePacket() {
		Vector3f data = new Vector3f(1, 2, 3);
		msg = MsgFactory.getCmdObservePacket(3, new Vector3f[] { data });
	}

	@Test
	public void testMotorDatePacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_MOTOR);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		MotorData body = (MotorData) msg.getBody();

		State state = new State();
		state.setStateCode(0);

		byte[] str = Arrays.copyOf("左前位置不对".getBytes(), State.size);
		state.setStr(str);
		body.setMotor(new State[] { state });

		body.setLift(1.2f);

		// 刷新数据包长度
		msg.refreshLength();
	}

	@Test
	public void testPathDataPacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_PATH);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		PathData body = (PathData) msg.getBody();

		Pose data = new Pose();
		data.setPosition(new Vector3f(1, 2, 3));
		data.setOrientation(new Quaternion(1, 2, 3, 5));

		body.setData(new Pose[] { data });

		// 刷新数据包长度
		msg.refreshLength();
	}

	@Test
	public void testPoseDataPacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_POSE);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		PoseData body = (PoseData) msg.getBody();

		Pose data = new Pose();
		data.setPosition(new Vector3f(1, 2, 3));
		data.setOrientation(new Quaternion(1, 2, 3, 5));

		body.setData(data);

		// 刷新数据包长度
		msg.refreshLength();
	}

	@Test
	public void testResponsePacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_RESPONSE);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		Response body = (Response) msg.getBody();
		body.setRes_type(MsgType.TYPE_HEART);

		// 刷新数据包长度
		msg.refreshLength();
	}

	@Test
	public void testSetPacket() {
		msg = MsgFactory.getSetPacket(3, Arrays.copyOf("设置机器人故障检测".getBytes(), Set.size));
	}

	@Test
	public void testSetPosePacket() {
		Pose data = new Pose();
		data.setPosition(new Vector3f(1, 2, 3));
		data.setOrientation(new Quaternion(1, 2, 3, 5));

		msg = MsgFactory.getSetPosePacket(3, data);
	}

	@Test
	public void testSetSpeedPacket() {
		msg = MsgFactory.getSetSpeedPacket(3, new Pose2D(1, 2, 1.5f));
	}

	@Test
	public void testSpeedDataPacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_SPEED);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		SpeedData body = (SpeedData) msg.getBody();
		body.setData(new Pose2D(1, 2, 1.5f));

		// 刷新数据包长度
		msg.refreshLength();
	}

	@Test
	public void testStateDataPacket() {
		// 根据类别获取数据包
		msg = MsgFactory.getPacket(MsgType.TYPE_STATE);

		// 填充包头数据域
		msg.getHead().setRobot_id(0);

		// 填充包体数据域
		StateData body = (StateData) msg.getBody();

		State runState = new State();
		runState.setStateCode(0);
		runState.setStr(Arrays.copyOf("移动中".getBytes(), State.size));
		body.setRunState(runState);

		State naviState = new State();
		runState.setStateCode(0);
		naviState.setStr(Arrays.copyOf("导航中".getBytes(), State.size));
		body.setNaviState(naviState);

		// 刷新数据包长度
		msg.refreshLength();
	}

	@After
	public void print() {
		System.out.println(msg.toString());
	}
}