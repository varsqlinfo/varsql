package com.varsql.web.app.manager.service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.StringUtil;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.dto.db.DbGroupRequestDTO;
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.db.DBGroupEntity;
import com.varsql.web.model.entity.db.DBGroupMappingDbEntity;
import com.varsql.web.model.entity.db.DBGroupMappingUserEntity;
import com.varsql.web.repository.db.DBGroupEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingDbEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingUserEntityRepository;
import com.varsql.web.repository.spec.DBGroupMappingDbSpec;
import com.varsql.web.repository.spec.DBGroupMappingUserSpec;
import com.varsql.web.repository.spec.DBGroupSpec;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

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
	private static final Logger logger = LoggerFactory.getLogger(DbGroupServiceImpl.class);
	
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
	public ResponseResult saveDbGroupInfo(DbGroupRequestDTO dbGroupInfo) {
		DBGroupEntity entity = dbGroupInfo.toEntity();
		entity = dbGroupEntityRepository.save(entity);
		
		return VarsqlUtils.getResponseResultItemOne(entity != null? 1 : 0);
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
		
		dbGroupEntityRepository.deleteByGroupId(groupId);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 * @method  : groupNDbMappingList
	 * @desc : 그룹 에 맵핑된 db 목록. 
	 * @author   : ytkim
	 * @date   : 2019. 8. 12. 
	 * @param groupId
	 * @return
	 */
	public ResponseResult groupNDbMappingList(String groupId) {
		
		List<DBGroupMappingDbEntity> result = dbGroupMappingDbEntityRepository.findAll(DBGroupMappingDbSpec.dbGroupConnList(groupId));
		
		return VarsqlUtils.getResponseResultItemList(result.stream().map(item -> {
			return domainMapper.convertToDomain(item.getConnInfo(), DBConnectionResponseDTO.class); 
		}).collect(Collectors.toList()));
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
		
		logger.info("updateDbGroupMappingInfo  mode :{}, groupId :{} ,selectItem : {} ",mode, groupId, selectItem);
		
		String[] vconnidArr = StringUtil.split(selectItem,",");
		
		List<DBGroupMappingDbEntity> dbConnList = new ArrayList<>();
		for(String id: vconnidArr){
			dbConnList.add(DBGroupMappingDbEntity.builder().groupId(groupId).vconnid(id).build());
        }
		
		int result = 0;
		if(dbConnList.size() > 0) {
			if("del".equals(mode)){
				dbGroupMappingDbEntityRepository.deleteAll(dbConnList);
			}else{
				dbGroupMappingDbEntityRepository.saveAll(dbConnList);
			}
			result = 1;
		}
		return VarsqlUtils.getResponseResultItemOne(result);
	}
	
	/**
	 * @method  : groupNUserMappingList
	 * @desc : db그룹  사용자 목록.
	 * @author   : ytkim
	 * @date   : 2018. 1. 23. 
	 * @param groupId
	 * @return
	 */
	public ResponseResult groupNUserMappingList(String groupId) {
		
		List<DBGroupMappingUserEntity> result = dbGroupMappingUserEntityRepository.findAll(DBGroupMappingUserSpec.dbGroupUserList(groupId));
		
		return VarsqlUtils.getResponseResultItemList(result.stream().map(item -> {
			return domainMapper.convertToDomain(item.getUserInfo(), UserResponseDTO.class); 
		}).collect(Collectors.toList()));
		
	}
	
	/**
	 * @method  : updateGroupNUserMappingInfo
	 * @desc : db그룹 사용자 추가, 삭제.
	 * @author   : ytkim
	 * @date   : 2019. 8. 16. 
	 * @param selectItem
	 * @param groupId
	 * @param mode
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateGroupNUserMappingInfo(String selectItem, String groupId, String mode) {
		logger.info("updateGroupNUserMappingInfo  mode :{}, groupId :{} ,selectItem : {} ",mode, groupId, selectItem);
		
		String[] vconnidArr = StringUtil.split(selectItem,",");
		
		List<DBGroupMappingUserEntity> dbGroupUserList = new ArrayList<>();
		for(String id: vconnidArr){
			dbGroupUserList.add(DBGroupMappingUserEntity.builder().groupId(groupId).viewid(id).build());
        }
		
		int result = 0;
		if(dbGroupUserList.size() > 0) {
			if("del".equals(mode)){
				dbGroupMappingUserEntityRepository.deleteAll(dbGroupUserList);
			}else{
				dbGroupMappingUserEntityRepository.saveAll(dbGroupUserList);
			}
			result = 1;
		}
		return VarsqlUtils.getResponseResultItemOne(result);
	}
	
}