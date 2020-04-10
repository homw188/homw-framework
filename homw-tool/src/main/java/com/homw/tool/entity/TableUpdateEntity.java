package com.homw.tool.entity;

import java.io.Serializable;

import com.homw.common.annotation.ExcelField;

/**
 * TableUpdate表
 * 
 * @author System
 * @email 
 * @date 2019-07-18 10:21:52
 */
public class TableUpdateEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ExcelField(title = "ID", sort = 1)
	private Long dataId;
	@ExcelField(title = "更新列", sort = 2)
	private String dataValue;
	
	// 表名
	private String tableName;
	// id字段名
	private String idColumn;
	// 更新字段
	private String updateColumn;
	
	public Long getDataId() {
		return dataId;
	}
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getUpdateColumn() {
		return updateColumn;
	}
	public void setUpdateColumn(String updateColumn) {
		this.updateColumn = updateColumn;
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
