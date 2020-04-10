package com.homw.tool.entity;

import java.io.Serializable;

import com.homw.common.annotation.ExcelField;

/**
 * spmsNo表
 * 
 * @author System
 * @email 
 * @date 2019-05-20 15:13:52
 */
public class SpmsNoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ExcelField(title = "ID", sort = 1)
	private Long dataId;
	@ExcelField(title = "编号", sort = 2)
	private String spmsNo;
	
	// 表名
	private String tableName;
	// id字段名
	private String idColumn;
	
	public Long getDataId() {
		return dataId;
	}
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	public String getSpmsNo() {
		return spmsNo;
	}
	public void setSpmsNo(String spmsNo) {
		this.spmsNo = spmsNo;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}
	
}
