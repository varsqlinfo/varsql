package com.varsql.web.app.manager.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.web.app.manager.dao.DbGroupDAO;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.dto.db.DbGroupRequestDTO;
import com.varsql.web.model.entity.db.DBGroupEntity;
import com.varsql.web.model.entity.db.DBGroupMappingDbEntity;
import com.varsql.web.repository.db.DBGroupEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingDbEntityRepository;
import com.varsql.web.repository.spec.DBGroupMappingDbSpec;
import com.varsql.web.repository.spec.DBGroupSpec;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;

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
	DbGroupDAO dbGroupDAO;
	
	@Autowired
	private DBGroupEntityRepository dbGroupEntityRepository;
	
	@Autowired
	private DBGroupMappingDbEntityRepository dbGroupMappingDbEntityRepository;
	
	
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
	 * @param parameter
	 * @return
	 */
	@Transactional
	public ResponseResult deleteDbGroupInfo(ParamMap parameter) {
		ResponseResult result = new ResponseResult();
		
		int resultCnt = dbGroupDAO.deleteDbGroupInfo(parameter);
		
		if(resultCnt > 0) {
			dbGroupDAO.deleteDbGroupNDbMappingInfo(parameter);
			dbGroupDAO.deleteDbGroupNUserMappingInfo(parameter);
		}
		
		result.setItemOne(resultCnt);
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : selectDbGroupMappingList
	 * @Method 설명 : db 그룹 맵핑 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 12. 
	 * @변경이력  :
	 * @param groupId
	 * @return
	 */
	public ResponseResult selectDbGroupMappingList(String groupId) {
		
		List<DBGroupMappingDbEntity> result = dbGroupMappingDbEntityRepository.findAll(DBGroupMappingDbSpec.dbGroupConnList(groupId));
		
		return VarsqlUtils.getResponseResultItemList(result.stream().map(item -> {
			return domainMapper.convertToDomain(item.getConnInfo(), DBConnectionResponseDTO.class); 
		}).collect(Collectors.toList()));
	}
	
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateDbGroupMappingInfo(String selectItem, String groupId, String mode) {
		
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
	 * 
	 * @Method Name  : selectDbGroupUserMappingList
	 * @Method 설명 : db 맵핑 사용자 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDbGroupUserMappingList(DataCommonVO paramMap) {
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(dbGroupDAO.selectDbGroupUserMappingList(paramMap));
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : updateDbGroupUser
	 * @Method 설명 : db그룹 사용자 추가, 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateDbGroupUser(DataCommonVO paramMap) {
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		SecurityUtil.setUserInfo(paramMap);
		
		ResponseResult resultObject = new ResponseResult();
		Map<String,String> addResultInfo = new HashMap<String,String>();
		
		if("del".equals(paramMap.getString("mode"))){
			paramMap.put("viewidArr", viewidArr);
			dbGroupDAO.deleteDbGroupUser(paramMap);
		}else{
			for(String id: viewidArr){
	        	paramMap.put("viewid", id);
	        	try{
	        		dbGroupDAO.updateDbGroupUser(paramMap);
	        		addResultInfo.put(id,ResultConst.CODE.SUCCESS.name());
	        	}catch(Exception e){
	        		addResultInfo.put(id,e.getMessage());
	    		}
	        }
			
			resultObject.setItemOne(addResultInfo);
		}
		
		return resultObject;
	}

	
	
}