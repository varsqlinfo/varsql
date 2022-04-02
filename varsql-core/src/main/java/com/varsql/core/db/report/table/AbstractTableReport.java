package com.varsql.core.db.report.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.report.VarsqlReportConfig;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.vartech.common.excel.ExcelCellStyle;
import com.vartech.common.excel.ExcelHeaderVO;
import com.vartech.common.excel.ExcelReport;
import com.vartech.common.excel.ExcelReportVO;
import com.vartech.common.report.ExcelConstants;

/**
 * 
 * @FileName  : AbstractTableReport.java
 * @프로그램 설명 : 테이블 명세서 내보내기.
 * @Date      : 2017. 11. 21. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class AbstractTableReport implements TableReport{
	
	protected AbstractTableReport(){
	}
	
	@Override
	public ExcelReport columnsInfo(DatabaseParamInfo dataParamInfo, List<Map> columnInfoList, boolean addTableDefinitionFlag, boolean multiSheetFlag, String... tableNmArr) throws Exception {
		ExcelReport report;
		
		List<TableInfo> tableList= MetaControlFactory.getDbInstanceFactory(dataParamInfo.getDbType()).getDBObjectMeta(ObjectType.TABLE.getObjectTypeId(),dataParamInfo,tableNmArr );
		
		List<String> tableNames= Arrays.asList(tableNmArr);
		
		Collections.sort(tableList, new Comparator<TableInfo>() {
			@Override
			public int compare(TableInfo o1, TableInfo o2) {
				return tableNames.indexOf(o1.getName()) > tableNames.indexOf(o2.getName()) ? 1 : -1;
			}
		});
		
		// 테이블 요약 정보 추가 여부. 
		if(addTableDefinitionFlag){
			List<ExcelReportVO> tableLabelInfo = new ArrayList<ExcelReportVO>();
			
			for(VarsqlReportConfig.TABLE tableInfo:VarsqlReportConfig.TABLE.values()){
				tableLabelInfo.add(
					ExcelReportVO.builder().key(tableInfo.getKey()).name(tableInfo.getLabel()).width(tableInfo.getWidth())
					.cellStyle(ExcelCellStyle.builder().align(ExcelConstants.ALIGN.CENTER).build())
					.headerCellStyle(ExcelCellStyle.builder().align(ExcelConstants.ALIGN.CENTER).bgColor(ExcelConstants.DEFAULT_LABEL_BG_COLOR).bold(true).build())
					.build()
				);
			}
			
			ExcelReportVO [] labelInfoArr =  tableLabelInfo.toArray(new ExcelReportVO[tableLabelInfo.size()]);
			report = new ExcelReport("Table Info", labelInfoArr);
			
			Map<String,String> tableInfoMap = new HashMap<String,String>();
			
			report.createSheet();
			report.addLabelRow();
			
			tableInfoMap.put(VarsqlReportConfig.TABLE.DATABASE.getKey(), dataParamInfo.getSchema());
			for (TableInfo tableInfo : tableList) {
				tableInfoMap.put(VarsqlReportConfig.TABLE.NAME.getKey(), tableInfo.getName());
				tableInfoMap.put(VarsqlReportConfig.TABLE.COMMENT.getKey(), tableInfo.getRemarks());
				report.addRow(tableInfoMap);
			}
		}else{
			report = new ExcelReport();
		}
		
		Map<String ,String> headerInfo = new HashMap<String ,String>();
		headerInfo.put("{databaseName}", dataParamInfo.getSchema());
		
		List<ExcelReportVO> columnList = new ArrayList<ExcelReportVO>();
		
		for(Map colInfo : columnInfoList){
			String colName  = String.valueOf(colInfo.get(VarsqlReportConfig.MapperKey.code.getKey()));
			
			VarsqlReportConfig.TABLE_COLUMN tableInfo = VarsqlReportConfig.TABLE_COLUMN.valueOf(colName.toUpperCase());
			
			String title  =tableInfo.colName();
			
			if(colInfo.containsKey(VarsqlReportConfig.MapperKey.title.getKey())){
				title  = String.valueOf(colInfo.get(VarsqlReportConfig.MapperKey.title.getKey()));
			}
			int width = tableInfo.colWidth();
			if(colInfo.containsKey(VarsqlReportConfig.MapperKey.width.getKey())){
				try{
					width  = Integer.parseInt(String.valueOf(colInfo.get(VarsqlReportConfig.MapperKey.width.getKey())));
				}catch(NumberFormatException e){e.getMessage();}
			}
			
			//columnList.add(new ExcelReportVO(tableInfo.getKey(), title, width, tableInfo.getAlign()));
			columnList.add(ExcelReportVO.builder().key(tableInfo.getKey()).name(title).width(width)
					.cellStyle(ExcelCellStyle.builder().align(tableInfo.getAlign()).build())
					.headerCellStyle(ExcelCellStyle.builder().align(ExcelConstants.ALIGN.CENTER).bgColor(ExcelConstants.DEFAULT_LABEL_BG_COLOR).bold(true).build())
					.build());
		}
		
		report.setColumnArr(columnList.toArray(new ExcelReportVO[columnList.size()]));
		report.setHaderInfoList(getHeaderInfoList());
		
		if(multiSheetFlag){
			report.createSheet(tableList.get(0).getName());
		}else{
			report.createSheet("Table Column Info");
		}
		
		boolean firstFlag = true;
		List<ColumnInfo> colList;
		ColumnInfo columnInfo = null; 
		String tableNm = "";
		for (TableInfo tableInfo : tableList) {
			
			tableNm = tableInfo.getName();
			
			if(multiSheetFlag && !firstFlag){
				report.createSheet(tableNm);
			}
			
			headerInfo.put("{tableName}", tableNm);
			headerInfo.put("{tableSpace}", "");
			headerInfo.put("{entity}", "");
			headerInfo.put("{comment}", tableInfo.getRemarks());
			
			report.setHeaderInfo(headerInfo);
			report.addHeaderInfo(true);
			report.addLabelRow();
			colList = tableInfo.getColList();
			for(int j =0 ; j <  colList.size(); j++){
				columnInfo = colList.get(j);
				columnInfo.setNo(j+1);
				report.addRow(columnInfo);
			}
			report.addSpaceRow();
			
			firstFlag = false; 
		}
		
		return report; 
	}
	
	protected List<List<ExcelHeaderVO>> getHeaderInfoList() {
		List<List<ExcelHeaderVO>> haderInfoList = new ArrayList<List<ExcelHeaderVO>>();
		
		ExcelCellStyle cellStyle = ExcelCellStyle.builder().bgColor(ExcelConstants.DEFAULT_LABEL_BG_COLOR).bold(true).build(); 
		
		haderInfoList.add(ExcelHeaderVO.builder()
				.addHeaderVO("Database", 0, 1, cellStyle).addHeaderVO("{databaseName}", 2,2)
				.addHeaderVO("Table Name", 3, 4, cellStyle).addHeaderVO("{tableName}", 5, 6).build());
			
			haderInfoList.add(ExcelHeaderVO.builder()
					.addHeaderVO("Table Space", 0, 1, cellStyle).addHeaderVO("{tableSpace}", 2,2)
					.addHeaderVO("Entity", 3, 4, cellStyle).addHeaderVO("{entity}",5, 6).build());
			
			haderInfoList.add(ExcelHeaderVO.builder()
					.addHeaderVO("Desc", 0, 1, cellStyle).addHeaderVO("{comment}", 2, 6).build());
		
		return haderInfoList;
	}
}
