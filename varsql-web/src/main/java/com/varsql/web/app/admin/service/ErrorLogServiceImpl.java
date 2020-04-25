package com.varsql.web.app.admin.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.varsql.web.common.service.AbstractService;
import com.varsql.web.model.entity.sql.SqlExceptionLogEntity;
import com.varsql.web.repository.spec.SqlExceptionLogSpec;
import com.varsql.web.repository.sql.SqlExceptionLogEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ErrorLogServiceImpl.java
* @desc		: error log service
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class ErrorLogServiceImpl  extends AbstractService{
	private static final Logger logger = LoggerFactory.getLogger(ErrorLogServiceImpl.class);

	@Autowired
	private SqlExceptionLogEntityRepository sqlExceptionLogEntityRepository;

	/**
	 * @method  : selectErrorList
	 * @desc : error list
	 * @author   : ytkim
	 * @date   : 2020. 4. 24.
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectErrorList(SearchParameter searchParameter) {
		Page<SqlExceptionLogEntity> result = sqlExceptionLogEntityRepository.findAll(
			SqlExceptionLogSpec.searchField(searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter);
	}
}