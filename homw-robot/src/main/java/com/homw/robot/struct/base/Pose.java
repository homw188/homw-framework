package com.homw.robot.struct.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

/**
 * 三维位姿,数据包含三维空间下位置坐标和位姿角四元数
 * 
 * @author Hom
 * @version 1.0
 */
public class Pose 
{
	private static final Logger logger = LoggerFactory.getLogger(Pose.class);
	
	private Vector3f position;
	private Quaternion orientation;
	
	public Pose() {
		super();
	}
	public Pose(Vector3f position, Quaternion orientation) {
		super();
		this.position = position;
		this.orientation = orientation;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Quaternion getOrientation() {
		return orientation;
	}
	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * write to byte buffer.
	 * 
	 * @param buf
	 * @param type
	 */
	public void writeToBuffer(ByteBuf buf, String type) 
	{
		if (position == null)
		{
			logger.info(type + "位置信息为空");
			buf.writeZero(3 * 4);
		} else
		{
			buf.writeFloat(position.getX());
			buf.writeFloat(position.getY());
			buf.writeFloat(position.getZ());
		}
		
		if (orientation == null)
		{
			logger.info(type + "方向信息为空");
			buf.writeZero(4 * 4);
		} else
		{
			buf.writeFloat(orientation.getX());
			buf.writeFloat(orientation.getY());
			buf.writeFloat(orientation.getZ());
			buf.writeFloat(orientation.getW());
		}
	}
	
	@Override
	public String toString() 
	{
		return "(position=" + position.toString() + ",orientation=" + orientation + ")";
	}
	
}
