package com.varsql.app.admin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerMgmtDAO.java
* @DESC		: 매니저 관리
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 23. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class ManagerMgmtDAO extends BaseDAO{
	
	public int selectRoleUserTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("adminMapper.selectRoleUserTotalcnt", searchParameter);
	}
	
	public List<Object> selectRoleUserList(SearchParameter searchParameter) {
		return getSqlSession().selectList("adminMapper.selectRoleUserList", searchParameter);
	}
	
	public int selectRoleManagerTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("adminMapper.selectRoleManagerTotalcnt", searchParameter);
	}
	
	public List<Object> selectRoleManagerList(SearchParameter searchParameter) {
		return getSqlSession().selectList("adminMapper.selectRoleManagerList", searchParameter);
	}

	public int updateManagerRole(DataCommonVO paramMap){
		return getSqlSession().update("adminMapper.updateManagerRole", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : deleteManagerAuthDb
	 * @Method 설명 : 매니저 권한 있는 사용자 db목록 삭제. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int deleteManagerAuthDb(DataCommonVO paramMap) {
		return getSqlSession().delete("adminMapper.deleteManagerAuthDb",paramMap);
	}
	
	public List<Object> selectDatabaseManager(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectDatabaseManager", paramMap);
	}

	public int updateDbManager(DataCommonVO paramMap) {
		return getSqlSession().update("adminMapper.updateDbManager", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbManager
	 * @Method 설명 : 데이터 베이스 매니저 삭제. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int deleteDbManager(DataCommonVO paramMap) {
		return getSqlSession().update("adminMapper.deleteDbManager", paramMap);
		
	}
}
