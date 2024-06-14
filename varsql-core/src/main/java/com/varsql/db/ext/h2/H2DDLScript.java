package com.varsql.db.ext.h2;

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
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : MariadbDDLScript.java
 * @프로그램 설명 : mariadb ddl
 * @Date      : 2019. 7. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class H2DDLScript extends AbstractDDLScript {
	private final Logger logger = LoggerFactory.getLogger(H2DDLScript.class);

	public H2DDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.H2);
	}

	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation...");

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		
		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
			
			for (String name : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(name);
				dataParamInfo.setObjectName(name);
	
				DDLTemplateParam param = getDefaultTemplateParam(ddlOption, dataParamInfo, null);
				param.setSourceText(StringUtils.trim(sqlSession.selectOne("functionScript", dataParamInfo)));
				
				String tempateSource = SQLTemplateFactory.getInstance().sqlRender(DBVenderType.H2, SQLTemplateCode.FUNCTION.create, param);
				
				ddlInfo.setChangeFormat(false);
				ddlInfo.setCreateScript(tempateSource);
	
				reval.add(ddlInfo);
			}
		}

		return reval;
	}
	
	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		dataParamInfo.setSchema(dataParamInfo.getSchema().toUpperCase());
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		
		try(SqlSession sqlSession = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			DDLInfo ddlInfo;
			for (String name : objNmArr) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(name);
				dataParamInfo.setObjectName(name);
	
				DDLTemplateParam param = getDefaultTemplateParam(ddlOption, dataParamInfo, sqlSession.selectList("indexScript", dataParamInfo));
				ddlInfo.setCreateScript(SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.INDEX.create, param));
				
				reval.add(ddlInfo);
			}
		}

		return reval;
	}
}