package com.homw.tool.entity;

import java.io.Serializable;

public class LightSerialLocationEntity implements Serializable {

	private static final long serialVersionUID = -6035060185968029733L;

	private Long id;

    private String codeInit;

    private Long areaId;

    private String serialNum;

    private String locationData;

    private Integer editState;
    
    private String lampsKey;
    public String getLampsKey() {
		return lampsKey;
	}
	public void setLampsKey(String lampsKey) {
		this.lampsKey = lampsKey;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeInit() {
        return codeInit;
    }

    public void setCodeInit(String codeInit) {
        this.codeInit = codeInit;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public Integer getEditState() {
        return editState;
    }

    public void setEditState(Integer editState) {
        this.editState = editState;
    }
}
