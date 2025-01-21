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
import com.varsql.core.sql.DDLTemplateCode;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.template.DDLTemplateFactory;
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
				
				if(source != null) {
					DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(source.getString("CREATE TABLE"))
					.build();
					
					ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TABLE.create, param));
				}else {
					ddlInfo.setCreateScript("");
				}
				
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
				
				if(source != null) {
					DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(source.getString("Create View"))
					.build();
					
					ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.VIEW.create, param));
				}else {
					ddlInfo.setCreateScript("");
				}
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
				
				List objInfos = sqlSession.selectList("functionScript", dataParamInfo);
				
				if(objInfos != null && objInfos.size() > 0) {
					DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.item(objInfos.get(0))
						.items(objInfos)
					.build();
		
					ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(dbType, DDLTemplateCode.FUNCTION.create, param));
				}else {
					ddlInfo.setCreateScript("");
				}
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
				
				List objInfos = sqlSession.selectList("procedureScript", dataParamInfo);
				
				if(objInfos != null && objInfos.size() > 0) {
					DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.item(objInfos.get(0))
						.items(objInfos)
					.build();
		
					ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(dbType, DDLTemplateCode.FUNCTION.create, param));
				}else {
					ddlInfo.setCreateScript("");
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
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TRIGGER.create, param));
	
				reval.add(ddlInfo);
			}
		}

		return reval;
	}
}