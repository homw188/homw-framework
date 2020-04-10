package com.homw.common.bean;

import org.apache.poi.ss.usermodel.Row;

import com.homw.common.annotation.ExcelField;

/**
 * @description Excel导入，字段校验接口
 * @author James
 * @version 1.0
 * @date 2019-05-06
 */
public interface ExcelFieldValidator 
{
	/**
	 * 校验
	 * @param field 字段元数据
	 * @param value 字段值
	 * @param row 当前行
	 * @param column 当前单元格索引
	 * @param target 映射对象
	 * @return
	 */
	<E> boolean validate(ExcelField field, Object value, Row row, int column, E target);
}
