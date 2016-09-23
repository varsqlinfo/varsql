package com.varsql.web.app.database;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;

@Repository
public class SQLDAO extends BaseDAO{
	public int insertSqlUserLog(DataCommonVO paramMap){
		return getSqlSession().insert("insertSqlUserLog", paramMap );
	}
}
