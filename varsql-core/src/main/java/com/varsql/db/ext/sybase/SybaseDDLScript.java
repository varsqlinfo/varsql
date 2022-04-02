package com.varsql.db.ext.sybase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 *
 * @FileName  : SybaseDBMeta.java
 * @프로그램 설명 : Sybase ddl
 * @Date      : 2021. 2. 06.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SybaseDDLScript extends AbstractDDLScript {
	private final Logger logger = LoggerFactory.getLogger(SybaseDDLScript.class);


	public SybaseDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.SYBASE);
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

			Map param = getDefaultTableTemplateParam(ddlOption, dataParamInfo, client.selectList("tableScript", dataParamInfo), client.selectList("tableScriptPk", dataParamInfo), client.selectList("tableScriptComments",dataParamInfo));

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.TABLE.create, param));

			ddlInfo.setCreateScript(ddlStr.toString());
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

			Map param = getDefaultTemplateParam(ddlOption, dataParamInfo, sqlSesseion.selectList("objectScriptSource", dataParamInfo));

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.VIEW.create, param));

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

			Map param = getDefaultTemplateParam(ddlOption, dataParamInfo, sqlSesseion.selectList("indexScript", dataParamInfo));

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.INDEX.create, param));

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

			Map param = getDefaultTemplateParam(ddlOption, dataParamInfo, sqlSesseion.selectList("objectScriptSource", dataParamInfo));

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

			Map param = getDefaultTemplateParam(ddlOption, dataParamInfo, sqlSesseion.selectList("objectScriptSource", dataParamInfo));

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
		
		for (String name : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			dataParamInfo.setObjectName(name);

			Map param = getDefaultTemplateParam(ddlOption, dataParamInfo, sqlSesseion.selectList("objectScriptSource", dataParamInfo));

			ddlStr.append(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.TRIGGER.create, param));

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