package com.varsql.app.admin.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.admin.dao.ErrorLogDAO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ErrorLogServiceImpl.java
* @DESC		: error log service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class ErrorLogServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(ErrorLogServiceImpl.class);
	
	@Autowired
	ErrorLogDAO errorLogDAO;
	
	/**
	 * 
	 * @Method Name  : selectErrorList
	 * @Method 설명 : error list 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 16. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectErrorList(SearchParameter searchParameter) {
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = errorLogDAO.selectErrorTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(errorLogDAO.selectErrorList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
		
	}
}