package com.varsql.app.database.service;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.app.util.VarsqlUtil;
import com.varsql.common.util.SecurityUtil;
import com.varsql.db.util.DbInstanceFactory;
import com.varsql.db.beans.DatabaseInfo;
import  com.varsql.db.beans.DatabaseParamInfo;
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
		
		
		DatabaseInfo  dbinfo= SecurityUtil.userDBInfo(databaseParamInfo.getConuid());
		
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDbInstanceFactory(dbinfo.getType());
		
		json.put("urlPrefix", dbMetaEnum.getDBMeta().getUrlPrefix(connid));
		json.put("schema", dbinfo.getSchema());
		json.put("conuid", dbinfo.getConnUUID());
		json.put("type", dbinfo.getType());
		json.put("db_object_list", dbMetaEnum.getDBMeta().getSchemas(databaseParamInfo));
		
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
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getConnidToDbInstanceFactory(databaseParamInfo.getVconnid());
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
	 */
	public ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo) {
		String connid =databaseParamInfo.getVconnid();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		try{
			String gubun = databaseParamInfo.getGubun();
			
			if("tables".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getTables(databaseParamInfo));
				
			}else if("views".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getViews(databaseParamInfo));
				
			}else if("procedures".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getProcedures(databaseParamInfo));
			}else if("functions".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getFunctions(databaseParamInfo));
			}
		}catch(Exception e){
			logger.error("serviceMenu : ", e);
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
		String connid =databaseParamInfo.getVconnid();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		
		try{
			
			String gubun = databaseParamInfo.getGubun();
			if("table".equals(gubun)){	//tableMetadata
				result.setItemList(dbMetaEnum.getDBMeta().getColumns(databaseParamInfo , databaseParamInfo.getObjectName()));
			}else if("view".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getColumns(databaseParamInfo	,databaseParamInfo.getObjectName()) );
			}else if("procedure".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getProceduresMetadata( databaseParamInfo,databaseParamInfo.getObjectName()));
			}else if("function".equals(gubun)){
				result.setItemList(null);
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
		String connid =databaseParamInfo.getVconnid();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		
		String gubun = databaseParamInfo.getGubun();
		
		ResponseResult result = new ResponseResult();
		
		try{
			if("table".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getTable(databaseParamInfo	,databaseParamInfo.getObjectName()));
			}else if("view".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getView(databaseParamInfo,databaseParamInfo.getObjectName()));
			}else if("procedure".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getProcedure(databaseParamInfo	,databaseParamInfo.getObjectName()));
			}else if("function".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getFunction(databaseParamInfo,databaseParamInfo.getObjectName()));
			}
		}catch(Exception e){
			logger.error("createDDL : ", e);
		}
		return result;
	}
}