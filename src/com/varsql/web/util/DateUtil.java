package com.varsql.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *날짜 관련 util
 * @author ytkim 
*/
public class DateUtil {
	private final static String DEFAULT_YYYYMMDD = "yyyy-MM-dd";
	private final static String DEFAULT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:MM.ss";
	
	private final static String DEFAULT_DELIMETER = "-";
	
	/**
	 * 두날짜의 차이 값 계산 .
	 * @param startDt yyyy-mm-dd
	 * @param endDt yyyy-mm-dd
	 * @return
	 * @throws ParseException
	 */
	public static int GetDifferenceOfDate(String startDt , String endDt) throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_YYYYMMDD);
		java.util.Calendar startCal = java.util.Calendar.getInstance();
		java.util.Calendar endCal = java.util.Calendar.getInstance();

		startCal.setTime(dateFormat.parse(startDt));
		endCal.setTime(dateFormat.parse(endDt));
		
		long startLong = startCal.getTimeInMillis();
		long endLong = endCal.getTimeInMillis();
		long diffTime = endLong -startLong;
		
		
		return (int) (diffTime/60/60/24/1000);
	
	}
	
	public static String  getCurrentDate(){
		return getCurrentDate(DEFAULT_YYYYMMDD);
	}
	
	public static String  getCurrentDate(String date_format){
		SimpleDateFormat sdf =new SimpleDateFormat(date_format, Locale.KOREA);
		return sdf.format(new Date());
	}
	
	/**
	 * 현재 요일 구하기.
	 * @param date_format
	 * @return 
	 */
	public static String  getDate(String date_format){
		SimpleDateFormat sdf =new SimpleDateFormat(date_format, Locale.KOREA);
		return sdf.format(new Date());
	}
	
	/**
	 * 현재 일부터 날짜 구하기 . 
	 * @param chkNum
	 * @return
	 */
	public static String getCurrentDayCalc(int chkNum){
		return getCurrentDayCalc(chkNum,DEFAULT_DELIMETER);
	}
	
	public static String getCurrentDayCalc(int chkNum, String sep){
		String reVal ="";
		GregorianCalendar greCal = new GregorianCalendar();
		int year , month, day ;
		String[] dateArr = null;
		
		dateArr = getDate(DEFAULT_YYYYMMDD).split(sep);
		
		year = new Integer(dateArr[0]);
		month = new Integer(dateArr[1]);
		day = new Integer(dateArr[2]);
		
		
		greCal.set(year, month-1 ,day);
		
		greCal.add(Calendar.DATE, chkNum);
		
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_YYYYMMDD, Locale.KOREA);
		
		reVal = sdf.format(greCal.getTime());
		
		return reVal; 
	}
	
	/**
	 * 지정한 날짜 부터 계산 
	 * @param chkNum
	 * @param chkDt
	 * @return
	 */
	public static String getCurrentDayCalc(int chkNum ,String sep,String chkDt){
		String reVal ="";
		GregorianCalendar greCal = new GregorianCalendar();
		int year , month, day ;
		String[] dateArr = null;
		
		dateArr = chkDt.split(sep);
		
		year = new Integer(dateArr[0]);
		month = new Integer(dateArr[1]);
		day = new Integer(dateArr[2]);
		
		
		greCal.set(year, month-1 ,day);
		
		greCal.add(Calendar.DATE, chkNum);
		
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_YYYYMMDD, Locale.KOREA);
		
		reVal = sdf.format(greCal.getTime());
		
		return reVal; 
	}
	
	/**
	 * 지정한 날짜로 부터 날짜 계산후 dateFormat에 맞춰 날짜 형식 리턴. 
	 * @param chkNum
	 * @param chkDt
	 * @param dateFormat
	 * @return
	 */
	public static String getDayCalc(String chkDt, int chkNum, String dateFormat){
		String reVal ="";
		GregorianCalendar greCal = new GregorianCalendar();
		int year , month, day ;
		String[] dateArr = null;
		
		dateArr = chkDt.split(DEFAULT_DELIMETER);
		
		year = new Integer(dateArr[0]);
		month = new Integer(dateArr[1]);
		day = new Integer(dateArr[2]);
		
		
		greCal.set(year, month-1 ,day);
		
		greCal.add(Calendar.DATE, chkNum);
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.KOREA);
		
		reVal = sdf.format(greCal.getTime());
		
		return reVal; 
	}
}
