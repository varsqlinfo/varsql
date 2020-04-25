package com.varsql.web.app.admin.service;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.common.util.StringUtil;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.response.UserResponseDTO;
import com.varsql.web.model.entity.db.DBManagerEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.UserMgmtRepository;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.db.DBManagerEntityRepository;
import com.varsql.web.repository.spec.DBManagerSpec;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ManagerMgmtServiceImpl.java
* @desc		: 매니저 관리
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class ManagerMgmtServiceImpl  extends AbstractService{
	private static final Logger logger = LoggerFactory.getLogger(ManagerMgmtServiceImpl.class);

	@Autowired
	DBConnectionEntityRepository  dbConnectionModelRepository;

	@Autowired
	UserMgmtRepository userMgmtRepository;

	@Autowired
	DBManagerEntityRepository dbManagerRepository;


	/**
	 * @method  : searchRoleList
	 * @desc : auth를  가진 사용자 보기.
	 * @author   : ytkim
	 * @date   : 2020. 4. 21.
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult searchRoleUserList(AuthorityType auth,SearchParameter searchParameter) {

		Page<UserEntity> result = userMgmtRepository.findAll(
			UserSpec.getVnameOrVurl(auth, (searchParameter.getKeyword()))
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter, domainMapper, UserResponseDTO.class);
	}

	/**
	 * @method  : updateManagerRole
	 * @desc : 매니저 role 등록 삭제.
	 * @author   : ytkim
	 * @date   : 2020. 4. 22.
	 * @param mode
	 * @param viewid
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateManagerRole(String mode, String viewid) {
		UserEntity  userInfo = userMgmtRepository.findByViewid(viewid);

		ResponseResult resultObject = new ResponseResult();

		userInfo.setUserRole("add".equals(mode)? AuthorityType.MANAGER.name() : AuthorityType.USER.name());

		userInfo = userMgmtRepository.save(userInfo);

		dbManagerRepository.deleteByViewid(viewid);

		resultObject.setItemOne(userInfo != null ? 1 : 0);

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : selectDatabaseManager
	 * @Method 설명 : 데이타 베이스 매니저
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult searchDatabaseManager(String vconnid) {
		List<DBManagerEntity> dbModelInfo = dbManagerRepository.findAll(DBManagerSpec.vconnid(vconnid));

		List<UserResponseDTO> result =new ArrayList<>();
		dbModelInfo.stream().forEach(item->{
			UserResponseDTO urd = domainMapper.convertToDomain(item.getUserModel(), UserResponseDTO.class);

			result.add(urd);
		});

		return VarsqlUtils.getResponseResultItemList(result);
	}

	/**
	 *
	 * @Method Name  : updateDbManager
	 * @Method 설명 : db 매니저 수정.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateDbManager(String selectItem ,String vconnid, String mode) {
		String[] viewidArr = StringUtil.split(selectItem,",");

		ResponseResult resultObject = new ResponseResult();

		List<DBManagerEntity> addManagerList = new ArrayList<>();
		for(String id: viewidArr){
			addManagerList.add(DBManagerEntity.builder().vconnid(vconnid).viewid(id).build());
        }

		if(addManagerList.size() > 0) {
			if("del".equals(mode)){
				dbManagerRepository.deleteAll(addManagerList);
			}else{
				dbManagerRepository.saveAll(addManagerList);
			}

			resultObject.setItemOne(1);
		}

		return resultObject;
	}
}