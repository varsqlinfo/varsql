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
import com.varsql.app.manager.dao.DbnUserDAO;
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
public class DbnUserServiceImpl{
	
	@Autowired
	DbnUserDAO dbnUserDAO;
	
	public List<Object> selectUserdbList(SearchParameter searchParameter) {
		return dbnUserDAO.selectdbList(searchParameter); 
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
		
		int totalcnt = dbnUserDAO.selectdbListTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(dbnUserDAO.selectdbList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : selectDbUserMappingList
	 * @Method 설명 : db 맵핑 사용자 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDbUserMappingList(DataCommonVO paramMap) {
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(dbnUserDAO.selectDbUserMappingList(paramMap));
		return resultObject;
	}
	
	public ResponseResult updateDbUser(DataCommonVO paramMap) {
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		Map<String,String> addResultInfo = new HashMap<String,String>();
		
		if("del".equals(paramMap.getString("mode"))){
			paramMap.put("viewidArr", viewidArr);
			dbnUserDAO.deleteDbUser(paramMap);
		}else{
			for(String id: viewidArr){
	        	paramMap.put("viewid", id);
	        	try{
	        		dbnUserDAO.updateDbUser(paramMap);
	        		addResultInfo.put(id,ResultConstants.CODE_VAL.SUCCESS.name());
	        	}catch(Exception e){
	        		addResultInfo.put(id,e.getMessage());
	    		}
	        }
			
			resultObject.setItemOne(addResultInfo);
		}
		
		return resultObject;
	}

}