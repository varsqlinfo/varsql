package com.varsql.web.app.database;
import javax.servlet.http.HttpServletResponse;

import com.varsql.web.common.vo.DataCommonVO;

public interface SQLService  {
	String sqlData(DataCommonVO paramMap) throws Exception;
	String sqlFormat(DataCommonVO paramMap) throws Exception;
	void dataExport(DataCommonVO paramMap, HttpServletResponse response) throws Exception;
	void columnInfoExport(DataCommonVO paramMap, HttpServletResponse response)throws Exception;
}