package com.varsql.db.ext.postgresql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.ddl.DDLTemplateFactory;
import com.varsql.core.db.ddl.script.DDLScriptImpl;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.vartech.common.app.beans.ParamMap;

/**
 * 
 * @FileName  : CubridDDLScript.java
 * @프로그램 설명 : Cubrid ddl 
 * @Date      : 2019. 1. 20. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class PostgresqlDDLScript extends DDLScriptImpl {
	Logger logger = LoggerFactory.getLogger(PostgresqlDDLScript.class);
	
	
	public PostgresqlDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory);
	}
	
	@Override
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		
		SqlSession client = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		
		for(String name : objNmArr){
			
			ddlStr = new StringBuilder();
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			dataParamInfo.setObjectName(name);
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP TABLE " + name + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			List<ParamMap> srcList = client.selectList("tableScript", dataParamInfo);

			ddlStr.append("CREATE TABLE " + name + "(\n");
			
			String dataType = "";
			
			dataParamInfo.setObjectName(name);
			
			DataTypeImpl dataTypeImpl = dbInstanceFactory.getDataTypeImpl();
			ParamMap source;
			for (int i = 0; i < srcList.size(); i++) {
				source = srcList.get(i);
				
				ddlStr.append(BlankConstants.TAB);
				if (i > 0){
					ddlStr.append(",");
				}
				ddlStr.append(source.get(MetaColumnConstants.COLUMN_NAME)).append(" ");
				
				ddlStr.append(source.get(MetaColumnConstants.DATA_TYPE)).append(" ");
				
				ddlStr.append(source.getString(MetaColumnConstants.COLUMN_DEF)).append(" ");
				
				ddlStr.append(getNotNullValue(source.getString(MetaColumnConstants.NULLABLE)));
				
				ddlStr.append(BlankConstants.NEW_LINE);
			}

			List srcPkList = client.selectList("tableScriptPk", dataParamInfo);
			Map pkMap;
			for (int i = 0; i < srcPkList.size(); i++) {
				pkMap = (HashMap) srcPkList.get(i);
				ddlStr.append(BlankConstants.TAB).append(",CONSTRAINT ")
				.append(pkMap.get("CONSTRAINT_NAME")).append(" ").append(pkMap.get("CONSTRAINDDEF"));
			}

			ddlStr.append(");").append(BlankConstants.NEW_LINE_TWO);
			
			List srcCommentList = client.selectList("tableScriptComments",dataParamInfo);
			for (int i = 0; i < srcCommentList.size(); i++) {
				ddlStr.append( srcCommentList.get(i)).append(BlankConstants.NEW_LINE);
			}
			
			if(srcCommentList.size() > 0){
				ddlStr.append(BlankConstants.NEW_LINE);
			}
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}

		return reval;
	}
	
	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		
		StringBuilder ddlStr;
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		
		for (String name : objNmArr) {
			
			ddlStr = new StringBuilder();
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			
			dataParamInfo.setObjectName(name);
			
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP ViEW " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			ParamMap source = sqlSesseion.selectOne("viewScript", dataParamInfo);
			
			ddlStr.append("CREATE OR REPLACE VIEW ").append(name).append(" AS ").append(BlankConstants.NEW_LINE_TWO);
			ddlStr.append(source.getString("VIEW_SOURCE")).append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
	
		return reval;
	}
	
	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			dataParamInfo.setObjectName(name);
			
			ddlStr = new StringBuilder();
			
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP INDEX " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			Map indexInfo = sqlSesseion.selectOne("indexScript", dataParamInfo);
			ddlStr.append(indexInfo.get(MetaColumnConstants.CREATE_SOURCE));
	
			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation...");
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			
			dataParamInfo.setObjectName(name);
			
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP FUNCTION " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			Map scriptInfo = sqlSesseion.selectOne("functionScript", dataParamInfo);
			
			ddlStr.append(scriptInfo.get(MetaColumnConstants.CREATE_SOURCE));
			
			ddlStr.append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Procedure DDL Generation... ");
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			
			dataParamInfo.setObjectName(name);
			
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP PROCEDURE " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			Map scriptInfo = sqlSesseion.selectOne("procedureScript", dataParamInfo);
			
			ddlStr.append(scriptInfo.get(MetaColumnConstants.CREATE_SOURCE));

			ddlStr.append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	/**
	 * 
	 * @Method Name  : getTrigger
	 * @Method 설명 : trigger ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScriptImpl#getTrigger(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2018. 9. 19. 
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNmArr
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getTriggers(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug("Trigger DDL Generation...");

		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		boolean addFlag;
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			dataParamInfo.setObjectName(name);

			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP Trigger " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			Map scriptInfo = sqlSesseion.selectOne("triggerScript", dataParamInfo);
			
			ddlStr.append(scriptInfo.get(MetaColumnConstants.CREATE_SOURCE));
			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}

		return reval;
	}
	
	/**
	 * 
	 * @Method Name  : getSequence
	 * @Method 설명 : Sequence 구하기.
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScriptImpl#getSequence(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2018. 9. 19. 
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNmArr
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getSequences(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug("Sequence DDL Generation...");
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			
			dataParamInfo.setObjectName(name);
			
			Map param =  sqlSesseion.selectOne("sequenceScript",  dataParamInfo);
			param.put("schema", dataParamInfo.getSchema());
			
			param.put("ddlOption", ddlOption);
			
			ddlStr.append(DDLTemplateFactory.getInstance().ddlRender(DBType.CUBRID.getDbVenderName(), "sequenceScript", param));
			
			ddlStr.append(BlankConstants.NEW_LINE_TWO);
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
}