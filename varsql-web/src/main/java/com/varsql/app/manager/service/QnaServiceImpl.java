package com.varsql.app.manager.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.manager.dao.QnaDAO;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: QnaServiceImpl.java
* @DESC		: qna service  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 1. 10. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class QnaServiceImpl{
	
	@Autowired
	QnaDAO qnaDAO;
	
	/**
	 * 
	 * @Method Name  : selectQnaMgmtList
	 * @Method 설명 : qna 매니저 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectQnaMgmtList(SearchParameter searchParameter) {
		
		ResponseResult result = new ResponseResult();
		
		int totalcnt = qnaDAO.selectQnaMgmtTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(qnaDAO.selectQnaMgmtList(searchParameter));
		}else{
			result.setItemList(null);
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : updateQnaAnswerContent
	 * @Method 설명 : qna answer 업데이트. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10. 
	 * @변경이력  :
	 * @param qnaInfo
	 * @return
	 */
	public ResponseResult updateQnaAnswerContent(QnAInfo qnaInfo) {
		
		ResponseResult result = new ResponseResult();
		
		result.setItemOne(qnaDAO.updateQnaAnswerContent(qnaInfo) );
		
		return result;
		
	}
}