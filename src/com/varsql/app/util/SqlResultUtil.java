package com.varsql.app.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.varsql.app.database.beans.SqlParamInfo;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.resultset.handler.ResultSetHandler;

/**
 *날짜 관련 util
 * @author ytkim 
*/
public class SqlResultUtil {
	
	private static SimpleDateFormat timestampSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private static SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd"); 
	private static SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss.SSS"); 
	
	private SqlResultUtil(){}
	/**
	 * resultSet을  리스트로 만드는 방법 
	 * 리스트 형식 List<Map> rows = new ArrayList<Map>();
	 * @param rs
	 * @param ssrv 
	 * @param sqlParamInfo 
	 * @param maxRow 
	 * @param vconnid 
	 * @return
	 * @throws SQLException 
	 */
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlParamInfo sqlParamInfo, int maxRow) throws SQLException{
		if (rs == null) {
			return ssrv;
		}
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		ResultSetHandler resultsetHandler = MetaControlFactory.getConnidToDbInstanceFactory(sqlParamInfo.getVconnid()).getResultsetHandler();
	
		int count = rsmd.getColumnCount();
		String [] columns_key = new String[count];
		String [] columns_type = new String[count];
		String [] columns_type_name = new String[count];
		
		int columnType=-1;
		String columnTypeName = "";
		GridColumnInfo columnInfo=null;
		List<GridColumnInfo> columnInfoList = new ArrayList<GridColumnInfo>();
		List<Boolean> columnNumberTypeFlag = new ArrayList<Boolean>();
		String columnName = "";
		
		Map<String,Integer> columnKeyCheck = new HashMap<String,Integer>();
		
		int idx = 0; 
		for (int i = 0; i < count; i++) {
			idx = i+1;
			columnName= rsmd.getColumnName(idx);
			columnType = rsmd.getColumnType(idx);
			columnTypeName = rsmd.getColumnTypeName(idx);
			
			if(columnKeyCheck.containsKey(columnName)){
				int idxVal =columnKeyCheck.get(columnName)+1; 
				columnKeyCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnKeyCheck.put(columnName, 0);
			}
			columns_key[i] = columnName; 
			
			columnInfo = new GridColumnInfo();
			columnInfo.setLabel(columnName);
			columnInfo.setKey(columnName);
			columnInfo.setDbType(columnTypeName);
			
			if(count > 10) columnInfo.setWidth(70);
			
			columnInfo.setAlign("left");
			
			boolean isNumber = false; 
			
			if(columnType == Types.INTEGER||columnType ==Types.NUMERIC||columnType ==Types.BIGINT||columnType ==Types.DECIMAL
					||columnType ==Types.DOUBLE||columnType ==Types.FLOAT||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){
				columns_type[i] = "number";
				columnInfo.setAlign("right");
				isNumber = true;
			}else if(columnType == Types.DATE ){
				columns_type[i] = "date";
			}else if(columnType == Types.TIME ){
				columns_type[i] = "time";
			}else if(columnType == Types.TIMESTAMP ){
				columns_type[i] = "timestamp";
			}else if(columnType == Types.BLOB ){
				columns_type[i] = "blob";
			}else if(columnType == Types.CLOB ){
				columns_type[i] = "clob";
			}else if(columnType == Types.REF ){
				columns_type[i] = "ref";
			}else if(columnType == Types.NCLOB ){
				columns_type[i] = "nclob";
			}else if(columnType == Types.VARBINARY ||columnType == Types.BINARY || columnType == Types.LONGVARBINARY){
				columns_type[i] = "binary";
			}else if(columnType == Types.SQLXML ){
				columns_type[i] = "sqlxml";
			}else{
				columns_type[i] = "string";
			}
			columns_type_name[i] = columnTypeName;
			
			columnNumberTypeFlag.add(isNumber);
			
			columnInfo.setType(columns_type[i]);
			columnInfoList.add(columnInfo);
		}
		
		columnKeyCheck = null; 
		
		int first = 0,last = maxRow;
		
		ssrv.setNumberTypeFlag(columnNumberTypeFlag);
		ssrv.setColumn(columnInfoList);
		
		Map row = null;
		String tmpColumnType = "";
		ArrayList rows = new ArrayList();
		int totalCnt = 0 ; 
		while (rs.next()) {
			
			row = new LinkedHashMap(count);
			for (int colIdx = 1; colIdx <= count; colIdx++) {				
				row = resultsetHandler.getDataValue(row, columns_key[colIdx-1], rs, colIdx, columns_type[colIdx-1], columns_type_name[colIdx-1]);
			}
			rows.add(row);
			++first;
			totalCnt++;
			
			if(first >= last) break;
		}
		ssrv.setData(rows);
		ssrv.setResultCnt(totalCnt);
		
		return ssrv;
	}
}
