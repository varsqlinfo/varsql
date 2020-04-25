package com.varsql.core.db;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.JdbcConstants;
import com.varsql.core.db.ddl.script.DDLScriptImpl;
import com.varsql.core.db.meta.DBMetaImpl;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.report.table.TableReportImpl;
import com.varsql.core.db.resultset.meta.handler.ResultSetMetaHandlerImpl;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.exception.VarsqlMethodNotFoundException;
import com.varsql.core.sql.resultset.handler.ResultSetHandler;
import com.varsql.core.sql.resultset.handler.ResultSetHandlerImpl;
import com.vartech.common.utils.StringUtil;
import com.vartech.common.utils.VartechReflectionUtils;

/**
 * 
 * @FileName  : MetaControlBean.java
 * @프로그램 설명 : 메타 데이타 control bean
 * @Date      : 2018. 6. 11. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class MetaControlBean {
	
	private DBMetaImpl dbMetaImpl;
	private DDLScriptImpl ddlScriptImpl;
	private ResultSetHandler resultSetHandler;
	private DataTypeImpl dataTypeImpl;
	private TableReportImpl tableReportImpl;
	
	private ResultSetMetaHandlerImpl resultSetMetaHandlerImpl;
	
	private static Logger logger;
	
	private String dbVenderName;

	public MetaControlBean(String db){
		
		this.dbVenderName =db; 
		// datatype load
		
		try {
			this.dataTypeImpl=(DataTypeImpl)getBeanObject(DataTypeImpl.class, "DataType");
		} catch (Exception e) {
			logger().info("DbInstanceFactory dataTypeImpl ",e);
		}
				
		// meta load
		try {
			this.dbMetaImpl=(DBMetaImpl)getBeanObject(DBMetaImpl.class, "DBMeta");
		} catch (Exception e) {
			logger().info("DbInstanceFactory dbMetaImpl ",e);
		}
		
		// script object load
		try {
			this.ddlScriptImpl=(DDLScriptImpl)getBeanObject(DDLScriptImpl.class, "DDLScript");
		} catch (Exception e) {
			logger().info("DbInstanceFactory ddlScriptImpl ",e);
		}
		
		//result set handler
		try {
			this.resultSetHandler=(ResultSetHandlerImpl)getBeanObject(ResultSetHandlerImpl.class, "ResultSetHandler");
		} catch (Exception e) {
			logger().info("DbInstanceFactory ResultsetHandler ",e);
		}
		
		
		// result set meata handler load
		try {
			this.resultSetMetaHandlerImpl=(ResultSetMetaHandlerImpl)getBeanObject(ResultSetMetaHandlerImpl.class, "ResultSetMetaHandler");
		} catch (Exception e) {
			logger().info("DbInstanceFactory resultSetMetaHandlerImpl ",e);
		}
		
		// tableReportImpl set meata handler load
		try {
			this.tableReportImpl = (TableReportImpl)getBeanObject(TableReportImpl.class, "TableReport");
		} catch (Exception e) {
			logger().info("DbInstanceFactory TableReportImpl ",e);
		}
	}
	
	private Object getBeanObject(Class clazz, String classSuffix) throws Exception {
		String nameLowerCase = getDbVenderName().toLowerCase(); 
		String cls = String.format("%s.%s.%s%s", "com.varsql.db.ext", nameLowerCase, StringUtil.capitalize(nameLowerCase), classSuffix);
		
		try{
			Class.forName(cls);
		}catch(Exception e){
			cls = clazz.getName()+"OTHER";
		}
		
		Constructor[] constructorArr = Class.forName(cls).getDeclaredConstructors();
		boolean flag=false ; 
		for (int i = 0; i < constructorArr.length; i++) {
			Class[] paramArr = constructorArr[i].getParameterTypes();
			
			for(Class tmpParam : paramArr){
				if(tmpParam == MetaControlBean.class){
					flag = true; 
				}
			}
		}
		
		if(flag){
			return Class.forName(cls).getDeclaredConstructor(MetaControlBean.class).newInstance(this);
		}else{
			return Class.forName(cls).newInstance();
		}
	}
	
	/**
	 * 
	 * @Method Name  : getServiceMenu
	 * @Method 설명 : db service menu
	 * @작성일   : 2018. 9. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 * @throws Exception
	 */
	public List<ServiceObject> getServiceMenu() {
		return this.dbMetaImpl.getServiceMenu();
	}
	
	/**
	 * 
	 * @Method Name  : getServiceMenu
	 * @Method 설명 : 버전 정보
	 * @작성일   : 2018. 10. 8. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dataParamInfo
	 * @return
	 */
	public List getDBInfo(DatabaseParamInfo dataParamInfo) {
		return this.dbMetaImpl.getVersion(dataParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : getSchemas
	 * @Method 설명  :  get db schemas 정보.
	 * @작성일   : 2018. 9. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dataParamInfo
	 * @return
	 */
	public List<String> getSchemas(DatabaseParamInfo dataParamInfo) {
		return this.dbMetaImpl.getSchemas(dataParamInfo);
	}
	
	
	/**
	 * 
	 * @Method Name  : getDBMeta
	 * @Method 설명 : get db object metadata
	 * @작성일   : 2018. 9. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param metaType
	 * @param paramInfo
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T>T getDBObjectMeta(String metaType, DatabaseParamInfo paramInfo, String ... objNm){
		
		String callMethodName =String.format("get%sMetadata", StringUtil.capitalize(metaType));
		
		try{
			if(VartechReflectionUtils.hasMethod(this.dbMetaImpl.getClass(), callMethodName, DatabaseParamInfo.class ,objNm.getClass())){
				Object [] paramArr  = {paramInfo, objNm};
				return (T)VartechReflectionUtils.invokeMethod(this.dbMetaImpl, callMethodName, paramArr);
			}else{
				return (T)this.dbMetaImpl.getExtensionMetadata(paramInfo, metaType, paramInfo.getCustom());
			}
		}catch(Exception e){
			logger().error("getDBMeta class : {} , callMethodName: {}, objArr : {} " , this.dbMetaImpl.getClass(), callMethodName, StringUtil.join(objNm));
			logger().error("getDBMeta callMethodName " , e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getDBObjectList(String dbObjType, DatabaseParamInfo paramInfo){
		
		String callMethodName =String.format("get%ss", StringUtil.capitalize(dbObjType));
		
		try{
			if(VartechReflectionUtils.hasMethod(this.dbMetaImpl.getClass(), callMethodName, DatabaseParamInfo.class)){
				Object [] paramArr  = {paramInfo};
				return (T) VartechReflectionUtils.invokeMethod(this.dbMetaImpl, callMethodName, paramArr);
			}else{
				return (T)this.dbMetaImpl.getExtensionObject(paramInfo, dbObjType, paramInfo.getCustom());
			}
		}catch(Exception e){
			logger().error("getDBObjectList class : {}  , callMethodName : {}" , this.dbMetaImpl.getClass(), callMethodName);
			logger().error("getDBObjectList callMethodName " , e);
		}
		return null;
	}
	
	public DDLInfo getDDLScript(String dbObjType, DatabaseParamInfo paramInfo,String objNm){
		List<DDLInfo> ddlList = getDDLScript(dbObjType, paramInfo, new DDLCreateOption() ,objNm);
		return ddlList.get(0);
	}
	
	public <T>T getDDLScript(String dbObjType, DatabaseParamInfo paramInfo, DDLCreateOption ddlOption, String ... objNm){
		
		String callMethodName =String.format("get%ss", StringUtil.capitalize(dbObjType));
		
		try{
			if(VartechReflectionUtils.hasMethod(this.ddlScriptImpl.getClass(), callMethodName, DatabaseParamInfo.class, DDLCreateOption.class, objNm.getClass())){
				Object [] paramArr  = {paramInfo, ddlOption, objNm};
				Object obj = VartechReflectionUtils.invokeMethod(this.ddlScriptImpl, callMethodName, paramArr);
				
				return (T)obj; 
			}else{
				throw new VarsqlMethodNotFoundException(String.format("MetaControlBean getDDLScript ->  %s method not found ", callMethodName));
			}
		}catch(Exception e){
			logger().error("getDDLScript class : {} , callMethodName : {}, objArr : {}  " , this.ddlScriptImpl.getClass(), callMethodName, StringUtil.join(objNm));
			logger().error("getDDLScript callMethodName " , e);
		}
		return null;
	}
	
	public ResultSetHandler getResultsetHandler(){
		return this.resultSetHandler;
	}
	
	public DataTypeImpl getDataTypeImpl(){
		return this.dataTypeImpl;
	}
	
	public ResultSetMetaHandlerImpl getResultSetMetaHandlerImpl() {
		return resultSetMetaHandlerImpl;
	}
	
	private static Logger logger(){
		if(logger ==null){
			logger = LoggerFactory.getLogger(MetaControlBean.class);
		}
		
		return logger; 
	}

	public TableReportImpl getTableReportImpl() {
		return tableReportImpl;
	}
	
	public String getDbVenderName(){
		return this.dbVenderName;
	}
	
}
