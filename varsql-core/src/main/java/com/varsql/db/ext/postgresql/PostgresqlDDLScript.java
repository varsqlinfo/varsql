package com.varsql.db.ext.postgresql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.ddl.script.AbstractDDLScript;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.db.valueobject.ddl.DDLTemplateParam;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.vartech.common.app.beans.DataMap;

/**
 *
 * @FileName  : PostgresqlDDLScript.java
 * @프로그램 설명 : Postgresql ddl
 * @Date      : 2019. 1. 20.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class PostgresqlDDLScript extends AbstractDDLScript {
	private final Logger logger = LoggerFactory.getLogger(PostgresqlDDLScript.class);

	public PostgresqlDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.POSTGRESQL);
	}

	@Override
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		SqlSession client = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;

		for(String objNm : objNmArr){

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.columnList(client.selectList("tableScriptColumn", dataParamInfo))
				.keyList(client.selectList("tableConstraints", dataParamInfo))
				.commentsList(client.selectList("tableColumnComments",dataParamInfo))
			.build();
			
			param.setSourceText(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TABLE.constraintKey, param));
			
			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TABLE.create, param));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;

		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);

			dataParamInfo.setObjectName(objNm);

			DataMap source = sqlSesseion.selectOne("viewScript", dataParamInfo);

			DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(source.getString("VIEW_SOURCE"))
				.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.VIEW.create, param));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;

		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);

			DataMap source = sqlSesseion.selectOne("indexScript", dataParamInfo);
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.sourceText(source.getString(MetaColumnConstants.CREATE_SOURCE))
			.build();

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.INDEX.create, param), dbType));
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

		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);

			dataParamInfo.setObjectName(objNm);
			
			DataMap source = sqlSesseion.selectOne("functionScript", dataParamInfo);
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.sourceText(source.getString(MetaColumnConstants.CREATE_SOURCE))
			.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.FUNCTION.create, param));
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

		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);

			DataMap source = sqlSesseion.selectOne("procedureScript", dataParamInfo);
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.sourceText(source.getString(MetaColumnConstants.CREATE_SOURCE))
			.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.PROCEDURE.create, param));
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
		
		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);
			
			DataMap source = sqlSesseion.selectOne("triggerScript", dataParamInfo);
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.sourceText(source.getString(MetaColumnConstants.CREATE_SOURCE))
			.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TRIGGER.create, param));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public String getStandardDefaultValue(String val, DataType type) {
		int lastIdx = val.lastIndexOf("::");
		
		if(lastIdx > -1) {
			return super.getStandardDefaultValue(val.substring(0,lastIdx), type);
		}
		return super.getStandardDefaultValue(val, type);
	}
}