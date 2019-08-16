package com.varsql.app.manager.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResultConstants;
import com.varsql.app.common.constants.UserConstants;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.manager.dao.ManagerCommonDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbnUserServiceImpl.java
* @DESC		: db 사용자 관리. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 1. 23. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class ManagerCommonServiceImpl{
	
	@Autowired
	ManagerCommonDAO managerCommonDAO;
	
	public List<Object> selectUserdbList(SearchParameter searchParameter) {
		return managerCommonDAO.selectdbList(searchParameter); 
	}
	/**
	 * 
	 * @Method Name  : selectdbList
	 * @Method 설명 : db 사용자 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectdbList(SearchParameter searchParameter) {
		
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = managerCommonDAO.selectdbListTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(managerCommonDAO.selectdbList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
	}
	/**
	 * 
	 * @Method Name  : selectUserList
	 * @Method 설명 : 사용자 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserList(SearchParameter searchParameter) {
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(managerCommonDAO.selectUserList(searchParameter));
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : selectUserPagingList
	 * @Method 설명 : 사용자 페이징 정보 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserPagingList(SearchParameter searchParameter) {
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = managerCommonDAO.selectUserListTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(managerCommonDAO.selectUserList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
	}
}