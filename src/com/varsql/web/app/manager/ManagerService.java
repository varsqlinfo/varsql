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