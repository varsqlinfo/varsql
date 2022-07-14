package com.varsql.core.common.constants;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.varsql.core.configuration.Configuration;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlConstants.java
* @desc		: varsql 상수
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 5. 4. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface VarsqlConstants {
	
	// 날짜 포켓.
	final String YEAR_FORMAT = "yyyy";
	
	final String DATE_FORMAT = "yyyy-MM-dd";

	final String TIME_FORMAT = "HH:mm:ss";
	
	final String TIME_MILLISECOND_FORMAT = "HH:mm:ss.SSS";

	final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
	final String TIMESTAMP_MILLISECOND_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	final DateTimeFormatter yearFormatter = DateTimeFormat.forPattern(YEAR_FORMAT);
	final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
	final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(TIME_FORMAT);
	final DateTimeFormatter timeMilliFormatter = DateTimeFormat.forPattern(TIME_MILLISECOND_FORMAT);
	final DateTimeFormatter timestampFormatter = DateTimeFormat.forPattern(TIMESTAMP_FORMAT);
	final DateTimeFormatter timestampMilliFormatter = DateTimeFormat.forPattern(TIMESTAMP_MILLISECOND_FORMAT);
    

    // runtime
	final String RUNTIME = System.getProperty("varsql.runtime");

	// default charset
	final String CHAR_SET = Configuration.getInstance().getCharset();


	final String UPLOAD_PATH = Configuration.getInstance().getFileUploadPath();

	final String FILE_ID_DELIMITER = ",";
	
	// SEMICOLON
	final String SEMICOLON = ";";

	final String JSON_CONTENT_TYPE = "application/json;charset=" + CHAR_SET;

	// sql 관련 상수.
	enum SQL{
		PARAM("?");

		private String code;

		SQL(String code) {
			this.code  = code;
		}

		public String val() {
			return this.code;
		}
	}
	
	// password 잊어 버렸을때 변경 방법. 
	enum PASSWORD_RESET_MODE{
		EMAIL, MANAGER;

		public static PASSWORD_RESET_MODE  getMode(String mode) {
			
			for(PASSWORD_RESET_MODE resetMode : PASSWORD_RESET_MODE.values()) {
				if(resetMode.name().equalsIgnoreCase(mode)) {
					return resetMode;
				}
			}
			
			return PASSWORD_RESET_MODE.MANAGER;
		}
	}
}
