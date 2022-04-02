package com.varsql.core.common.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class VarsqlDateUtils {

	private VarsqlDateUtils() {
	};

	// 날짜 포켓.
	final static String DATE_FORMAT = "yyyy-MM-dd";

	final static String TIME_FORMAT = "HH:mm:ss.SSS";

	final static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	final static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
	final static DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(TIME_FORMAT);
	final static DateTimeFormatter timestampFormatter = DateTimeFormat.forPattern(TIMESTAMP_FORMAT);

	public enum DateCheckType {
		YEAR, MONTH, DAY, HOUR, MINUTES
	}

	public static void main(String[] args) {

		// DateTimeFormatter dateTimeFormatter =
		// DateTimeFormatter.ofPattern(DATE_FORMAT);
		new DateTime();
		// date foramt 변경 할것.

//		System.out.println(dateFormat(System.currentTimeMillis()));
//		System.out.println(timeFormat(System.currentTimeMillis()));
//		System.out.println(timestampFormat(System.currentTimeMillis()));
//		System.out.println(dateFormat(stringToDate("2022-03-09")));

		DateTime today = new DateTime();
		DateTime yesterday = today.minusDays(165);

		System.out.println(dateDiff(today.toDate(), yesterday.toDate(), DateCheckType.YEAR));
		System.out.println(dateDiff(today.toDate(), yesterday.toDate(), DateCheckType.MONTH));
		System.out.println(dateDiff(today.toDate(), yesterday.toDate(), DateCheckType.DAY));
		System.out.println(dateDiff(today.toDate(), yesterday.toDate(), DateCheckType.HOUR));
		System.out.println(dateDiff(today.toDate(), yesterday.toDate(), DateCheckType.MINUTES));
		System.out.println(dateFormat(today.toDate()) + " :: " + dateFormat(yesterday.toDate()));
	}

	public static String format(Date date, String foramt) {
		return new DateTime(date).toString(DateTimeFormat.forPattern(foramt));
	}

	public static String dateFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(dateFormatter);
	}

	public static String dateFormat(Date date) {
		return new DateTime(date).toString(dateFormatter);
	}

	public static String timeFormat(Time time) {
		return new DateTime(time).toString(timeFormatter);
	}

	public static String timeFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(timeFormatter);
	}

	public static String timestampFormat(Timestamp timestamp) {
		return new DateTime(timestamp).toString(timestampFormatter);
	}

	public static String timestampFormat(long timeMilli) {
		return new DateTime(timeMilli).toString(timestampFormatter);
	}

	/**
	 * 
	 * @method : stringToDate
	 * @desc : string -> date
	 * @author : ytkim
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date) {
		return DateTime.parse(date, dateFormatter).toDate();
	}

	public static Date stringToDate(String date, String format) {
		return DateTime.parse(date, DateTimeFormat.forPattern(format)).toDate();
	}

	/**
	 * 
	 * @method : stringToTimestamp
	 * @desc : string -> timestamp
	 * @author : ytkim
	 * @param date
	 * @return
	 */
	public static Timestamp stringToTimestamp(String date) {
		return new Timestamp(DateTime.parse(date, timestampFormatter).getMillis());
	}

	public static Timestamp stringToTimestamp(String date, String format) {
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

	public static String currentDate() {
		return new DateTime().toString(dateFormatter);
	}

	public static String currentDate(String format) {
		return new DateTime().toString(DateTimeFormat.forPattern(format));
	}

	public static String currentDateTime() {
		return new DateTime().toString(timestampFormatter);
	}

	public static Date calcDate(int num, DateCheckType checkType) {

		if (checkType == null)
			return null;

		DateTime datetime = new DateTime();
		if (DateCheckType.YEAR.equals(checkType)) {
			return (num < 0 ? datetime.minusYears(num).toDate() : datetime.plusYears(num).toDate());
		} else if (DateCheckType.MONTH.equals(checkType)) {
			return (num < 0 ? datetime.minusMonths(num).toDate() : datetime.plusMonths(num).toDate());
		} else if (DateCheckType.HOUR.equals(checkType)) {
			return (num < 0 ? datetime.minusHours(num).toDate() : datetime.plusHours(num).toDate());
		} else if (DateCheckType.MINUTES.equals(checkType)) {
			return (num < 0 ? datetime.minusMinutes(num).toDate() : datetime.plusMinutes(num).toDate());
		} else {
			return (num < 0 ? datetime.minusDays(num).toDate() : datetime.plusDays(num).toDate());
		}
	}

	public static Date calcDate(Date date, int num, DateCheckType checkType) {
		if (checkType == null)
			return null;

		DateTime datetime = new DateTime(date);
		if (DateCheckType.YEAR.equals(checkType)) {
			return (num < 0 ? datetime.minusYears(num).toDate() : datetime.plusYears(num).toDate());
		} else if (DateCheckType.MONTH.equals(checkType)) {
			return (num < 0 ? datetime.minusMonths(num).toDate() : datetime.plusMonths(num).toDate());
		} else if (DateCheckType.HOUR.equals(checkType)) {
			return (num < 0 ? datetime.minusHours(num).toDate() : datetime.plusHours(num).toDate());
		} else if (DateCheckType.MINUTES.equals(checkType)) {
			return (num < 0 ? datetime.minusMinutes(num).toDate() : datetime.plusMinutes(num).toDate());
		} else {
			return (num < 0 ? datetime.minusDays(num).toDate() : datetime.plusDays(num).toDate());
		}
	}

}
