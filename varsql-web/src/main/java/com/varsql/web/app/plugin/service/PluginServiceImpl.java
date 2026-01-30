package com.varsql.web.app.plugin.service;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.varsql.web.common.service.AbstractService;
import com.varsql.web.dto.sql.SqlHistoryResponseDTO;
import com.varsql.web.model.entity.app.GlossaryEntity;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.mapper.sql.SqlHistoryMapper;
import com.varsql.web.repository.app.GlossaryEntityRepository;
import com.varsql.web.repository.spec.GlossarySpec;
import com.varsql.web.repository.spec.SqlHistorySpec;
import com.varsql.web.repository.sql.SqlHistoryEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

import lombok.RequiredArgsConstructor;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PluginServiceImpl.java
* @DESC		:
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
@RequiredArgsConstructor
public class PluginServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);

	private final GlossaryEntityRepository glossaryEntityRepository;

	private final SqlHistoryEntityRepository sqlHistoryEntityRepository;

	/**
	 *
	 * @Method Name  : glossarySearch
	 * @Method 설명 : 용어 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 24.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult glossarySearch(SearchParameter searchParameter) {
		List<GlossaryEntity> result = glossaryEntityRepository.findAll(GlossarySpec.searchField(searchParameter));
		return VarsqlUtils.getResponseResultItemList(result);
	}

	/**
	 *
	 * @Method Name  : historySearch
	 * @Method 설명 : user sql history search
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 24.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult historySearch(SearchParameter searchParameter) {

		Page<SqlHistoryEntity> result = sqlHistoryEntityRepository.findAll(
			SqlHistorySpec.userHisotryearch(String.valueOf(searchParameter.getCustomParam().get("conuid")), searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter , SqlHistoryEntity.START_TIME)
		);

		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(result.getContent().stream().map(item ->{
			SqlHistoryResponseDTO sqlUserHistoryInfo = SqlHistoryMapper.INSTANCE.toDto(item);
			sqlUserHistoryInfo.setErrorLog(null);
			sqlUserHistoryInfo.setUsrIp(null);
			return sqlUserHistoryInfo;
		}).collect(Collectors.toList()));
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));

		return responseResult;
	}
}