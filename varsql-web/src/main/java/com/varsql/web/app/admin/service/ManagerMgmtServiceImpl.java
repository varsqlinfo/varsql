package com.varsql.web.app.admin.service;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.AuthorityType;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBManagerEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.mapper.user.UserMapper;
import com.varsql.web.repository.db.DBManagerEntityRepository;
import com.varsql.web.repository.spec.DBManagerSpec;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.StringUtils;

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
	private final Logger logger = LoggerFactory.getLogger(ManagerMgmtServiceImpl.class);

	@Autowired
	private UserMgmtRepository userMgmtRepository;

	@Autowired
	private DBManagerEntityRepository dbManagerRepository;


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
			UserSpec.likeUnameOrUid(auth, (searchParameter.getKeyword()))
			, VarsqlUtils.convertSearchInfoToPage(searchParameter, Sort.by(UserEntity.SORT_UNAME))
		);

		return VarsqlUtils.getResponseResult(result, searchParameter, UserMapper.INSTANCE);
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

		logger.info("updateManagerRole  mode : {} , viewid : {} ",mode,viewid);

		UserEntity  userInfo = userMgmtRepository.findByViewid(viewid);

		userInfo.setUserRole("add".equals(mode)? AuthorityType.MANAGER.name() : AuthorityType.USER.name());

		userInfo = userMgmtRepository.save(userInfo);

		if(!"add".equals(mode)) {
			dbManagerRepository.deleteByViewid(viewid);
		}

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : selectDatabaseManager
	 * @Method 설명 : 데이터 베이스 매니저
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23.
	 * @변경이력  :
	 * @param vconnid
	 * @return
	 */
	public ResponseResult findDatabaseManager(String vconnid) {
		List<DBManagerEntity> dbModelInfo = dbManagerRepository.findAll(DBManagerSpec.findAllVconnidManager(vconnid), Sort.by(DBManagerEntity.SORT_USERINFO) );

		List<UserResponseDTO> result =new ArrayList<>();
		dbModelInfo.stream().forEach(item->{
			result.add(UserMapper.INSTANCE.toDto(item.getUser()));
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
	 * @param selectItem
	 * @param vconnid
	 * @param mode
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateDbManager(String selectItem ,String vconnid, String mode) {
		logger.info(" mode :{}, vconnid :{} ,viewid : {} ",mode, vconnid, selectItem);

		String[] viewidArr = StringUtils.split(selectItem,",");

		List<DBManagerEntity> addManagerList = new ArrayList<>();
		for(String id: viewidArr){
			addManagerList.add(
				DBManagerEntity.builder().vconnid(vconnid).viewid(id)
				.user(UserEntity.builder().viewid(id).build())
				.dbConnInfo(DBConnectionEntity.builder().vconnid(vconnid).build())
				.build()
			);
        }

		int result = 0;
		if(addManagerList.size() > 0) {
			if("del".equals(mode)){
				dbManagerRepository.deleteAll(addManagerList);
			}else{
				dbManagerRepository.saveAll(addManagerList);
			}
			result = 1;
		}

		return findDatabaseManager(vconnid);
	}
}