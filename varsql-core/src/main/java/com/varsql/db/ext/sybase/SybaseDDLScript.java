package com.varsql.db.ext.sybase;

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
import com.varsql.core.sql.DDLTemplateCode;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.DDLTemplateFactory;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.StringUtils;

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
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
			StringBuilder ddlStr;
	
			for(String name : objNmArr){
	
				ddlStr = new StringBuilder();
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(name);
				dataParamInfo.setObjectName(name);
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(dataParamInfo.getObjectName())
					.ddlOpt(ddlOption)
					.columnList(sqlSession.selectList("tableScriptColumn", dataParamInfo))
					.keyList(sqlSession.selectList("tableConstraints", dataParamInfo))
					.commentsList(sqlSession.selectList("tableColumnComments",dataParamInfo))
				.build();
	
				ddlStr.append(DDLTemplateFactory.getInstance().render(dbType, DDLTemplateCode.TABLE.create, param));
	
				ddlInfo.setCreateScript(ddlStr.toString());
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
				
				StringBuilder sourceSb = new StringBuilder();
				
				List<DataMap> scrList = sqlSession.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < scrList.size(); i++) {
					sourceSb.append(scrList.get(i).getString("SOURCE_TEXT"));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(StringUtils.trim(sourceSb.toString()))
				.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.VIEW.create, param));
				
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
	
			for (String objNm : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.items(sqlSession.selectList("indexScript", dataParamInfo))
				.build();
	
				ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.INDEX.create, param), dbType));
	
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation...");
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
	
			for (String objNm : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
	
				dataParamInfo.setObjectName(objNm);
	
				StringBuilder sourceSb = new StringBuilder();
				List<DataMap> scrList = sqlSession.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < scrList.size(); i++) {
					sourceSb.append(scrList.get(i).getString("SOURCE_TEXT"));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(StringUtils.trim(sourceSb.toString()))
				.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.FUNCTION.create, param));
				
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Procedure DDL Generation... ");
		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
	
			for (String objNm : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
	
				StringBuilder sourceSb = new StringBuilder();
				List<DataMap> scrList = sqlSession.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < scrList.size(); i++) {
					sourceSb.append(scrList.get(i).getString("SOURCE_TEXT"));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(StringUtils.trim(sourceSb.toString()))
				.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.PROCEDURE.create, param));
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
				List<DataMap> scrList = sqlSession.selectList("objectScriptSource", dataParamInfo);
				for (int i = 0; i < scrList.size(); i++) {
					sourceSb.append(scrList.get(i).getString("SOURCE_TEXT"));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(StringUtils.trim(sourceSb.toString()))
				.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TRIGGER.create, param));
				
				reval.add(ddlInfo);
			}
		}

		return reval;
	}
}