package com.varsql.app.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;


@Repository
public class SqlStatsDAO extends BaseDAO{
	
	public List dbSqlDateStats(DataCommonVO paramMap) {
		return getSqlSession().selectList("manageMapper.selectSqlDateStat", paramMap);
	}
	
	public List dbSqlDayStats(DataCommonVO paramMap) {
		return getSqlSession().selectList("manageMapper.selectSqlDayStat", paramMap);
	}
	
	public List dbSqlDayUserRank(DataCommonVO paramMap) {
		return getSqlSession().selectList("manageMapper.selectSqlDayUserRank", paramMap);
	}

	
}
