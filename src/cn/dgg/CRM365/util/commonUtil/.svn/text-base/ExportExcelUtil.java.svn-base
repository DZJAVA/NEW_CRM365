package cn.dgg.CRM365.util.commonUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.hssf.util.HSSFColor.RED;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import cn.dgg.CRM365.util.orm.ICommonDAO;

public class ExportExcelUtil {
	/**
	 * 执行导出
	 * 
	 * @param dao
	 *            e2q封装的dao类
	 * @param clazz
	 *            导出的实体类的Class
	 * @param conditions
	 *            查询条件
	 * @param headers
	 *            excle表头
	 * @param configMap
	 *            配置键值对，键为表头，值为实体属性
	 * @param centerName
	 *            excel名称
	 * @param sheetName
	 *            excel中一页的名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public byte[] executeExport(ICommonDAO dao, Class clazz, String conditions,
			String[] headers, LinkedHashMap<String, String> configMap,
			String centerName, String sheetName) {
		// 执行方法必须条件判定
		if (dao == null || headers.length == 0 || configMap.size() == 0) {
			return new byte[] {};
		}
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建excel中的sheet页
		HSSFSheet excelSheet = workbook.createSheet(sheetName);
		// 创建表头
		createHeader(excelSheet, headers, centerName);
		// 获取查询值，并转换成想要的数组
		List<Map<String, String>> results = getResultList(dao, clazz,
				conditions, configMap);
		// 遍历集合中的Object数组
		for (int i = 0; i < results.size(); i++) {
			// 创建excel中的行
			createRow(excelSheet, i, headers, results.get(i));
		}

		return createResultBytes(workbook, excelSheet);
	}

	/**
	 * 执行导出
	 * 
	 * @param dao
	 *            e2q封装的dao类
	 * @param clazz
	 *            导出的实体类的Class
	 * @param conditions
	 *            查询条件
	 * @param headers
	 *            excle表头
	 * @param configMap
	 *            配置键值对，键为表头，值为实体属性
	 * @param centerName
	 *            excel名称
	 * @param sheetName
	 *            excel中一页的名称
	 * @return
	 */
	public byte[] executeExport(List<?> dataList, String[] headers,
			Map<String, String> configMap, String centerName, String sheetName) {
		// 执行方法必须条件判定
		if (headers.length == 0 || configMap.size() == 0) {
			return new byte[] {};
		}
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建excel中的sheet页
		HSSFSheet excelSheet = workbook.createSheet(sheetName);
		// 创建表头
		createHeader(excelSheet, headers, centerName);
		// 获取查询值，并转换成想要的数组
		List<Map<String, String>> results = getResultList(dataList, configMap);
		// 遍历集合中的Object数组
		for (int i = 0; i < results.size(); i++) {
			// 创建excel中的行
			createRow(excelSheet, i, headers, results.get(i));
		}

		return createResultBytes(workbook, excelSheet);
	}

	/**
	 * 执行导出
	 * 
	 * @param dao
	 *            e2q封装的dao类
	 * @param clazz
	 *            导出的实体类的Class
	 * @param conditions
	 *            查询条件
	 * @param headers
	 *            excle表头
	 * @param configMap
	 *            配置键值对，键为表头，值为实体属性
	 * @param centerName
	 *            excel名称
	 * @param sheetName
	 *            excel中一页的名称
	 * @return
	 */
	public byte[] executeExport1(List<?> dataList, String[] headers,
			Map<String, String> configMap, String centerName, String sheetName) {
		// 执行方法必须条件判定
		if (headers.length == 0 || configMap.size() == 0) {
			return new byte[] {};
		}
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建excel中的sheet页
		HSSFSheet excelSheet = workbook.createSheet(sheetName);

		// 创建表头
		createHeader(workbook, excelSheet, headers, centerName);
		// 获取查询值，并转换成想要的数组
		List<Map<String, String>> results = getResultList(dataList, configMap);
		// 遍历集合中的Object数组
		for (int i = 0; i < results.size(); i++) {
			// 创建excel中的行
			createRow(excelSheet, i + 1, headers, results.get(i));
		}

		return createResultBytes(workbook, excelSheet);
	}

	public byte[] executeExport2(List<?> dataList, String[] headers,
			Map<String, String> configMap, String centerName, String sheetName) {
		// 执行方法必须条件判定
		if (headers.length == 0 || configMap.size() == 0) {
			return new byte[] {};
		}
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建excel中的sheet页
		HSSFSheet excelSheet = workbook.createSheet(sheetName);

		// 创建表头
		createHeader2(workbook, excelSheet, headers, centerName);
		// 获取查询值，并转换成想要的数组
		List<Map<String, String>> results = getResultList(dataList, configMap);
		// 遍历集合中的Object数组
		for (int i = 0; i < results.size(); i++) {
			// 创建excel中的行
			createRow(excelSheet, i + 1, headers, results.get(i));
		}

		return createResultBytes(workbook, excelSheet);
	}

	/**
	 * 执行导出
	 * 
	 * @param headers
	 *            excle表头
	 * @param exportData
	 *            所需导出数据
	 * @param configMap
	 *            配置键值对，键为表头，值为实体属性
	 * @param centerName
	 *            excel名称
	 * @param sheetName
	 *            excel中一页的名称
	 * @return
	 */
	public byte[] executeExport(String[] headers, JSONArray exportData,
			Map<String, String> configMap, String centerName, String sheetName) {
		// 执行方法必须条件判定
		if (headers.length == 0 || configMap.size() == 0) {
			return new byte[] {};
		}
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建excel中的sheet页
		HSSFSheet excelSheet = workbook.createSheet(sheetName);
		// 创建表头
		createHeader(excelSheet, headers, centerName);

		for (int i = 0; i < exportData.size(); i++) {
			createJSONRow(excelSheet, i, headers, (JSONObject) exportData
					.get(i), configMap);
		}
		return createResultBytes(workbook, excelSheet);
	}

	// 创建行
	private void createJSONRow(HSSFSheet sheet, int rowIndex, String[] headers,
			JSONObject obj, Map<String, String> configMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Entry<String, String> entry : configMap.entrySet()) {
			resultMap.put(entry.getKey(), formatObject(obj
					.get(entry.getValue() == null ? "" : entry.getValue())));
		}
		createRow(sheet, rowIndex, headers, resultMap);
	}

	// 获取查询结果
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getResultList(ICommonDAO dao,
			Class clazz, String conditions,
			LinkedHashMap<String, String> configMap) {
		StringBuffer hql = new StringBuffer("SELECT ");
		int entryIndex = 0;
		// 构建查询语句
		for (Entry<String, String> entry : configMap.entrySet()) {
			if (entryIndex == configMap.entrySet().size() - 1) {
				hql.append(entry.getValue());
				hql.append(" FROM ");
				hql.append(clazz.getSimpleName());
			} else {
				hql.append(entry.getValue());
				hql.append(",");
			}
			entryIndex++;
		}
		// 构建查询条件
		if (conditions != null && !"".equals(conditions)) {
			hql.append(" WHERE ");
			hql.append(conditions);
		}
		List<Object[]> results;
		// 查询所需结果
		try {
			results = dao.findAll(hql.toString());
		} catch (Exception e) {
			results = new ArrayList<Object[]>();
		}
		// 构建返回集合
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < results.size(); i++) {
			Map<String, String> resultMap = new HashMap<String, String>();
			entryIndex = 0;
			// 构建输出的键值对
			for (String key : configMap.keySet()) {
				resultMap.put(key, formatObject(results.get(i)[entryIndex]));
				entryIndex++;
			}
			list.add(resultMap);
		}
		return list;
	}

	// 获取查询结果
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getResultList(List<?> dataList,
			Map<String, String> configMap) {
		// 构建返回集合
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> resultMap = new HashMap<String, String>();
			for (Entry<String, String> entry : configMap.entrySet()) {
				resultMap.put(entry.getKey(), getObjectResult(dataList.get(i),
						entry.getValue()));
			}
			list.add(resultMap);
		}
		return list;
	}

	// 反射调用实体方法
	private String getObjectResult(Object obj, String property) {
		if (property == null) {
			return "";
		}

		String[] propertys = property.split("\\.");

		if (propertys.length == 0) {
			obj = getInvokeResult(obj, property);
		} else {
			for (String propertyName : propertys) {
				if (obj != null) {
					obj = getInvokeResult(obj, propertyName);
					System.out.print(propertyName);
				} else {
					return "";
				}
			}
		}

		return formatObject(obj);
	}

	// 反射获取方法的值
	private Object getInvokeResult(Object obj, String oneProperty) {
		if (oneProperty.length() < 2) {
			return null;
		}
		String methodName = "get" + oneProperty.substring(0, 1).toUpperCase()
				+ oneProperty.substring(1);
		try {
			Method method = obj.getClass().getMethod(methodName, new Class[] {});
			return method.invoke(obj, new Object[] {});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 格式化数据
	private String formatObject(Object obj) {
		if (obj == null) {
			return "";
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Long) {
			return obj.toString();
		} else if (obj instanceof Double || obj instanceof Float) {
			DecimalFormat df = new DecimalFormat("######0.00");
			return df.format(obj);
		} else if (obj instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format((Date) obj);
		} else {
			String k = obj + "";
			return k;
		}
	}

	// 创建表头
	private void createHeader(HSSFSheet sheet, String[] headers,
			String centerName) {
		HSSFHeader tabHeader = sheet.getHeader();
		tabHeader.setCenter(centerName);
		HSSFRow heardRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell headerCell = heardRow.createCell(i);
			headerCell.setCellValue(headers[i]);
		}
	}

	// 创建导出日报表表头
	private void createHeader(HSSFWorkbook hw, HSSFSheet sheet,
			String[] headers, String centerName) {
		HSSFHeader tabHeader = sheet.getHeader();
		HSSFCellStyle style1 = hw.createCellStyle(); // 样式对象
		HSSFFont font1 = hw.createFont();
		// 设置字体大小
		font1.setFontHeight((short) 220);
		// 设置字体为粗体
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 指定单元格垂直居中对齐
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 指定单元格居中对齐
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置单元格之间的缩进距离
		// style1.setIndention((short)100);
		// 把字体应用到当前的样式
		// 设置表格背景颜色
		// style1.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
		style1.setFont(font1);
		// 四个参数分别是：起始行，起始列，结束行，结束列
		sheet.addMergedRegion(new Region(0, (short) 0, 1,
				(short) (headers.length - 7)));
		sheet.addMergedRegion(new Region(0, (short) 1, 1,
				(short) (headers.length - 6)));
		sheet.addMergedRegion(new Region(0, (short) 2, 0,
				(short) (headers.length - 1)));
		// 指定当单元格内容显示不下时自动换行
		style1.setWrapText(true);
		tabHeader.setCenter(centerName);
		HSSFRow heardRow = sheet.createRow(0);
		HSSFRow heardRow1 = sheet.createRow(1);
		// 循环前面两个合并的单元格
		for (int i = 0; i < 2; i++) {
			HSSFCell headerCell = heardRow.createCell(i);
			headerCell.setCellValue(headers[i]);
			headerCell.setCellStyle(style1);
			sheet.setColumnWidth(i, 4000);
		}
		// 显示上面宣传报道标题
		HSSFCell headerCell = heardRow.createCell(2);
		headerCell.setCellValue("宣传报道（含四川电力相关报道关键词指标）");
		headerCell.setCellStyle(style1);
		sheet.setColumnWidth(3, 4000);
		// 循环宣传报道标题下的内容
		for (int j = 2; j < headers.length; j++) {
			HSSFCell headerCell2 = heardRow1.createCell(j);
			headerCell2.setCellValue(headers[j]);
			headerCell2.setCellStyle(style1);
			sheet.setColumnWidth(j, 4000);
		}
	}

	private void createHeader2(HSSFWorkbook hw, HSSFSheet sheet,
			String[] headers, String centerName) {
		HSSFHeader tabHeader = sheet.getHeader();
		HSSFCellStyle style1 = hw.createCellStyle(); // 样式对象
		HSSFFont font1 = hw.createFont();
		// 设置字体大小
		font1.setFontHeight((short) 220);
		// 设置字体为粗体
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 指定单元格垂直居中对齐
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 指定单元格居中对齐
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置单元格之间的缩进距离
		// style1.setIndention((short)100);
		// 把字体应用到当前的样式
		// 设置表格背景颜色
		// style1.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
		style1.setFont(font1);
		// 四个参数分别是：起始行，起始列，结束行，结束列
		sheet.addMergedRegion(new Region(0, (short) 0, 1, (short) 0));
		sheet.addMergedRegion(new Region(0, (short) 1, 1, (short) 2));
		sheet.addMergedRegion(new Region(0, (short) 3, 1, (short) 4));
		sheet.addMergedRegion(new Region(0, (short) (headers.length - 3), 1,
				(short) (headers.length - 3)));
		sheet.addMergedRegion(new Region(0, (short) (headers.length - 2), 1,
				(short) (headers.length - 2)));
		sheet.addMergedRegion(new Region(0, (short) (headers.length - 1), 1,
				(short) (headers.length - 1)));
		sheet.addMergedRegion(new Region(0, (short) 5, 0, (short) 8));
		sheet.addMergedRegion(new Region(0, (short) 9, 0, (short) 12));
		sheet.addMergedRegion(new Region(0, (short) 13, 0, (short) 16));
		sheet.addMergedRegion(new Region(0, (short) 17, 0, (short) 19));
		sheet.addMergedRegion(new Region(0, (short) 21, 0, (short) 24));
		// 指定当单元格内容显示不下时自动换行
		style1.setWrapText(true);
		tabHeader.setCenter(centerName);
		HSSFRow heardRow = sheet.createRow(0);
		HSSFRow heardRow1 = sheet.createRow(1);
		// 循环前面三个合并的单元格
		for (int i = 0; i < 4; i++) {
			HSSFCell headerCell = heardRow.createCell(i);
			headerCell.setCellValue(headers[i]);
			headerCell.setCellStyle(style1);
			sheet.setColumnWidth(i, 4000);
		}

		// String[] aa = {"","","","中央媒体","","","","省级媒体","地方媒体","行业媒体","公司媒体"};
		// for(int b = 3;b < 11;b++){
		// HSSFCell headerCell8 = heardRow.createCell(b+3);
		// headerCell8.setCellValue(aa[b]);
		// headerCell8.setCellStyle(style1);
		// sheet.setColumnWidth(b+3, 4000);
		// }
		HSSFCell headerCell8 = heardRow.createCell(5);
		headerCell8.setCellValue("中央媒体");
		headerCell8.setCellStyle(style1);
		sheet.setColumnWidth(5, 4000);

		HSSFCell headerCell5 = heardRow.createCell(9);
		headerCell5.setCellValue("省级媒体");
		headerCell5.setCellStyle(style1);
		sheet.setColumnWidth(9, 4000);

		HSSFCell headerCell6 = heardRow.createCell(13);
		headerCell6.setCellValue("地方媒体");
		headerCell6.setCellStyle(style1);
		sheet.setColumnWidth(13, 4000);

		HSSFCell headerCell7 = heardRow.createCell(17);
		headerCell7.setCellValue("行业媒体");
		headerCell7.setCellStyle(style1);
		sheet.setColumnWidth(17, 4000);

		HSSFCell headerCell9 = heardRow.createCell(21);
		headerCell9.setCellValue("公司媒体");
		headerCell9.setCellStyle(style1);
		sheet.setColumnWidth(21, 4000);

		for (int j = 5; j < headers.length - 3; j++) {
			HSSFCell headerCell2 = heardRow1.createCell(j);
			headerCell2.setCellValue(headers[j]);
			headerCell2.setCellStyle(style1);
			sheet.setColumnWidth(j, 1500);
		}

		for (int a = headers.length - 3; a < headers.length; a++) {
			HSSFCell headerCell = heardRow.createCell(a);
			headerCell.setCellValue(headers[a]);
			headerCell.setCellStyle(style1);
			sheet.setColumnWidth(a, 4000);
		}
	}

	// 创建行
	private void createRow(HSSFSheet sheet, int rowIndex, String[] headers,
			Map<String, String> resultMap) {
		HSSFRow row = sheet.createRow(rowIndex + 1);
		for (int i = 0; i < headers.length; i++) {
			if (resultMap.get(headers[i]) != null) {
				String cellValue = "";
				cellValue = resultMap.get(headers[i]);
				if ("商机类型".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "房贷"
							: "信贷";
					if ("3".equals(resultMap.get(headers[i]))) {
						cellValue = "短借";
					}
					if ("4".equals(resultMap.get(headers[i]))) {
						cellValue = "企贷";
					}
				}
				if ("客户状态".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "已签单"
							: "未签单";
					if ("3".equals(resultMap.get(headers[i]))) {
						cellValue = "淘汰";
					}
					if ("4".equals(resultMap.get(headers[i]))) {
						cellValue = "退单";
					}
				}
				if ("成单率".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "100%"
							: "80%";
					if ("3".equals(resultMap.get(headers[i]))) {
						cellValue = "50%";
					}
					if ("4".equals(resultMap.get(headers[i]))) {
						cellValue = "0%";
					}
				}
				if ("是否免费".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "免费": "扣款";
				}
				if("性别".equals(headers[i])){
					if("1".equals(resultMap.get(headers[i]))){
						cellValue = "男";
					}
					if("2".equals(resultMap.get(headers[i]))){
						cellValue = "女";
					}
				}
				if("婚姻状况".equals(headers[i])){
					if("1".equals(resultMap.get(headers[i]))){
						cellValue = "已婚";
					}
					if("2".equals(resultMap.get(headers[i]))){
						cellValue = "未婚";
					}
				}
				if ("在职状态".equals(headers[i])) {
					if("1".equals(resultMap.get(headers[i]))){
						cellValue = "实习";
					}
					if("2".equals(resultMap.get(headers[i]))){
						cellValue = "见习";
					}
					if("3".equals(resultMap.get(headers[i]))){
						cellValue = "正式";
					}
				}
				if ("接单状态".equals(headers[i])) {
					if("0".equals(resultMap.get(headers[i]))){
						cellValue = "不接单";
					}
					if("1".equals(resultMap.get(headers[i]))){
						cellValue = "接单";
					}
				}
				if ("属性".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "自我安排"
							: "领导安排";
					if ("3".equals(resultMap.get(headers[i]))) {
						cellValue = "协作他人";
					}
				}
				if ("员工状态".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "在职"
							: "离职";
					if ("3".equals(resultMap.get(headers[i]))) {
						cellValue = "休假";
					}
				}
				if ("完成情况".equals(headers[i])) {
					cellValue = "1".equals(resultMap.get(headers[i])) ? "完成"
							: "未完成";
				}
				row.createCell(i).setCellValue(cellValue);
			}
		}
	}

	// 创建输出的字节码
	private byte[] createResultBytes(HSSFWorkbook workbook, HSSFSheet excelSheet) {
		excelSheet.setGridsPrinted(true);
		HSSFFooter footer = excelSheet.getFooter();
		footer.setRight("page" + HSSFFooter.page() + " of"
				+ HSSFFooter.numPages());
		// 创建输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] resultBytes;
		try {
			// 写入输出流
			workbook.write(os);
			// 转换字节码
			resultBytes = os.toByteArray();
		} catch (IOException e) {
			resultBytes = new byte[] {};
		} finally {
			try {
				// 关闭输出流
				os.close();
			} catch (IOException e) {
				System.out.println("执行导出时输出流关闭出现错误！");
			}
		}
		return resultBytes;
	}
}
