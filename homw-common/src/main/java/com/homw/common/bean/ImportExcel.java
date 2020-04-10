package com.homw.common.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.common.annotation.ExcelField;
import com.homw.common.util.ReflectUtil;

/**
 * @description 导入Excel文件（支持“XLS”和“XLSX”格式）
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public class ImportExcel {
	private static Logger log = LoggerFactory.getLogger(ImportExcel.class);

	/**
	 * 工作薄对象
	 */
	private Workbook wb;

	/**
	 * 工作表对象
	 */
	private Sheet sheet;

	/**
	 * 标题行号
	 */
	private int headerNum;

	/**
	 * 日期格式化
	 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 构造函数
	 * 
	 * @param path      导入文件，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(String fileName, int headerNum) throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum);
	}

	/**
	 * 构造函数
	 * 
	 * @param path      导入文件对象，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(File file, int headerNum) throws InvalidFormatException, IOException {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 * 
	 * @param path       导入文件
	 * @param headerNum  标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(String fileName, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum, sheetIndex);
	}

	/**
	 * 构造函数
	 * 
	 * @param path       导入文件对象
	 * @param headerNum  标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(File file, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
		this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
	}

	/**
	 * 构造函数
	 * 
	 * @param path       导入文件对象
	 * @param headerNum  标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex)
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)) {
			throw new RuntimeException("导入文档为空!");
		} else if (fileName.toLowerCase().endsWith("xls")) {
			this.wb = new HSSFWorkbook(is);
		} else if (fileName.toLowerCase().endsWith("xlsx")) {
			this.wb = new XSSFWorkbook(is);
		} else {
			throw new RuntimeException("文档格式不正确!");
		}

		if (this.wb.getNumberOfSheets() < sheetIndex) {
			throw new RuntimeException("文档中没有工作表!");
		}
		this.sheet = this.wb.getSheetAt(sheetIndex);
		this.headerNum = headerNum;
		log.debug("Initialize success.");
	}

	/**
	 * 获取行对象
	 * 
	 * @param rownum
	 * @return
	 */
	public Row getRow(int rownum) {
		return this.sheet.getRow(rownum);
	}

	/**
	 * 获取数据行号
	 * 
	 * @return
	 */
	public int getDataRowNum() {
		return headerNum + 1;
	}

	/**
	 * 获取最后一个数据行号
	 * 
	 * @return
	 */
	public int getLastDataRowNum() {
		return this.sheet.getLastRowNum() + 1;
	}

	/**
	 * 获取最后一个列号
	 * 
	 * @return
	 */
	public int getLastCellNum() {
		return this.getRow(headerNum).getLastCellNum();
	}

	/**
	 * 获取单元格值
	 * 
	 * @param row    获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column) {
		Object val = "";
		try {
			Cell cell = row.getCell(column);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					// val = cell.getNumericCellValue();
					// 当excel 中的数据为数值或日期是需要特殊处理
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						double d = cell.getNumericCellValue();
						Date date = HSSFDateUtil.getJavaDate(d);
						val = dateFormat.format(date);
					} else {
						NumberFormat nf = NumberFormat.getInstance();
						nf.setGroupingUsed(false);// true时的格式：1,234,567,890
						val = nf.format(cell.getNumericCellValue());// 数值类型的数据为double，所以需要转换一下
					}
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					val = cell.getStringCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					val = cell.getCellFormula();
				} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					val = cell.getBooleanCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
					val = cell.getErrorCellValue();
				}
			}
		} catch (Exception e) {
			return val;
		}
		return val;
	}

	/**
	 * 获取导入数据列表
	 * 
	 * @param cls       导入对象类型
	 * @param validator 字段校验器
	 * @param groups    导入分组
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public <E> List<E> getDataList(Class<E> cls, ExcelFieldValidator validator, int... groups)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException {
		List<Object[]> annotationList = new ArrayList<>();

		getAnnotationList(cls, annotationList, groups);

		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
			};
		});

		// log.debug("Import column count:"+annotationList.size());
		// Get excel data
		boolean isValid = true;
		List<E> dataList = new ArrayList<>();
		for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
			E e = (E) cls.newInstance();
			int column = 0;
			Row row = this.getRow(i);
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList) {
				Object val = this.getCellValue(row, column++);
				if (val != null) {
					ExcelField ef = (ExcelField) os[0];

					if (validator != null) {
						boolean validateRet = validator.validate(ef, val, row, column - 1, e);
						if (!validateRet) {
							if (isValid) {
								isValid = validateRet;
							}
							markInvalidCell(row, column - 1);
						}
					}

					// If is dict type, get dict value
					/*
					 * if (StringUtils.isNotBlank(ef.dictType())){ val =
					 * DictUtils.getDictValue(val.toString(), ef.dictType(), "");
					 * //log.debug("Dictionary type value: ["+i+","+colunm+"] " + val); }
					 */

					// Get param type and type cast
					Class<?> valType = Class.class;
					if (os[1] instanceof Field) {
						valType = ((Field) os[1]).getType();
					} else if (os[1] instanceof Method) {
						Method method = ((Method) os[1]);
						if ("get".equals(method.getName().substring(0, 3))) {
							valType = method.getReturnType();
						} else if ("set".equals(method.getName().substring(0, 3))) {
							valType = ((Method) os[1]).getParameterTypes()[0];
						}
					}

					// log.debug("Import value type: ["+i+","+column+"] " + valType);
					try {
						// 如果导入的java对象，需要在这里自己进行变换。
						if (valType == String.class) {
							String s = String.valueOf(val.toString());
							if (StringUtils.endsWith(s, ".0")) {
								val = StringUtils.substringBefore(s, ".0");
							} else {
								val = String.valueOf(val.toString());
							}
						} else if (valType == Integer.class) {
							val = Double.valueOf(val.toString()).intValue();
						} else if (valType == Long.class) {
							val = Double.valueOf(val.toString()).longValue();
						} else if (valType == Double.class) {
							val = Double.valueOf(val.toString());
						} else if (valType == Float.class) {
							val = Float.valueOf(val.toString());
						} else if (valType == Date.class) {
							val = dateFormat.parse(val.toString());
						} else if (valType == BigDecimal.class) {
							val = new BigDecimal(val.toString());
						} /*
							 * else if (valType == User.class){ val =
							 * UserUtils.getByUserName(val.toString()); }else if (valType ==
							 * Organization.class){ val = UserUtils.getByOrganizationName(val.toString());
							 * }else if (valType == Area.class){ val =
							 * UserUtils.getByAreaName(val.toString()); }
							 */else {
							if (ef.fieldType() != Class.class) {
								val = ef.fieldType().getMethod("getValue", String.class).invoke(null, val.toString());
							} else {
								val = Class
										.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
												"fieldtype." + valType.getSimpleName() + "Type"))
										.getMethod("getValue", String.class).invoke(null, val.toString());
							}
						}
					} catch (Exception ex) {
						log.info("Get cell value [" + i + "," + column + "] error: " + ex.toString());
						val = null;
					}

					// set entity value
					if (os[1] instanceof Field) {
						ReflectUtil.invokeSetter(e, ((Field) os[1]).getName(), val);
					} else if (os[1] instanceof Method) {
						String mthodName = ((Method) os[1]).getName();
						if ("get".equals(mthodName.substring(0, 3))) {
							mthodName = "set" + StringUtils.substringAfter(mthodName, "get");
						}
						ReflectUtil.invokeMethod(e, mthodName, new Class[] { valType }, new Object[] { val });
					}
				}
				sb.append(val + ", ");
			}
			dataList.add(e);
			log.debug("Read success: [" + i + "] " + sb.toString());
		}

		if (!isValid) {
			throw new IllegalArgumentException("设备参数异常，请检查导入文件");
		}
		return dataList;
	}

	/**
	 * 标记非法字段
	 * 
	 * @param row
	 * @param column
	 */
	private void markInvalidCell(Row row, int column) {
		Cell cell = row.getCell(column);
		if (cell == null) {
			cell = row.createCell(column);
		}
		CellStyle style = wb.createCellStyle();
		style.cloneStyleFrom(cell.getCellStyle());
		style.setFillForegroundColor(IndexedColors.RED.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style);
	}

	private <E> void getAnnotationList(Class<E> cls, List<Object[]> annotationList, int... groups) {
		if (cls == null) {
			return;
		}

		// Get annotation field
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			ExcelField ef = f.getAnnotation(ExcelField.class);
			addAnnotationField(annotationList, f, ef, groups);
		}

		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms) {
			ExcelField ef = m.getAnnotation(ExcelField.class);
			addAnnotationField(annotationList, m, ef, groups);
		}
		getAnnotationList(cls.getSuperclass(), annotationList, groups);
	}

	private void addAnnotationField(List<Object[]> annotationList, Object source, ExcelField ef, int... groups) {
		if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
			if (groups != null && groups.length > 0) {
				boolean inGroup = false;
				for (int g : groups) {
					if (inGroup) {
						break;
					}

					for (int efg : ef.groups()) {
						if (g == efg) {
							inGroup = true;
							annotationList.add(new Object[] { ef, source });
							break;
						}
					}
				}
			} else {
				annotationList.add(new Object[] { ef, source });
			}
		}
	}

	/**
	 * 输出数据流
	 * 
	 * @param os 输出数据流
	 */
	public ImportExcel write(OutputStream os) throws IOException {
		wb.write(os);
		return this;
	}

	/**
	 * 输出到客户端
	 * 
	 * @param fileName 输出文件名
	 */
	public ImportExcel write(OutputStream out, String fileName) throws IOException {
		write(out);
		return this;
	}

	/**
	 * 输出到文件
	 * 
	 * @param fileName 输出文件名
	 */
	public ImportExcel writeFile(String name) throws FileNotFoundException, IOException {
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}

}