package com.varsql.web.app.user.join;
import com.varsql.web.common.vo.DataCommonVO;

public interface JoinService  {
	
	/**
	 * 
	 * @Method Name  : selectDetailUser
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 사용자 정보 상세보기
	 * @param dcv
	 * @return
	 */
	String selectUserDetail(DataCommonVO dcv);
	
	/**
	 * 
	 * @Method Name  : insertUserInfo
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 사용자 정보 등록
	 * @param paramMap
	 * @return
	 */
	boolean insertUserInfo(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : updateUserInfo
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 사용자 정보 수정
	 * @param paramMap
	 * @return
	 */
	boolean updateUserInfo(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : selectIdCheck
	 * @작성일   : 2014. 8. 20. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 아이디 중복 체크
	 * @param dcv
	 * @return
	 */
	String selectIdCheck(DataCommonVO dcv);
	

	
}