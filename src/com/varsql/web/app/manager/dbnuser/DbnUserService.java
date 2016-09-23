package com.varsql.web.app.manager.dbnuser;
import com.varsql.web.common.vo.DataCommonVO;

public interface DbnUserService  {
	
	/**
	 * 
	 * @Method Name  : selectdbList
	 * @Method 설명 : database 목록 가져오기
	 * @작성일   : 2014. 8. 29. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	String selectdbList(DataCommonVO paramMap);

	String selectDbUserMappingList(DataCommonVO paramMap);

	String updateDbUser(DataCommonVO paramMap);
	
	
}