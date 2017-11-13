package com.varsql.web.app.database;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.common.util.SecurityUtil;
import com.varsql.db.util.DbInstanceFactory;
import com.varsql.web.app.database.bean.DatabaseParamInfo;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;
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
		String connid =databaseParamInfo.getVconnid();
		
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		
		json.put("urlPrefix", dbMetaEnum.getDBMeta().getUrlPrefix(connid));
		json.put("connInfo", SecurityUtil.userDBInfo(databaseParamInfo.getConuid()));
		json.put("db_object_list", dbMetaEnum.getDBMeta().getSchemas(connid));
		
		return json;
	}
	
	/**
	 * 
	 * @Method Name  : serviceMenu
	 * @Method 설명 : 메뉴 정보보기 
	 * @Method override : @see com.varsql.web.app.database.db2.Db2Service#serviceMenu(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 10. 
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseResult serviceMenu(DatabaseParamInfo databaseParamInfo) {
		ResponseResult result = new ResponseResult();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(databaseParamInfo.getVconnid());
		result.setItemList(dbMetaEnum.getDBMeta().getServiceMenu());
		return result;
	}
	
	public ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo) {
		String connid =databaseParamInfo.getVconnid();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		try{
			String gubun = databaseParamInfo.getGubun();
			
			if("tables".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getTables(connid
						,databaseParamInfo.getSchema()));
				
			}else if("views".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getViews(connid,databaseParamInfo.getSchema()));
				
			}else if("procedures".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getProcedures(connid,databaseParamInfo.getSchema()));
			}else if("functions".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getFunctions(connid,databaseParamInfo.getSchema()));
			}
		}catch(Exception e){
			logger.error("serviceMenu : ", e);
		}
		
		return result;
	}
	
	public ResponseResult dbObjectMetadataList(DatabaseParamInfo databaseParamInfo) {
		String connid =databaseParamInfo.getVconnid();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(databaseParamInfo.getConuid());
		
		ResponseResult result = new ResponseResult();
		
		try{
			
			String gubun = databaseParamInfo.getGubun();
			if("table".equals(gubun)){	//tableMetadata
				result.setItemList(dbMetaEnum.getDBMeta().getColumns(connid
						,databaseParamInfo.getObjectName()
						,databaseParamInfo.getSchema()));
			}else if("view".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getColumns(connid
						,databaseParamInfo.getObjectName()
						,databaseParamInfo.getSchema()));
			}else if("procedure".equals(gubun)){
				result.setItemList(dbMetaEnum.getDBMeta().getProceduresMetadata( connid
						,databaseParamInfo.getSchema()
						,databaseParamInfo.getObjectName()));
			}else if("function".equals(gubun)){
				result.setItemList(null);
			}
		}catch(Exception e){
			logger.error("serviceMenu : ", e);
		}
		return result;
	}
	
	public ResponseResult createDDL(DatabaseParamInfo databaseParamInfo) {
		String connid =databaseParamInfo.getVconnid();
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(databaseParamInfo.getConuid());
		
		String gubun = databaseParamInfo.getGubun();
		
		ResponseResult result = new ResponseResult();
		
		try{
			if("table".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getTable(connid
						,databaseParamInfo.getSchema()
						,databaseParamInfo.getObjectName()));
			}else if("view".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getView(connid
						,databaseParamInfo.getSchema()
						,databaseParamInfo.getObjectName()));
			}else if("procedure".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getProcedure(connid
						,databaseParamInfo.getSchema()
						,databaseParamInfo.getObjectName()));
			}else if("function".equals(gubun)){
				result.setItemOne(dbMetaEnum.getDDLScript().getFunction(connid
						,databaseParamInfo.getSchema()
						,databaseParamInfo.getObjectName()));
			}
		}catch(Exception e){
			logger.error("createDDL : ", e);
		}
		return result;
	}
}