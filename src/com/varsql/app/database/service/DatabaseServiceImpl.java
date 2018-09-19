package com.varsql.app.database.service;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.app.exception.DatabaseInvalidException;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.vartech.common.app.beans.ResponseResult;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class DatabaseServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);
	
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
	public Map schemas(DatabaseParamInfo databaseParamInfo) throws Exception {
		Map json = new HashMap();
		String connid =databaseParamInfo.getConuid();
		
		try{
			DatabaseInfo  dbinfo= SecurityUtil.userDBInfo(databaseParamInfo.getConuid());
			
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbinfo.getType());
			
			json.put("schema", dbinfo.getSchema());
			json.put("conuid", dbinfo.getConnUUID());
			json.put("type", dbinfo.getType());
			json.put("db_object_list", dbMetaEnum.getSchemas(databaseParamInfo));
		}catch(Exception e){
			logger.error("schemas {}" , e.getMessage());
			throw new DatabaseInvalidException(e.getMessage());
		}
		
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
	public ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo) throws Exception {
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		String gubun = databaseParamInfo.getGubun();
		
		try{
			result.setItemList(dbMetaEnum.getDBMeta(DBObjectType.getDBObjectType(gubun).getObjName(),databaseParamInfo));
		}catch(Exception e){
			logger.error("dbObjectList gubun : [{}]",gubun);
			logger.error("dbObjectList ", e);
			try{
				result.setItemList(MetaControlBean.OTHER.getDBMeta(DBObjectType.getDBObjectType(gubun).getObjName(),databaseParamInfo));
			}catch(Exception subE){
				logger.error("dbObjectList serverName : [{}]",gubun);
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
			result.setItemList(dbMetaEnum.getDBMeta(DBObjectType.getDBObjectType(databaseParamInfo.getGubun()).getObjName(),databaseParamInfo, databaseParamInfo.getObjectName()));
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
			result.setItemOne(dbMetaEnum.getDDLScript(DBObjectType.getDBObjectType( databaseParamInfo.getGubun()).getObjName(),databaseParamInfo, databaseParamInfo.getObjectName()));
		}catch(Exception e){
			logger.error("createDDL : ", e);
		}
		return result;
	}
}