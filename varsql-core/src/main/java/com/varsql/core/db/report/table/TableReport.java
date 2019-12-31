package com.varsql.core.db.report.table;

import java.util.List;
import java.util.Map;

import com.varsql.core.db.beans.DatabaseParamInfo;
import com.vartech.common.excel.ExcelReport;

public interface TableReport{
	
	public ExcelReport columnsInfo(DatabaseParamInfo dataParamInfo, List<Map> columnInfoList,boolean addTableDefinitionFlag ,boolean multiSheetFlag, String... tableNm) throws Exception;
}
