package com.varsql.db.ext.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.meta.AbstractDBMeta;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.mybatis.handler.resultset.IndexInfoHandler;
import com.varsql.core.db.mybatis.handler.resultset.TableInfoHandler;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.servicemenu.ObjectTypeTabInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.IndexInfo;
import com.varsql.core.db.valueobject.ObjectInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.TableInfo;


/**
 *
 * @FileName : OracleDBMeta.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public class OracleDBMeta extends AbstractDBMeta{
	
	private final Logger logger = LoggerFactory.getLogger(OracleDBMeta.class);

	public OracleDBMeta(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory
			,new ServiceObject[] { 
				new ServiceObject(ObjectType.TABLE)
				, new ServiceObject(ObjectType.VIEW)
				, new ServiceObject(ObjectType.FUNCTION)
				, new ServiceObject(ObjectType.INDEX)
				, new ServiceObject(ObjectType.PROCEDURE)
				, new ServiceObject(ObjectType.PACKAGE)
				, new ServiceObject(ObjectType.TRIGGER,false,ObjectTypeTabInfo.MetadataTab.INFO ,ObjectTypeTabInfo.MetadataTab.DDL)
				, new ServiceObject(ObjectType.SEQUENCE, false,ObjectTypeTabInfo.MetadataTab.INFO ,ObjectTypeTabInfo.MetadataTab.DDL)
			}
		);
	}

	@Override
	public List getVersion(DatabaseParamInfo dataParamInfo)  {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("dbSystemView", dataParamInfo);
		}
	}

	@Override
	public List<TableInfo> getTables(DatabaseParamInfo dataParamInfo) throws Exception {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("tableList", dataParamInfo);
		}
	}

	@Override
	public List<TableInfo> getTableMetadata(DatabaseParamInfo dataParamInfo,String... tableNames) throws Exception {
		logger.debug("getTableMetadata {}  tableArr :: {}", dataParamInfo, tableNames);
		return tableAndColumnsInfo(dataParamInfo,"tableMetadata", tableNames);
	}

	@Override
	public List<TableInfo> getViews(DatabaseParamInfo dataParamInfo) throws Exception {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("viewList", dataParamInfo);
		}
	}
	@Override
	public List<TableInfo> getViewMetadata(DatabaseParamInfo dataParamInfo,String... viewNames) throws Exception	{
		return tableAndColumnsInfo(dataParamInfo,"viewMetadata" ,viewNames);
	}

	@Override
	public List<ObjectInfo> getProcedures(DatabaseParamInfo dataParamInfo) throws Exception {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("procedureList", dataParamInfo);
		}
	}

	@Override
	public List<ObjectInfo> getProcedureMetadata(DatabaseParamInfo dataParamInfo, String... procedureNames) throws Exception {
		setObjectNameList(dataParamInfo, procedureNames);
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("objectMetadataList", dataParamInfo);
		}
	}


	@Override
	public List<ObjectInfo> getFunctions(DatabaseParamInfo dataParamInfo) throws Exception {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("functionList", dataParamInfo);
		}
	}
	@Override
	public List<ObjectInfo> getFunctionMetadata(DatabaseParamInfo dataParamInfo, String... functionNames) throws Exception {
		setObjectNameList(dataParamInfo, functionNames);
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("objectMetadataList", dataParamInfo);
		}
	}


	@Override
	public List getIndexs(DatabaseParamInfo dataParamInfo) throws Exception {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("indexList", dataParamInfo);
		}
	}
	@Override
	public List<IndexInfo> getIndexMetadata(DatabaseParamInfo dataParamInfo, String... indexNames) throws Exception {

		IndexInfoHandler handler = new IndexInfoHandler(dbInstanceFactory.getDataTypeImpl());

		setObjectNameList(dataParamInfo, indexNames);
		
		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			sqlSession.select("indexMetadata" ,dataParamInfo , handler);
		}

		return handler.getIndexInfoList();
	}

	@Override
	public List getTriggers(DatabaseParamInfo dataParamInfo){
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("triggerList", dataParamInfo);
		}
	}

	@Override
	public List getTriggerMetadata(DatabaseParamInfo dataParamInfo, String... triggerNames) throws Exception {
		setObjectNameList(dataParamInfo, triggerNames);
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("triggerMetadata", dataParamInfo);
		}
	}

	private List<TableInfo> tableAndColumnsInfo (DatabaseParamInfo dataParamInfo, String queryId, String... names){

		setObjectNameList(dataParamInfo, names);
		TableInfoHandler tableInfoHandler;

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			if("viewMetadata".equals(queryId)){
				tableInfoHandler = new TableInfoHandler(dbInstanceFactory.getDataTypeImpl(), sqlSession.selectList("viewList" ,dataParamInfo));
			}else{
				tableInfoHandler = new TableInfoHandler(dbInstanceFactory.getDataTypeImpl(), sqlSession.selectList("tableList" ,dataParamInfo));
				if(tableInfoHandler.getTableNameList() !=null  && tableInfoHandler.getTableNameList().size() > 0){
					dataParamInfo.addCustom(OBJECT_NAME_LIST_KEY, tableInfoHandler.getTableNameList());
				}
			}
			
			sqlSession.select(queryId, dataParamInfo, tableInfoHandler);
		}

		return tableInfoHandler.getTableInfoList();
	}

	@Override
	public List<ObjectInfo> getSequences(DatabaseParamInfo dataParamInfo) throws Exception {
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("sequenceList", dataParamInfo);
		}
	}

	@Override
	public List getSequenceMetadata(DatabaseParamInfo dataParamInfo, String... sequenceNames) throws Exception {
		setObjectNameList(dataParamInfo, sequenceNames);
		try (SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			return sqlSession.selectList("sequenceMetadata", dataParamInfo);
		}
	}

	@Override
	public <T>T getExtensionMetadata(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception {
		if("package".equalsIgnoreCase(serviceName)){
			try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
				return (T)sqlSession.selectList("packageList" ,dataParamInfo);
			}
		}

		return null;
	}
}
