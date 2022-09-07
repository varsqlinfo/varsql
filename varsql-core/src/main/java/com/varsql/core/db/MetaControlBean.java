package com.varsql.core.db;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.datatype.DataTypeFactoryOTHER;
import com.varsql.core.db.ddl.script.DDLScript;
import com.varsql.core.db.ddl.script.DDLScriptOTHER;
import com.varsql.core.db.meta.DBMeta;
import com.varsql.core.db.meta.DBMetaOTHER;
import com.varsql.core.db.report.table.TableReport;
import com.varsql.core.db.report.table.TableReportOTHER;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.exception.DBMetadataException;
import com.varsql.core.exception.VarsqlMethodNotFoundException;
import com.varsql.core.sql.type.CommandTypeFactory;
import com.varsql.core.sql.type.CommandTypeFactoryOther;
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

	private String dbVenderName;

	public MetaControlBean(String db){

		this.dbVenderName =db;
		// datatype load

		try {
			this.dataTypeFactory = getBeanObject(DataTypeFactoryOTHER.class, DataTypeFactory.class);
		} catch (Exception e) {
			logger.error("@@@ varsql bean error dataTypeFactory :{} ", e.getMessage(), e);
		}

		// meta load
		try {
			this.dbMeta = getBeanObject(DBMetaOTHER.class, DBMeta.class);
		} catch (Exception e) {
			logger.error("@@@ varsql bean error dbMeta :{} ", e.getMessage(), e);
		}

		// script object load
		try {
			this.ddlScript = getBeanObject(DDLScriptOTHER.class, DDLScript.class);
		} catch (Exception e) {
			logger.error("@@@ varsql bean error ddlScript :{} ", e.getMessage(), e);
		}

		// tableReportImpl set meata handler load
		try {
			this.tableReport = getBeanObject(TableReportOTHER.class, TableReport.class);
		} catch (Exception e) {
			logger.error("@@@ varsql bean error tableReport :{} ", e.getMessage(), e);
		}
		
		// sql command type factory
		try {
			this.commandTypeFactory = getBeanObject(CommandTypeFactoryOther.class, CommandTypeFactory.class);
		} catch (Exception e) {
			logger.error("@@@ varsql bean error commandTypeFactory :{} ", e.getMessage(), e);
		}
	}

	private <T> T getBeanObject(Class<?> clazz, Class<?> classSuffix) throws Exception {
		String nameLowerCase = getDbVenderName().toLowerCase();
		String cls = String.format("%s.%s.%s%s", "com.varsql.db.ext", nameLowerCase, StringUtils.capitalize(nameLowerCase), classSuffix.getSimpleName());

		try{
			Class.forName(cls);
		}catch(Exception e){
			cls = clazz.getName();
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
			return (T)Class.forName(cls).getDeclaredConstructor(MetaControlBean.class).newInstance(this);
		}else{
			return (T)Class.forName(cls).getDeclaredConstructor().newInstance();
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
			logger.error("getDBMeta class : {} , callMethodName: {}, objArr : {} " , this.dbMeta.getClass(), callMethodName, StringUtils.join(objNm));
			logger.error("getDBMeta callMethodName " , e);
			if(hasMethod) {
				throw new DBMetadataException(VarsqlAppCode.DB_META_ERROR , e);
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
			logger.error("getDBObjectList class : {}  , callMethodName : {}" , this.dbMeta.getClass(), callMethodName);
			logger.error("getDBObjectList callMethodName :{} ", e.getMessage(), e);
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
			logger.error("getDDLScript class : {} , callMethodName : {}, objArr : {}  " , this.ddlScript.getClass(), callMethodName, StringUtils.join(objNm));
			logger.error("getDDLScript callMethodName " , e);
		}
		return null;
	}

	public DataTypeFactory getDataTypeImpl(){
		return this.dataTypeFactory;
	}

	public TableReport getTableReportImpl() {
		return tableReport;
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
