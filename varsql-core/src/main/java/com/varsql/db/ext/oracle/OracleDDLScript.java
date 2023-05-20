package com.varsql.db.ext.oracle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.ddl.script.AbstractDDLScript;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.db.valueobject.ddl.DDLTemplateParam;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.VartechUtils;

/**
 *
 * @FileName  : OracleDDLScript.java
 * @프로그램 설명 : oracle ddl
 * @Date      : 2019. 7. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class OracleDDLScript extends AbstractDDLScript {
	private final Logger logger = LoggerFactory.getLogger(OracleDDLScript.class);
	
	public OracleDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.ORACLE);
		
		addDefaultValueToVenderValue("CURRENT_TIMESTAMP", "sysdate");
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
			
			DataMap sourceInfo = new DataMap();
			
			StringBuilder sourceSb = new StringBuilder();
			List<String> viewHeaderList = sqlSesseion.selectList("viewHeaderScript", dataParamInfo);
			boolean firstCheck = true;
			for (String source :viewHeaderList) {
				sourceSb.append(firstCheck ? "" :", ").append(source);
				firstCheck = false;
			}
			
			sourceInfo.put("header", sourceSb.toString());

			List<String> srcViewBodyList = sqlSesseion.selectList("viewBodyScript", dataParamInfo);
			sourceSb = new StringBuilder();
			for (String source :srcViewBodyList) {
				sourceSb.append(source).append(BlankConstants.NEW_LINE);
			}
			sourceInfo.put("body", sourceSb.toString());
			
			DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.item(sourceInfo)
				.build();

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.VIEW.create, param), dbType));
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

			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(objNm)
				.ddlOpt(ddlOption)
				.items(sqlSesseion.selectList("indexScriptSource", dataParamInfo))
			.build();

			ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.INDEX.create, param), dbType));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation info {}" , VartechUtils.reflectionToString(dataParamInfo));
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;

		dataParamInfo.setObjectType(ObjectType.FUNCTION.name());

		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			dataParamInfo.setObjectName(objNm);

			StringBuilder sourceSb = new StringBuilder();
			List<String> srcProcList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				sourceSb.append( srcProcList.get(j));
			}

			DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(sourceSb.toString())
				.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.FUNCTION.create, param));
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {
		logger.debug(" Procedure DDL Generation...");

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
			.build();
			
			reval.add(ddlInfo);
			
			String objType = sqlSesseion.selectOne("sourceObjectType",dataParamInfo);
			if (StringUtils.contains(objType, "PROCEDURE")) {
				
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
				for (int j = 0; j < srcList.size(); j++) {
					sourceSb.append( srcList.get(j));
				}
				param.setSourceText(sourceSb.toString());
				
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.PROCEDURE.create, param));
			} else if (StringUtils.contains(objType, "PACKAGE")) {
				DataMap sourceInfo = new DataMap();
				
				dataParamInfo.setObjectType("PACKAGE");
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < srcList.size(); i++) {
					sourceSb.append( srcList.get(i));
				}
				sourceInfo.put("package", sourceSb.toString());

				sourceSb = new StringBuilder();
				dataParamInfo.setObjectType("PACKAGE BODY");
				srcList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < srcList.size(); i++) {
					sourceSb.append( srcList.get(i));
				}
				sourceInfo.put("packageBody", sourceSb.toString());
				
				param.setItem(sourceInfo);
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.PACKAGE.create, param));
			}
			
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
			
			StringBuilder sourceSb = new StringBuilder();
			List<String> srcProcList = sqlSesseion.selectList("objectScriptSource", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				sourceSb.append( srcProcList.get(j));
			}

			DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(sourceSb.toString())
				.build();

			ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TRIGGER.create, param));
			
			reval.add(ddlInfo);
		}

		return reval;
	}
}