package com.varsql.web.app.database;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.common.util.SecurityUtil;
import com.varsql.db.util.DbInstanceFactory;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;

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
	
	public String selectQna(DataCommonVO paramMap) {
		return "";
	}
	
	/**
	 * 
	 * @Method Name  : schemas
	 * @Method 설명 : 스키마 정보보기
	 * @작성일   : 2015. 4. 10. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map schemas(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		
		json.put("urlPrefix", dbMetaEnum.getDBMeta().getUrlPrefix(connid));
		json.put("connInfo", SecurityUtil.userDBInfo(connid));
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
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map serviceMenu(DataCommonVO paramMap) throws Exception {
		try{
			String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
			DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
			List serviceMenu = dbMetaEnum.getDBMeta().getServiceMenu();
			
			Map reval = new HashMap();
			
			reval.put("menuData", serviceMenu);
			
			return reval;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new HashMap();
	}
	
	/**
	 * 
	 * @Method Name  : tables
	 * @Method 설명 : 테이블 정보 보기 
	 * @Method override : @see com.varsql.web.app.database.db2.Db2Service#tables(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 10. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map tables(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, dbMetaEnum.getDBMeta().getTables(connid
				,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		return json;
	}
	
	/**
	 * 
	 * @Method Name  : views
	 * @Method 설명 : view 정보보기
	 * @작성일   : 2015. 4. 10. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map views(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, dbMetaEnum.getDBMeta().getViews(connid,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
		return json;
	}
	
	/**
	 * 
	 * @Method Name  : procedures
	 * @Method 설명 : 프로시저 보기 
	 * @작성일   : 2015. 4. 10. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map procedures(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, dbMetaEnum.getDBMeta().getProcedures(connid,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
		return json;
	}
	
	/**
	 * 
	 * @Method Name  : functions
	 * @Method 설명 : function 보기
	 * @작성일   : 2015. 4. 10. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map functions(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, dbMetaEnum.getDBMeta().getFunctions(connid,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
		return json;
	}
	
	/**
	 * 
	 * @Method Name  : tableMetadata
	 * @Method 설명 : 테이블 메타 정보보기 
	 * @Method override : @see com.varsql.web.app.database.db2.Db2Service#tableMetadata(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 14. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map tableMetadata(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
				dbMetaEnum.getDBMeta().getColumns(connid
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA))
		);
		
		return json;
	}
	
	/**
	 * 
	 * @Method Name  : viewMetadata
	 * @Method 설명 : 뷰 메타 정보보기
	 * @Method override : @see com.varsql.web.app.database.db2.Db2Service#viewMetadata(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 14. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map viewMetadata(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
				dbMetaEnum.getDBMeta().getColumns(connid
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA))
		);
		
		return json;
	}

	public Map procedureMetadata(DataCommonVO paramMap) throws Exception {
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
				dbMetaEnum.getDBMeta().getProceduresMetadata( connid
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME))
		);
		
		return json;
	}

	public Map functionMetadata(DataCommonVO paramMap) throws Exception {
		return null;
	}
	
	/**
	 * 
	 * @Method Name  : ddlTableScript
	 * @Method 설명 : tabel ddl  
	 * @Method override : @see com.varsql.web.app.database.oracle.OracleService#ddlTableScript(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public Map ddlTableScript(DataCommonVO paramMap) throws Exception {
		
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID); 
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getDBMetaImpl(connid);
		
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
				dbMetaEnum.getDDLScript().getTable(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME))
		);
		
		return json;
	}

	public Map ddlViewScript(DataCommonVO paramMap) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	public Map ddlProcedureScript(DataCommonVO paramMap)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Map ddlFunctionScript(DataCommonVO paramMap) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}
	
}