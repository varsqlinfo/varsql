package com.varsql.web.app.manager.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.app.manager.dao.SqlStatsDAO;
import com.varsql.web.common.beans.DataCommonVO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlStatsServiceImpl.java
* @DESC		: sql 통계 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
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
	
	/**
	 * 
	 * @Method Name  : selectLogSearch
	 * @Method 설명 : log 검색. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 26. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectLogSearch(SearchParameter searchParameter) {
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = sqlStatsDAO.selectLogSearchTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(sqlStatsDAO.selectLogSearch(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
	}

}