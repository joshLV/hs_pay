package com.hszsd.common.util.date;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 日期处理公共类<br/>
 * 版权所有：贵州合石电子商务有限公司
 * 
 * @author 艾伍
 * @version 1.0.0
 */
public class DateUtil implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7237663964305871251L;
	/**
	 * 字符串转换为短类型的时间
	 * 
	 * @param date
	 *            字符串时间
	 * @return 返回短类型的日期 不含时分秒
	 */
	public static Date StrigToDateShort(String date) {
		return StrigToDate(date, "yyyy-MM-dd");
	}
	/**
	 * 获取当前格式时间
	 * @return
	 */
	public static Date getNowDate(){
		return new Date();
	}
	/**
	 * 字符串转换为长类型时间
	 * 
	 * @param date
	 *            字符串时间
	 * @return 返回带有时分秒的日期
	 */
	public static Date StrigToDateLong(String date) {
		return StrigToDate(date, null);
	}

	/**
	 * 字符串转换为时间
	 * 
	 * @param date
	 *            字符串日期
	 * @return yyyy-MM-dd HH:mm:ss 格式的日期
	 */
	public static Date StrigToDate(String date) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		if (date == null || "".equals(date)) {
			return new Date();
		}
		Date d = null;
		try {
			d = new java.text.SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return d;
	}

	/**
	 * 字符串转换为时间
	 * 
	 * @param date
	 *            字符串日期
	 * @param pattern
	 *            时间格式
	 * @return 对应格式的日期
	 */
	public static Date StrigToDate(String date, String pattern) {
		if (pattern == null || "".equals(pattern) || "null".equals(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (date == null || "".equals(date) || "null".equals(pattern)) {
			return new Date();
		}
		Date d = null;
		try {
			d = new java.text.SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return d;
	}

	/**
	 * 时间转换为短日期格式
	 * 
	 * @param date
	 *            日期
	 * @return 返回格式 yyyy-MM-dd
	 */
	public static String dateToStringShort(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	/**
	 * 时间转换为长日期格式
	 * 
	 * @param date
	 *            日期
	 * @return 返回格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String dateToStringLong(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @return 日期字符串，Date为null时返回null
	 */
	public static String dateToString(Date date) {
		return dateToString(date, null);
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式,如：yyyy-MM-dd HH:mm:ss、yyyyMMdd等，默认yyyy-MM-dd HH:mm:ss
	 * @return 日期字符串，Date为null时返回null
	 */
	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		try {
			return new java.text.SimpleDateFormat(pattern).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 求两个日期相差的天数
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 两个日期相差天数
	 */
	public static long getDateReduce(Date beginDate, Date endDate) {
		long margin = 0;
		margin = endDate.getTime() - beginDate.getTime();
		margin = margin / (1000 * 60 * 60 * 24);
		return margin;
	}

	public static Date getDatePlus(Date beginDate, int year, int month,
			int day, int hour, int minute, int second) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(beginDate);
		rightNow.add(Calendar.YEAR, year);
		rightNow.add(Calendar.MONTH, month);
		rightNow.add(Calendar.DAY_OF_YEAR, day);
		long dateLong = rightNow.getTimeInMillis();
		dateLong = dateLong + (hour * 60 * 60 * 1000) + (minute * 60 * 1000)
				+ (second * 1000);
		return rightNow.getTime();
	}

	/**
	 * 判断是否为月末
	 * 
	 * @param date
	 *            日期
	 * @return true表示月末
	 */
	public static boolean isMonthEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DATE) == cal
				.getActualMaximum(Calendar.DAY_OF_MONTH))
			return true;
		else
			return false;
	}

	/***
	 * 判断日期是否为当前月
	 * 
	 * @param date
	 *            传入日期
	 * @return true表示是
	 */
	public static boolean IsNowPreDate(Date date) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String stMonth = sdf.format(now);
		// 当前月
		String month = sdf.format(date);
		if (month.equals(stMonth)) {
			return true;
		} else {
			// 非当前月
			return false;
		}
	}

	/***
	 * 判断日期是否为当前月
	 * 
	 * @param date
	 *            传入日期
	 * @return true表示是
	 */
	public static boolean IsNowPreDateString(String date) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String stMonth = sdf.format(now);
		// 当前月
		if (date.equals(stMonth)) {
			return true;
		} else {
			// 非当前月
			return false;
		}
	}

	/**
	 * 获取一个月最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return 返回对应日期
	 */
	public static Date getLastDateOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		int date = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(year, month - 1, date, 23, 59, 59);
		return c.getTime();
	}

	/**
	 * 获取一个月第一天日期
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return 返回对应日期
	 */
	public static Date getFirstDateOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		int date = c.getActualMinimum(Calendar.DAY_OF_MONTH);
		c.set(year, month - 1, date, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 查询指定日期所在周的周次
	 * 
	 * @param date
	 *            日期
	 * @return 返回周次
	 */
	public static int getWeekOfDate(Date date) {
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		return time.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取一年周次数
	 * 
	 * @param year
	 *            年份
	 * @return 周数
	 */
	public static int getCountWeekNumOfYear(int year) {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		int weekAmountOfYear = time.getActualMaximum(Calendar.WEEK_OF_YEAR);
		return weekAmountOfYear;
	}

	/**
	 * 获取周次的最后一天
	 * 
	 * @param year
	 *            年份
	 * @param weekNum
	 *            周次
	 * @return 返回周次最后一天
	 */
	public static Date getLastDayOfWeek(int year, int weekNum) {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.WEEK_OF_YEAR, weekNum);
		time.setFirstDayOfWeek(Calendar.MONDAY);
		time.set(Calendar.DAY_OF_WEEK, time.getFirstDayOfWeek() + 6);
		return time.getTime();
	}

	/**
	 * 获取周次的最后一天
	 * 
	 * @param year
	 *            年份
	 * @param weekNum
	 *            周次
	 * @param pattern
	 *            返回的时间格式
	 * @return 返回周次的最后一天
	 */
	public static String getLastDayOfWeek(int year, int weekNum, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.WEEK_OF_YEAR, weekNum);
		time.setFirstDayOfWeek(Calendar.MONDAY);
		time.set(Calendar.DAY_OF_WEEK, time.getFirstDayOfWeek() + 6);
		return df.format(time.getTime());
	}

	/**
	 * 获取周次的第一天
	 * 
	 * @param year
	 *            年份
	 * @param weekNum
	 *            周次
	 * @return 返回周次的第一天
	 */
	public static Date getFirstDayOfWeek(int year, int weekNum) {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.WEEK_OF_YEAR, weekNum);
		time.set(Calendar.DAY_OF_WEEK, time.getFirstDayOfWeek() + 1);
		return time.getTime();
	}

	/**
	 * 获取周次的第一天
	 * 
	 * @param year
	 *            年份
	 * @param weekNum
	 *            周次
	 * @param pattern
	 *            返回的时间格式
	 * @return 返回周次的第一天
	 */
	public static String getFirstDayOfWeek(int year, int weekNum, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.WEEK_OF_YEAR, weekNum);
		time.set(Calendar.DAY_OF_WEEK, time.getFirstDayOfWeek() + 1);
		return df.format(time.getTime());
	}

	/**
	 * 获取指定日期所在的周开始和结束日期
	 * 
	 * @param date
	 *            查询的日期
	 * @return d[0]表示开始日期，d[1]表示结束日期
	 */
	public static Date[] getWeekDate(Date date) {
		Date[] d = new Date[2];
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		int year = time.getWeekYear();
		int weekNum = getWeekOfDate(date);
		// 第一天
		d[0] = getFirstDayOfWeek(year, weekNum);
		// 最后一天
		d[0] = getLastDayOfWeek(year, weekNum);
		return d;
	}

	/**
	 * 获取指定日期所在的周开始和结束日期
	 * 
	 * @param date
	 *            查询的日期
	 * @param pattern
	 *            返回的日期格式
	 * @return str[0]表示开始日期，str[1]表示结束日期
	 */
	public static String[] getWeekDate(Date date, String pattern) {
		String[] str = new String[2];
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		int year = time.getWeekYear();
		int weekNum = getWeekOfDate(date);
		// 第一天
		str[0] = getFirstDayOfWeek(year, weekNum, pattern);
		// 最后一天
		str[1] = getLastDayOfWeek(year, weekNum, pattern);
		return str;
	}

	/**
	 * 获取指定周次中的开始日期和结束日期
	 * 
	 * @param year
	 *            年份
	 * @param weekNum
	 *            周次
	 * @return d[0]表示开始日期，d[1]表示结束日期
	 */
	public static Date[] getWeekDateOfNum(int year, int weekNum) {
		Date[] d = new Date[2];
		// 第一天
		d[0] = getFirstDayOfWeek(year, weekNum);
		// 最后一天
		d[1] = getLastDayOfWeek(year, weekNum);
		return d;
	}

	/**
	 * 获取指定周次中的开始日期和结束日期
	 * 
	 * @param year
	 *            年份
	 * @param weekNum
	 *            周次
	 * @param pattern
	 *            返回的时间格式
	 * @return str[0]表示开始日期，str[1]表示结束日期
	 */
	public static String[] getWeekDateOfNum(int year, int weekNum,
			String pattern) {
		String[] str = new String[2];
		// 第一天
		str[0] = getFirstDayOfWeek(year, weekNum, pattern);
		// 最后一天
		str[1] = getLastDayOfWeek(year, weekNum, pattern);
		return str;
	}

	/**
	 * 获取一年内的所有周次列表
	 * 
	 * @param year
	 *            年份
	 * @return list索引为周次减1，值为对应周次范围，str[0]为开始日期，str[1]为结束日期
	 */
	public static List<Date[]> getWeekAllYear(int year) {
		List<Date[]> weekDate = new ArrayList<Date[]>();
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		int weekAmountOfYear = time.getActualMaximum(Calendar.WEEK_OF_YEAR);
		for (int i = 1; i <= weekAmountOfYear; i++) {
			Date[] d = new Date[2];
			time.set(Calendar.WEEK_OF_YEAR, i);
			time.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			d[0] = time.getTime();
			time.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			d[1] = time.getTime();
			weekDate.add(i - 1, d);
		}
		return weekDate;
	}

	/**
	 * 获取一年内的所有周次列表
	 * 
	 * @param year
	 *            年份
	 * @param pattern
	 *            返回的字符串格式
	 * @return list索引为周次减1，值为对应周次范围，str[0]为开始日期，str[1]为结束日期
	 */
	public static List<String[]> getWeekAllYear(int year, String pattern) {
		List<String[]> weekString = new ArrayList<String[]>();
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, year);
		int weekAmountOfYear = time.getActualMaximum(Calendar.WEEK_OF_YEAR);
		for (int i = 1; i <= weekAmountOfYear; i++) {
			String[] str = new String[2];
			time.set(Calendar.WEEK_OF_YEAR, i);
			time.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			str[0] = df.format(time.getTime());
			time.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			str[1] = df.format(time.getTime());
			weekString.add(i - 1, str);
		}
		return weekString;
	}
	/**
	 * 获取当前时间的long
	 * @return
	 */
	public static long getDateLong(){
		return getDateLong(null);
	}
	/**
	 * 将时间转换为long
	 * @param date
	 * @return
	 */
	public static long getDateLong(Date date){
		if(null!=date){
			return date.getTime();
		}else{
			return new Date().getTime();
		}
	}
}
