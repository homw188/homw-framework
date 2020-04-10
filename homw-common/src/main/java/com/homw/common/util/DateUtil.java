package com.homw.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @description 日期处理
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public class DateUtil {
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_PATTERN);

	public static String formatDate(Date date) {
		Validate.notNull(date, "date param must not be null");
		return dateFormat.format(date);
	}
	
	public static String formatDateTime(Date date) {
		Validate.notNull(date, "date param must not be null");
		return dateTimeFormat.format(date);
	}
	
	public static String formatDateTime(Long timeMillis) {
		Validate.notNull(timeMillis, "timeMillis param must not be null");
		return formatDateTime(new Date(timeMillis));
	}
	
	public static Date parseDate(String date) {
		Validate.notBlank(date, "date param must not be blank");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parseDateTime(String date) {
		Validate.notBlank(date, "date param must not be blank");
		try {
			return dateTimeFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parse(String date, String pattern) {
		Validate.notNull(date, "date param must not be null");
		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_TIME_PATTERN;
		}
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Long parseTimeMillis(String date, String pattern) {
		Date d = parse(date, pattern);
		return d == null ? null : d.getTime();
	}

	public static String format(Date date, String pattern) {
		Validate.notNull(date, "date param must not be null");
		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_TIME_PATTERN;
		}
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static String format(Long timeMillis, String pattern) {
		Validate.notNull(timeMillis, "date param must not be null");
		return format(new Date(timeMillis), pattern);
	}

	public static Date currentDate() {
		return new Date();
	}
	
	public static Calendar calendar(Long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal;
	}
	
	public static long addYear(Long startTime, int num) {
		Calendar cal = calendar(startTime);
		cal.add(Calendar.YEAR, num);
		return cal.getTimeInMillis();
	}

	public static long addMonth(Long startTime, int num) {
		Calendar cal = calendar(startTime);
		cal.add(Calendar.MONTH, num);
		return cal.getTimeInMillis();
	}

	public static long addWeek(Long startTime, int num) {
		Calendar cal = calendar(startTime);
		cal.add(Calendar.WEEK_OF_YEAR, num);
		return cal.getTimeInMillis();
	}

	public static long addDay(Long startTime, int num) {
		Calendar cal = calendar(startTime);
		cal.add(Calendar.DAY_OF_MONTH, num);
		return cal.getTimeInMillis();
	}

	public static long addHour(Long startTime, int num) {
		Calendar cal = calendar(startTime);
		cal.add(Calendar.HOUR_OF_DAY, num);
		return cal.getTimeInMillis();
	}

	public static long addMinute(Long startTime, int num) {
		Calendar cal = calendar(startTime);
		cal.add(Calendar.MINUTE, num);
		return cal.getTimeInMillis();
	}
}