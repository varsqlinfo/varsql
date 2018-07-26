package com.varsql.app.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class SqlStatsDAO extends BaseDAO{
	
	public List dbSqlDateStats(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlStatsMapper.selectSqlDateStat", paramMap);
	}
	
	public List dbSqlDayStats(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlStatsMapper.selectSqlDayStat", paramMap);
	}
	
	public List dbSqlDayUserRank(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlStatsMapper.selectSqlDayUserRank", paramMap);
	}

	public int selectLogSearchTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("sqlStatsMapper.selectLogSearchTotalCnt", searchParameter);
	}

	public List selectLogSearch(SearchParameter searchParameter) {
		return getSqlSession().selectList("sqlStatsMapper.selectLogSearch", searchParameter);
	}

	
}
