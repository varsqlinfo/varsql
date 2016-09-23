package com.varsql.web.app.admin.usermenu;
import com.varsql.web.common.vo.DataCommonVO;

public interface UserMenuMgmtService  {
	/**
	 * 
	 * @Method Name  : listDbMenu
	 * @Method 설명 : db메뉴 보기
	 * @작성일   : 2015. 4. 16. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	String listDbMenu(DataCommonVO paramMap);
	/**
	 * 
	 * @Method Name  : moodifyDbMenu
	 * @Method 설명 : 메뉴 수정하기
	 * @작성일   : 2015. 4. 16. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	boolean moodifyDbMenu(DataCommonVO paramMap);
	/**
	 * 
	 * @Method Name  : addDbMenu
	 * @Method 설명 : 메뉴 추가하기
	 * @작성일   : 2015. 4. 16. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	boolean addDbMenu(DataCommonVO paramMap);
	/**
	 * 
	 * @Method Name  : deleteDbMenu
	 * @Method 설명 : 메뉴 삭제 . 
	 * @작성일   : 2015. 4. 16. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	boolean deleteDbMenu(DataCommonVO paramMap);
}