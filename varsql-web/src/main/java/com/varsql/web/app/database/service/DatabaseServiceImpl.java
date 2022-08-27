package com.varsql.web.app.database.service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.User;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.exception.DBMetadataException;
import com.varsql.web.common.cache.CacheInfo;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnTabRequestDTO;
import com.varsql.web.dto.db.DBConnTabResponseDTO;
import com.varsql.web.dto.db.DBMetadataRequestDTO;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.dto.user.UserPermissionInfoDTO;
import com.varsql.web.model.entity.db.DBConnHistEntity;
import com.varsql.web.model.entity.db.DBConnTabEntity;
import com.varsql.web.repository.db.DBConnTabEntityRepository;
import com.varsql.web.repository.spec.DBConnTabSpec;
import com.varsql.web.repository.user.UserInfoRepository;
import com.varsql.web.util.DefaultValueUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.sort.TreeDataSort;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : DatabaseServiceImpl.java
 * @Date      : 2014. 8. 18.
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class DatabaseServiceImpl{
	private final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

	@Autowired
	private DBConnTabEntityRepository dbConnTabEntityRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private CommonLogService commonLogService;

	/**
	 *
	 * @Method Name  : schemas
	 * @Method 설명 : 스키마 정보보기
	 * @작성일   : 2015. 4. 10.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Map schemas(PreferencesRequestDTO preferencesRequestDTO) throws SQLException{
		Map json = new HashMap();

		DatabaseInfo dbinfo= SecurityUtil.userDBInfo(preferencesRequestDTO.getConuid());
		
		DBVenderType venderType = DBVenderType.getDBType(dbinfo.getType());

		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(venderType);

		json.put("conuid", preferencesRequestDTO.getConuid());
		json.put("schema", dbinfo.getSchema());
		json.put("type", dbinfo.getType());
		json.put("lazyload", dbinfo.isLazyLoad());
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(dbinfo);
		
		if(venderType.isUseDatabaseName()) {
			json.put("schemaList", dbMetaEnum.getDatabases(dpi));
		}else {
			json.put("schemaList", dbMetaEnum.getSchemas(dpi));
		}
		
		
		json.put("serviceObject", dbMetaEnum.getServiceMenu());

		return json;
	}

	/**
	 *
	 * @Method Name  : serviceMenu
	 * @Method 설명 : 메뉴 정보보기
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 10.
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 * @return
	 * @throws Exception
	 */
	public ResponseResult serviceMenu(DBMetadataRequestDTO dbMetadataRequestDTO) {
		ResponseResult result = new ResponseResult();
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbMetadataRequestDTO.getDbType());
		result.setItemList(dbMetaEnum.getServiceMenu());
		return result;
	}

	/**
	 *
	 * @Method Name  : dbObjectList
	 * @Method 설명 : db object 목록 보기 (table, view 등등)
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 16.
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 * @return
	 * @throws Exception
	 */
	@Cacheable(cacheNames =CacheInfo.CACHE_KEY_OBJECTYPE_METADATA
			, key="@"+CacheInfo.CACHE_KEY_OBJECTYPE_METADATA+".dbObjectTypeKey(#dbMetadataRequestDTO)"
			, condition = "@"+CacheInfo.CACHE_KEY_OBJECTYPE_METADATA+".dbObjectListCondition(#dbMetadataRequestDTO)")
	public ResponseResult dbObjectList(DBMetadataRequestDTO dbMetadataRequestDTO) {

		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbMetadataRequestDTO.getDbType());

		ResponseResult result = new ResponseResult();
		String objectType = dbMetadataRequestDTO.getObjectType();

		try{
			String [] objectNames = dbMetadataRequestDTO.getObjectNames();

			if(ObjectType.TABLE.getObjectTypeId().equals(objectType) && dbMetadataRequestDTO.isLazyLoad()){
				if(dbMetadataRequestDTO.getCustom()!=null && "Y".equals(dbMetadataRequestDTO.getCustom().get("allMetadata"))){
					result.setItemList(dbMetaEnum.getDBObjectMeta(ObjectType.getDBObjectType(objectType).getObjectTypeId(), dbMetadataRequestDTO, objectNames));
				} else {
					result.setItemList(dbMetaEnum.getDBObjectList(ObjectType.getDBObjectType(objectType).getObjectTypeId(),dbMetadataRequestDTO));
				}
			}else{
				result.setItemList(dbMetaEnum.getDBObjectMeta(ObjectType.getDBObjectType(objectType).getObjectTypeId(), dbMetadataRequestDTO, objectNames));
			}
		}catch(Exception e){
			logger.error("dbObjectList objectType : [{}]",objectType);
			logger.error("dbObjectList ", e);

			if(e instanceof DBMetadataException) {
				result.setResultCode(((DBMetadataException)e).getErrorCode());
				result.setMessage(e.getMessage());
			}else {
				try{
					result.setItemList(MetaControlFactory.getDbInstanceFactory(DBVenderType.OTHER).getDBObjectMeta(ObjectType.getDBObjectType(objectType).getObjectTypeId(),dbMetadataRequestDTO));
				}catch(Exception subE){
					logger.error("dbObjectList serverName : [{}]",objectType);
					logger.error("dbObjectList ", subE);
					throw subE;
				}
			}
		}

		return result;
	}

	/**
	 *
	 * @Method Name  : dbObjectMetadataList
	 * @Method 설명 : meta 정보 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 16.
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 * @return
	 */
	public ResponseResult dbObjectMetadataList(DBMetadataRequestDTO dbMetadataRequestDTO) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbMetadataRequestDTO.getDbType());

		ResponseResult result = new ResponseResult();

		try{
			result.setItemList(dbMetaEnum.getDBObjectMeta(ObjectType.getDBObjectType(dbMetadataRequestDTO.getObjectType()).getObjectTypeId(),dbMetadataRequestDTO, dbMetadataRequestDTO.getObjectName()));
		}catch(Exception e){
			logger.error("dbObjectMetadataList : {} ", e.getMessage() , e);
		}
		return result;
	}

	/**
	 *
	 * @Method Name  : createDDL
	 * @Method 설명 : 생성 스크립트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 16.
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 * @return
	 */
	public ResponseResult createDDL(DBMetadataRequestDTO dbMetadataRequestDTO) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbMetadataRequestDTO.getDbType());

		ResponseResult result = new ResponseResult();

		try{
			result.setItemOne(dbMetaEnum.getDDLScript(ObjectType.getDBObjectType( dbMetadataRequestDTO.getObjectType()).getObjectTypeId(),dbMetadataRequestDTO, dbMetadataRequestDTO.getObjectName()));
		}catch(Exception e){
			logger.error("createDDL : ", e);
		}
		return result;
	}

	/**
	 *
	 * @Method Name  : dbInfo
	 * @Method 설명 : db 정보
	 * @작성자   : ytkim
	 * @작성일   : 2018. 10. 8.
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 * @return
	 */
	public ResponseResult dbInfo(DBMetadataRequestDTO dbMetadataRequestDTO) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbMetadataRequestDTO.getDbType());

		ResponseResult result = new ResponseResult();

		try{
			result.setItemList(dbMetaEnum.getDBInfo(dbMetadataRequestDTO));
		}catch(Exception e){
			logger.error("createDDL : ", e);
		}
		return result;
	}

	/**
	 *
	 * @Method Name  : insertDbConnectionHistory
	 * @Method 설명 : db 접속 로그
	 * @작성자   : ytkim
	 * @작성일   : 2019. 9. 21.
	 * @변경이력  :
	 * @param dbMetadataRequestDTO
	 */
	public void insertDbConnectionHistory(PreferencesRequestDTO preferencesRequestDTO) {
		commonLogService.saveDbConnectionHistory(DBConnHistEntity.builder()
				.vconnid(preferencesRequestDTO.getVconnid())
				.viewid(SecurityUtil.userViewId())
				.connTime(DefaultValueUtils.currentTimestamp())
				.reqUrl("main")
				.build());
	}

	/**
	 * @method  : connTabInfo
	 * @desc : conn info 등록.
	 * @author   : ytkim
	 * @date   : 2020. 7. 3.
	 * @param dbMetadataRequestDTO
	 * @return
	 */
	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public ResponseResult connTabInfo(DBConnTabRequestDTO dbConnTabRequestDTO, DataMap param) {

		ResponseResult result = new ResponseResult();
		
		String viewId = SecurityUtil.userViewId();

		String mode = param.getString("mode");

		if("add".equals(mode)){
			DBConnTabEntity connTabInfo = dbConnTabRequestDTO.toEntity();

			dbConnTabEntityRepository.updateConnTabDisable(viewId);
			connTabInfo = dbConnTabEntityRepository.save(connTabInfo);

		}else if("del".equals(mode)){
			DBConnTabEntity dte = dbConnTabEntityRepository.findByViewidAndVconnid(viewId, dbConnTabRequestDTO.getVconnid());

			dbConnTabEntityRepository.updateNextTabPrevVconnid(dte.getVconnid(), dte.getViewid(), dte.getPrevVconnid());
			dbConnTabEntityRepository.delete(dte);
		}else if("view".equals(mode)){
			dbConnTabEntityRepository.updateConnTabDisable(viewId);
			dbConnTabEntityRepository.updateConnTabEnable(dbConnTabRequestDTO.getVconnid(), viewId);
		}else if("moveTab".equals(mode)){
			
			DBConnTabEntity currentTabInfo = dbConnTabEntityRepository.findByViewidAndVconnid(viewId, dbConnTabRequestDTO.getVconnid());
			
			// 이동전 앞에 vconnid 업데이트.
			dbConnTabEntityRepository.updateNextTabPrevVconnid(viewId, dbConnTabRequestDTO.getVconnid(), currentTabInfo.getPrevVconnid());

			if(StringUtils.isBlank(dbConnTabRequestDTO.getPrevVconnid())) {
				dbConnTabEntityRepository.updatePrevIdByVconnid(viewId, dbConnTabRequestDTO.getFirstVconnid(), dbConnTabRequestDTO.getVconnid());
				currentTabInfo.setPrevVconnid(null);
			}else {
				// 이동 할 위치 이전  sqlid 업데이트
				dbConnTabEntityRepository.updateNextTabPrevVconnid(viewId, dbConnTabRequestDTO.getPrevVconnid(), dbConnTabRequestDTO.getVconnid());
							
				currentTabInfo.setPrevVconnid(dbConnTabRequestDTO.getPrevVconnid());
			}
			
			currentTabInfo.setViewYn(true);
			
			dbConnTabEntityRepository.save(currentTabInfo);
		}

		result.setItemOne(dbConnTabRequestDTO);

		return result;
	}

	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public List findTabInfo() {

		List<DBConnTabResponseDTO> resultList = new ArrayList<DBConnTabResponseDTO>();
		try {
			String viewid = SecurityUtil.userViewId();

			List<DBConnTabEntity> tabList = dbConnTabEntityRepository.findAll(DBConnTabSpec.findTabs(viewid));

			TreeDataSort tds = new TreeDataSort(DBConnTabEntity.VCONNID, DBConnTabEntity.PREV_VCONNID);
			tds.sortTreeData(tabList);
			List<DBConnTabEntity> sortList = tds.getSortList();
			
			User user = SecurityUtil.loginInfo();
			Map<String, DatabaseInfo> databaseInfo= user.getDatabaseInfo();
			Map<String,String> vconnidNconuid = user.getVconnidNconuid();

			List<String> notExistsVconnid = new ArrayList<>();
			
			for(DBConnTabEntity item:sortList) {
				String vconnid = item.getVconnid();
				if(vconnidNconuid.containsKey(vconnid)) {
					String conuid = vconnidNconuid.get(vconnid);
					
					resultList.add(DBConnTabResponseDTO.builder()
					.conuid(conuid)
					.name(databaseInfo.get(conuid).getName())
					.prevConuid(vconnidNconuid.get(item.getPrevVconnid()))
					.viewYn(item.isViewYn())
					.build());
				}else {
					notExistsVconnid.add(vconnid);
				}
			}
			
			if(notExistsVconnid.size() > 0) {
				dbConnTabEntityRepository.deleteAllTabInfo(viewid, notExistsVconnid);
			}

		}catch(Exception e) {
			logger.error("findTabInfo : {} ", e.getMessage() , e);
		}

		return resultList;
	}

	public List<UserPermissionInfoDTO> findUserPermission() {
		return userInfoRepository.findPermissionInfo(SecurityUtil.userViewId());
	}
}