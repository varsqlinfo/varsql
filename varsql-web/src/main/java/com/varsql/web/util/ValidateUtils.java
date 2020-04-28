package com.varsql.web.util;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ValidateUtils.java
* @desc		: 유효성 체크 util
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class ValidateUtils {

	private ValidateUtils() {}

	public static Long longValueOf(Integer val) {
		return longValueOf(val+"");
	}

	public static Long longValueOf(String val) {
		if(val ==null || "".equals(val.trim())) {
			return null;
		}
		return Long.valueOf(val);
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
}

