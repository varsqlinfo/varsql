package com.varsql.app.database.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.dao.BaseDAO;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabaseDAO.java
* @DESC		: database dao 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 9. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class DatabaseDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : insertDbConnectionHistory
	 * @Method 설명 : db 접속 로그 추가. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 9. 21. 
	 * @변경이력  :
	 * @param logInfo
	 * @return
	 */
	public int insertDbConnectionHistory(Map logInfo){
		return getSqlSession().insert("databaseMapper.insertDbConnectionHistory", logInfo);
	}
}
