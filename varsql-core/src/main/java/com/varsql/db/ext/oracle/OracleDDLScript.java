package com.varsql.db.ext.oracle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
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
import com.varsql.core.sql.DDLTemplateCode;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.DDLTemplateFactory;
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
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

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
					.columnList(sqlSession.selectList("tableScriptColumn", dataParamInfo))
					.keyList(sqlSession.selectList("tableConstraints", dataParamInfo))
					.commentsList(sqlSession.selectList("tableColumnComments",dataParamInfo))
				.build();
				
				param.setSourceText(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TABLE.constraintKey, param));
				
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TABLE.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
			for (String objNm : objNmArr) {
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
	
				dataParamInfo.setObjectName(objNm);
				
				DataMap sourceInfo = new DataMap();
				
				StringBuilder sourceSb = new StringBuilder();
				List<String> viewHeaderList = sqlSession.selectList("viewHeaderScript", dataParamInfo);
				boolean firstCheck = true;
				for (String source :viewHeaderList) {
					sourceSb.append(firstCheck ? "" :", ").append(source);
					firstCheck = false;
				}
				
				sourceInfo.put("header", sourceSb.toString());
	
				List<String> srcViewBodyList = sqlSession.selectList("viewBodyScript", dataParamInfo);
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
						.commentsList(sqlSession.selectList("tableColumnComments",dataParamInfo))
					.build();
	
				ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.VIEW.create, param), dbType));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.ddlOpt(ddlOption)
			.build();
			
			boolean dbmsAuthError = false; 
			for (String objNm : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				param.setObjectName(objNm);
				List items = null;
				String sql = "";
				try {
					if(dbmsAuthError) {
						items =sqlSession.selectList("indexScript", dataParamInfo);
					}else {
						sql = StringUtils.trim(sqlSession.selectOne("indexScriptSource", dataParamInfo));
					}
				}catch(Exception e) {
					dbmsAuthError = true; 
					logger.error("index ddl error : ", e.getMessage());
					if(e instanceof PersistenceException) {
						sql = "/*Error Reading Source;\n" +e.getCause().getMessage()+"\n*/";
					}else {
						sql = "/*Error Reading Source;\n" +e.getMessage()+"\n*/";
					}
				}
				
				if(dbmsAuthError) {
					param.setSourceText("");
					if(items == null) {
						items =sqlSession.selectList("indexScript", dataParamInfo);
					}
					param.setItems(items);
				}else {
					param.setSourceText(sql);
				}
				ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.INDEX.create, param), dbType));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation info {}" , VartechUtils.reflectionToString(dataParamInfo));
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
	
			dataParamInfo.setObjectType(ObjectType.FUNCTION.name());
	
			for (String objNm : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
	
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcProcList = sqlSession.selectList("objectScriptSource", dataParamInfo);
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
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.FUNCTION.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {
		logger.debug(" Procedure DDL Generation...");
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

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
				
				String objType = sqlSession.selectOne("sourceObjectType",dataParamInfo);
				if (StringUtils.contains(objType, "PROCEDURE")) {
					
					StringBuilder sourceSb = new StringBuilder();
					List<String> srcList = sqlSession.selectList("objectScriptSource", dataParamInfo);
					for (int j = 0; j < srcList.size(); j++) {
						sourceSb.append( srcList.get(j));
					}
					param.setSourceText(sourceSb.toString());
					
					ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.PROCEDURE.create, param));
				} else if (StringUtils.contains(objType, "PACKAGE")) {
					DataMap sourceInfo = new DataMap();
					
					dataParamInfo.setObjectType("PACKAGE");
					StringBuilder sourceSb = new StringBuilder();
					List<String> srcList = sqlSession.selectList("objectScriptSource", dataParamInfo);
					for (int i = 0; i < srcList.size(); i++) {
						sourceSb.append( srcList.get(i));
					}
					sourceInfo.put("package", sourceSb.toString());
	
					sourceSb = new StringBuilder();
					dataParamInfo.setObjectType("PACKAGE BODY");
					srcList = sqlSession.selectList("objectScriptSource", dataParamInfo);
					for (int i = 0; i < srcList.size(); i++) {
						sourceSb.append( srcList.get(i));
					}
					sourceInfo.put("packageBody", sourceSb.toString());
					
					param.setItem(sourceInfo);
					ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.PACKAGE.create, param));
				}
				
				reval.add(ddlInfo);
			}
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
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
			for (String objNm : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcProcList = sqlSession.selectList("objectScriptSource", dataParamInfo);
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
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TRIGGER.create, param));
				
				reval.add(ddlInfo);
			}
		}

		return reval;
	}
}