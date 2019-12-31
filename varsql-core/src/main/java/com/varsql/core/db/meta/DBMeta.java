package com.varsql.core.db.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.db.beans.DatabaseParamInfo;
import com.varsql.core.db.beans.IndexInfo;
import com.varsql.core.db.beans.ObjectInfo;
import com.varsql.core.db.beans.ServiceObject;
import com.varsql.core.db.beans.TableInfo;
import com.varsql.core.db.serviceobject.ObjectType;

/**
 * 
 * @FileName : DBMeta.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public interface DBMeta{
	
	public List getVersion(DatabaseParamInfo dataParamInfo);
	
	public List<String> getSchemas(DatabaseParamInfo dataParamInfo);
	public List getUsers(DatabaseParamInfo dataParamInfo) throws Exception;
	
	// 테이블 정보. 
	public List<TableInfo> getTables(DatabaseParamInfo dataParamInfo) throws Exception;
	// 테이블 메타 정보.
	public List<TableInfo> getTableMetadata(DatabaseParamInfo dataParamInfo,String... tableNm) throws Exception;
	
	
	// view 목록. 
	public List<TableInfo> getViews(DatabaseParamInfo dataParamInfo) throws Exception;
	// view 메뉴 목록.
	public List<TableInfo> getViewMetadata(DatabaseParamInfo dataParamInfo,String... tableNm) throws Exception;
	
	
	// 프로시저 목록
	public List<ObjectInfo> getProcedures(DatabaseParamInfo dataParamInfo) throws Exception;
	// 프로시저 메타데이타
	public List<ObjectInfo> getProcedureMetadata(DatabaseParamInfo dataParamInfo, String... proceduresNm) throws Exception;
	
	
	// function info
	public List<ObjectInfo> getFunctions(DatabaseParamInfo dataParamInfo) throws Exception;
	// function metadata
	public List<ObjectInfo> getFunctionMetadata(DatabaseParamInfo dataParamInfo, String... functionNm) throws Exception;
	
	
	// index list
	public List<ObjectInfo> getIndexs(DatabaseParamInfo dataParamInfo) throws Exception;
	// index metadata
	public List<IndexInfo> getIndexMetadata(DatabaseParamInfo dataParamInfo, String... indexNm) throws Exception;
	
	
	// sequences list
	public List<ObjectInfo> getSequences(DatabaseParamInfo dataParamInfo) throws Exception;
	// sequences metadata
	public List getSequenceMetadata(DatabaseParamInfo dataParamInfo,String... indexNm) throws Exception;
	
	
	// trigger list
	public List<ObjectInfo> getTriggers(DatabaseParamInfo dataParamInfo) throws Exception;
	// trigger metadata
	public List getTriggerMetadata(DatabaseParamInfo dataParamInfo,String... indexNm) throws Exception;
	
	
	public <T>T getExtensionMetadata(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception;
	public <T>T getExtensionObject(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception;
	
}
