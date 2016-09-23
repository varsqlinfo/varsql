package com.varsql.web.app.manager.qna;
import com.varsql.web.common.vo.DataCommonVO;

public interface QnaService  {
	
	/**
	 * 
	 * @Method Name  : seletQnaMgmtList
	 * @작성일   : 2014. 8. 26. 
	 * @작성자   : ytkim
	 * @Method 설명 : qna 목록 보기
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	String selectQnaMgmtList(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : updateQnaMgmtInfo
	 * @작성일   : 2014. 8. 26. 
	 * @작성자   : ytkim
	 * @Method 설명 : qna 정보 업데이트
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	String updateQnaAnswerContent(DataCommonVO paramMap);
	

	
}