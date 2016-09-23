package com.varsql.web.app.database.db2;
import com.varsql.web.common.vo.DataCommonVO;

public interface Db2Service  {
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

	
}