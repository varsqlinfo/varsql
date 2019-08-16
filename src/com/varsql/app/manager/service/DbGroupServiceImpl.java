package com.varsql.app.manager.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResultConstants;
import com.varsql.app.manager.beans.DbGroupInfo;
import com.varsql.app.manager.dao.DbGroupDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbGroupServiceImpl.java
* @DESC		: 용어집  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class DbGroupServiceImpl{
	
	@Autowired
	DbGroupDAO dbGroupDAO;
	
	
	/**
	 * 
	 * @Method Name  : selectUserList
	 * @Method 설명 : 사용자 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectDbGroupList(SearchParameter searchParameter) {
		
		ResponseResult result = new ResponseResult();
		
		int totalcnt = dbGroupDAO.selectDbGroupTotalcnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(dbGroupDAO.selectDbGroupList(searchParameter));
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
	}

	/**
	 * 
	 * @Method Name  : saveDbGroupInfo
	 * @Method 설명 : 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult saveDbGroupInfo(DbGroupInfo dbGroupInfo) {
		ResponseResult result = new ResponseResult();
		
		dbGroupInfo.setUserId(SecurityUtil.loginId());
		
		if("".equals(dbGroupInfo.getGroupId())){
			dbGroupInfo.setGroupId(VarsqlUtil.generateUUID());
			result.setItemOne(dbGroupDAO.insertDbGroupInfo(dbGroupInfo));
		}else{
			result.setItemOne(dbGroupDAO.updateDbGroupInfo(dbGroupInfo));
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbGroupInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param parameter
	 * @return
	 */
	public ResponseResult deleteDbGroupInfo(ParamMap parameter) {
		ResponseResult result = new ResponseResult();
		
		result.setItemOne(dbGroupDAO.deleteDbGroupInfo(parameter));
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : selectDbGroupMappingList
	 * @Method 설명 : db 그룹 맵핑 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 12. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDbGroupMappingList(DataCommonVO paramMap) {
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(dbGroupDAO.selectDbGroupMappingList(paramMap));
		return resultObject;
	}
	
	@Transactional
	public ResponseResult updateDbGroupMappingInfo(DataCommonVO paramMap) {
		String[] vconnidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		Map<String,String> addResultInfo = new HashMap<String,String>();
		
		if("del".equals(paramMap.getString("mode"))){
			paramMap.put("vconnidArr", vconnidArr);
			dbGroupDAO.deleteDbGroupMappingInfo(paramMap);
		}else{
			for(String id: vconnidArr){
	        	paramMap.put("vconnid", id);
	        	try{
	        		dbGroupDAO.insertDbGroupMappingInfo(paramMap);
	        		addResultInfo.put(id,ResultConstants.CODE_VAL.SUCCESS.name());
	        	}catch(Exception e){
	        		addResultInfo.put(id,e.getMessage());
	    		}
	        }
			
			resultObject.setItemOne(addResultInfo);
		}
		
		return resultObject;
	}
	
	
	/**
	 * 
	 * @Method Name  : selectDbGroupUserMappingList
	 * @Method 설명 : db 맵핑 사용자 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDbGroupUserMappingList(DataCommonVO paramMap) {
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(dbGroupDAO.selectDbGroupUserMappingList(paramMap));
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : updateDbGroupUser
	 * @Method 설명 : db그룹 사용자 추가, 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateDbGroupUser(DataCommonVO paramMap) {
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		Map<String,String> addResultInfo = new HashMap<String,String>();
		
		if("del".equals(paramMap.getString("mode"))){
			paramMap.put("viewidArr", viewidArr);
			dbGroupDAO.deleteDbGroupUser(paramMap);
		}else{
			for(String id: viewidArr){
	        	paramMap.put("viewid", id);
	        	try{
	        		dbGroupDAO.updateDbGroupUser(paramMap);
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