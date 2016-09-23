package com.varsql.web.app.admin.managermgmt;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class ManagerMgmtDAO extends BaseDAO{
	
	public int selectRoleUserTotalcnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectRoleUserTotalcnt", paramMap);
	}
	
	public List<Object> selectRoleUserList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectRoleUserList", paramMap);
	}
	
	public int selectRoleManagerTotalcnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectRoleManagerTotalcnt", paramMap);
	}
	
	public List<Object> selectRoleManagerList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectRoleManagerList", paramMap);
	}

	public int updateManagerRole(DataCommonVO paramMap){
		return getSqlSession().update("updateManagerRole", paramMap);
	}

	public List<Object> selectDatabaseManager(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectDatabaseManager", paramMap);
	}

	public Object updateDbManager(String[] viewidArr, DataCommonVO paramMap) throws Exception {
        SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
        	batchSqlSession.commit(false);
        	
        	batchSqlSession.delete("deleteDbManager",paramMap);
        	
            for(String id: viewidArr){
            	paramMap.put("viewid", id);
            	batchSqlSession.update("updateDbManager", paramMap);
            }
            batchSqlSession.commit();
            result = true;
        }catch(Exception e){
        	batchSqlSession.rollback();
        	throw e;
        }finally{
        	
        	batchSqlSession.close();
        }
		return result;
	}
}
