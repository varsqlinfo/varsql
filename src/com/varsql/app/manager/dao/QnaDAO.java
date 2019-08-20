package com.varsql.app.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.user.beans.QnAInfo;
import com.vartech.common.app.beans.SearchParameter;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: QnaDAO.java
* @DESC		: qna  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class QnaDAO extends BaseDAO{
	
	public int selectQnaMgmtTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectQnaMgmtTotalCnt", searchParameter);
	}
	
	public List<Object> selectQnaMgmtList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectQnaMgmtList", searchParameter);
	}

	public int updateQnaAnswerContent(QnAInfo qnaInfo){
		return getSqlSession().update("manageMapper.updateQnaAnswerContent", qnaInfo);
	}

}
