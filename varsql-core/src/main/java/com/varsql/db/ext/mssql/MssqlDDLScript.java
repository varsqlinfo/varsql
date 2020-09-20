package com.varsql.db.ext.mssql;

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
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.vartech.common.app.beans.ParamMap;

/**
 *
 * @FileName  : MssqlDDLScript.java
 * @프로그램 설명 : mssql ddl
 * @Date      : 2019. 1. 20.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class MssqlDDLScript extends DDLScriptImpl {
	private final Logger logger = LoggerFactory.getLogger(MssqlDDLScript.class);

	public MssqlDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory);
	}

	@Override
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		SqlSession client = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

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
				ddlStr.append((String) srcCommentList.get(i)).append(BlankConstants.NEW_LINE);
			}
			if(srcCommentList.size() > 0){
				ddlStr.append(BlankConstants.NEW_LINE);
			}

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		StringBuilder ddlStr;

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;

		boolean addFlag;
		for (String name : objNmArr) {

			ddlStr = new StringBuilder();

			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);

			dataParamInfo.setObjectName(name);

			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP ViEW " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> scrList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			addFlag = false;
			for (int i = 0; i < scrList.size(); i++) {
				if(addFlag==false && !"".equals(StringUtils.trim(scrList.get(i)))){
					addFlag = true;
				}

				if(addFlag){
					ddlStr.append(scrList.get(i));
				}
			}

			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"");

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		for (String name : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			dataParamInfo.setObjectName(name);

			ddlStr = new StringBuilder();
			List srcScriptList = sqlSesseion.selectList("indexScriptSource", dataParamInfo);

			Map indexMap;
			if (srcScriptList.size() > 0) {
				if(ddlOption.isAddDropClause()){
					ddlStr.append("/* DROP INDEX " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
				}

				for (int i = 0; i < srcScriptList.size(); i++) {
					indexMap = (Map) srcScriptList.get(i);

					ddlStr.append(indexMap.get("SOURCES"));
				}
			}

			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation...");

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

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
				ddlStr.append("/* DROP FUNCTION " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> scrList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			addFlag = false;
			for (int i = 0; i < scrList.size(); i++) {
				if(addFlag==false && !"".equals(StringUtils.trim(scrList.get(i)))){
					addFlag = true;
				}

				if(addFlag){
					ddlStr.append(scrList.get(i));
				}
			}

			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"");
			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		logger.debug(" Procedure DDL Generation...");

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
				ddlStr.append("/* DROP PROCEDURE " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> scrList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			addFlag = false;
			for (int i = 0; i < scrList.size(); i++) {
				if(addFlag==false && !"".equals(StringUtils.trim(scrList.get(i)))){
					addFlag = true;
				}

				if(addFlag){
					ddlStr.append(scrList.get(i));
				}
			}

			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"");

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
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

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

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

			List<String> scrList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			addFlag = false;
			for (int i = 0; i < scrList.size(); i++) {
				if(addFlag==false && !"".equals(StringUtils.trim(scrList.get(i)))){
					addFlag = true;
				}

				if(addFlag){
					ddlStr.append(scrList.get(i));
				}
			}

			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"");
			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
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
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

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

			ddlStr.append(DDLTemplateFactory.getInstance().ddlRender(DBType.MSSQL.getDbVenderName(), "sequenceScript", param));

			ddlStr.append(ddlOption.isAddLastSemicolon()?";":"");
			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(ddlStr.toString(),DBType.MSSQL));
			reval.add(ddlInfo);
		}

		return reval;
	}

}