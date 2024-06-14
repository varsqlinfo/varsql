package com.varsql.db.ext.cubrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.vartech.common.app.beans.DataMap;

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

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		
		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
	
			for(String objNm : objNmArr){
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				DataMap source = sqlSession.selectOne("tableScript", dataParamInfo);
				
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(source.getString("CREATE TABLE"))
				.build();
				
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TABLE.create, param));
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
	
				DataMap source = sqlSession.selectOne("viewScript", dataParamInfo);
				
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.sourceText(source.getString("Create View"))
				.build();
				
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.VIEW.create, param));
				
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
	
				ddlInfo.setCreateScript(VarsqlFormatterUtil.ddlFormat(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.INDEX.create, param), dbType));
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
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.item(sqlSession.selectOne("functionScript", dataParamInfo))
				.build();
	
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.FUNCTION.create, param));
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
	
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.item(sqlSession.selectOne("procedureScript", dataParamInfo))
				.build();
	
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.PROCEDURE.create, param));
	
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
				List<String> srcList = sqlSession.selectList("objectScriptSource", dataParamInfo);
				for (int j = 0; j < srcList.size(); j++) {
					sourceSb.append(srcList.get(j));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(StringUtils.trim(sourceSb.toString()))
					.build();
	
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(this.dbType, SQLTemplateCode.TRIGGER.create, param));
	
				reval.add(ddlInfo);
			}
		}

		return reval;
	}
}