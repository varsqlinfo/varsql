package com.varsql.db.ext.cubrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.ddl.script.AbstractDDLScript;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.vartech.common.app.beans.ParamMap;

/**
 *
 * @FileName  : CubridDDLScript.java
 * @프로그램 설명 : Cubrid ddl
 * @Date      : 2019. 1. 20.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class CubridDDLScript extends AbstractDDLScript {
	private final Logger logger = LoggerFactory.getLogger(CubridDDLScript.class);

	public CubridDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.CUBRID);
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

			ParamMap source = client.selectOne("tableScript", dataParamInfo);

			ddlStr.append(source.getString("CREATE TABLE"));

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));

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
			ddlStr.append(source.getString("Create View"));

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));
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

			List srcScriptList = sqlSesseion.selectList("indexScript", dataParamInfo);

			Map indexMap;

			if(ddlOption.isAddDropClause()){
				if (srcScriptList.size() > 0) {
					indexMap = (Map) srcScriptList.get(0);
					ddlStr.append("/* DROP INDEX " + dataParamInfo.getObjectName() + " ON ").append(indexMap.get("TABLE_NAME")).append("; */").append(BlankConstants.NEW_LINE_TWO);
				}
			}

			ddlStr.append("CREATE ");


			if (srcScriptList.size() > 0) {
				indexMap = (Map) srcScriptList.get(0);
				if ("UQ".equals(indexMap.get("INDEX_TYPE")))
					ddlStr.append(" UNIQUE INDEX ");
				else {
					ddlStr.append(" INDEX ");
				}

				ddlStr.append( indexMap.get("INDEX_NAME") + " ON ");
				ddlStr.append( indexMap.get("TABLE_NAME") + "\n ( ");

				for (int i = 0; i < srcScriptList.size(); i++) {
					indexMap = (Map) srcScriptList.get(i);

					ddlStr.append(i > 0 ?", " :"");
					ddlStr.append( indexMap.get("COLUMN_NAME"));
					ddlStr.append(" ").append( indexMap.get("ASC_OR_DESC"));
				}
				ddlStr.append(")").append(BlankConstants.NEW_LINE);
			}

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));
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

		for (String name : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();

			dataParamInfo.setObjectName(name);

			if(ddlOption.isAddDropClause()){
				ddlStr.append("/* DROP FUNCTION " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<Map> scriptInfoList = sqlSesseion.selectList("functionScript", dataParamInfo);

			Map param = scriptInfoList.get(0);

			StringBuffer argsSb = new StringBuffer();
			for (int i = 0; i < scriptInfoList.size(); i++) {
				Map scriptInfoMap = scriptInfoList.get(i);
				argsSb.append(scriptInfoMap.get("")).append(scriptInfoMap.get(""));

			}

			param.put("schema", dataParamInfo.getSchema());

			param.put("ddlOption", ddlOption);
			param.put("ARGUMENTS",argsSb.toString());

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.FUNCTION.create, param));

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Procedure DDL Generation... ");

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

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

			List<Map> scriptInfoList = sqlSesseion.selectList("procedureScript", dataParamInfo);

			Map param = scriptInfoList.get(0);

			StringBuffer argsSb = new StringBuffer();
			for (int i = 0; i < scriptInfoList.size(); i++) {
				Map scriptInfoMap = scriptInfoList.get(i);
				argsSb.append(scriptInfoMap.get("")).append(scriptInfoMap.get(""));

			}

			param.put("schema", dataParamInfo.getSchema());

			param.put("ddlOption", ddlOption);
			param.put("ARGUMENTS",argsSb.toString());

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.PROCEDURE.create, param));

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));
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

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));
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

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.SEQUENCE.create, param));

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(VarsqlFormatterUtil.addLastSemicolon(ddlStr, ddlOption), dbType));
			reval.add(ddlInfo);
		}

		return reval;
	}

}