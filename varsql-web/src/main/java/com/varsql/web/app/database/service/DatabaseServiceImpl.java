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
import com.varsql.core.db.DBType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.exception.DBMetadataException;
import com.varsql.web.common.cache.CacheInfo;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnTabRequestDTO;
import com.varsql.web.dto.db.DBConnTabResponseDTO;
import com.varsql.web.dto.user.UserPermissionInfoDTO;
import com.varsql.web.model.entity.db.DBConnHistEntity;
import com.varsql.web.model.entity.db.DBConnTabEntity;
import com.varsql.web.repository.db.DBConnHistEntityRepository;
import com.varsql.web.repository.db.DBConnTabEntityRepository;
import com.varsql.web.repository.user.UserInfoRepository;
import com.varsql.web.util.DefaultValueUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.sort.TreeDataSort;

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
	private DBConnHistEntityRepository dbConnHistEntityRepository;

	@Autowired
	private DBConnTabEntityRepository dbConnTabEntityRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	/**
	 *
	 * @Method Name  : schemas
	 * @Method 설명 : 스키마 정보보기
	 * @작성일   : 2015. 4. 10.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Map schemas(DatabaseParamInfo databaseParamInfo) throws SQLException{
		Map json = new HashMap();
		String connid =databaseParamInfo.getConuid();

		DatabaseInfo dbinfo= SecurityUtil.userDBInfo(databaseParamInfo.getConuid());

		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbinfo.getType());

		json.put("schema", dbinfo.getSchema());
		json.put("conuid", dbinfo.getConnUUID());
		json.put("type", dbinfo.getType());
		json.put("lazyload", dbinfo.isLazyLoad());
		json.put("schemaList", dbMetaEnum.getSchemas(databaseParamInfo));
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
	 * @param databaseParamInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseResult serviceMenu(DatabaseParamInfo databaseParamInfo) {
		ResponseResult result = new ResponseResult();
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(databaseParamInfo.getDbType());
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
	 * @param databaseParamInfo
	 * @return
	 * @throws Exception
	 */
	@Cacheable(cacheNames =CacheInfo.CACHE_KEY_OBJECTYPE_METADATA
			, key="@"+CacheInfo.CACHE_KEY_OBJECTYPE_METADATA+".dbObjectTypeKey(#databaseParamInfo)"
			, condition = "@"+CacheInfo.CACHE_KEY_OBJECTYPE_METADATA+".dbObjectListCondition(#databaseParamInfo)")
	public ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo) {

		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(databaseParamInfo.getDbType());

		ResponseResult result = new ResponseResult();
		String objectType = databaseParamInfo.getObjectType();

		try{
			String [] objectNames = databaseParamInfo.getObjectNames();

			if(ObjectType.TABLE.getObjectTypeId().equals(objectType) && databaseParamInfo.isLazyLoad()){
				if(databaseParamInfo.getCustom()!=null && "Y".equals(databaseParamInfo.getCustom().get("allMetadata"))){
					result.setItemList(dbMetaEnum.getDBObjectMeta(ObjectType.getDBObjectType(objectType).getObjectTypeId(), databaseParamInfo, objectNames));
				} else {
					result.setItemList(dbMetaEnum.getDBObjectList(ObjectType.getDBObjectType(objectType).getObjectTypeId(),databaseParamInfo));
				}
			}else{
				result.setItemList(dbMetaEnum.getDBObjectMeta(ObjectType.getDBObjectType(objectType).getObjectTypeId(), databaseParamInfo, objectNames));
			}
		}catch(Exception e){
			logger.error("dbObjectList objectType : [{}]",objectType);
			logger.error("dbObjectList ", e);

			if(e instanceof DBMetadataException) {
				result.setResultCode(((DBMetadataException)e).getErrorCode());
				result.setMessage(e.getMessage());
			}else {
				try{
					result.setItemList(MetaControlFactory.getDbInstanceFactory(DBType.OTHER).getDBObjectMeta(ObjectType.getDBObjectType(objectType).getObjectTypeId(),databaseParamInfo));
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
	 * @param databaseParamInfo
	 * @return
	 */
	public ResponseResult dbObjectMetadataList(DatabaseParamInfo databaseParamInfo) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(databaseParamInfo.getDbType());

		ResponseResult result = new ResponseResult();

		try{
			result.setItemList(dbMetaEnum.getDBObjectMeta(ObjectType.getDBObjectType(databaseParamInfo.getObjectType()).getObjectTypeId(),databaseParamInfo, databaseParamInfo.getObjectName()));
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
	 * @param databaseParamInfo
	 * @return
	 */
	public ResponseResult createDDL(DatabaseParamInfo databaseParamInfo) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(databaseParamInfo.getDbType());

		ResponseResult result = new ResponseResult();

		try{
			result.setItemOne(dbMetaEnum.getDDLScript(ObjectType.getDBObjectType( databaseParamInfo.getObjectType()).getObjectTypeId(),databaseParamInfo, databaseParamInfo.getObjectName()));
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
	 * @param databaseParamInfo
	 * @return
	 */
	public ResponseResult dbInfo(DatabaseParamInfo databaseParamInfo) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(databaseParamInfo.getDbType());

		ResponseResult result = new ResponseResult();

		try{
			result.setItemList(dbMetaEnum.getDBInfo(databaseParamInfo));
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
	 * @param databaseParamInfo
	 */
	public void insertDbConnectionHistory(DatabaseParamInfo databaseParamInfo) {
		try{
			dbConnHistEntityRepository.save(DBConnHistEntity.builder()
				.vconnid(databaseParamInfo.getVconnid())
				.viewid(databaseParamInfo.getViewid())
				.connTime(DefaultValueUtils.currentTimestamp())
				.reqUrl("main")
				.build()
			);
		}catch(Exception e){
			logger.error("insertDbConnectionHistory : ", e);
		}
	}

	/**
	 * @method  : connTabInfo
	 * @desc : conn info 등록.
	 * @author   : ytkim
	 * @date   : 2020. 7. 3.
	 * @param databaseParamInfo
	 * @return
	 */
	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public ResponseResult connTabInfo(DBConnTabRequestDTO dbConnTabRequestDTO) {

		ResponseResult result = new ResponseResult();

		String mode = String.valueOf(dbConnTabRequestDTO.getCustom().get("mode"));

		if("add".equals(mode)){
			DBConnTabEntity connTabInfo = dbConnTabRequestDTO.toEntity();

			dbConnTabEntityRepository.updateConnTabDisable(dbConnTabRequestDTO.getViewid());
			connTabInfo = dbConnTabEntityRepository.save(connTabInfo);

		}else if("del".equals(mode)){
			DBConnTabEntity dte = dbConnTabEntityRepository.findByViewidAndVconnid(dbConnTabRequestDTO.getViewid(), dbConnTabRequestDTO.getVconnid());

			dbConnTabEntityRepository.updateNextTabPrevVconnid(dbConnTabRequestDTO.getVconnid(), dbConnTabRequestDTO.getViewid() , dte.getPrevVconnid());

			dbConnTabEntityRepository.deleteConnTabInfo(dbConnTabRequestDTO.getVconnid(), dbConnTabRequestDTO.getViewid());
		}else if("view".equals(mode)){
			dbConnTabEntityRepository.updateConnTabDisable(dbConnTabRequestDTO.getViewid());
			dbConnTabEntityRepository.updateConnTabEnable(dbConnTabRequestDTO.getVconnid(), dbConnTabRequestDTO.getViewid());
		}

		result.setItemOne(dbConnTabRequestDTO);

		return result;
	}

	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public List findTabInfo() {
		TreeDataSort tds = new TreeDataSort("conuid", "prevConuid");

		try {
			String viewid = SecurityUtil.userViewId();

			List<DBConnTabEntity> tabList = dbConnTabEntityRepository.findAllByViewid(viewid);

			User user = SecurityUtil.loginInfo();
			Map<String, DatabaseInfo> databaseInfo= user.getDatabaseInfo();
			Map<String,String> vconnidNconuid = user.getVconnidNconuid();

			List<String> notExistsVconnid = new ArrayList<>();

			tabList.forEach(item ->{
				String vconnid = item.getVconnid();
				if(vconnidNconuid.containsKey(vconnid)) {
					String conuid = vconnidNconuid.get(vconnid);
					DatabaseInfo di= databaseInfo.get(conuid);

					tds.sortTreeData(
						DBConnTabResponseDTO.builder()
						.conuid(conuid)
						.name(di.getName())
						.prevConuid(vconnidNconuid.get(item.getPrevVconnid()))
						.viewYn(item.isViewYn())
						.build()
					);
				}else {
					notExistsVconnid.add(vconnid);
				}
			});

			if(notExistsVconnid.size() > 0) {
				dbConnTabEntityRepository.deleteAllTabInfo(viewid, notExistsVconnid);
			}

		}catch(Exception e) {
			logger.error("findTabInfo : {} ", e.getMessage() , e);
		}
		List sortList = tds.getSortList();

		return sortList ==null? new ArrayList() :sortList;
	}

	public List<UserPermissionInfoDTO> findUserPermission() {
		return userInfoRepository.findPermissionInfo(SecurityUtil.userViewId());
	}
}