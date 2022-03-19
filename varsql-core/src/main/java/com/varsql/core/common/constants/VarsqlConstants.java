package com.varsql.core.common.constants;

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
	final String DATE_FORMAT = "yyyy-MM-dd";
    final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // runtime
	final String RUNTIME = System.getProperty("varsql.runtime");

	// default charset
	final String CHAR_SET = Configuration.getInstance().getCharset();


	final String UPLOAD_PATH = Configuration.getInstance().getFileUploadPath();

	final String FILE_ID_DELIMITER = ",";


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
}
