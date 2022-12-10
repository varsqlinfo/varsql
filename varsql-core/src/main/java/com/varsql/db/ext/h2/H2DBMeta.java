package com.varsql.db.ext.h2;

import java.sql.SQLException;
import java.util.List;

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
import com.vartech.common.utils.VartechUtils;


/**
 *
 * @FileName : H2DBMeta.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public class H2DBMeta extends AbstractDBMeta{

	private final Logger logger = LoggerFactory.getLogger(H2DBMeta.class);

	public H2DBMeta(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory
			, new ServiceObject(ObjectType.FUNCTION)
			, new ServiceObject(ObjectType.INDEX)
			, new ServiceObject(ObjectType.TRIGGER,false,ObjectTypeTabInfo.MetadataTab.INFO ,ObjectTypeTabInfo.MetadataTab.DDL)
			, new ServiceObject(ObjectType.SEQUENCE, false,ObjectTypeTabInfo.MetadataTab.INFO ,ObjectTypeTabInfo.MetadataTab.DDL)
		);
	}

	@Override
	public List getVersion(DatabaseParamInfo dataParamInfo)  {
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("dbSystemView" ,dataParamInfo);
	}
	
	@Override
	public List<TableInfo> getTables(DatabaseParamInfo dataParamInfo) throws Exception {
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("tableList" ,dataParamInfo);
	}
	
	@Override
	public List<TableInfo> getTableMetadata(DatabaseParamInfo dataParamInfo,String... tableNames) throws Exception {
		return tableAndColumnsInfo(dataParamInfo,"tableMetadata" ,tableNames);
	}
	
	@Override
	public List<TableInfo> getViews(DatabaseParamInfo dataParamInfo) throws Exception {
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("viewList" ,dataParamInfo);
	}

	@Override
	public List<TableInfo> getViewMetadata(DatabaseParamInfo dataParamInfo,String... viewNames) throws Exception	{
		return tableAndColumnsInfo(dataParamInfo,"viewMetadata" ,viewNames);
	}

	@Override
	public List<ObjectInfo> getFunctions(DatabaseParamInfo dataParamInfo) throws Exception {
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("functionList" ,dataParamInfo);
	}
	@Override
	public List<ObjectInfo> getFunctionMetadata(DatabaseParamInfo dataParamInfo, String... functionNames) throws Exception {
		setObjectNameList(dataParamInfo, functionNames);
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("functionMeta" ,dataParamInfo);
	}

	@Override
	public List getIndexs(DatabaseParamInfo dataParamInfo) throws Exception {
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("indexList" ,dataParamInfo);
	}
	@Override
	public List<IndexInfo> getIndexMetadata(DatabaseParamInfo dataParamInfo, String... indexNames) throws Exception {

		IndexInfoHandler handler = new IndexInfoHandler(dbInstanceFactory.getDataTypeImpl());

		setObjectNameList(dataParamInfo, indexNames);

		SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).select("indexMetadata" ,dataParamInfo , handler);

		return handler.getIndexInfoList();
	}

	@Override
	public List getTriggers(DatabaseParamInfo dataParamInfo){
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("triggerList" ,dataParamInfo);
	}

	@Override
	public List getTriggerMetadata(DatabaseParamInfo dataParamInfo, String... triggerNames) throws Exception {
		setObjectNameList(dataParamInfo, triggerNames);
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("triggerMetadata" ,dataParamInfo);
	}

	private List<TableInfo> tableAndColumnsInfo (DatabaseParamInfo dataParamInfo, String queryId, String... names){

		setObjectNameList(dataParamInfo, names);

		SqlSession sqlSession = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());


		logger.debug("H2DBMeta tableAndColumnsInfo {} ",VartechUtils.reflectionToString(dataParamInfo));

		TableInfoHandler tableInfoHandler;

		if("viewMetadata".equals(queryId)){
			tableInfoHandler = new TableInfoHandler(dbInstanceFactory.getDataTypeImpl());
		}else{
			tableInfoHandler = new TableInfoHandler(dbInstanceFactory.getDataTypeImpl(), sqlSession.selectList("tableList" ,dataParamInfo));

			if(tableInfoHandler.getTableNameList() !=null  && tableInfoHandler.getTableNameList().size() > 0){
				dataParamInfo.addCustom(OBJECT_NAME_LIST_KEY, tableInfoHandler.getTableNameList());
			}
		}

		sqlSession.select(queryId ,dataParamInfo, tableInfoHandler);

		return tableInfoHandler.getTableInfoList();
	}

	@Override
	public List<ObjectInfo> getSequences(DatabaseParamInfo dataParamInfo) throws Exception {
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("sequenceList" ,dataParamInfo);
	}

	@Override
	public List getSequenceMetadata(DatabaseParamInfo dataParamInfo, String... sequenceNames) throws Exception {
		setObjectNameList(dataParamInfo, sequenceNames);
		return SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid()).selectList("sequenceMetadata" ,dataParamInfo);
	}
}
