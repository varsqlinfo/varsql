package com.varsql.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.manager.beans.GlossaryInfo;
import com.varsql.app.manager.dao.GlossaryDAO;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GlossaryServiceImpl.java
* @DESC		: 용어집  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class GlossaryServiceImpl{
	
	@Autowired
	GlossaryDAO glossaryDAO;
	
	
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
	public ResponseResult selectGlossaryList(SearchParameter searchParameter) {
		
		ResponseResult result = new ResponseResult();
		
		int totalcnt = glossaryDAO.selectGlossaryTotalcnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(glossaryDAO.selectGlossaryList(searchParameter));
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
	}

	/**
	 * 
	 * @Method Name  : saveGlossaryInfo
	 * @Method 설명 : 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult saveGlossaryInfo(GlossaryInfo glossaryInfo) {
		ResponseResult result = new ResponseResult();
		
		glossaryInfo.setUserId(SecurityUtil.loginId());
		
		if("".equals(glossaryInfo.getWordIdx())){
			result.setItemOne(glossaryDAO.insertGlossaryInfo(glossaryInfo));
		}else{
			result.setItemOne(glossaryDAO.updateGlossaryInfo(glossaryInfo));
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : deleteGlossaryInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param parameter
	 * @return
	 */
	public ResponseResult deleteGlossaryInfo(ParamMap parameter) {
		ResponseResult result = new ResponseResult();
		
		result.setItemOne(glossaryDAO.deleteGlossaryInfo(parameter));
		
		return result;
	}
	
	
}