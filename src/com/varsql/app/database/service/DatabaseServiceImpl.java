package com.varsql.app.database.service;
import java.util.HashMap;
import java.util.List;
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
			
			json.put("urlPrefix", dbMetaEnum.getDBMeta().getUrlPrefix(connid));
			json.put("schema", dbinfo.getSchema());
			json.put("conuid", dbinfo.getConnUUID());
			json.put("type", dbinfo.getType());
			json.put("db_object_list", dbMetaEnum.getDBMeta().getSchemas(databaseParamInfo));
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
		result.setItemList(dbMetaEnum.getDBMeta().getServiceMenu());
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
			if(DBObjectType.TABLE.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getTablesAndColumns(databaseParamInfo));
			}else if(DBObjectType.VIEW.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getViewsAndColumns(databaseParamInfo));
			}else if(DBObjectType.PROCEDURE.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getProceduresAndMetadatas(databaseParamInfo));
			}else if(DBObjectType.FUNCTION.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getFunctionsAndMetadatas(databaseParamInfo));
			}else if(DBObjectType.INDEX.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getIndexsAndMetadatas(databaseParamInfo));
			}else if(DBObjectType.TRIGGER.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getTriggers(databaseParamInfo));
			}else if(DBObjectType.SEQUENCE.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getSequences(databaseParamInfo));
			}else{
				result.setItemList((List) dbMetaEnum.getDBMeta().getExtensionService(databaseParamInfo, "gubun", List.class, null));
			}
		}catch(Exception e){
			logger.error("dbObjectList serverName : [{}]",gubun);
			logger.error("dbObjectList ", e);
			try{
				if(DBObjectType.TABLE.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getTablesAndColumns(databaseParamInfo));
				}else if(DBObjectType.VIEW.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getViewsAndColumns(databaseParamInfo));
				}else if(DBObjectType.PROCEDURE.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getProceduresAndMetadatas(databaseParamInfo));
				}else if(DBObjectType.FUNCTION.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getFunctionsAndMetadatas(databaseParamInfo));
				}else if(DBObjectType.INDEX.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getIndexsAndMetadatas(databaseParamInfo));
				}else if(DBObjectType.TRIGGER.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getTriggers(databaseParamInfo));
				}else if(DBObjectType.SEQUENCE.getObjName().equals(gubun)){
					result.setItemList(MetaControlBean.OTHER.getDBMeta().getSequences(databaseParamInfo));
				}else{
					result.setItemList((List) dbMetaEnum.getDBMeta().getExtensionService(databaseParamInfo, "gubun", List.class, null));
				}
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
			String gubun = databaseParamInfo.getGubun();
			if(DBObjectType.TABLE.getObjName().equals(gubun)){	//tableMetadata
				result.setItemList(dbMetaEnum.getDBMeta().getColumns(databaseParamInfo,DBObjectType.TABLE, databaseParamInfo.getObjectName()));
			}else if(DBObjectType.VIEW.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getColumns(databaseParamInfo,DBObjectType.VIEW, databaseParamInfo.getObjectName()) );
			}else if(DBObjectType.PROCEDURE.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getProceduresAndMetadatas(databaseParamInfo,databaseParamInfo.getObjectName()));
			}else if(DBObjectType.FUNCTION.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getFunctionsAndMetadatas(databaseParamInfo,databaseParamInfo.getObjectName()));
			}else if(DBObjectType.INDEX.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getIndexsAndMetadatas(databaseParamInfo,databaseParamInfo.getObjectName()));
			}else if(DBObjectType.SEQUENCE.getObjName().equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getSequences(databaseParamInfo));
			}
		}catch(Exception e){
			logger.error("serviceMenu : ", e);
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
		
		String gubun = databaseParamInfo.getGubun();
		
		ResponseResult result = new ResponseResult();
		
		try{
			if(DBObjectType.TABLE.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getTable(databaseParamInfo));
			}else if(DBObjectType.VIEW.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getView(databaseParamInfo));
			}else if(DBObjectType.PROCEDURE.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getProcedure(databaseParamInfo));
			}else if(DBObjectType.FUNCTION.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getFunction(databaseParamInfo));
			}else if(DBObjectType.INDEX.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getIndex(databaseParamInfo));
			}else if(DBObjectType.TRIGGER.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getTrigger(databaseParamInfo));
			}else if(DBObjectType.SEQUENCE.getObjName().equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getSequence(databaseParamInfo));
			}
		}catch(Exception e){
			logger.error("createDDL : ", e);
		}
		return result;
	}
}