package com.varsql.web.app.manager;
import com.varsql.web.common.vo.DataCommonVO;

public interface ManagerService  {
	
	/**
	 * 
	 * @Method Name  : selectUserList
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 사용자 목록보기
	 * @param paramMap
	 * @return
	 */
	String selectUserList(DataCommonVO paramMap);
	
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
	 * @Method Name  : deleteUserInfo
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 시용자 정보 삭제.
	 * @param paramMap
	 * @return
	 */
	boolean deleteUserInfo(DataCommonVO paramMap);
	
	
	/**
	 * 
	 * @Method Name  : updateAccept
	 * @Method 설명 : 사용자 접근 여부 업데이트 
	 * @작성일   : 2014. 8. 29. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	String updateAccept(DataCommonVO paramMap);
	
}