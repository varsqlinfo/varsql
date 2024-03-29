package com.varsql.core.common.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;

import com.varsql.core.common.constants.VarsqlConstants;

public final class VarsqlDateUtils {

	private VarsqlDateUtils() {
	};

	public enum DateCheckType {
		YEAR, MONTH, DAY, HOUR, MINUTES
	}

	public static String currentYyyy() {
		return new DateTime(System.currentTimeMillis()).toString(VarsqlConstants.yearFormatter);
	}
	
	public static String currentYyyyMM() {
		return new DateTime(System.currentTimeMillis()).toString(VarsqlConstants.monthFormatter);
	}
	
	public static String currentYyyyMMdd() {
		return new DateTime(System.currentTimeMillis()).toString(VarsqlConstants.dateFormatter);
	}
	
	public static String format(String format) {
		return format(format, new Date());
	}
	public static String format(String format, Date date) {
		return new DateTime(date).toString(DateTimeFormat.forPattern(format));
	}
	
	public static String format(String format, long mili) {
		return new DateTime(mili).toString(DateTimeFormat.forPattern(format));
	}

	public static String dateFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(VarsqlConstants.dateFormatter);
	}

	public static String dateFormat(Date date) {
		return new DateTime(date).toString(VarsqlConstants.dateFormatter);
	}

	public static String timeFormat(Time time) {
		return new DateTime(time).toString(VarsqlConstants.timeFormatter);
	}

	public static String timeFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(VarsqlConstants.TIME_MILLISECOND_FORMAT);
	}
	
	public static String timeMilliFormat(Time time) {
		return new DateTime(time).toString(VarsqlConstants.TIME_MILLISECOND_FORMAT);
	}
	
	public static String timeMilliFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(VarsqlConstants.timeFormatter);
	}

	public static String timestampFormat(Timestamp timestamp) {
		return new DateTime(timestamp).toString(VarsqlConstants.timestampFormatter);
	}

	public static String timestampFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(VarsqlConstants.timestampFormatter);
	}
	
	public static String timestampMilliFormat(Timestamp timestamp) {
		return new DateTime(timestamp).toString(VarsqlConstants.timestampMilliFormatter);
	}
	
	public static String timestampMilliFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(VarsqlConstants.timestampMilliFormatter);
	}
	
	public static String currentDateFormat() {
		return new DateTime().toString(VarsqlConstants.dateFormatter);
	}

	public static String currentDateTimeFormat() {
		return new DateTime().toString(VarsqlConstants.timestampFormatter);
	}
	
	/**
	 * 
	 * @method : stringToTime
	 * @desc : string - time
	 * @author : ytkim
	 * @param date
	 * @return
	 */
	public static Time stringToTime(String date) {
		return new Time(DateTime.parse(date).getMillis());
	}

	public static Date stringToTime(String date, String format) {
		return new Time(DateTime.parse(date, DateTimeFormat.forPattern(format)).getMillis());
	}

	/**
	 * 
	 * @method : stringToDate
	 * @desc : string - date
	 * @author : ytkim
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date) {
		return DateTime.parse(date).toDate();
	}

	public static Date stringToDate(String date, String format) {
		return DateTime.parse(date, DateTimeFormat.forPattern(format)).toDate();
	}

	/**
	 * 
	 * @method : stringToTimestamp
	 * @desc : string - timestamp
	 * @author : ytkim
	 * @param date
	 * @return
	 */
	public static Timestamp stringToTimestamp(String date) {
		return new Timestamp(DateTime.parse(date).getMillis());
	}

	public static Timestamp stringToTimestamp(String date, String format) {
		return new Timestamp(DateTime.parse(date, DateTimeFormat.forPattern(format)).getMillis());
	}
	
	public static Timestamp millisecondStringToTimestamp(String date) {
		return new Timestamp(DateTime.parse(date).getMillis());
	}
	
	public static Timestamp millisecondStringToTimestamp(String date, String format) {
		return new Timestamp(DateTime.parse(date, DateTimeFormat.forPattern(format)).getMillis());
	}

	/**
	 * 
	 * @method : dateDiff
	 * @desc :날짜 비교
	 * @author : ytkim
	 * @param startDt
	 * @param endDt
	 * @param checkType
	 * @return
	 */
	public static int dateDiff(Date startDt, Date endDt, DateCheckType checkType) {

		if (checkType == null) {
			return -1;
		}
		if (DateCheckType.YEAR.equals(checkType)) {
			return new Period(new DateTime(startDt), new DateTime(endDt), PeriodType.years()).getYears();
		} else if (DateCheckType.MONTH.equals(checkType)) {
			return new Period(new DateTime(startDt), new DateTime(endDt), PeriodType.months()).getMonths();
		} else if (DateCheckType.HOUR.equals(checkType)) {
			return new Period(new DateTime(startDt), new DateTime(endDt), PeriodType.hours()).getHours();
		} else if (DateCheckType.MINUTES.equals(checkType)) {
			return new Period(new DateTime(startDt), new DateTime(endDt), PeriodType.minutes()).getMinutes();
		} else {
			return new Period(new DateTime(startDt), new DateTime(endDt), PeriodType.days()).getDays();
		}
	}

	/**
	 * 
	 * @method : calcDate
	 * @desc : 현재일 기준 날짜 계산
	 * @author : ytkim
	 * @param num
	 * @param checkType
	 * @return
	 */
	public static Date calcDate(int num, DateCheckType checkType) {

		if (checkType == null)
			return null;
		
		boolean addFlag = num < 0; 
		num = Math.abs(num);

		DateTime datetime = new DateTime();
		if (DateCheckType.YEAR.equals(checkType)) {
			return (addFlag ? datetime.minusYears(num).toDate() : datetime.plusYears(num).toDate());
		} else if (DateCheckType.MONTH.equals(checkType)) {
			return (addFlag ? datetime.minusMonths(num).toDate() : datetime.plusMonths(num).toDate());
		} else if (DateCheckType.HOUR.equals(checkType)) {
			return (addFlag ? datetime.minusHours(num).toDate() : datetime.plusHours(num).toDate());
		} else if (DateCheckType.MINUTES.equals(checkType)) {
			return (addFlag ? datetime.minusMinutes(num).toDate() : datetime.plusMinutes(num).toDate());
		} else {
			return (addFlag ? datetime.minusDays(num).toDate() : datetime.plusDays(num).toDate());
		}
	}
	
	/**
	 * 
	 * @method : calcDateFormat
	 * @desc : date 연산 후 format으로 리턴.
	 * @author : ytkim
	 * @param date
	 * @param num
	 * @param checkType
	 * @return
	 */
	public static String calcDateFormat(int num, DateCheckType checkType, String format) {
		return format(format, calcDate(num, checkType));
	}
	
	/**
	 * 
	 * @method : calcDate
	 * @desc : date 연산
	 * @author : ytkim
	 * @param date
	 * @param num
	 * @param checkType
	 * @return
	 */
	public static Date calcDate(Date date, int num, DateCheckType checkType) {
		if (checkType == null)
			return null;
		
		boolean addFlag = num < 0; 
		num = Math.abs(num);
		
		DateTime datetime = new DateTime(date);
		if (DateCheckType.YEAR.equals(checkType)) {
			return (addFlag ? datetime.minusYears(num).toDate() : datetime.plusYears(num).toDate());
		} else if (DateCheckType.MONTH.equals(checkType)) {
			return (addFlag ? datetime.minusMonths(num).toDate() : datetime.plusMonths(num).toDate());
		} else if (DateCheckType.HOUR.equals(checkType)) {
			return (addFlag ? datetime.minusHours(num).toDate() : datetime.plusHours(num).toDate());
		} else if (DateCheckType.MINUTES.equals(checkType)) {
			return (addFlag ? datetime.minusMinutes(num).toDate() : datetime.plusMinutes(num).toDate());
		} else {
			return (addFlag ? datetime.minusDays(num).toDate() : datetime.plusDays(num).toDate());
		}
	}
	
	/**
	 * 
	 * @method : calcDateFormat
	 * @desc : date 연산 후 format으로 리턴.
	 * @author : ytkim
	 * @param date
	 * @param num
	 * @param checkType
	 * @param format
	 * @return
	 */
	public static String calcDateFormat(Date date, int num, DateCheckType checkType, String format) {
		return format(format, calcDate(date, num, checkType));
	}
	
	public static String calcDuration(long elapsedTime) {
		long millis = elapsedTime;
	    long dd = TimeUnit.MILLISECONDS.toDays(millis);
	    millis -= TimeUnit.DAYS.toMillis(dd);
	    long hh = TimeUnit.MILLISECONDS.toHours(millis);
	    millis -= TimeUnit.HOURS.toMillis(hh);
	    long mm = TimeUnit.MILLISECONDS.toMinutes(millis);
	    millis -= TimeUnit.MINUTES.toMillis(mm);
	    long ss = TimeUnit.MILLISECONDS.toSeconds(millis);
	    millis -= TimeUnit.SECONDS.toMillis(ss);
		
		return String.format("%d days %02d:%02d:%02d.%03d", dd, hh, mm, ss, millis);
	}
	

}
