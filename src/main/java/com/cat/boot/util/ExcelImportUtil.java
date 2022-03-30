package com.cat.boot.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelImportUtil {

	public static List<Map<String, Object>> readXls(MultipartFile file) throws Exception {
		InputStream is = file.getInputStream();

		System.out.println(file.getOriginalFilename());
		// 循环工作表Sheet
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (file.getOriginalFilename().contains("xlsx")) {
			XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(is);
			for (int numSheet = 0; numSheet < xSSFWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet hssfSheet = xSSFWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				Map<Object, String> key = new HashMap<Object, String>();
				// 循环行Row
				for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}
					hssfRow.getLastCellNum();
					// 循环列Cell
					// ExcelBean eb = new ExcelBean();
					Map<String, Object> values = new HashMap<String, Object>();
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
						XSSFCell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}
						Object value;
						value = getValue(hssfCell, key.get("TYPE" + String.valueOf(cellNum)));
						// 取第一行的值作为key
						if (rowNum == 0) {
							String tmp = (String) value;
							if (!StringUtil.isEmpty(tmp) && !"null".equals(tmp)) {
								key.put(cellNum, (String) value);
							}
						} else if (rowNum == 2) {
							System.out.println(cellNum);
							System.out.println(value);

							key.put("TYPE" + String.valueOf(cellNum), (String) value);
						} else {
							if (key.containsKey(cellNum)) {
								values.put(key.get(cellNum), value);
							}
						}
					}
					if (!StringUtil.isMapEmpty(values)) {
						list.add(values);
					}
				}
			}
		} else {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				Map<Object, String> key = new HashMap<Object, String>();
				// 循环行Row
				for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}
					hssfRow.getLastCellNum();
					// 循环列Cell
					// ExcelBean eb = new ExcelBean();
					Map<String, Object> values = new HashMap<String, Object>();
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
						HSSFCell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}
						Object value;
						value = getValue(hssfCell, key.get("TYPE" + String.valueOf(cellNum)));
						// 取第一行的值作为key
						if (rowNum == 0) {
							String tmp = (String) value;
							if (!StringUtil.isEmpty(tmp) && !"null".equals(tmp)) {
								key.put(cellNum, (String) value);
							}
						} else if (rowNum == 2) {
							key.put("TYPE" + String.valueOf(cellNum), (String) value);
						} else {
							if (key.containsKey(cellNum)) {
								values.put(key.get(cellNum), value);
							}
						}
					}
					if (!StringUtil.isMapEmpty(values)) {
						list.add(values);
					}
				}
			}
		}
		return list;
	}

	public static List<Map<String, Object>> readXls2(MultipartFile file) throws Exception {
		InputStream is = file.getInputStream();
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// 循环工作表Sheet
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			Map<Object, String> key = new HashMap<Object, String>();
			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				System.out.println("rowNum" + rowNum);
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				hssfRow.getLastCellNum();
				// 循环列Cell
				// ExcelBean eb = new ExcelBean();
				Map<String, Object> values = new HashMap<String, Object>();
				for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
					HSSFCell hssfCell = hssfRow.getCell(cellNum);
					if (hssfCell == null) {
						continue;
					}
					Object value;
					value = getValue(hssfCell, key.get("TYPE" + String.valueOf(cellNum)));
					// 取第一行的值作为key
					if (rowNum == 0) {
						System.out.println("v:" + value);
						String tmp = (String) value;
						if (!StringUtil.isEmpty(tmp) && !"null".equals(tmp)) {
							key.put(cellNum, (String) value);
						}
					} else {
						if (key.containsKey(cellNum)) {
							values.put(key.get(cellNum), value);
						}
					}
				}
				if (!StringUtil.isMapEmpty(values)) {
					list.add(values);
				}
			}
		}
		return list;
	}

	private static Object getValue(HSSFCell hssfCell, String type) {
		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue()).trim();
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(hssfCell)) {
				Date theDate = hssfCell.getDateCellValue();
				SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
				return dff.format(theDate);
			} else {
				if ("String".equals(type) || "int".equals(type)) {
					DecimalFormat df = new DecimalFormat("0");
					String tmp = df.format(hssfCell.getNumericCellValue());
					if ("String".equals(type)) {
						return tmp;
					} else {
						return Integer.valueOf(tmp);
					}
				} else {
					return hssfCell.getNumericCellValue();
				}
			}
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue()).trim();
		}
	}

	private static Object getValue(XSSFCell hssfCell, String type) {
		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue()).trim();
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(hssfCell)) {
				Date theDate = hssfCell.getDateCellValue();
				SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
				return dff.format(theDate);
			} else {
				if ("String".equals(type) || "int".equals(type)) {
					DecimalFormat df = new DecimalFormat("0");
					String tmp = df.format(hssfCell.getNumericCellValue());
					if ("String".equals(type)) {
						return tmp;
					} else {
						return Integer.valueOf(tmp);
					}
				} else {
					return hssfCell.getNumericCellValue();
				}
			}
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue()).trim();
		}
	}
}
