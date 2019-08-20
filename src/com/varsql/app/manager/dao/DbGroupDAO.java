package com.varsql.app.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.manager.beans.DbGroupInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.SearchParameter;


/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbGroupDAO.java
* @DESC		: db group 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class DbGroupDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectDbGroupTotalcnt
	 * @Method 설명 : 그룹 총 카운트
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectDbGroupTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageDbGroupMapper.selectDbGroupTotalcnt", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectDbGroupList
	 * @Method 설명 : 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public List<Object> selectDbGroupList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageDbGroupMapper.selectDbGroupList", searchParameter);
	}

	/**
	 * 
	 * @Method Name  : insertDbGroupInfo
	 * @Method 설명 : 등록
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param dbGroupInfo
	 * @return
	 */
	public Object insertDbGroupInfo(DbGroupInfo dbGroupInfo) {
		return getSqlSession().insert("manageDbGroupMapper.insertDbGroupInfo",dbGroupInfo);
	}
	
	/**
	 * 
	 * @Method Name  : updateDbGroupInfo
	 * @Method 설명 : 수정
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param dbGroupInfo
	 * @return
	 */
	public Object updateDbGroupInfo(DbGroupInfo dbGroupInfo) {
		return getSqlSession().update("manageDbGroupMapper.updateDbGroupInfo",dbGroupInfo);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbGroupInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param parameter
	 * @return
	 */
	public Object deleteDbGroupInfo(ParamMap parameter) {
		return getSqlSession().delete("manageDbGroupMapper.deleteDbGroupInfo", parameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectDbGroupMappingList
	 * @Method 설명 : db group mapping list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 12. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public List selectDbGroupMappingList(DataCommonVO paramMap) {
		return getSqlSession().selectList("manageDbGroupMapper.selectDbGroupMappingList", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbGroupMappingInfo
	 * @Method 설명 : delete db group mapping info
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 12. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int deleteDbGroupMappingInfo(DataCommonVO paramMap) {
		return getSqlSession().delete("manageDbGroupMapper.deleteDbGroupMappingInfo",paramMap);
		
	}
	
	/**
	 * 
	 * @Method Name  : updateDbGroupMappingInfo
	 * @Method 설명 : update db group mapping info
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 12. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int insertDbGroupMappingInfo(DataCommonVO paramMap) {
		return getSqlSession().update("manageDbGroupMapper.insertDbGroupMappingInfo", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : selectDbGroupUserMappingList
	 * @Method 설명 : db그룹 사용자 맵핑 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public List<Object>  selectDbGroupUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("manageDbGroupMapper.selectDbGroupUserMappingList", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : updateDbUser
	 * @Method 설명 : db 사용자 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int updateDbGroupUser( DataCommonVO paramMap){
		return getSqlSession().update("manageDbGroupMapper.updateDbGroupUser", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbUser
	 * @Method 설명 : db 사용자 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int deleteDbGroupUser( DataCommonVO paramMap){
		return getSqlSession().delete("manageDbGroupMapper.deleteDbGroupUser",paramMap);
	}
}
