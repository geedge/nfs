/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final Logger logger = Logger.getLogger(DateUtils.class);
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	/**
	 * 
	 * 
	 * @Title: getLocalTime
	 * @Description: (各种业务查询的时间条件默认值)
	 * @param @param startTime
	 * @param @param endTime
	 * @param @param 本地存储时间长度(小时)
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public static Map<String, String> getLocalTime(String startTime,String endTime,Long localLen,String type)throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> timeMap=new HashMap<String, String>();
		Date date=new Date();
		//日报表默认查询前一天的数据
		if("daily".equals(type) && startTime == null && endTime == null){
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			timeMap.put("startTime", sdf.format(cal.getTime()));
			timeMap.put("endTime", sdf.format(sdf.parse(sdf2.format(cal.getTime())+" 23:59:59")));
			logger.info("日报默认开始时间条件："+sdf.format(cal.getTime()));
			logger.info("日报默认结束时间条件："+sdf.format(sdf.parse(sdf2.format(cal.getTime())+" 23:59:59")));
			return timeMap;
		}
		//月报表默认查询前一天的数据
		if("month".equals(type) && startTime == null && endTime == null){
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.MONTH, date.getMonth()-2);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			timeMap.put("startTime", sdf.format(cal.getTime()));
			timeMap.put("endTime", sdf.format(date));
			logger.info("月报默认开始时间条件："+sdf.format(cal.getTime()));
			logger.info("月报默认结束时间条件："+sdf.format(date));
			return timeMap;
		}
		
		if(startTime == null && endTime == null && localLen != null){
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.HOUR, -localLen.intValue());
			timeMap.put("startTime", sdf.format(cal.getTime()));
			timeMap.put("endTime", sdf.format(date));
			logger.info("默认开始时间条件："+sdf.format(cal.getTime()));
			logger.info("默认结束时间条件："+sdf.format(date));
			return timeMap;
		}else {
			timeMap.put("startTime", startTime);
			timeMap.put("endTime", endTime);
			logger.info("开始时间条件："+startTime);
			logger.info("结束时间条件："+endTime);
			return timeMap;
		}
	}
	public static Date formateUtcDate(String date) throws ParseException{
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}
	/**
	 * 
	 * isLeapYear(计算是否为闰年)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param years
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean isLeapYear(int years){
		Calendar calendar=Calendar.getInstance();
		calendar.set(years,Calendar.DECEMBER,31);
		if(calendar.get(Calendar.DAY_OF_YEAR)==366){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * setLastDayOfMonth(设置天为月末最后一天)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param cal 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void setLastDayOfMonth(Calendar cal){
		switch(cal.get(Calendar.MONTH)){
		case 0:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 1:{
			if(DateUtils.isLeapYear(cal.get(Calendar.YEAR))){
				cal.set(Calendar.DAY_OF_MONTH, 29);
			}else{
				cal.set(Calendar.DAY_OF_MONTH, 28);
			}
			break;
		}
		case 2:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 3:{
			cal.set(Calendar.DAY_OF_MONTH, 30);
			break;
		}
		case 4:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 5:{
			cal.set(Calendar.DAY_OF_MONTH, 30);
			break;
		}
		case 6:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 7:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 8:{
			cal.set(Calendar.DAY_OF_MONTH, 30);
			break;
		}
		case 9:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
		case 10:{
			cal.set(Calendar.DAY_OF_MONTH, 30);
			break;
		}
		case 11:{
			cal.set(Calendar.DAY_OF_MONTH, 31);
			break;
		}
	}
	}
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		Date date=new Date();
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH, date.getMonth()-2);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//timeMap.put("startTime", sdf.format(cal2.getTime()));
		//timeMap.put("endTime", sdf.format(cal.getTime()));
		logger.info("月报默认开始时间条件："+sdf.format(cal.getTime()));
	}
}
