package com.varsql.web.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DefaultValueUtils.java
* @desc		: 기본값 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DefaultValueUtils {
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    public static Date currentDate() {
    	return new Date();
    }
}
