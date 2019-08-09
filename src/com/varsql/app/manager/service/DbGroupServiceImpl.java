package com.varsql.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.manager.beans.DbGroupInfo;
import com.varsql.app.manager.dao.DbGroupDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.util.SecurityUtil;
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
	
	
}