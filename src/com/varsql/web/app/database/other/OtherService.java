package com.varsql.web.app.database.other;
import com.varsql.web.common.vo.DataCommonVO;

public interface OtherService  {
	String serviceMenu(DataCommonVO paramMap) throws Exception;
	String schemas(DataCommonVO paramMap) throws Exception;
	String tables(DataCommonVO paramMap) throws Exception;
	String views(DataCommonVO paramMap) throws Exception;
	String procedures(DataCommonVO paramMap) throws Exception;
	String functions(DataCommonVO paramMap) throws Exception;
	String tableMetadata(DataCommonVO paramMap) throws Exception;
	String viewMetadata(DataCommonVO paramMap) throws Exception;
	String procedureMetadata(DataCommonVO paramMap) throws Exception;
	String functionMetadata(DataCommonVO paramMap) throws Exception;
	String ddlTableScript(DataCommonVO paramMap) throws Exception;
	String ddlViewScript(DataCommonVO paramMap) throws Exception;
	String ddlProcedureScript(DataCommonVO paramMap) throws Exception;
	String ddlFunctionScript(DataCommonVO paramMap) throws Exception;

	
}