package com.varsql.web.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.manager.dao.QnaDAO;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.varsql.web.model.entity.user.QnAEntity;
import com.varsql.web.repository.spec.QnASpec;
import com.varsql.web.repository.user.QnAEntityRepository;
import com.varsql.web.util.DefaultValueUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

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
	private QnAEntityRepository qnaEntityRepository; 
	
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
		
		Page<QnAEntity> result = qnaEntityRepository.findAll(
			QnASpec.answerTypeSearch(searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter);
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
	public ResponseResult updateQnaAnswerContent(QnARequesetDTO qnaInfo) {
		
		QnAEntity qnaEntity = qnaInfo.toEntity();
		
		qnaEntity.setAnswerId(SecurityUtil.userViewId());
		qnaEntity.setAnswerDt(DefaultValueUtils.currentTimestamp());
		
		qnaEntity = qnaEntityRepository.save(qnaEntity);
		return VarsqlUtils.getResponseResultItemOne(qnaEntity != null? 1 : 0);
		
	}
}