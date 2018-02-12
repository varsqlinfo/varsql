package com.varsql.app.admin.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;


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

	public List<Object> selectDatabaseManager(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectDatabaseManager", paramMap);
	}

	public int updateDbManager(DataCommonVO paramMap) {
		return getSqlSession().update("adminMapper.updateDbManager", paramMap);
	}

	public int deleteDbManager(DataCommonVO paramMap) {
		return getSqlSession().update("adminMapper.deleteDbManager", paramMap);
		
	}
}
