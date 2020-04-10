package com.homw.tool.entity;

import java.io.Serializable;

import com.homw.common.annotation.ExcelField;

/**
 * 设备信息表
 * 
 * @author System
 * @email 
 * @date 2019-05-20 15:13:52
 */
public class DeviceInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long deviceId;
	//
	private String deviceType;
	//外部编号
	@ExcelField(title = "设备号", sort = 1)
	private String outerNo;
	//
	@ExcelField(title = "IP", sort = 3)
	private String doorIp;
	//
	private Integer doorPort;
	//门禁表设备
	@ExcelField(title = "电表地址", sort = 2)
	private String doorAddr;
	//门禁所属控制器的编号 
	@ExcelField(title = "回路号", sort = 5)
	private Integer doorReadno;
	//
	@ExcelField(title = "设备名称", sort = 4)
	private String doorName;
	//
	private String elecAddr;
	//0100 关灯 0000开灯
	private String elecStatus;
	//
	private Integer elecUsePoint;
	//
	private Integer elecLeftPoint;
	//
	private Integer status;
	//
	private Integer version;
	//
	private Long createUserId;
	//
	private Long createTime;
	//
	private String createUserType;
	//
	private Long updateUserId;
	//
	private Long updateTime;
	//
	private String updateUserType;
	//
	private Long referElecId;
	//是否关联扣费设备
	private Integer isReferNode;
	//电表倍率
	@ExcelField(title = "倍率", sort = 6)
	private Integer rate;

	/**
	 * 设置：
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * 获取：
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * 设置：外部编号
	 */
	public void setOuterNo(String outerNo) {
		this.outerNo = outerNo;
	}
	/**
	 * 获取：外部编号
	 */
	public String getOuterNo() {
		return outerNo;
	}
	/**
	 * 设置：
	 */
	public void setDoorIp(String doorIp) {
		this.doorIp = doorIp;
	}
	/**
	 * 获取：
	 */
	public String getDoorIp() {
		return doorIp;
	}
	/**
	 * 设置：
	 */
	public void setDoorPort(Integer doorPort) {
		this.doorPort = doorPort;
	}
	/**
	 * 获取：
	 */
	public Integer getDoorPort() {
		return doorPort;
	}
	/**
	 * 设置：门禁表设备
	 */
	public void setDoorAddr(String doorAddr) {
		this.doorAddr = doorAddr;
	}
	/**
	 * 获取：门禁表设备
	 */
	public String getDoorAddr() {
		return doorAddr;
	}
	/**
	 * 设置：门禁所属控制器的编号 
	 */
	public void setDoorReadno(Integer doorReadno) {
		this.doorReadno = doorReadno;
	}
	/**
	 * 获取：门禁所属控制器的编号 
	 */
	public Integer getDoorReadno() {
		return doorReadno;
	}
	/**
	 * 设置：
	 */
	public void setDoorName(String doorName) {
		this.doorName = doorName;
	}
	/**
	 * 获取：
	 */
	public String getDoorName() {
		return doorName;
	}
	/**
	 * 设置：
	 */
	public void setElecAddr(String elecAddr) {
		this.elecAddr = elecAddr;
	}
	/**
	 * 获取：
	 */
	public String getElecAddr() {
		return elecAddr;
	}
	/**
	 * 设置：0100 关灯 0000开灯
	 */
	public void setElecStatus(String elecStatus) {
		this.elecStatus = elecStatus;
	}
	/**
	 * 获取：0100 关灯 0000开灯
	 */
	public String getElecStatus() {
		return elecStatus;
	}
	/**
	 * 设置：
	 */
	public void setElecUsePoint(Integer elecUsePoint) {
		this.elecUsePoint = elecUsePoint;
	}
	/**
	 * 获取：
	 */
	public Integer getElecUsePoint() {
		return elecUsePoint;
	}
	/**
	 * 设置：
	 */
	public void setElecLeftPoint(Integer elecLeftPoint) {
		this.elecLeftPoint = elecLeftPoint;
	}
	/**
	 * 获取：
	 */
	public Integer getElecLeftPoint() {
		return elecLeftPoint;
	}
	/**
	 * 设置：
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 获取：
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * 设置：
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：
	 */
	public Long getCreateUserId() {
		return createUserId;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Long getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setCreateUserType(String createUserType) {
		this.createUserType = createUserType;
	}
	/**
	 * 获取：
	 */
	public String getCreateUserType() {
		return createUserType;
	}
	/**
	 * 设置：
	 */
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	/**
	 * 获取：
	 */
	public Long getUpdateUserId() {
		return updateUserId;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Long getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateUserType(String updateUserType) {
		this.updateUserType = updateUserType;
	}
	/**
	 * 获取：
	 */
	public String getUpdateUserType() {
		return updateUserType;
	}
	/**
	 * 设置：
	 */
	public void setReferElecId(Long referElecId) {
		this.referElecId = referElecId;
	}
	/**
	 * 获取：
	 */
	public Long getReferElecId() {
		return referElecId;
	}
	/**
	 * 设置：是否关联扣费设备
	 */
	public void setIsReferNode(Integer isReferNode) {
		this.isReferNode = isReferNode;
	}
	/**
	 * 获取：是否关联扣费设备
	 */
	public Integer getIsReferNode() {
		return isReferNode;
	}
	/**
	 * 设置：电表倍率
	 */
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	/**
	 * 获取：电表倍率
	 */
	public Integer getRate() {
		return rate;
	}
}
