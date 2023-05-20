package com.varsql.core.db.ddl.script;

import java.util.List;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.db.valueobject.ddl.DDLTemplateParam;
import com.varsql.core.sql.CommentType;


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
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption,String ...objNames) throws Exception;
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
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames) throws Exception;
	
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
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNames) throws Exception;
	
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
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNames) throws Exception;
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
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNames) throws Exception;
	
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
	public List<DDLInfo> getTriggers(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNames) throws Exception;
	
	/**
	 * getSequence
	 * @param dataParamInfo
	 * @param ddlOption
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getSequences(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, String ...objNames) throws Exception;
	
	
	/**
	 * DB 테이블 변환 데이터 구하기
	 * 
	 * @param dataParamInfo
	 * @param ddlOption
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> tableDdlConvertDB(DatabaseParamInfo dataParamInfo,DDLCreateOption ddlOption, DBVenderType convertDB ,String ...objNames) throws Exception;
	
	
	/**
	 * 표준 코멘트 정보 얻기
	 * @param comment
	 * @return
	 */
	public String getStandardDefaultValue(String val, DataType type);
	
	/**
	 * 표준 기본값 -> db 별 값 얻기
	 * @param val
	 * @param type
	 * @return
	 */
	public String getDefaultValueToVenderValue(String val, DataType type);
}