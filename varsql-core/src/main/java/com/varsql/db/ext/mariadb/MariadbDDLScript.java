package com.varsql.db.ext.mariadb;

import java.util.ArrayList;
import java.util.List;

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
import com.varsql.core.db.valueobject.ddl.DDLTemplateParam;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : MariadbDDLScript.java
 * @프로그램 설명 : mariadb ddl
 * @Date      : 2019. 7. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class MariadbDDLScript extends AbstractDDLScript {
	private final Logger logger = LoggerFactory.getLogger(MariadbDDLScript.class);

	public MariadbDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.MARIADB);
	}

	@Override
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		
		SqlSession client = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;

		for(String objNm : objNmArr){

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);
			
			DataMap source = client.selectOne("tableScript", dataParamInfo);
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.sourceText(StringUtils.trim(source.getString("Create Table")))
			.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TABLE.create, param));
				
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		
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
					.sourceText(source.getString("Create View"))
				.build();

			ddlInfo.setChangeFormat(false);
			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.VIEW.create, param));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);

			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.items(sqlSesseion.selectList("indexScript", dataParamInfo))
			.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.INDEX.create, param));
			
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation...");

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		
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
					.sourceText(source.getString("Create Function"))
				.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.FUNCTION.create, param));
			ddlInfo.setChangeFormat(false);

			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		logger.debug(" Procedure DDL Generation...");

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
				.sourceText(source.getString("Create Procedure"))
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
		
		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());

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
				.sourceText(source.getString("SQL Original Statement"))
			.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TRIGGER.create, param));
			ddlInfo.setChangeFormat(false);

			reval.add(ddlInfo);
		}

		return reval;
	}
}