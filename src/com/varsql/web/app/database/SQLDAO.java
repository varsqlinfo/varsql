package com.varsql.web.app.database;

import java.util.List;
import java.util.Map;

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

	public Map selectLastSqlInfo(DataCommonVO paramMap) {
		return getSqlSession().selectOne("sqlServiceMapper.selectLastSqlInfo", paramMap );
	}

	public int selectSqlListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("sqlServiceMapper.selectSqlListTotalCnt", paramMap );
	}
	public List selectSqlList(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlServiceMapper.selectSqlList", paramMap );
	}

	public int deleteSqlSaveInfo(DataCommonVO paramMap) {
		return getSqlSession().delete("sqlServiceMapper.deleteSqlSaveInfo", paramMap );
	}

	public int insertSendSqlInfo(DataCommonVO paramMap) {
		return getSqlSession().insert("sqlServiceMapper.insertSendSqlInfo", paramMap );
	}
}
