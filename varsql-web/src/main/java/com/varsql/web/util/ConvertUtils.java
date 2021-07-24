package com.varsql.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.varsql.core.common.constants.VarsqlConstants;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ConvertUtils.java
* @desc		: 값 변경 utils
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class ConvertUtils {

	private ConvertUtils() {}

	public static long longValueOf(Long val) {
		return val != null ? val.intValue() : Long.valueOf(0L);
	}

	public static long longValueOf(Integer val) {
		return val != null ? val.longValue() : Long.valueOf(0L);
	}

	public static long longValueOf(String val) {
		if(val !=null && !"".equals(val.trim())) {
			return Long.valueOf(val);
		}
		return Long.valueOf(0L);
	}

	/**
	 * @method  : notEmptyValue
	 * @desc : 업데이트시 id 가 있을경우 업데이트 하지 않기 위해서 null처리.
	 * @author   : ytkim
	 * @date   : 2020. 4. 29.
	 * @param id
	 * @param val
	 * @return
	 */
	public static String updateValueNull(String id, String val) {
		return id !=null && !"".equals(id) ? null: val;
	}

	/**
	 * @method  : intValue
	 * @desc : long -> int
	 * @author   : ytkim
	 * @date   : 2020. 5. 2.
	 * @param val
	 * @return
	 */
	public static int intValue(Long val) {
		return val != null ? val.intValue() : null;
	}

	public static LocalDateTime stringToLocalDateTime(String strDate) {
		return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(VarsqlConstants.DATE_TIME_FORMAT));
	}

	public static Date stringToDate(String strDate) throws ParseException {
		return new SimpleDateFormat(VarsqlConstants.DATE_TIME_FORMAT).parse(strDate);
	}

	public static String dateToStringDate(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(VarsqlConstants.DATE_FORMAT));
	}
	public static String dateToStringDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern(VarsqlConstants.DATE_FORMAT));
	}

	public static String dateToStringDateTime(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern(VarsqlConstants.DATE_TIME_FORMAT));
	}

	public static String toUpperCase(String str) {
		return toUpperCase(str,false);
	}

	public static String toUpperCase(String str, boolean blankChk) {
		if(blankChk) {
			return ValidateUtils.isNotBlank(str) ? str.toUpperCase(): "";
		}else {
			return str.toUpperCase();
		}
	}
	public static String toLowerCase(String str) {
		return toLowerCase(str, false);
	}

	public static String toLowerCase(String str, boolean blankChk) {
		if(blankChk) {
			return ValidateUtils.isNotBlank(str) ? str.toLowerCase() : "";
		}else {
			return str.toLowerCase();
		}
	}


}

