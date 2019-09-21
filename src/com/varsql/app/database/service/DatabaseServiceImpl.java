package com.varsql.app.database.service;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.database.dao.DatabaseDAO;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.vartech.common.app.beans.ResponseResult;

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
	private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);
	
	@Autowired
	private DatabaseDAO databaseDAO ;
	/**
	 * 
	 * @Method Name  : schemas
	 * @Method 설명 : 스키마 정보보기
	 * @작성일   : 2015. 4. 10. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @return
	 * @throws Exception
	 */
	public Map schemas(DatabaseParamInfo databaseParamInfo){
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
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
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
	public ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo) {
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		String objectType = databaseParamInfo.getObjectType();
		
		try{
			//System.out.println("databaseParamInfo.getCustom() : "+ databaseParamInfo.getCustom());
			if(DBObjectType.TABLE.getObjectTypeId().equals(objectType) && databaseParamInfo.isLazyLoad()){
				if(databaseParamInfo.getCustom()!=null && "Y".equals(databaseParamInfo.getCustom().get("allMetadata"))){
					result.setItemList(dbMetaEnum.getDBObjectMeta(DBObjectType.getDBObjectType(objectType).getObjectTypeId(),databaseParamInfo));
				} else {
					result.setItemList(dbMetaEnum.getDBObjectList(DBObjectType.getDBObjectType(objectType).getObjectTypeId(),databaseParamInfo));
				}
			}else{
				result.setItemList(dbMetaEnum.getDBObjectMeta(DBObjectType.getDBObjectType(objectType).getObjectTypeId(),databaseParamInfo));
			}
		}catch(Exception e){
			logger.error("dbObjectList objectType : [{}]",objectType);
			logger.error("dbObjectList ", e);
			try{
				result.setItemList(MetaControlBean.OTHER.getDBObjectMeta(DBObjectType.getDBObjectType(objectType).getObjectTypeId(),databaseParamInfo));
			}catch(Exception subE){
				logger.error("dbObjectList serverName : [{}]",objectType);
				logger.error("dbObjectList ", subE);
				throw subE; 
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
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		
		try{
			result.setItemList(dbMetaEnum.getDBObjectMeta(DBObjectType.getDBObjectType(databaseParamInfo.getObjectType()).getObjectTypeId(),databaseParamInfo, databaseParamInfo.getObjectName()));
		}catch(Exception e){
			logger.error("dbObjectMetadataList : ", e);
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
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		
		try{
			result.setItemOne(dbMetaEnum.getDDLScript(DBObjectType.getDBObjectType( databaseParamInfo.getObjectType()).getObjectTypeId(),databaseParamInfo, databaseParamInfo.getObjectName()));
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
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
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
			Map param = new HashMap();
			
			param.put(VarsqlParamConstants.VCONNID, databaseParamInfo.getVconnid());
			param.put(VarsqlParamConstants.VIEWID, databaseParamInfo.getUserid());
			param.put("reqUrl", "main");
			databaseDAO.insertDbConnectionHistory(param);
		}catch(Exception e){
			logger.error("insertDbConnectionHistory : ", e);
		}
	}
}