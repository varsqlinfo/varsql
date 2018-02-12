package com.varsql.app.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.varsql.sql.builder.SqlSourceResultVO;
import com.varsql.sql.resultset.handle.ResultSetHandle;
import com.varsql.app.database.beans.SqlParamInfo;
import com.varsql.sql.beans.GridColumnInfo;

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
		
		ResultSetMetaData rsmd = null;
		
		rsmd = rs.getMetaData();
		
		ResultSetHandle resultsetHandle = VarsqlUtil.getConnidToDbInstanceFactory(sqlParamInfo.getVconnid()).getResultsetHandle();
	
		int count = rsmd.getColumnCount();
		String [] columns_key = new String[count];
		String [] columns_type = new String[count];
		
		int columnType=-1;
		String columnTypeName = "";
		GridColumnInfo columnInfo=null;
		List<GridColumnInfo> columnInfoList = new ArrayList<GridColumnInfo>();
		List<Boolean> columnNumberTypeFlag = new ArrayList<Boolean>();
		String columnName = "";
		
		for (int i = 1; i <= count; i++) {
			columnName=columns_key[i - 1] = rsmd.getColumnName(i);
			columnType = rsmd.getColumnType(i);
			columnTypeName = rsmd.getColumnTypeName(i);
			
			columnInfo = new GridColumnInfo();
			columnInfo.setLabel(columnName);
			columnInfo.setKey(columnName);
			columnInfo.setDbType(columnTypeName);
			
			if(count > 10) columnInfo.setWidth(70);
			
			columnInfo.setAlign("left");
			
			columnNumberTypeFlag.add(false);
			
			if(columnType == Types.INTEGER||columnType ==Types.NUMERIC||columnType ==Types.BIGINT||columnType ==Types.DECIMAL
					||columnType ==Types.DOUBLE||columnType ==Types.FLOAT||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){
				columns_type[i - 1] = "number";
				columnInfo.setAlign("right");
				columnNumberTypeFlag.add(true);
			}else if(columnType == Types.DATE ){
				columns_type[i - 1] = "date";
			}else if(columnType == Types.TIME ){
				columns_type[i - 1] = "time";
			}else if(columnType == Types.TIMESTAMP ){
				columns_type[i - 1] = "timestamp";
			}else if(columnType == Types.BLOB ){
				columns_type[i - 1] = "blob";
			}else if(columnType == Types.CLOB ){
				columns_type[i - 1] = "clob";
			}else if(columnType == Types.REF ){
				columns_type[i - 1] = "ref";
			}else if(columnType == Types.NCLOB ){
				columns_type[i - 1] = "nclob";
			}else if(columnType == Types.VARBINARY ||columnType == Types.BINARY || columnType == Types.LONGVARBINARY){
				columns_type[i - 1] = "binary";
			}else if(columnType == Types.SQLXML ){
				columns_type[i - 1] = "sqlxml";
			}else{
				columns_type[i - 1] = "string";
			}
			
			columnInfo.setType(columns_type[i - 1]);
			columnInfoList.add(columnInfo);
		}
		
		int first = 0,last = maxRow;
		
		ssrv.setNumberTypeFlag(columnNumberTypeFlag);
		ssrv.setColumn(columnInfoList);
		
		Map columns = null;
		String tmpColumnType = "";
		ArrayList rows = new ArrayList();
		int totalCnt = 0 ; 
		while (rs.next()) {
			
			columns = new LinkedHashMap(count);
			for (int i = 1; i <= count; i++) {				
				columnName = columns_key[i-1];
				tmpColumnType = columns_type[i-1]; 
				
				if("number".equals(tmpColumnType)){
					columns.put(columnName,resultsetHandle.getNumber(rs, columnName));
				}else if("string".equals(tmpColumnType)){
					columns.put(columnName,resultsetHandle.getString(rs, columnName));
				}else if( "clob".equals(tmpColumnType)){
					columns.put(columnName , resultsetHandle.getClob(rs, columnName));
				}else if( "blob".equals(tmpColumnType)){
					columns.put(columnName , resultsetHandle.getBlob(rs, columnName));
				}else if("timestamp".equals(tmpColumnType)){
					columns.put(columnName, resultsetHandle.getTimeStamp(rs, columnName));
				}else if("date".equals(tmpColumnType)){
					columns.put(columnName, resultsetHandle.getDate(rs, columnName));
				}else if("time".equals(tmpColumnType)){
					columns.put(columnName,resultsetHandle.getTime(rs, columnName));
				}else if("sqlxml".equals(tmpColumnType)){
					columns.put(columnName,resultsetHandle.getSQLXML(rs, columnName));
				}else if("binary".equals(tmpColumnType)){
					columns.put(columnName,resultsetHandle.getBinary(rs, columnName));
				}else if("nclob".equals(tmpColumnType)){
					columns.put(columnName,resultsetHandle.getNCLOB(rs, columnName));
				}else{
					columns.put(columnName,resultsetHandle.getObject(rs, columnName));
				}
			}
			rows.add(columns);
			++first;
			totalCnt++;
			
			if(first >= last) break;
		}
		ssrv.setData(rows);
		ssrv.setResultCnt(totalCnt);
		
		return ssrv;
	}
}
