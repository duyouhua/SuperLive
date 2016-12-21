package com.joytouch.superlive.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 根据时间戳格式化成类型时间
 */
public class TimeUtil {
	public TimeUtil(Context paramContext) {
	}

	/**
	 * 获取当前0点时间戳
	 * @return
	 */
	public static long getTodayZero() {
		Date date = new Date();
		long l = 24*60*60*1000; //每天的毫秒数
		//date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
		//减8个小时的毫秒值是为了解决时区的问题。
		return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 * 1000);
	}
	@SuppressLint("SimpleDateFormat")
	public static String currentLocalTimeString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}

	@SuppressLint("SimpleDateFormat")
	public static String dateToMessageTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(new Date(time));
	}

	@SuppressLint("SimpleDateFormat")
	public static String toLocalTimeString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return sdf.format(System.currentTimeMillis());
	}
	@SuppressLint("SimpleDateFormat")
	public static String currentLocalDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(System.currentTimeMillis());
	}

	@SuppressLint("SimpleDateFormat")
	public static String currentLocalDateStrings() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(System.currentTimeMillis());
	}
	public String getCurrenMinute() {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("mm", localCalendar).toString();
	}
	@SuppressLint("SimpleDateFormat")
	public static String currentSplitTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(System.currentTimeMillis());
	}
	@SuppressLint("SimpleDateFormat")
	public static String currentSplitTimeString(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datetime;
		try {
			datetime = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			datetime = new Date();
		}
		sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(datetime);
	}
	@SuppressLint("SimpleDateFormat")
	public static String currentSplitTimeStringHm(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datetime;
		try {
			datetime = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			datetime = new Date();
		}
		sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(datetime);
	}
	public static long getLongTime(String time, String type) {
		SimpleDateFormat sdf;
		if(TextUtils.isEmpty(type)) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else{
			sdf = new SimpleDateFormat(type);
		}
		Date datetime;

		try {
			datetime = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			datetime = new Date();
		}
		;
		return datetime.getTime();
	}
	public static String getCurrentDay() {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("dd", localCalendar).toString();
	}

	public static String getCurrentHour() {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("kk", localCalendar).toString();
	}

	public static String getCurrentMonth() {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("MM", localCalendar).toString();
	}

	public static String getCurrentSecond() {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("ss", localCalendar).toString();
	}

	public static String getCurrentYear() {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("yyyy", localCalendar).toString();
	}

	public static String getDay(Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("dd", localCalendar).toString();
	}

	public static String getHour(Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("kk", localCalendar).toString();
	}

	public static String getMinute(Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("mm", localCalendar).toString();
	}

	public static String getMonth(Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("MM", localCalendar).toString();
	}

	public static String getSecond(Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("ss", localCalendar).toString();
	}
	public static String getTimeString(Long paramLong,String type) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format(type, localCalendar).toString();
	}

	public String getTime(String paramString) {
		Calendar localCalendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format(paramString, localCalendar).toString();
	}

	public String getTime(String paramString, Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format(paramString, localCalendar).toString();
	}

	public String getYear(Long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		long l = paramLong.longValue();
		localCalendar.setTimeInMillis(l);
		return DateFormat.format("yyyy", localCalendar).toString();
	}
	@SuppressLint("SimpleDateFormat")
	public static int compareTime(String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			long start = sdf.parse(startTime).getTime();
			long end = sdf.parse(endTime).getTime();
			long now = System.currentTimeMillis();
			if (start <= now && now < end) {
				return 0;
			} else if (start > now) {
				return -1;
			} else if (now >= end) {
				return 1;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -2;
	}

	public static boolean needShowTime(long time1,long time2){
		int time=(int) (((time1-time2))/60)/1000;
		if(time>=5){
			return true;
		}else{
			return false;
		}
	}
	
	public static String getVoiceTime(int time) {
		int second = time / 1000;
		if (second <= 0) {
			second = 1;
		}
		return String.valueOf(second) + "\"";
	}

	/**
	 * 指定日期的时间戳，java获取指定日期的时间戳
	 * @param s
	 * @return
	 */
	public static Long getLongTime(String s) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse("s");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long timestamp = cal.getTimeInMillis();
		return timestamp;
	}

	/**
	 * 指定日期的时间戳，java获取指定日期的时间戳
	 * 在使用SimpleDateFormat时格式化时间的 yyyy.MM.dd 为年月日而如果希望格式化时间为12小时制的，
	 * 则使用hh:mm:ss 如果希望格式化时间为24小时制的，则使用HH:mm:ss
	 * @return
	 */
	public static Long getDataLong(String dateString){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		String dateString = "2014/10/11 14:50:11";
		Date date = null;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long s=date.getTime();
		return s/1000;
	}
}

/*
 * Location: C:\Documents and Settings\Administrator\桌面\classes_dex2jar.jar
 * Qualified Name: org.dns.framework.util.TimeUtil JD-Core Version: 0.6.0
 */