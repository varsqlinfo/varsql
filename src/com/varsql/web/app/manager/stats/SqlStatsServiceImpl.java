package com.varsql.web.app.manager.stats;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.common.beans.DataCommonVO;

@Service
public class SqlStatsServiceImpl{
	
	@Autowired
	SqlStatsDAO sqlStatsDAO;
	
	/**
	 * 
	 * @Method Name  : dbSqlDateStats
	 * @Method 설명 : sql 날짜 별 보기 
	 * @작성일   : 2015. 5. 6. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public Map dbSqlDateStats(DataCommonVO paramMap) {
		Map json = new HashMap();
		
		List result = sqlStatsDAO.dbSqlDateStats(paramMap);
		json.put("items", result);
		return json;
	}

	/**
	 * 
	 * @Method Name  : dbSqlDayStats
	 * @Method 설명 : sql 하루 값 보기
	 * @작성일   : 2015. 5. 6. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public Map dbSqlDayStats(DataCommonVO paramMap) {
		Map json = new HashMap();
		List result = sqlStatsDAO.dbSqlDayStats(paramMap);
		json.put("items", result);
		return json;
	}

	/**
	 * 
	 * @Method Name  : dbSqlDayUserTop
	 * @Method 설명 : 하루  top 5 보기 
	 * @작성일   : 2015. 5. 6. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public Map dbSqlDayUserRank(DataCommonVO paramMap) {
		
		Map json = new HashMap();
		json.put("result", sqlStatsDAO.dbSqlDayUserRank(paramMap));
		
		return json;
	}

}