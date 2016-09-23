package com.varsql.web.app.database.db2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.varsql.db.meta.DBMetaImplDB2;
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
public class Db2ServiceImpl implements Db2Service{
	private static final Logger logger = LoggerFactory.getLogger(Db2ServiceImpl.class);
	
	@Autowired
	Db2DAO db2DAO ;
	
	DBMetaImplDB2 db2metaImpl = new DBMetaImplDB2();

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
		json.put("urlPrefix", db2metaImpl.getUrlPrefix(connid));
		json.put("db_object_list", db2metaImpl.getSchemas(connid));
		
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
			List serviceMenu = db2metaImpl.getServiceMenu();
			
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
		json.put(VarsqlParamConstants.JSON_REUSLT, db2metaImpl.getTables(paramMap.getString(VarsqlParamConstants.VCONNID) ,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
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
		json.put(VarsqlParamConstants.JSON_REUSLT, db2metaImpl.getViews(paramMap.getString(VarsqlParamConstants.VCONNID),paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
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
		json.put(VarsqlParamConstants.JSON_REUSLT, db2metaImpl.getProcedures(paramMap.getString(VarsqlParamConstants.VCONNID),paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
		
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
			json.put(VarsqlParamConstants.JSON_REUSLT, db2metaImpl.getFunctions(paramMap.getString(VarsqlParamConstants.VCONNID),paramMap.getString(VarsqlParamConstants.DB_SCHEMA)));
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
			db2metaImpl.getColumns(paramMap.getString(VarsqlParamConstants.VCONNID)
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
			db2metaImpl.getColumns(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA))
		);
		
		System.out.println("------------------------");
		System.out.println("paramMap : "+ paramMap);
		System.out.println("------------------------");
		
		return VarsqlUtil.objectToString(json);
	}

	public String procedureMetadata(DataCommonVO paramMap) throws Exception {
		Map json = new HashMap();
		json.put(VarsqlParamConstants.JSON_REUSLT, 
			db2metaImpl.getProceduresMetadata(paramMap.getString(VarsqlParamConstants.VCONNID)
					,paramMap.getString(VarsqlParamConstants.DB_SCHEMA)
					,paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME))
		);
		
		System.out.println("------------------------");
		System.out.println("paramMap : "+ paramMap);
		System.out.println("------------------------");
		return VarsqlUtil.objectToString(json);
	}

	public String functionMetadata(DataCommonVO paramMap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}