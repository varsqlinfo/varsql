package com.varsql.core.db.report.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.junit.jupiter.api.Test;

import com.varsql.core.db.report.VarsqlReportConfig;
import com.varsql.core.test.BaseTest;
import com.vartech.common.excel.ExcelHeaderVO;
import com.vartech.common.excel.ExcelReport;
import com.vartech.common.excel.ExcelReportVO;

class TableInfoReportTest extends BaseTest{
	
	@Test
	public void testExport() throws IOException{
		
		ExcelReportVO[] columnArr1 = new ExcelReportVO[VarsqlReportConfig.TABLE_COLUMN.values().length];
		
		int idx = 0;
		for(VarsqlReportConfig.TABLE_COLUMN tableInfo : VarsqlReportConfig.TABLE_COLUMN.values()){
			columnArr1[idx] = new ExcelReportVO(tableInfo.colName() , tableInfo.name() , tableInfo.colWidth() , tableInfo.getAlign());
			
			idx++;
		}
		
		List<Map> dataList =new ArrayList<Map>();
		
		Map tmp;
		for( int i =0 ;i <100; i++){
			tmp = new HashMap();
			
			tmp.put("NO",i);
			tmp.put("NAME", "한글1 "+i);
			tmp.put("DATATYPE", "한글2 "+i);
			tmp.put("TYPENAME", "한글3 "+i);
			tmp.put("LENGTH", "aawefe"+i);
			tmp.put("NULLABLE", "aawefe"+i);
			tmp.put("COMMENT", "aawefe"+i);
			tmp.put("PRIMAYKEY", "aawefe"+i);
			tmp.put("AUTOINCREMENT", "aawefe"+i);
			tmp.put("DEFAULTVAL", "aawefe"+i);
			
			dataList.add(tmp);
		}
		
		List<List<ExcelHeaderVO>> haderInfoList = new ArrayList<List<ExcelHeaderVO>>();
		haderInfoList.add(new ExcelHeaderVO.Builder()
			.addHeaderVO("Database", 0, 1).addHeaderVO("asdf ", 2, 3 , HSSFColor.HSSFColorPredefined.WHITE.getIndex())
			.addHeaderVO("Table Name", 4, 5).addHeaderVO("asdf", 6, 7, HSSFColor.HSSFColorPredefined.WHITE.getIndex()).build());
		
		haderInfoList.add(new ExcelHeaderVO.Builder()
				.addHeaderVO("Table Name", 0, 1).addHeaderVO("asdf2", 2, 3 , HSSFColor.HSSFColorPredefined.WHITE.getIndex())
				.addHeaderVO("Entity", 4, 5).addHeaderVO("ccc4", 6, 7, HSSFColor.HSSFColorPredefined.WHITE.getIndex()).build());
		
		haderInfoList.add(new ExcelHeaderVO.Builder()
				.addHeaderVO("Desc", 0, 1).addHeaderVO("asdf2", 2, 7 , HSSFColor.HSSFColorPredefined.WHITE.getIndex()).build());
		
		
		ExcelReport report = new ExcelReport();
		
		report.setColumnArr(columnArr1);
		report.setHaderInfoList(haderInfoList);
		
		report.createSheet("asdfasdf");
		
		report.addHeaderInfo(true);
		
		report.addListRow(dataList);
		
		report.write("c:/zzz/hugeDataTest.xlsx");
		
		//System.out.println("1111111111111111111 : end");
	}
	
	

}
	
