package com.varsql.app.admin.service;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.admin.dao.ManagerMgmtDAO;
import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResultConstants;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.common.util.SecurityUtil;
import com.varsql.common.util.StringUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class ManagerMgmtServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(ManagerMgmtServiceImpl.class);
	
	@Autowired
	ManagerMgmtDAO managerMgmtDAO ;
	
	/**
	 * 
	 * @Method Name  : selectRoleUserList
	 * @Method 설명 : 사용자 권한 목록보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectRoleUserList(SearchParameter searchParameter) {
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = managerMgmtDAO.selectRoleUserTotalcnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(managerMgmtDAO.selectRoleUserList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
		
	}
	
	/**
	 * 
	 * @Method Name  : selectRoleManagerList
	 * @Method 설명 : 매니저 role 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectRoleManagerList(SearchParameter searchParameter) {
		
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = managerMgmtDAO.selectRoleManagerTotalcnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(managerMgmtDAO.selectRoleManagerList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : updateManagerRole
	 * @Method 설명 : 매니저 role 등록 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateManagerRole(DataCommonVO paramMap) {
		
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		
		resultObject.setItemOne(managerMgmtDAO.updateManagerRole( paramMap));
		
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : selectDatabaseManager
	 * @Method 설명 : 데이타 베이스 매니저 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDatabaseManager(DataCommonVO paramMap) {
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		
		resultObject.setItemList(managerMgmtDAO.selectDatabaseManager(paramMap));
		
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : updateDbManager
	 * @Method 설명 : db 매니저 수정. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateDbManager(DataCommonVO paramMap) {
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		Map<String,String> addResultInfo = new HashMap<String,String>();
		
		if("del".equals(paramMap.getString("mode"))){
			paramMap.put("viewidArr", viewidArr);
			managerMgmtDAO.deleteDbManager(paramMap);
		}else{
			for(String id: viewidArr){
	        	paramMap.put("viewid", id);
	        	try{
	        		managerMgmtDAO.updateDbManager(paramMap);
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