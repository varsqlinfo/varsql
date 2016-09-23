package com.varsql.web.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDAO {
	@Autowired
	protected SqlSession sqlSession;
	
	protected SqlSession getSqlSession(){
		return sqlSession; 
	}
	
	protected SqlSession getSqlSession(boolean batchflag){
		if(batchflag){
			SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(sqlSession.getConfiguration());
	        return ssf.openSession(ExecutorType.BATCH);
		}
		
		return sqlSession; 
	}
	
	protected SqlSession getBatchSqlSession( SqlSession sqlSession){
		SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(sqlSession.getConfiguration());
		
        return ssf.openSession(ExecutorType.BATCH);
	}
}
