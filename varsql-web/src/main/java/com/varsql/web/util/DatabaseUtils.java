package com.varsql.web.util;

import com.varsql.web.security.DatabaseService;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DatabaseUtils.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 10. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class DatabaseUtils {
	
	private DatabaseUtils(){}
	
	/**
	 * 
	 * @Method Name  : reloadUserDatabaseInfo
	 * @Method 설명 : 사용자 권한 있는 db 정보 reload
	 * @작성일   : 2019. 3. 20. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 * @throws Exception
	 */
	public static void reloadUserDatabaseInfo() throws Exception{
		new DatabaseService().getUserDatabaseInfo();
	}
	
}
