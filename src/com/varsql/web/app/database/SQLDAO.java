package com.varsql.web.app.database;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;

@Repository
public class SQLDAO extends BaseDAO{
	public int insertSqlUserLog(DataCommonVO paramMap){
		return getSqlSession().insert("sqlServiceMapper.insertSqlUserLog", paramMap );
	}

	public int saveQueryInfo(DataCommonVO paramMap) {
		return getSqlSession().insert("sqlServiceMapper.saveQueryInfo", paramMap );
		
	}

	public int updateQueryInfo(DataCommonVO paramMap) {
		return getSqlSession().update("sqlServiceMapper.updateQueryInfo", paramMap );
	}
}
