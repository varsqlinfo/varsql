package com.varsql.db.ext.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.ddl.script.DDLScriptImpl;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.vartech.common.app.beans.ParamMap;

/**
 * 
 * @FileName  : MysqlDDLScript.java
 * @프로그램 설명 : mysql ddl
 * @Date      : 2019. 7. 3. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class MysqlDDLScript extends DDLScriptImpl {
	Logger logger = LoggerFactory.getLogger(MysqlDDLScript.class);
	
	
	public MysqlDDLScript(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory);
	}
	
	@Override
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		
		SqlSession client = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
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
			
			ddlStr.append(source.getString("Create Table")).append(BlankConstants.NEW_LINE_TWO);
			ddlInfo.setCreateScript(ddlStr.toString());
			
			reval.add(ddlInfo);
		}

		return reval;
	}
	
	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		
		StringBuilder ddlStr;
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
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
			
			ddlStr.append(source.getString("Create View")).append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}
	
		return reval;
	}
	
	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			dataParamInfo.setObjectName(name);
			ddlInfo.setCreateScript("");
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		logger.debug(" Function DDL Generation...");
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
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
				ddlStr.append("/* DROP FUNCTION " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			ParamMap source = sqlSesseion.selectOne("functionScript", dataParamInfo);
			
			ddlStr.append(source.getString("Create Function")).append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			
			reval.add(ddlInfo);
		}
		
		return reval;
	}
	
	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {
		
		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
		logger.debug(" Procedure DDL Generation...");
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		StringBuilder ddlStr;
		boolean addFlag;
		for (String name : objNmArr) {
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			ddlStr = new StringBuilder();
			dataParamInfo.setObjectName(name);
			
			ParamMap source = sqlSesseion.selectOne("procedureScript", dataParamInfo);
			
			ddlStr.append(source.getString("Create Procedure")).append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
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

		SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());
		
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
			
			ParamMap source = sqlSesseion.selectOne("triggerScript", dataParamInfo);
			
			ddlStr.append(source.getString("Create Trigger")).append(BlankConstants.NEW_LINE_TWO);
			
			ddlInfo.setCreateScript(ddlStr.toString());
			reval.add(ddlInfo);
		}

		return reval;
	}
}