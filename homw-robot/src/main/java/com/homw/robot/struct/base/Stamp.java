package com.homw.robot.struct.base;

/**
 * 时间戳/命令ID
 * 
 * @author Hom
 * @version 1.0
 */
public class Stamp {
	/**
	 * 秒数
	 */
	private int secs;

	/**
	 * 纳秒数
	 */
	private int nsecs;

	public Stamp() {
		super();
	}

	public Stamp(int secs, int nsecs) {
		super();
		this.secs = secs;
		this.nsecs = nsecs;
	}

	public int getSecs() {
		return secs;
	}

	public void setSecs(int secs) {
		this.secs = secs;
	}

	public int getNsecs() {
		return nsecs;
	}

	public void setNsecs(int nsecs) {
		this.nsecs = nsecs;
	}

	@Override
	public String toString() {
		return "(secs=" + secs + ",nsecs=" + nsecs + ")";
	}

}
