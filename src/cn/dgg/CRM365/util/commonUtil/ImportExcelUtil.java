package cn.dgg.CRM365.util.commonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 导入Excel表
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 21, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
public class ImportExcelUtil {
	/**
	 * 执行导入
	  *<功能简述>
	  *<功能详细描述>
	  *@param sheetNum 导入sheet第几页
	  * @param is 输入流
	  * @param fields 实体字段数组
	  * @param list 返回的实体集合
	  * @return
	  * @throws IOException [参数说明]
	  * 
	  * @return List<?> [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public static List<Map<String, String>> readXls(int sheetNum, InputStream is, String[] fields, List<Map<String, String>> list) throws Exception {
		Workbook book = create(is);
//		XSSFWorkbook workbook = new XSSFWorkbook(is);
		list = cycleSheet(sheetNum, book, fields, list);
		return list;
	} 
	/**
	 * 获取想要第几张工作表的数据
	  *<功能简述>
	  *<功能详细描述>
	  * @param hw HSSFWorkbook
	  * @param fields
	  * @param list [参数说明]
	  * 
	  * @return void [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	private static List<Map<String, String>> cycleSheet(int sheetNum, Workbook hw, String[] fields, List<Map<String, String>> list) throws Exception{
		Sheet sheet = null;//sheet
		Row xr = null;//行
		sheet = hw.getSheetAt(sheetNum);
		if(sheet != null){
			// 循环行Row
			fields = juge(sheet.getRow(0), fields);
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) { 
            	int flag = 0;
                xr = sheet.getRow(rowNum);
                if (xr == null) {
                    continue;
                }
                // 循环列Cell
                Map<String, String> map = new HashMap<String, String>();
                for (int cellNum = 0; cellNum < fields.length; cellNum++) {
                	if(xr.getCell(cellNum) == null){
                		map.put(fields[cellNum], "");
                		flag++;
	               		continue;
               	 	}
                	if(!"".equals(getValue(xr.getCell(cellNum))) && getValue(xr.getCell(cellNum)) != null){
                		map.put(fields[cellNum], getValue(xr.getCell(cellNum)));
                	}else{
                		map.put(fields[cellNum], "");
                		flag++;
                	}
                }
                if(flag >= fields.length){
                	break;
                }
                list.add(map);
            }
		}
		return list;
	}
	/**
	 * 获取每个单元格的内容
	  *<功能简述>
	  *<功能详细描述>
	  * @param hc 单元格
	  * @return [参数说明]
	  * 
	  * @return String [返回类型说明]单元格内容
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	private static String getValue(Cell hssfCell) throws Exception{
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        }else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
	}
	/**
	 * 将传入的字段集合同Excel中的表头相对应
	  *<功能简述>
	  *<功能详细描述>
	  * @param hr 表头
	  * @param fields 传入的字段集合
	  * @return [参数说明]
	  * 
	  * @return String[] [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	  * 黄剑锋
	  * 
	 */
	private static String[] juge(Row hr, String[] fields) throws Exception{
		String cellValue = "";
		String center = "";
		for(int i = 0; i < hr.getLastCellNum(); i++){
			Cell hc = hr.getCell(i);
			cellValue = getValue(hc);
			for(int j = 0; j < fields.length; j++){
				if(fields[j].equals(cellValue)){
					center = fields[i];
					fields[i] = fields[j];
					fields[j] = center;
					break;
				}
			}
		}
		return fields;
	}
	/**
	 * 解析不同版本的excel
	  *<功能简述>
	  *<功能详细描述>
	  * @param inp
	  * @return
	  * @throws IOException
	  * @throws InvalidFormatException [参数说明]
	  * 
	  * @return Workbook [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public static Workbook create(InputStream inp) throws IOException,InvalidFormatException {
	    if (!inp.markSupported()) {
	        inp = new PushbackInputStream(inp, 8);
	    }
	    if (POIFSFileSystem.hasPOIFSHeader(inp)) {
	        return new HSSFWorkbook(inp);
	    }
	    if (POIXMLDocument.hasOOXMLHeader(inp)) {
	        return new XSSFWorkbook(OPCPackage.open(inp));
	    }
	    throw new IllegalArgumentException("你的excel版本目前poi解析不了");
	}
	
	/**
	 * 截取电话号码
	 * @param cons
	 * @return
	 */
	public String getContent(String cons){
		if(-1!=cons.lastIndexOf("E")){
			return cons.substring(0,1)+cons.substring(2,cons.lastIndexOf("E"));
		}else if(-1 != cons.lastIndexOf(".")){
			return cons.substring(0,cons.lastIndexOf("."));
		}else{
			return cons;
		}
	}
}
