package com.varsql.web.app.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.Authority;
import com.varsql.core.common.util.StringUtil;
import com.varsql.web.app.user.dao.UserPreferencesDAO;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserPreferencesSqlFileServiceImpl.java
* @DESC		: user sql file service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class UserPreferencesSqlFileServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesSqlFileServiceImpl.class);
	
	@Autowired
	UserPreferencesDAO userPreferencesDAO;

	/**
	 * 
	 * @Method Name  : sqlFileList
	 * @Method 설명 :sql file list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult sqlFileList(SearchParameter searchParameter) {
		
		ResponseResult result = new ResponseResult();
		
		int totalcnt = userPreferencesDAO.selectUserSqlFileTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(userPreferencesDAO.selectUserSqlFileList(searchParameter));
		}else{
			result.setItemList(null);
		}
		
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : selectSqlFiledetail
	 * @Method 설명 : sql file 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult selectSqlFileDetail(ParamMap param) {
		ResponseResult result = new ResponseResult();
		
		result.setItemOne(userPreferencesDAO.selectSqlFileDetail(param));
		
		return result;
	}
	
	/**
	 *  
	 * @Method Name  : deleteSqlFile
	 * @Method 설명 : sql file 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 7. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult deleteSqlFile(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		String[] sqlIdArr = StringUtil.split(paramMap.getString("selectItem"),",");
		
		result.setItemOne(userPreferencesDAO.deleteSqlFile(sqlIdArr, paramMap));
		
		return result;
	}
}