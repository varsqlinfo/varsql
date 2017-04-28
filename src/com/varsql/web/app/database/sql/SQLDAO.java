package com.varsql.web.app.database.sql;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.varsql.web.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;

@Repository
public class SQLDAO extends BaseDAO{
	public int insertSqlUserLog(ParamMap paramMap){
		return getSqlSession().insert("sqlServiceMapper.insertSqlUserLog", paramMap );
	}

	public int saveQueryInfo(ParamMap paramMap) {
		return getSqlSession().insert("sqlServiceMapper.saveQueryInfo", paramMap );
	}

	public int updateQueryInfo(ParamMap paramMap) {
		return getSqlSession().update("sqlServiceMapper.updateQueryInfo", paramMap );
	}

	public Map selectLastSqlInfo(ParamMap paramMap) {
		return getSqlSession().selectOne("sqlServiceMapper.selectLastSqlInfo", paramMap );
	}

	public int selectSqlListTotalCnt(ParamMap paramMap) {
		return getSqlSession().selectOne("sqlServiceMapper.selectSqlListTotalCnt", paramMap );
	}
	
	public List selectSqlList(ParamMap paramMap) {
		return getSqlSession().selectList("sqlServiceMapper.selectSqlList", paramMap );
	}
	
	public int deleteSqlSaveInfo(ParamMap paramMap) {
		return getSqlSession().delete("sqlServiceMapper.deleteSqlSaveInfo", paramMap );
	}
}
