package com.varsql.app.database.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.database.beans.SqlLogInfo;
import com.varsql.app.database.beans.SqlParamInfo;
import com.varsql.app.database.beans.SqlUserHistoryInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.sort.TreeDataSort;

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
	 * @Method Name  : insertSqlFileTabInfo
	 * @Method 설명 : insert sql tab info
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 7. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 */
	public int insertSqlFileTabInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.insertSqlFileTabInfo", sqlParamInfo);
		
	}
	/**
	 * 
	 * @Method Name  : deleteSqlFileTabInfo
	 * @Method 설명 : delete sql file tab info
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 7. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	@Transactional
	public int deleteSqlFileTabInfo(SqlParamInfo sqlParamInfo) {
		SqlSession sqlSession = getSqlSession();
		
		sqlSession.update("sqlServiceMapper.updateSqlFileTabPrevSqlIdInfo",sqlParamInfo);
		
		return sqlSession.delete("sqlServiceMapper.deleteSqlFileTabInfo", sqlParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : deleteAllSqlFileTabInfo
	 * @Method 설명 : 사용자 file tab 정보 전부 삭제 . 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 22. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public int deleteAllSqlFileTabInfo(SqlParamInfo sqlParamInfo) {
		return getSqlSession().delete("sqlServiceMapper.deleteAllSqlFileTabInfo", sqlParamInfo);
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
	public int updateSqlFileTabDisable(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.updateSqlFileTabDisable", sqlParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : updateSqlFileTabEnable
	 * @Method 설명 : tab enable
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 1. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public int updateSqlFileTabEnable(SqlParamInfo sqlParamInfo) {
		return getSqlSession().update("sqlServiceMapper.updateSqlFileTabEnable", sqlParamInfo);
	}

	public List selectSqlFileList(SqlParamInfo sqlParamInfo) {
		return getSqlSession().selectList("sqlServiceMapper.selectSqlFileList", sqlParamInfo );
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
	
	/**
	 * 
	 * @Method Name  : selectSqlFileTabList
	 * @Method 설명 : sql editor tab 정보.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 9. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public List selectSqlFileTabList(SqlParamInfo sqlParamInfo) {
		
		class TabListResultHandler implements ResultHandler<Map> {
			
			TreeDataSort tds = new TreeDataSort("SQL_ID", "PREV_SQL_ID");
			
			@Override
			public void handleResult(ResultContext<? extends Map> paramResultContext) {
				tds.sortTreeData(paramResultContext.getResultObject());
			}
			
			public List getResultList(){
				return tds.getSortList();
			}
		}

		TabListResultHandler reportResultHandler = new TabListResultHandler();
		getSqlSession().select("sqlServiceMapper.selectSqlFileTabList", sqlParamInfo , reportResultHandler);
		return reportResultHandler.getResultList(); 
	}
}



