package com.varsql.app.manager.dao;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.dao.BaseDAO;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.auth.Authority;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class ManagerDAO extends BaseDAO{
	
	
	public int selectUserTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectUserTotalcnt", searchParameter);
	}
	
	public List<Object> selectUserList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectUserList", searchParameter);
	}

	public boolean updateAccept(String[] viewidArr, DataCommonVO paramMap){
        SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
            for(String id: viewidArr){
            	paramMap.put("viewid", id);
            	batchSqlSession.update("manageMapper.updateAccept", paramMap);
            }
            batchSqlSession.commit();
            result = true; 
        }finally{
        	batchSqlSession.close();
        }
		return result;
	}
}
