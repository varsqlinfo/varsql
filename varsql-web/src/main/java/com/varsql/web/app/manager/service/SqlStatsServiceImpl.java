package com.varsql.web.app.manager.service;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.varsql.web.common.service.AbstractService;
import com.varsql.web.dto.sql.SqlHistoryResponseDTO;
import com.varsql.web.dto.user.RegInfoDTO;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.entity.user.RegInfoEntity;
import com.varsql.web.repository.spec.SqlHistorySpec;
import com.varsql.web.repository.sql.SqlHistoryEntityRepository;
import com.varsql.web.repository.sql.SqlStatisticsEntityRepository;
import com.varsql.web.util.VarsqlUtils;
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
public class SqlStatsServiceImpl extends AbstractService{
	
	@Autowired
	private SqlStatisticsEntityRepository sqlStatisticsEntityRepository;
	
	@Autowired
	private SqlHistoryEntityRepository sqlHistoryEntityRepository;
	
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
	public ResponseResult dbSqlDateStats(String vconid, String s_date, String e_date) {
		return VarsqlUtils.getResponseResultItemList(sqlStatisticsEntityRepository.findSqlDateStat(vconid, s_date, e_date));
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
	public ResponseResult dbSqlDayStats(String vconid, String s_date, String e_date) {
		return VarsqlUtils.getResponseResultItemList(sqlStatisticsEntityRepository.findSqlDayStat(vconid, s_date, e_date));
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
	public ResponseResult dbSqlDayUserRank(String vconid, String s_date, String e_date, String command_type) {
		return VarsqlUtils.getResponseResultItemList(sqlStatisticsEntityRepository.findDayUserRank(vconid, s_date, e_date, command_type));
	}
	
	/**
	 * 
	 * @Method Name  : findSqlLog
	 * @Method 설명 : log 검색. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 26. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult findSqlLog(String vconnid,SearchParameter searchParameter) {
		
		Page<SqlHistoryEntity> result = sqlHistoryEntityRepository.findAll(
			SqlHistorySpec.logSqlSearch(vconnid, searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter , SqlHistoryEntity.START_TIME)
		);
		
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemList(result.getContent().stream().map(item ->{
			SqlHistoryResponseDTO sqlUserHistoryInfo = domainMapper.convertToDomain(item, SqlHistoryResponseDTO.class);
			RegInfoEntity regInfo = item.getRegInfo();
			sqlUserHistoryInfo.setRegInfo(RegInfoDTO.builder().uid(regInfo.getUid()).uname(regInfo.getUname()).build());
			return sqlUserHistoryInfo; 
		}).collect(Collectors.toList()));
		
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));
		
		return responseResult;
	}
}