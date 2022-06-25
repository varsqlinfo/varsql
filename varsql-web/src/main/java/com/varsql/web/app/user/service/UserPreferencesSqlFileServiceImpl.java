package com.varsql.web.app.user.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.sql.SqlFileResponseDTO;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.sql.SqlFileEntity;
import com.varsql.web.model.mapper.sql.SqlFileMapper;
import com.varsql.web.repository.spec.SqlFileSpec;
import com.varsql.web.repository.sql.SqlFileEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.StringUtils;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserPreferencesSqlFileServiceImpl.java
* @DESC		: user sql file service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class UserPreferencesSqlFileServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(UserPreferencesSqlFileServiceImpl.class);

	@Autowired
	private SqlFileEntityRepository sqlFileEntityRepository;

	/**
	 *
	 * @Method Name  : sqlFileList
	 * @Method 설명 :sql file list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult sqlFileList(String vconnid , SearchParameter searchParameter) {

		Page<SqlFileEntity> result = null;
		if(vconnid != null && !"".equals(vconnid) && !"ALL".equals(vconnid)) {
			result = sqlFileEntityRepository.findAll(SqlFileSpec.findVconnSqlFileNameOrCont(vconnid, searchParameter.getKeyword())
				,VarsqlUtils.convertSearchInfoToPage(searchParameter)
			);
		}else {
			result = sqlFileEntityRepository.findAll(SqlFileSpec.findSqlFileNameOrCont(searchParameter.getKeyword())
				,VarsqlUtils.convertSearchInfoToPage(searchParameter)
			);
		}

		return VarsqlUtils.getResponseResult(result.getContent().stream().map(item ->{
			SqlFileResponseDTO resultItem = SqlFileMapper.INSTANCE.toDto(item);
			resultItem.setVname(item.getConnInfo().getVname());
			return resultItem;
		}).collect(Collectors.toList()), result.getTotalElements(), searchParameter);

	}

	/**
	 *
	 * @Method Name  : selectSqlFiledetail
	 * @Method 설명 : sql file 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult selectSqlFileDetail(String sqlId) {
		return VarsqlUtils.getResponseResultItemOne(sqlFileEntityRepository.findOne(SqlFileSpec.findSqlFile(sqlId)).orElseThrow(()->new VarsqlAppException("sql file not found : "+ sqlId)));
	}

	/**
	 *
	 * @Method Name  : deleteSqlFile
	 * @Method 설명 : sql file 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 7.
	 * @변경이력  :
	 * @param selectItem
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteSqlFiles(String selectItem) {

		logger.debug("deleteSqlFiles : {}" , selectItem);

		String[] sqlIdArr = StringUtils.split(selectItem,",");

		sqlFileEntityRepository.deleteSqlFiles(SecurityUtil.userViewId() , sqlIdArr);

		return VarsqlUtils.getResponseResultItemOne(1);
	}
}