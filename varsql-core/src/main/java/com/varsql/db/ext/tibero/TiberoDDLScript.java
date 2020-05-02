package com.varsql.db.ext.tibero;

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
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.utils.VartechUtils;

/**
 * 
 * @FileName  : TiberoDDLScript.java
 * @프로그램 설명 : ddl script
 * @Date      : 2019. 3. 13. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class TiberoDDLScript extends DDLScriptImpl {
	Logger logger = LoggerFactory.getLogger(TiberoDDLScript.class);
	
	public TiberoDDLScript(MetaControlBean dbInstanceFactory){
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
				dataType = String.valueOf(source.get("DATA_TYPE"));
				
				DataTypeInfo dataTypeInfo = dataTypeImpl.getDataType(dataType);
				
				ddlStr.append("\t");
				if (i > 0){
					ddlStr.append(",");
				}
				ddlStr.append(source.get(MetaColumnConstants.COLUMN_NAME)).append(" ");
				
				ddlStr.append(DbMetaUtils.getTypeName(dataTypeInfo ,null ,dataTypeInfo.getDataTypeName(), source.getString(MetaColumnConstants.COLUMN_SIZE) ,source.getString(MetaColumnConstants.DECIMAL_DIGITS)));
				
				ddlStr.append(getDefaultValue(source.getString("DATA_DEFAULT"), dataTypeInfo , true));
				
				ddlStr.append(getNotNullValue(source.getString("NULLABLE")));
				
				ddlStr.append(BlankConstants.NEW_LINE);
			}

			List srcPkList = client.selectList("tableScriptPk", dataParamInfo);
			Map pkMap;
			for (int i = 0; i < srcPkList.size(); i++) {
				pkMap = (HashMap) srcPkList.get(i);
				if (i == 0)
					ddlStr.append(BlankConstants.TAB).append(",CONSTRAINT ")
							.append(pkMap.get("CONSTRAINT_NAME"))
							.append(" PRIMARY KEY ( ")
							.append(pkMap.get("COLUMN_NAME"));
				else {
					ddlStr.append(", " + pkMap.get("COLUMN_NAME"));
				}

				if (i == srcPkList.size() - 1) {
					ddlStr.append(")").append(BlankConstants.NEW_LINE);
				}
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
				ddlStr.append("/* DROP ViEW " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			List<String> srcViewHeadList = sqlSesseion.selectList("viewScript", dataParamInfo);
			
			ddlStr.append("CREATE OR REPLACE FORCE VIEW ").append(name).append("(");
			
			boolean firstCheck = true; 
			for (String source :srcViewHeadList) {
				ddlStr.append(firstCheck ? "" :", ").append(source);
				firstCheck = false; 
			}
			ddlStr.append(" )").append(BlankConstants.NEW_LINE).append("AS ");
			
			ddlStr.append(BlankConstants.NEW_LINE);
			
			List<String> srcViewBodyList = sqlSesseion.selectList("viewScriptSource", dataParamInfo);
			
			for (String source :srcViewBodyList) {
				ddlStr.append(source).append(BlankConstants.NEW_LINE);
			}
			
			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);
			
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
			
			List srcScriptList = sqlSesseion.selectList("indexScriptSource", dataParamInfo);
			ddlStr.append("CREATE ");
	
			Map indexMap;
			if (srcScriptList.size() > 0) {
				indexMap = (Map) srcScriptList.get(0);
				if ("UNIQUE".equals(indexMap.get("UNIQUENESS")))
					ddlStr.append(" UNIQUE INDEX ");
				else {
					ddlStr.append(" INDEX ");
				}
	
				ddlStr.append( indexMap.get("TABLE_OWNER") + ".");
				ddlStr.append( indexMap.get("INDEX_NAME") + " ON ");
				ddlStr.append( indexMap.get("TABLE_OWNER") + ".");
				ddlStr.append( indexMap.get("TABLE_NAME") + "\n ( ");
	
				for (int i = 0; i < srcScriptList.size(); i++) {
					indexMap = (Map) srcScriptList.get(i);
	
					if (("NORMAL".equals(indexMap.get("INDEX_TYPE")))
							&& (indexMap.get("COLUMN_EXPRESSION") == null)) {
						if (i > 0)
							ddlStr.append(",");
						ddlStr.append( indexMap.get("COLUMN_NAME"));
					} else {
						if (i > 0)
							ddlStr.append(",");
						ddlStr.append( indexMap.get("COLUMN_EXPRESSION"));
					}
				}
				ddlStr.append(" ) ").append(BlankConstants.NEW_LINE);;
	
				if ("YES".equals(indexMap.get("LOGGING")))
					ddlStr.append(" LOGGING ").append(BlankConstants.NEW_LINE);
				else {
					ddlStr.append(" NO LOGGING ").append(BlankConstants.NEW_LINE);
				}
				ddlStr.append(" TABLESPACE " +  indexMap.get("TABLESPACE_NAME") + BlankConstants.NEW_LINE);
	
				ddlStr.append(" PCTFREE " + String.valueOf(indexMap.get("PCT_FREE")) + BlankConstants.NEW_LINE);
				ddlStr.append(" INITRANS " + String.valueOf(indexMap.get("INI_TRANS")) +BlankConstants.NEW_LINE);
				ddlStr.append(" MAXTRANS " + String.valueOf(indexMap.get("MAX_TRANS")) + BlankConstants.NEW_LINE);
				ddlStr.append(" STORAGE ( \n ");
				ddlStr.append(BlankConstants.TAB+" INITIAL " + String.valueOf(indexMap.get("INITIAL_EXTENT")) + BlankConstants.NEW_LINE);
				ddlStr.append(BlankConstants.TAB+" MINEXTENTS " + String.valueOf(indexMap.get("MIN_EXTENTS")) + BlankConstants.NEW_LINE);
				ddlStr.append(BlankConstants.TAB+" MAXEXTENTS " + String.valueOf(indexMap.get("MAX_EXTENTS")) + BlankConstants.NEW_LINE);
				ddlStr.append(BlankConstants.TAB+" PCTINCREASE " + String.valueOf(indexMap.get("PCT_INCREASE")) + BlankConstants.NEW_LINE);
				ddlStr.append(BlankConstants.TAB+" BUFFER_POOL " + String.valueOf(indexMap.get("BUFFER_POOL")) + BlankConstants.NEW_LINE);
				ddlStr.append("\t ) \n ");
				
			}
			
			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation info {}" , VartechUtils.reflectionToString(dataParamInfo));
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		
		dataParamInfo.setObjectType(ObjectType.FUNCTION.name());
		
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			
			dataParamInfo.setObjectName(name);
			
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP FUNCTION " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			
			//ddlStr.append("CREATE OR REPLACE ");
	
			List srcList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			for (int i = 0; i < srcList.size(); i++) {
				ddlStr.append( srcList.get(i));
			}
			
			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {
		logger.debug(" Procedure DDL Generation...");
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			dataParamInfo.setObjectName(name);
			
			String objType = sqlSesseion.selectOne("sourceObjectType",dataParamInfo);
	
			List srcScriptList = null;
			if (StringUtils.contains(objType, "PROCEDURE")) {
				if(ddlOption.isAddDropClause()){
					ddlStr.append("/* DROP PROCEDURE " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
				}
				//ddlStr.append("CREATE OR REPLACE ");
				srcScriptList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < srcScriptList.size(); i++){
					ddlStr.append( srcScriptList.get(i));
				}
			} else if (StringUtils.contains(objType, "PACKAGE")) {
				
				if(ddlOption.isAddDropClause()){
					ddlStr.append(BlankConstants.NEW_LINE).append("/* DROP PACKAGE BODY " + name + "; */").append(BlankConstants.NEW_LINE_TWO);;
					ddlStr.append("/* DROP PACKAGE " + name + "; */").append(BlankConstants.NEW_LINE_TWO);
				}
				
				dataParamInfo.setObjectType("PACKAGE");
				//ddlStr.append("CREATE OR REPLACE ");
				srcScriptList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < srcScriptList.size(); i++) {
					ddlStr.append( srcScriptList.get(i));
				}
				
				dataParamInfo.setObjectType("PACKAGE BODY");
				ddlStr.append("/ \n\n ");
				//ddlStr.append("CREATE OR REPLACE ");
				srcScriptList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < srcScriptList.size(); i++) {
					ddlStr.append( srcScriptList.get(i));
				}
			}
			
			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);
			
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
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			dataParamInfo.setObjectName(name);
			
			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP TRIGGER " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			//ddlStr.append("CREATE OR REPLACE ");
			
			List scrList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			
			for (int i = 0; i < scrList.size(); i++) {
				ddlStr.append( scrList.get(i));
			}
			
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
			
			ddlStr.append(DDLTemplateFactory.getInstance().ddlRender(DBType.TIBERO.getDbVenderName(), "sequenceScript", param));
			
			ddlStr.append(BlankConstants.NEW_LINE_TWO);
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
		
		return reval;
	}
}