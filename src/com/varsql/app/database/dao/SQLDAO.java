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

@Repository
public class SQLDAO extends BaseDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(SQLDAO.class);
	
	/**
	 * 
	 * @Method Name  : insertSqlUserLog
	 * @Method 설명 : sql 로그
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 1. 
	 * @변경이력  :
	 * @param logInfo
	 * @return
	 */
	public int insertSqlUserLog(SqlLogInfo logInfo){
		return getSqlSession().insert("sqlServiceMapper.insertSqlUserLog", logInfo);
	}

	/**
	 * 
	 * @Method Name  : saveQueryInfo
	 * @Method 설명 :  editor 쿼리 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 1. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public int saveQueryInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().insert("sqlServiceMapper.saveQueryInfo", sqlParamInfo );
	}
	
	/**
	 * 
	 * @Method Name  : updateQueryInfo
	 * @Method 설명 : 쿼리 업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 1. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public int updateQueryInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.updateQueryInfo", sqlParamInfo );
	}
	
	/**
	 * 
	 * @Method Name  : updateSqlFileViewYInfo
	 * @Method 설명 : 쿼리 viewyn N으로 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 1. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public int updateSqlFileViewYInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.updateSqlFileViewYInfo", sqlParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : updateSqlFileTabDisableInfo
	 * @Method 설명 : tab disable
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 1. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public int updateSqlFileTabDisableInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.updateSqlFileTabDisableInfo", sqlParamInfo);
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
