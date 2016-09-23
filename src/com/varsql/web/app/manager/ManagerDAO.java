package com.varsql.web.app.manager;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;

import com.varsql.auth.Authority;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class ManagerDAO extends BaseDAO{
	
	
	public int selectUserTotalcnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectUserTotalcnt", paramMap);
	}
	
	public List<Object> selectUserList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectUserList", paramMap);
	}

	public Object selectUserDetail(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectUserDetail", paramMap);
	}

	public String selectUserMaxVal() {
		return getSqlSession().selectOne("selectUserMaxVal");
	}
	
	public int insertUserInfo(DataCommonVO paramMap){
		return getSqlSession().insert("insertUserInfo", paramMap );
	}
	
	public int updateUserInfo(DataCommonVO paramMap){
		return getSqlSession().update("updateUserInfo", paramMap);
	}

	public int deleteUserInfo(DataCommonVO paramMap) {
		return getSqlSession().delete("deleteUserInfo", paramMap);
	}
	
	public boolean updateAccept(String[] viewidArr, DataCommonVO paramMap){
        SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
            for(String id: viewidArr){
            	paramMap.put("viewid", id);
            	batchSqlSession.update("updateAccept", paramMap);
            }
            batchSqlSession.commit();
            result = true; 
        }finally{
        	batchSqlSession.close();
        }
		return result;
	}
}
