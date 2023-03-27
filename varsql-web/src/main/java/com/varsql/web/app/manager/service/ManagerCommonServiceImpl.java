package com.varsql.web.app.manager.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.mapper.db.DBConnectionMapper;
import com.varsql.web.model.mapper.user.UserMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ManagerCommonServiceImpl.java
* @desc		: 매니저 공통 처리.
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 1. 23. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class ManagerCommonServiceImpl extends AbstractService{

	@Autowired
	private UserMgmtRepository userMgmtRepository;

	@Autowired
	private DBConnectionEntityRepository dbConnectionModelRepository;


	public List<DBConnectionResponseDTO> selectdbList() {
		return dbConnectionModelRepository.findAll(
			DBConnectionSpec.mgmtDbList(SecurityUtil.userViewId() , "")
		).stream().map(item -> DBConnectionMapper.INSTANCE.toDto(item)).collect(Collectors.toList());
	}

	/**
	 * @method  : selectdbList
	 * @desc : db 목록.
	 * @author   : ytkim
	 * @date   : 2018. 1. 23
	 * @param searchParameter
	 * @param allFlag
	 * @return
	 */
	public ResponseResult selectdbList(SearchParameter searchParameter) {
		Page<DBConnectionEntity> result = dbConnectionModelRepository.findAll(
			DBConnectionSpec.mgmtDbList(SecurityUtil.userViewId() , searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter, Sort.by(DBConnectionEntity.VNAME))
		);
		return VarsqlUtils.getResponseResult(result, searchParameter, DBConnectionMapper.INSTANCE);
	}

	/**
	 * @method  : selectUserList
	 * @desc : 사용자 목록
	 * @author   : ytkim
	 * @date   : 2019. 8. 16.
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserList(SearchParameter searchParameter) {
		Page<UserEntity> result = userMgmtRepository.findAll(
			UserSpec.likeUnameOrUid(SecurityUtil.isAdmin(), searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter, Sort.by(UserEntity.UNAME))
		);

		return VarsqlUtils.getResponseResult(result, searchParameter, UserMapper.INSTANCE);
	}
}