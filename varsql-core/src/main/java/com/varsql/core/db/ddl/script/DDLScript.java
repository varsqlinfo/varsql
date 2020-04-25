package com.varsql.core.db.ddl.script;

import java.util.List;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;


public interface DDLScript{
	/**
	 * 
	 * @Method Name  : getTableScript
	 * @Method 설명 : 테이블 생성 스크립트 
	 * @작성일   : 2015. 6. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption,String ...objNm) throws Exception;
	/**
	 * 
	 * @Method Name  : getView
	 * @Method 설명 : view ddl 생성
	 * @작성일   : 2015. 6. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNm) throws Exception;
	
	/**
	 * 
	 * @Method Name  : getIndex
	 * @Method 설명 : index ddl 생성
	 * @작성일   : 2015. 6. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNm) throws Exception;
	
	/**
	 * 
	 * @Method Name  : getFunction
	 * @Method 설명 : function ddl 생성
	 * @작성일   : 2015. 6. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNm) throws Exception;
	/**
	 * 
	 * @Method Name  : getProcedure
	 * @Method 설명 : procedure ddl 생성
	 * @작성일   : 2015. 6. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNm) throws Exception;
	
	/**
	 * 
	 * @Method Name  : getTrigger
	 * @Method 설명 : procedure out parameter
	 * @작성일   : 2015. 6. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getTriggers(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNm) throws Exception;
	
	/**
	 * 
	 * @Method Name  : getSequence
	 * @Method 설명 : get sequence
	 * @작성일   : 2018. 6. 1. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dataParamInfo
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getSequences(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNm) throws Exception;
	
}