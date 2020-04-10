package com.homw.tool.entity;

import java.math.BigDecimal;

/**
 * @description 合同实体 
 * @author Hom
 * @version 1.0
 * @since 2019-09-23
 */
public class AgreementEntity {
    private Long agreementId;
    private String agreementNo;
    private String apptName;
    private String apptMobile;
    private String apptIdentityType;
    private String apptIdentity;
    private String companyName;
    private String licenseCode;
    private Long userId;
    private Long startTime;
    private Long endTime;
    private Long spaceId;
    private Long spaceGroupId;
    private Short meetingTime;
    private BigDecimal rentAmt;
    private BigDecimal propertyAmt;
    private BigDecimal pledgeAmt;
    private String agreementStatus;
    private String agreementType;
    private Short periodNum;
    private Short pledgeNum;
    private Short totalRentNum;
    private Boolean isIncludeProFee;
    private Long freeStartTime;
    private Long freeEndTime;
    private String period;
    private Boolean isUnoauth;
    private Long lastStartTime;
    private Long lastEndTime;
    private String vlanTag;
    private String epCode;
    private Long renewAgreementId;
    private Boolean isRenew;
    private Short status;
    private Integer version;
    private Long createUserId;
    private Long createTime;
    private String createUserType;
    private Long updateUserId;
    private Long updateTime;
    private String updateUserType;
    private Long companyId;
    private String spmsNo;// ERP系统号
    
	public Long getAgreementId() {
		return agreementId;
	}
	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}
	public String getAgreementNo() {
		return agreementNo;
	}
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	public String getApptName() {
		return apptName;
	}
	public void setApptName(String apptName) {
		this.apptName = apptName;
	}
	public String getApptMobile() {
		return apptMobile;
	}
	public void setApptMobile(String apptMobile) {
		this.apptMobile = apptMobile;
	}
	public String getApptIdentityType() {
		return apptIdentityType;
	}
	public void setApptIdentityType(String apptIdentityType) {
		this.apptIdentityType = apptIdentityType;
	}
	public String getApptIdentity() {
		return apptIdentity;
	}
	public void setApptIdentity(String apptIdentity) {
		this.apptIdentity = apptIdentity;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLicenseCode() {
		return licenseCode;
	}
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	public Long getSpaceGroupId() {
		return spaceGroupId;
	}
	public void setSpaceGroupId(Long spaceGroupId) {
		this.spaceGroupId = spaceGroupId;
	}
	public Short getMeetingTime() {
		return meetingTime;
	}
	public void setMeetingTime(Short meetingTime) {
		this.meetingTime = meetingTime;
	}
	public BigDecimal getRentAmt() {
		return rentAmt;
	}
	public void setRentAmt(BigDecimal rentAmt) {
		this.rentAmt = rentAmt;
	}
	public BigDecimal getPropertyAmt() {
		return propertyAmt;
	}
	public void setPropertyAmt(BigDecimal propertyAmt) {
		this.propertyAmt = propertyAmt;
	}
	public BigDecimal getPledgeAmt() {
		return pledgeAmt;
	}
	public void setPledgeAmt(BigDecimal pledgeAmt) {
		this.pledgeAmt = pledgeAmt;
	}
	public String getAgreementStatus() {
		return agreementStatus;
	}
	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}
	public String getAgreementType() {
		return agreementType;
	}
	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}
	public Short getPeriodNum() {
		return periodNum;
	}
	public void setPeriodNum(Short periodNum) {
		this.periodNum = periodNum;
	}
	public Short getPledgeNum() {
		return pledgeNum;
	}
	public void setPledgeNum(Short pledgeNum) {
		this.pledgeNum = pledgeNum;
	}
	public Short getTotalRentNum() {
		return totalRentNum;
	}
	public void setTotalRentNum(Short totalRentNum) {
		this.totalRentNum = totalRentNum;
	}
	public Boolean getIsIncludeProFee() {
		return isIncludeProFee;
	}
	public void setIsIncludeProFee(Boolean isIncludeProFee) {
		this.isIncludeProFee = isIncludeProFee;
	}
	public Long getFreeStartTime() {
		return freeStartTime;
	}
	public void setFreeStartTime(Long freeStartTime) {
		this.freeStartTime = freeStartTime;
	}
	public Long getFreeEndTime() {
		return freeEndTime;
	}
	public void setFreeEndTime(Long freeEndTime) {
		this.freeEndTime = freeEndTime;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Boolean getIsUnoauth() {
		return isUnoauth;
	}
	public void setIsUnoauth(Boolean isUnoauth) {
		this.isUnoauth = isUnoauth;
	}
	public Long getLastStartTime() {
		return lastStartTime;
	}
	public void setLastStartTime(Long lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	public Long getLastEndTime() {
		return lastEndTime;
	}
	public void setLastEndTime(Long lastEndTime) {
		this.lastEndTime = lastEndTime;
	}
	public String getVlanTag() {
		return vlanTag;
	}
	public void setVlanTag(String vlanTag) {
		this.vlanTag = vlanTag;
	}
	public String getEpCode() {
		return epCode;
	}
	public void setEpCode(String epCode) {
		this.epCode = epCode;
	}
	public Long getRenewAgreementId() {
		return renewAgreementId;
	}
	public void setRenewAgreementId(Long renewAgreementId) {
		this.renewAgreementId = renewAgreementId;
	}
	public Boolean getIsRenew() {
		return isRenew;
	}
	public void setIsRenew(Boolean isRenew) {
		this.isRenew = isRenew;
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
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getSpmsNo() {
		return spmsNo;
	}
	public void setSpmsNo(String spmsNo) {
		this.spmsNo = spmsNo;
	}
}