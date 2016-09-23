package com.varsql.web.app.guest;
import com.varsql.web.common.vo.DataCommonVO;

public interface GuestService  {
	
	/**
	 * 
	 * @Method Name  : selectGuestQna
	 * @작성일   : 2014. 8. 22. 
	 * @작성자   : ytkim
	 * @Method 설명 : 사용자 qna 목록
	 * @변경이력  :
	 * @param dcv
	 * @return
	 */
	String selectQna(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : insertGuestQnaInfo
	 * @작성일   : 2014. 8. 22. 
	 * @작성자   : ytkim
	 * @Method 설명 : gna 등록
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	boolean insertQnaInfo(DataCommonVO paramMap);
	
	
	/**
	 * 
	 * @Method Name  : deleteQnaInfo
	 * @작성일   : 2014. 8. 26. 
	 * @작성자   : ytkim
	 * @Method 설명 : qan 정보 삭제 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	boolean deleteQnaInfo(DataCommonVO paramMap);
	
	
	/**
	 * 
	 * @Method Name  : updateQnaInfo
	 * @작성일   : 2014. 8. 26. 
	 * @작성자   : ytkim
	 * @Method 설명 :qna 정보 업데이트
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	boolean updateQnaInfo(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : selectDetailQna
	 * @작성일   : 2014. 8. 26. 
	 * @작성자   : ytkim
	 * @Method 설명 : qna 상세보기
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	String selectDetailQna(DataCommonVO paramMap);

	
}