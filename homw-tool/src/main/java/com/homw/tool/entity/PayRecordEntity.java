package com.homw.tool.entity;

import java.math.BigDecimal;

/**
 * @description 支付记录实体
 * @author Hom
 * @version 1.0
 * @date 2019-09-23
 */
public class PayRecordEntity {
    private Long rentPayrecordId;
    private Long payerId;
    private String payerType;
    private String payerName;
    private Long agreementId;
    private String recordStatus;
    private BigDecimal totalAmt;
    private BigDecimal payAmt;
    private Long spaceId;
    private String orderType;
    private String period;
    private Short rentPeriodNum;
    private String year;
    private String month;
    private Short status;
    private Integer version;
    private Long createUserId;
    private Long createTime;
    private String createUserType;
    private Long updateUserId;
    private Long updateTime;
    private String updateUserType;
    private Long startTime;
    private Long endTime;
    
	public Long getRentPayrecordId() {
		return rentPayrecordId;
	}
	public void setRentPayrecordId(Long rentPayrecordId) {
		this.rentPayrecordId = rentPayrecordId;
	}
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public String getPayerType() {
		return payerType;
	}
	public void setPayerType(String payerType) {
		this.payerType = payerType;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public Long getAgreementId() {
		return agreementId;
	}
	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public BigDecimal getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Short getRentPeriodNum() {
		return rentPeriodNum;
	}
	public void setRentPeriodNum(Short rentPeriodNum) {
		this.rentPeriodNum = rentPeriodNum;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserType() {
		return createUserType;
	}
	public void setCreateUserType(String createUserType) {
		this.createUserType = createUserType;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUserType() {
		return updateUserType;
	}
	public void setUpdateUserType(String updateUserType) {
		this.updateUserType = updateUserType;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}