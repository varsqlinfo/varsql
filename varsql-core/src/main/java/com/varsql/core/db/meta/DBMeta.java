package com.varsql.core.db.meta;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.IndexInfo;
import com.varsql.core.db.valueobject.ObjectInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.TableInfo;

/**
 *
 * @FileName : DBMeta.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public interface DBMeta{
	
	public String OBJECT_NAME_LIST_KEY = "objectNameList";
	
	public List<ServiceObject> getServiceMenu();

	public List getVersion(DatabaseParamInfo dataParamInfo) throws Exception;

	public List<String> getDatabases(DatabaseParamInfo dataParamInfo) throws SQLException;
	
	public List<String> getSchemas(DatabaseParamInfo dataParamInfo) throws SQLException;
	public List getUsers(DatabaseParamInfo dataParamInfo) throws Exception;

	// 테이블 정보.
	public List<TableInfo> getTables(DatabaseParamInfo dataParamInfo) throws Exception;
	// 테이블 메타 정보.
	public List<TableInfo> getTableMetadata(DatabaseParamInfo dataParamInfo,String... tableNames) throws Exception;


	// view 목록.
	public List<TableInfo> getViews(DatabaseParamInfo dataParamInfo) throws Exception;
	// view 메뉴 목록.
	public List<TableInfo> getViewMetadata(DatabaseParamInfo dataParamInfo,String... viewNames) throws Exception;


	// 프로시저 목록
	public List<ObjectInfo> getProcedures(DatabaseParamInfo dataParamInfo) throws Exception;
	// 프로시저 메타데이타
	public List<ObjectInfo> getProcedureMetadata(DatabaseParamInfo dataParamInfo, String... procedureNames) throws Exception;


	// function info
	public List<ObjectInfo> getFunctions(DatabaseParamInfo dataParamInfo) throws Exception;
	// function metadata
	public List<ObjectInfo> getFunctionMetadata(DatabaseParamInfo dataParamInfo, String... functionNames) throws Exception;


	// index list
	public List<ObjectInfo> getIndexs(DatabaseParamInfo dataParamInfo) throws Exception;
	// index metadata
	public List<IndexInfo> getIndexMetadata(DatabaseParamInfo dataParamInfo, String... indexNames) throws Exception;


	// sequences list
	public List<ObjectInfo> getSequences(DatabaseParamInfo dataParamInfo) throws Exception;
	// sequences metadata
	public List getSequenceMetadata(DatabaseParamInfo dataParamInfo,String... sequenceNames) throws Exception;


	// trigger list
	public List<ObjectInfo> getTriggers(DatabaseParamInfo dataParamInfo) throws Exception;
	// trigger metadata
	public List getTriggerMetadata(DatabaseParamInfo dataParamInfo,String... triggerNames) throws Exception;


	public <T>T getExtensionMetadata(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception;
	public <T>T getExtensionObject(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception;

}
