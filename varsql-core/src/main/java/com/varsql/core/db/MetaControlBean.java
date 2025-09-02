package com.varsql.core.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.ddl.script.DDLScript;
import com.varsql.core.db.meta.DBMeta;
import com.varsql.core.db.meta.DBVersionInfo;
import com.varsql.core.db.meta.MetaBeanConfig;
import com.varsql.core.db.report.table.TableReport;
import com.varsql.core.db.valueobject.ConstraintInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.exception.DBMetadataException;
import com.varsql.core.exception.VarsqlMethodNotFoundException;
import com.varsql.core.sql.StatementSetter;
import com.varsql.core.sql.type.CommandTypeFactory;
import com.vartech.common.utils.StringUtils;
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

	private final static Logger logger = LoggerFactory.getLogger(MetaControlBean.class);
	

	private DBMeta dbMeta;
	private DDLScript ddlScript;
	private DataTypeFactory dataTypeFactory;
	private TableReport tableReport;
	private CommandTypeFactory commandTypeFactory;
	private StatementSetter statementSetter;

	private String dbVenderName;

	public MetaControlBean(MetaBeanConfig annoInfo) {
		this.dbVenderName = annoInfo.dbVenderType().getName();
		
		try {
			this.dataTypeFactory = getBeanObject(annoInfo.dataTypeBean());
		} catch (Exception e) {
			logger.error("@@@ varsql bean error dataTypeFactory :{} ", e.getMessage(), e);
		}

		// meta load
		try {
			this.dbMeta = getBeanObject(annoInfo.metaBean());
		} catch (Exception e) {
			logger.error("@@@ varsql bean error dbMeta :{} ", e.getMessage(), e);
		}

		// script object load
		try {
			this.ddlScript = getBeanObject(annoInfo.ddlBean());
		} catch (Exception e) {
			logger.error("@@@ varsql bean error ddlScript :{} ", e.getMessage(), e);
		}

		// tableReportImpl set meata handler load
		try {
			this.tableReport = getBeanObject(annoInfo.tableReportBean());
		} catch (Exception e) {
			logger.error("@@@ varsql bean error tableReport :{} ", e.getMessage(), e);
		}
		
		// sql command type factory
		try {
			this.commandTypeFactory = getBeanObject(annoInfo.commandTypeBean());
		} catch (Exception e) {
			logger.error("@@@ varsql bean error commandTypeFactory :{} ", e.getMessage(), e);
		}
		
		try {
			this.statementSetter = getBeanObject(annoInfo.statementSetterBean());
		} catch (Exception e) {
			logger.error("@@@ varsql bean error statementSetter :{} ", e.getMessage(), e);
		}
	}

	private <T> T getBeanObject(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Constructor[] constructorArr = (Constructor[]) clazz.getDeclaredConstructors();
		boolean flag = false;
		for (int i = 0; i < constructorArr.length; i++) {
			Class[] paramArr = constructorArr[i].getParameterTypes();
			for (Class<MetaControlBean> tmpParam : paramArr) {
				if (tmpParam == MetaControlBean.class)
					flag = true;
			}
		}

		if(flag){
			return (T)clazz.getDeclaredConstructor(MetaControlBean.class).newInstance(this);
		}else{
			return (T)clazz.getDeclaredConstructor().newInstance();
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
		return this.dbMeta.getServiceMenu();
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
	 * @throws Exception
	 */
	public List getDBInfo(DatabaseParamInfo dataParamInfo) throws Exception {
		return this.dbMeta.getVersion(dataParamInfo);
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
	 * @throws Exception 
	 */
	public List<String> getSchemas(DatabaseParamInfo dataParamInfo) throws SQLException {
		return this.dbMeta.getSchemas(dataParamInfo);
	}
	/**
	 * 
	 * Method Name  : getVenderVersionInfo
	 * Method 설명 : get vender version info
	 * @author   : ytkim
	 * 작성일   : 2023. 2. 13. 
	 * 변경이력  :
	 * @param dataParamInfo
	 * @return
	 */
	public List<DBVersionInfo> getVenderVersionInfo() {
		return this.dbMeta.getVenderVersionInfo();
	}
	
	/**
	 * 
	 * Method Name  : getDefaultVenderVersion
	 * Method 설명 :
	 * @author   : ytkim
	 * 작성일   : 2023. 2. 15. 
	 * 변경이력  :
	 * @return
	 */
	public DBVersionInfo getDefaultVenderVersion() {
		return this.dbMeta.getDefaultVenderVersion();
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

		String callMethodName =String.format("get%sMetadata", StringUtils.capitalize(metaType));

		boolean hasMethod = VartechReflectionUtils.hasMethod(this.dbMeta.getClass(), callMethodName, DatabaseParamInfo.class, new String[0].getClass());

		try{
			if(hasMethod){
				if(objNm == null) {
					objNm = new String[0];
				}

				Object [] paramArr  = {paramInfo, objNm};
				return (T)VartechReflectionUtils.invokeMethod(this.dbMeta, callMethodName, paramArr);
			}else{
				return (T)this.dbMeta.getExtensionMetadata(paramInfo, metaType, paramInfo.getCustom());
			}
		}catch(Exception e){
			Throwable th = e.getCause();
			th = th != null ? th: e; 
			
			String errorMessage = th.getMessage();
			//logger.error("message {}, getDBMeta class : {} , callMethodName: {}, objArr : {} ", errorMessage, this.dbMeta.getClass(), callMethodName, StringUtils.join(objNm), e);
			if(hasMethod) {
				throw new DBMetadataException(errorMessage, th);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T>T getDBObjectList(String dbObjType, DatabaseParamInfo paramInfo){

		String callMethodName =String.format("get%ss", StringUtils.capitalize(dbObjType));

		try{
			if(VartechReflectionUtils.hasMethod(this.dbMeta.getClass(), callMethodName, DatabaseParamInfo.class)){
				Object [] paramArr  = {paramInfo};
				return (T) VartechReflectionUtils.invokeMethod(this.dbMeta, callMethodName, paramArr);
			}else{
				return (T)this.dbMeta.getExtensionObject(paramInfo, dbObjType, paramInfo.getCustom());
			}
		}catch(Exception e){
			logger.error("getDBObjectList class : {}, callMethodName : {}", e.getMessage(), this.dbMeta.getClass(), callMethodName, e);
		}
		return null;
	}

	public DDLInfo getDDLScript(String dbObjType, DatabaseParamInfo paramInfo,String objNm){
		List<DDLInfo> ddlList = getDDLScript(dbObjType, paramInfo, new DDLCreateOption() ,objNm);
		return ddlList.get(0);
	}

	public <T>T getDDLScript(String dbObjType, DatabaseParamInfo paramInfo, DDLCreateOption ddlOption, String ... objNm){

		String callMethodName =String.format("get%ss", StringUtils.capitalize(dbObjType));

		try{
			if(VartechReflectionUtils.hasMethod(this.ddlScript.getClass(), callMethodName, DatabaseParamInfo.class, DDLCreateOption.class, objNm.getClass())){
				Object [] paramArr  = {paramInfo, ddlOption, objNm};
				Object obj = VartechReflectionUtils.invokeMethod(this.ddlScript, callMethodName, paramArr);

				return (T)obj;
			}else{
				throw new VarsqlMethodNotFoundException(String.format("MetaControlBean getDDLScript ->  %s method not found ", callMethodName));
			}
		}catch(Exception e){
			logger.error("getDDLScript class : {}, callMethodName : {}, objArr : {}", this.ddlScript.getClass(), callMethodName, StringUtils.join(objNm), e);
		}
		return null;
	}
	
	public List<ConstraintInfo> getConstraintsKeys(DatabaseParamInfo dataParamInfo, String tableNm) throws Exception{
		return this.dbMeta.getConstraintsKeys(dataParamInfo, tableNm);
	}
	
	@SuppressWarnings("unchecked")
	public List<DDLInfo> tableDdlConvertDB(DatabaseParamInfo paramInfo, DDLCreateOption ddlOption, DBVenderType convertDB, String ... objNm){
		try{
			return this.ddlScript.tableDdlConvertDB(paramInfo, ddlOption, convertDB, objNm);
		}catch(Exception e){
			logger.error("msg : {}, objArr : {}", e.getMessage(), StringUtils.join(objNm), e);
		}
		return Collections.EMPTY_LIST;
	}
	
	public String getDefaultValueToVenderValue(String val, DataType dataType){
		return this.ddlScript.getDefaultValueToVenderValue(val, dataType);
	}

	public DataTypeFactory getDataTypeImpl(){
		return this.dataTypeFactory;
	}

	public TableReport getTableReportImpl() {
		return tableReport;
	}
	
	public StatementSetter getStatementSetter() {
		return this.statementSetter;
	}

	public String getDbVenderName(){
		return this.dbVenderName;
	}
	
	/**
	 * database list
	 * @param databaseParamInfo
	 * @return
	 * @throws SQLException
	 */
	public List<String> getDatabases(DatabaseParamInfo databaseParamInfo) throws SQLException {
		return this.dbMeta.getDatabases(databaseParamInfo);
	}
	
	/**
	 * sql command type factory
	 * @return CommandTypeFactory
	 */
	public CommandTypeFactory getCommandTypeFactory() {
		return commandTypeFactory;
	}

}
