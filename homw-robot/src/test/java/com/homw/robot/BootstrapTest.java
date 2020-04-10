package com.homw.robot;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Pose2D;
import com.homw.robot.struct.base.Quaternion;
import com.homw.robot.struct.base.Vector3f;
import com.homw.robot.struct.packet.Cmd;
import com.homw.robot.struct.packet.Set;

/**
 * 机器人通信端测试.
 * 
 * @author Hom
 * @version 1.0
 */
public class BootstrapTest {
	private String server_ip = "192.168.5.44";
	private int server_port = 33532;

	@Test
	public void testServer() {
		try {
			new RobotDataServer().bind(server_port);// 创建TCP服务端
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testClient() {
		final RobotDataClient app = new RobotDataClient();// 创建TCP客户端
		new Thread() {
			public void run() {
				try {
					app.connect(server_ip, server_port);// 建立TCP连接，同步阻塞
				} catch (Exception e) {
					try {
						// 等待执行自动重连操作
						TimeUnit.DAYS.sleep(1);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			};
		}.start();

		// while(true);

		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// 组装数据包
			MsgPacket cmd = MsgFactory.getCmdPacket(3, Arrays.copyOf("执行某任务".getBytes(), Cmd.size));

			Pose goData = new Pose();
			goData.setPosition(new Vector3f(1, 2, 3));
			goData.setOrientation(new Quaternion(1, 2, 3, 5));
			MsgPacket cmdGo = MsgFactory.getCmdGoPacket(3, goData);

			MsgPacket cmdJob = MsgFactory.getCmdJobPacket(3, 101);

			Pose jobsData = new Pose();
			jobsData.setPosition(new Vector3f(1, 2, 3));
			jobsData.setOrientation(new Quaternion(1, 2, 3, 5));
			MsgPacket cmdJobs = MsgFactory.getCmdJobsPacket(3, new Pose[] { jobsData });

			MsgPacket cmdMove = MsgFactory.getCmdMovePacket(3, new Pose2D(1, 2, 3));

			Vector3f observeData = new Vector3f(1, 2, 3);
			MsgPacket cmdObserve = MsgFactory.getCmdObservePacket(3, new Vector3f[] { observeData });

			MsgPacket set = MsgFactory.getSetPacket(3, Arrays.copyOf("设置机器人故障检测".getBytes(), Set.size));

			Pose poseData = new Pose();
			poseData.setPosition(new Vector3f(1, 2, 3));
			poseData.setOrientation(new Quaternion(1, 2, 3, 5));
			MsgPacket setPose = MsgFactory.getSetPosePacket(3, poseData);

			MsgPacket setSpeed = MsgFactory.getSetSpeedPacket(3, new Pose2D(1, 2, 1.5f));

			// 新增数据包
			MsgPacket cmdDock = MsgFactory.getCmdDockPacket(3, 1);
			MsgPacket cmdInspection = MsgFactory.getCmdInspectionPacket(3, new int[] { 1, 3, 4, 11, 8 });
			MsgPacket controlMove = MsgFactory.getControlMovePacket(3, new Pose2D(1, 2, 3));

			Pose data = new Pose();
			data.setPosition(new Vector3f(1, 2, 3));
			data.setOrientation(new Quaternion(1, 2, 3, 5));
			MsgPacket controlPtz = MsgFactory.getControlPtzPacket(3, data, (byte) 'a');

			MsgPacket cmdCharge = MsgFactory.getCmdChargePacket(1);

			// 发送数据包
			app.sendCmd(cmd);
			app.sendCmd(cmdGo);
			// app.sendCmd(cmdJob);
			// app.sendCmd(cmdJobs);
			// app.sendCmd(cmdMove);
			app.sendCmd(cmdCharge);
			app.sendCmd(cmdObserve);

			app.sendCmd(set);
			app.sendCmd(setPose);
			app.sendCmd(setSpeed);

			app.sendCmd(cmdDock);
			app.sendCmd(cmdInspection);
			app.sendCmd(controlMove);
			app.sendCmd(controlPtz);

			// 输出
			System.out.println("client send:" + cmd);
			/*
			 * System.out.println("client send:"+cmdGo);
			 * //System.out.println("client send:"+cmdJob);
			 * //System.out.println("client send:"+cmdJobs);
			 * //System.out.println("client send:"+cmdMove);
			 * System.out.println("client send:"+cmdCharge);
			 * System.out.println("client send:"+cmdObserve);
			 * 
			 * System.out.println("client send:"+set);
			 * System.out.println("client send:"+setPose);
			 * System.out.println("client send:"+setSpeed);
			 * 
			 * System.out.println("client send:"+cmdDock);
			 * System.out.println("client send:"+cmdInspection);
			 * System.out.println("client send:"+controlMove);
			 * System.out.println("client send:"+controlPtz);
			 */
		}
	}
}