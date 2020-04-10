package com.homw.robot.struct.base;

/**
 * 二维位姿,数据包含点的(x, y, r)二维坐标
 * 
 * @author Hom
 * @version 1.0
 */
public class Pose2D {
	private float x;
	private float y;

	/**
	 * r代表朝向,即右手法则下与x轴夹角 rad
	 */
	private float r;

	public Pose2D() {
		super();
	}

	public Pose2D(float x, float y, float r) {
		super();
		this.x = x;
		this.y = y;
		this.r = r;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	@Override
	public String toString() {
		return "(x=" + x + ",y=" + y + ",r=" + r + ")";
	}

}
