package com.homw.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.homw.common.bean.ExportExcel;

/**
 * Excel操作测试
 * 
 * @author Hom
 * @version 1.0
 * @since 2018-12-12
 */
public class ExcelTest {
	/**
	 * 生成excel工作簿
	 * 
	 * @param elementType
	 * @param dataList
	 * @param title
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SXSSFWorkbook build(Class elementType, List dataList, String title) {
		ExportExcel export = new ExportExcel(title, elementType, 1);
		export.setDataList(dataList);
		return export.getWorkBook();
	}

	/**
	 * 下载excel
	 * 
	 * @param workBook excel工作簿
	 * @param response HTTP响应
	 * @param fileName 文件名称
	 * 
	 * @throws IOException
	 */
	public void download(SXSSFWorkbook workBook, OutputStream response, String fileName) throws IOException {
		workBook.write(response);
		workBook.dispose();
	}
}
