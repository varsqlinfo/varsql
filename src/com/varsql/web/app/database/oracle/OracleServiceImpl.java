package com.varsql.web.app.database.oracle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.db.meta.DBMetaImplORACLE;
import com.varsql.sql.ddl.script.DDLScriptAbstract;
import com.varsql.sql.ddl.script.DDLScriptImplOracle;
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
public class OracleServiceImpl implements OracleService{
	private static final Logger logger = LoggerFactory.getLogger(OracleServiceImpl.class);
	
	@Autowired
	OracleDAO oracleDAO ;
	
	DBMetaImplORACLE oracleMetaImpl = new DBMetaImplORACLE();
	DDLScriptAbstract ddlScriptImplOracle = new DDLScriptImplOracle();

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
	public String schemas(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		
		String connid =paramMap.getString(VarsqlParamConstants.VCONNID);
		
		json.put("urlPrefix", oracleMetaImpl.getUrlPrefix(connid));
		json.put("db_object_list", oracleMetaImpl.getSchemas(connid));
		
		return VarsqlUtil.objectToString(json);
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
	public String serviceMenu(DataCommonVO paramMap) throws Exception {
		try{
			List serviceMenu = oracleMetaImpl.getServiceMenu();
			
			Map reval = new HashMap();
			
			reval.put("menuData", serviceMenu);
			
			return VarsqlUtil.objectToString(reval);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
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
	public String tables(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, oracleMetaImpl.getTables(paramMap.getString(VarsqlParamConstants.VCONNID) ,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
		return VarsqlUtil.objectToString(json);
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
	public String views(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, oracleMetaImpl.getViews(paramMap.getString(VarsqlParamConstants.VCONNID),paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
		return VarsqlUtil.objectToString(json);
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
	public String procedures(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, oracleMetaImpl.getProcedures(paramMap.getString(VarsqlParamConstants.VCONNID),paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
		return VarsqlUtil.objectToString(json);
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
	public String functions(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		try{
			json.put(VarsqlParamConstants.JSON_REUSLT, oracleMetaImpl.getFunctions(paramMap.getString(VarsqlParamConstants.VCONNID),paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		}catch(Exception e){
			e.printStackTrace();
		}
		return VarsqlUtil.objectToString(json);
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
	public String tableMetadata(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
			oracleMetaImpl.getColumns(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA))
		);
		
		return VarsqlUtil.objectToString(json);
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
	public String viewMetadata(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
			oracleMetaImpl.getColumns(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA))
		);
		
		return VarsqlUtil.objectToString(json);
	}
	
	/**
	 * 
	 * @Method Name  : procedureMetadata
	 * @Method 설명 : 프로시저 메타 데이타. 
	 * @Method override : @see com.varsql.web.app.database.oracle.OracleService#procedureMetadata(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String procedureMetadata(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
			oracleMetaImpl.getProceduresMetadata(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME))
		);
		
		return VarsqlUtil.objectToString(json);
	}
	
	public String functionMetadata(DataCommonVO paramMap) throws Exception {
		// TODO Auto-generated method stub
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
	public String ddlTableScript(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
				ddlScriptImplOracle.getTable(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME))
		);
		
		return VarsqlUtil.objectToString(json);
	}

	public String ddlViewScript(DataCommonVO paramMap) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	public String ddlProcedureScript(DataCommonVO paramMap)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String ddlFunctionScript(DataCommonVO paramMap) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}