package com.varsql.web.app.admin;
import java.util.List;

import com.varsql.web.common.vo.DataCommonVO;

public interface AdminService  {
	
	/**
	 * 
	 * @Method Name  : selectPageList
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db 커넥션 정보 가져오기
	 * @param paramMap
	 * @return
	 */
	String selectPageList(DataCommonVO paramMap);
	/**
	 * 
	 * @Method Name  : selectDetailObject
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 커넥션 정보 상세보기
	 * @param dcv
	 * @return
	 */
	String selectDetailObject(DataCommonVO dcv);
	
	/**
	 * 
	 * @Method Name  : connectionCheck
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 커넥션 체크
	 * @param dcv
	 * @return
	 */
	String connectionCheck(DataCommonVO dcv);
	
	/**
	 * 
	 * @Method Name  : insertVtconnectionInfo
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 커넥션 정보 등록
	 * @param paramMap
	 * @return
	 */
	boolean insertVtconnectionInfo(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : updateVtconnectionInfo
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 커넥션 정보 업데이트
	 * @param paramMap
	 * @return
	 */
	boolean updateVtconnectionInfo(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : deleteVtconnectionInfo
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 커넥션 정보 삭제 
	 * @param paramMap
	 * @return
	 */
	boolean deleteVtconnectionInfo(DataCommonVO paramMap);
	
	/**
	 * 
	 * @Method Name  : selectAllDbType
	 * @Method 설명 : 데이타 베이스 타입 보기
	 * @작성일   : 2015. 4. 16. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	List selectAllDbType();

	
}