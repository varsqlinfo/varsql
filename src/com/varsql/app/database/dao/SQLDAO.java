package com.varsql.app.database.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.database.beans.SqlLogInfo;
import com.varsql.app.database.beans.SqlParamInfo;
import com.varsql.app.database.beans.SqlUserHistoryInfo;
import com.varsql.app.database.service.SQLServiceImpl;
import com.vartech.common.app.beans.ParamMap;

@Repository
public class SQLDAO extends BaseDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(SQLDAO.class);
	
	public int insertSqlUserLog(SqlLogInfo logInfo){
		return getSqlSession().insert("sqlServiceMapper.insertSqlUserLog", logInfo);
	}

	public int saveQueryInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().insert("sqlServiceMapper.saveQueryInfo", sqlParamInfo );
	}

	public int updateQueryInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.updateQueryInfo", sqlParamInfo );
	}

	public Map selectLastSqlInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().selectOne("sqlServiceMapper.selectLastSqlInfo", sqlParamInfo );
	}

	public int selectSqlListTotalCnt(SqlParamInfo sqlParamInfo) {
		return getSqlSession().selectOne("sqlServiceMapper.selectSqlListTotalCnt", sqlParamInfo );
	}
	
	public List selectSqlList(SqlParamInfo sqlParamInfo) {
		return getSqlSession().selectList("sqlServiceMapper.selectSqlList", sqlParamInfo );
	}
	
	public int deleteSqlSaveInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().delete("sqlServiceMapper.deleteSqlSaveInfo", sqlParamInfo );
	}
	
	
	public int insertUserHistory(SqlUserHistoryInfo sqlUserHistoryInfo) {
		try{
			return getSqlSession().insert("sqlServiceMapper.insertUserHistory", sqlUserHistoryInfo );
		}catch(Exception e){
			logger.error("insertUserHistory message : {} ",e.getMessage());
			logger.error("insertUserHistory ",e);
			// exception 상광없이 돌아야 함으로 처리. 
			return -1; 
		}
	}

}
