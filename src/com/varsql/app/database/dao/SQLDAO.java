package com.varsql.app.database.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.database.beans.SqlParamInfo;
import com.vartech.common.app.beans.ParamMap;

@Repository
public class SQLDAO extends BaseDAO{
	public int insertSqlUserLog(ParamMap paramMap){
		return getSqlSession().insert("sqlServiceMapper.insertSqlUserLog", paramMap );
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
}
