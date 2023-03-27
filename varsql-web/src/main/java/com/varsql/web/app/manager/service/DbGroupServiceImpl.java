package com.varsql.web.app.manager.service;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.dto.db.DBGroupRequestDTO;
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBGroupEntity;
import com.varsql.web.model.entity.db.DBGroupMappingDbEntity;
import com.varsql.web.model.entity.db.DBGroupMappingUserEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.db.DBGroupEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingDbEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingUserEntityRepository;
import com.varsql.web.repository.spec.DBGroupSpec;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.StringUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbGroupServiceImpl.java
* @DESC		: db 그룹 관리.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class DbGroupServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(DbGroupServiceImpl.class);

	@Autowired
	private DBGroupEntityRepository dbGroupEntityRepository;

	@Autowired
	private DBGroupMappingDbEntityRepository dbGroupMappingDbEntityRepository;

	@Autowired
	private DBGroupMappingUserEntityRepository dbGroupMappingUserEntityRepository;

	/**
	 *
	 * @Method Name  : selectUserList
	 * @Method 설명 : 사용자 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectDbGroupList(SearchParameter searchParameter) {

		Page<DBGroupEntity> result = dbGroupEntityRepository.findAll(
			DBGroupSpec.searchKeyWord(searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter);
	}

	/**
	 * @method  : saveDbGroupInfo
	 * @desc : 저장
	 * @author   : ytkim
	 * @date   : 2018. 7. 19.
	 * @param dbGroupInfo
	 * @return
	 */
	public ResponseResult saveDbGroupInfo(DBGroupRequestDTO dbGroupInfo) {
		DBGroupEntity entity = dbGroupInfo.toEntity();
		dbGroupEntityRepository.save(entity);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : deleteDbGroupInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19.
	 * @변경이력  :
	 * @param groupId
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteDbGroupInfo(String groupId) {
		
		DBGroupEntity entity = dbGroupEntityRepository.findByGroupId(groupId);
		
		if(entity==null) {
			return ResponseResult.builder().item(false).resultCode(RequestResultCode.NOT_FOUND).message("db group not found [id : "+ groupId+"]").build();
		}

		dbGroupEntityRepository.deleteByGroupId(groupId);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 * @method  : groupNDbMappingList
	 * @desc : 그룹 에 맵핑된 db 목록.
	 * @author   : ytkim
	 * @date   : 2019. 8. 12.
	 * @param groupId
	 * @param searchParameter 
	 * @return
	 */
	public ResponseResult groupNDbMappingList(String groupId, SearchParameter searchParameter) {

		Page<DBConnectionResponseDTO> result = dbGroupMappingDbEntityRepository.findByDbGroupDbList(groupId, searchParameter, VarsqlUtils.convertSearchInfoToPage(searchParameter));

		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements() ,searchParameter);
	}

	/**
	 * @method  : updateGroupNDbMappingInfo
	 * @desc : 그룹  db 맵핑 정보 업데이트.
	 * @author   : ytkim
	 * @date   : 2020. 5. 2.
	 * @param selectItem
	 * @param groupId
	 * @param mode
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateGroupNDbMappingInfo(String selectItem, String groupId, String mode) {

		logger.info("mode :{}, groupId :{}, selectItem : {} ", mode, groupId, selectItem);
		
		DBGroupEntity entity = dbGroupEntityRepository.findByGroupId(groupId);
		
		if(entity==null) {
			return ResponseResult.builder().item(false).resultCode(RequestResultCode.NOT_FOUND).message("db group not found [id : "+ groupId+"]").build();
		}

		String[] vconnidArr = StringUtils.split(selectItem,",");

		List<DBGroupMappingDbEntity> dbConnList = new ArrayList<>();
		for(String id: vconnidArr){
			dbConnList.add(DBGroupMappingDbEntity.builder()
				.groupId(groupId)
				.vconnid(id)
				.groupDbEntity(DBGroupEntity.builder().groupId(groupId).build())
				.connInfo(DBConnectionEntity.builder().vconnid(id).build())
				.build()
			);
        }

		if(dbConnList.size() > 0) {
			if(dbConnList.size() ==1) {
				if("del".equals(mode)){
					dbGroupMappingDbEntityRepository.delete(dbConnList.get(0));
				}else{
					dbGroupMappingDbEntityRepository.save(dbConnList.get(0));
				}
			}else {
				if("del".equals(mode)){
					dbGroupMappingDbEntityRepository.deleteAll(dbConnList);
				}else{
					dbGroupMappingDbEntityRepository.saveAll(dbConnList);
				}
			}
		}
		
		return VarsqlUtils.getResponseResultItemOne(true);
	}

	/**
	 * @method  : groupNUserMappingList
	 * @desc : db그룹  사용자 목록.
	 * @author   : ytkim
	 * @date   : 2018. 1. 23.
	 * @param groupId
	 * @param searchParameter 
	 * @return
	 */
	public ResponseResult groupNUserMappingList(String groupId, SearchParameter searchParameter) {

		Page<UserResponseDTO> result  = dbGroupMappingUserEntityRepository.findByDbGroupUserList(groupId, searchParameter, VarsqlUtils.convertSearchInfoToPage(searchParameter));

		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements() ,searchParameter);

	}

	/**
	 * @method  : updateGroupNUserMappingInfo
	 * @desc : db그룹 사용자 추가, 삭제.
	 * @author   : ytkim
	 * @date   : 2019. 8. 16.
	 * @param selectItem
	 * @param groupId
	 * @param mode
	 * @param searchParameter 
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateGroupNUserMappingInfo(String selectItem, String groupId, String mode) {
		logger.info("updateGroupNUserMappingInfo  mode :{}, groupId :{} ,selectItem : {} ",mode, groupId, selectItem);
		
		DBGroupEntity entity = dbGroupEntityRepository.findByGroupId(groupId);
		
		if(entity==null) {
			return ResponseResult.builder().item(false).resultCode(RequestResultCode.NOT_FOUND).message("project not found id : "+ groupId).build();
		}
		entity = null; 
		
		String[] vconnidArr = StringUtils.split(selectItem,",");

		List<DBGroupMappingUserEntity> dbGroupUserList = new ArrayList<>();
		for(String id: vconnidArr){
			dbGroupUserList.add(DBGroupMappingUserEntity.builder()
				.groupId(groupId).viewid(id)
				.groupUserEntity(DBGroupEntity.builder().groupId(groupId).build())
				.userInfo(UserEntity.builder().viewid(id).build())
				.build()
			);
        }

		if(dbGroupUserList.size() > 0) {
			
			if(dbGroupUserList.size() ==1) {
				if("del".equals(mode)){
					dbGroupMappingUserEntityRepository.delete(dbGroupUserList.get(0));
				}else{
					dbGroupMappingUserEntityRepository.save(dbGroupUserList.get(0));
				}
			}else {
				if("del".equals(mode)){
					dbGroupMappingUserEntityRepository.deleteAll(dbGroupUserList);
				}else{
					dbGroupMappingUserEntityRepository.saveAll(dbGroupUserList);
				}
			}
			
			
			
		}
		
		return VarsqlUtils.getResponseResultItemOne(true);
	}

}