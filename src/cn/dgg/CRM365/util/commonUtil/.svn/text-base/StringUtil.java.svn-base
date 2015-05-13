package cn.dgg.CRM365.util.commonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 工具类 <功能简述> <功能详细描述>
 * 
 * @author 王科（小）
 * @version [版本号, Jun 29, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StringUtil {

	/**
	 * 以yyyy-MM-dd为格式
	 */
	public static final String YMD = "yyyy-MM-dd";

	/**
	 * 以yyyy-MM-dd hh:mm为格式
	 */
	public static final String YMDHM = "yyyy-MM-dd hh:mm";

	/**
	 * 以yyyy-MM-dd hh:mm:ss为格式
	 */
	public static final String YMDHMS = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 是否为空
	 */
	public static boolean isBlank(String str) {
		boolean result = true;

		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) != ' ') {
					result = false;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 获取DATE时间
	 * 
	 * @return
	 */
	public static Date getDate() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	/**
	 * 
	 * <功能简述> 获取当月有多少天 <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return int [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */

	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 
	 * <功能简述> 得到指定月的天数 <功能详细描述>
	 * 
	 * @param year
	 *            指定年份
	 * @param month
	 *            指定月份
	 * @return [参数说明]
	 * 
	 * @return int [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static int getDayOfMonth(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.clear();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		// a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		// a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.getActualMaximum(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 
	 * <功能简述> 获取系统当前时间 <功能详细描述>
	 * 
	 * @param dateFormat
	 *            时间格式
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getSystemDate(String dateFormat) {
		Calendar c = Calendar.getInstance(Locale.CHINESE);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * <功能简述> 把系统取出的时间转换成yyyy-MM-dd HH:mm时间格式 <功能详细描述>
	 * 
	 * @param datetime
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String convertDate(String datetime) {
		try {
			if (datetime != null && datetime.length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(datetime);
				return sdf.format(date);
			} else {
				return "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * <功能简述> 把系统取出的时间转换成指定的时间格式 <功能详细描述>
	 * 
	 * @param datetime
	 *            要转换的时间
	 * @param pattern
	 *            转换的时间格式
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String convertDate(String datetime, String pattern) {
		try {
			if (datetime != null && datetime.length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date date = sdf.parse(datetime);
				return sdf.format(date);
			} else {
				return "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumberString(String str) {
		boolean result = true;

		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i))) {
					result = false;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 比较时间大小 返回 0 时间相等 1 开始时间小于结束时间 2 开始时间大于结束时间 3 输入时间为空
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static int timeDifference(String beginTime, String endTime) {

		if ((!"".equals(beginTime) || beginTime != null)
				&& (!"".equals(endTime) || endTime != null)) {
			java.text.DateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			java.util.Calendar c1 = java.util.Calendar.getInstance();
			java.util.Calendar c2 = java.util.Calendar.getInstance();
			try {
				c1.setTime(df.parse(beginTime));
				c2.setTime(df.parse(endTime));
			} catch (java.text.ParseException e) {
				System.err.println("格式不正确");
			}
			int result = c1.compareTo(c2);

			return result;
		} else {
			return 3;
		}
	}

	/**
	 * 通过当前时间与规定办理天数，算出结束时间
	 * 
	 * @param to
	 * @return
	 */
	@SuppressWarnings("all")
	public static String dateAdd(String to, Calendar strDate) {

		// 日期处理模块 (将日期加上某些天或减去天数)返回字符串
		int strTo;
		try {
			strTo = Integer.parseInt(to);
		} catch (Exception e) {
			System.out.println("日期标识转换出错!   :   \n::: " + to + "不能转为数字型 ");
			e.printStackTrace();
			strTo = 0;
		}
		strDate.add(strDate.DATE, strTo); // 日期减 如果不够减会将月变动
		// 生成 (年-月-日)字符串
		String meStrDate = strDate.get(strDate.YEAR) + "-"
				+ String.valueOf(strDate.get(strDate.MONTH) + 1) + "-"
				+ strDate.get(strDate.DATE) + " " + strDate.get(strDate.HOUR)
				+ ":" + strDate.get(strDate.MINUTE) + ":"
				+ strDate.get(strDate.SECOND);

		return meStrDate;
	}

	/**
	 * 转换时间
	 * 
	 * @param strDate
	 * @return
	 */
	@SuppressWarnings("all")
	public static String toDate(Calendar strDate) {
		String beginTime = strDate.get(strDate.YEAR) + "-"
				+ String.valueOf(strDate.get(strDate.MONTH) + 1) + "-"
				+ strDate.get(strDate.DATE) + " " + strDate.get(strDate.HOUR)
				+ ":" + strDate.get(strDate.MINUTE) + ":"
				+ strDate.get(strDate.SECOND);
		return beginTime;
	}

	/**
	 * Arraylist去重复数据
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("all")
	public static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		return newList;
	}

	public static void main(String[] args) {
		System.out.println(getSystemDate("yyyyMMddHHssmm"));
	}

	/**
	 * 
	 * <功能简述> 取小数点后两位，并四舍五入 <功能详细描述>
	 * 
	 * @param value
	 * @return [参数说明]
	 * 
	 * @return double [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static double convert(double value) {
		long lg = Math.round(value * 100); // 四舍五入
		double d = lg / 100.0; // 注意：使用 100.0而不是 100
		return d;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

	/**
	 * 
	 * <功能简述> 获取指定时间所属周的星期几的时间 <功能详细描述>
	 * 
	 * @param date
	 *            指定时间 yyyy-MM-dd
	 * @param dayOfWeek
	 *            一周中的第几天
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDayOfWeek(String date, int dayOfWeek) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		Calendar cal = null;
		try {
			d = format.parse(date);
			cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			System.out.println("currentMonday:" + format.format(cal.getTime()));
		} catch (ParseException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format.format(cal.getTime());
	}

	/**
	 * 
	 * <功能简述> 获取指定时间所属月的几号 <功能详细描述>
	 * 
	 * @param date
	 * @param dayOfMonth
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDayOfMonth(String date, int dayOfMonth) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		Calendar cal = null;
		try {
			d = format.parse(date);
			cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			System.out.println("currentMonday:" + format.format(cal.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return format.format(cal.getTime());
	}
	
	/**
	 * 得到上个月的今天
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * @author chenqin
	  * 
	  * @return String [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public static String getNowOfLastMonth(){
		SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar aGregorianCalendar = new GregorianCalendar();
        aGregorianCalendar.set(Calendar.MONTH, aGregorianCalendar
                .get(Calendar.MONTH) - 1);
        String nowOfLastMonth = aSimpleDateFormat.format(aGregorianCalendar.getTime());
        return nowOfLastMonth;
	}
	/**
	 * 得到本周时间
	  *<功能简述>
	  *<功能详细描述>
	  * @param format
	  * @return [参数说明]
	  * 
	  * @return String [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	private static String getthisWeek() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        String start = df.format(cal.getTime());
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        String end = df.format(cal.getTime());
        return start+end;
	}
	
}
