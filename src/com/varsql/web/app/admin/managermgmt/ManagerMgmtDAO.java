package com.varsql.web.app.admin.managermgmt;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class ManagerMgmtDAO extends BaseDAO{
	
	public int selectRoleUserTotalcnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("adminMapper.selectRoleUserTotalcnt", paramMap);
	}
	
	public List<Object> selectRoleUserList(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectRoleUserList", paramMap);
	}
	
	public int selectRoleManagerTotalcnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("adminMapper.selectRoleManagerTotalcnt", paramMap);
	}
	
	public List<Object> selectRoleManagerList(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectRoleManagerList", paramMap);
	}

	public int updateManagerRole(DataCommonVO paramMap){
		return getSqlSession().update("adminMapper.updateManagerRole", paramMap);
	}

	public List<Object> selectDatabaseManager(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectDatabaseManager", paramMap);
	}

	public Object updateDbManager(String[] viewidArr, DataCommonVO paramMap) throws Exception {
        SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
        	batchSqlSession.commit(false);
        	
        	batchSqlSession.delete("adminMapper.deleteDbManager",paramMap);
        	
            for(String id: viewidArr){
            	paramMap.put("viewid", id);
            	batchSqlSession.update("adminMapper.updateDbManager", paramMap);
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
