package com.varsql.core.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.GridUtils;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.ResultSetConvertException;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;
import com.varsql.core.sql.executor.handler.SQLHandlerParameter;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SQLResultSetUtils.java
* @DESC		: sql resultset utils
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 10. 31. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class SQLResultSetUtils {

	private SQLResultSetUtils(){}
	/**
	 * resultSet을  리스트로 만드는 방법
	 * 리스트 형식 List<Map> rows = new ArrayList<Map>();
	 * @param rs
	 * @param ssrv
	 * @param sqlExecuteInfo
	 * @param maxRow
	 * @param vconnid
	 * @return
	 * @throws SQLException
	 */
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlStatementInfo sqlExecuteInfo, DatabaseInfo dbInfo, int maxRow) throws SQLException{
		return resultSetHandler(rs, ssrv, sqlExecuteInfo, dbInfo, maxRow, true);
	}
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlStatementInfo sqlExecuteInfo, DatabaseInfo dbInfo, int maxRow, boolean gridKeyAlias) throws SQLException{
		if (rs == null) {
			return ssrv;
		}
		
		DataTypeFactory dataTypeFactory = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(sqlExecuteInfo.getDbType())).getDataTypeImpl();

		ResultSetMetaData rsmd = rs.getMetaData();

		int count = rsmd.getColumnCount();
		String [] columnNameArr = new String[count];
		DataType [] dataTypeArr = new DataType[count];
		String [] columnGridKeyArr = GridUtils.getAliasKeyArr(count);

		List<GridColumnInfo> columnInfoList = new LinkedList<GridColumnInfo>();
		String columnName = "";

		String viewColumnInfo = sqlExecuteInfo.getColumnInfo();

		Set<String> viewColumnCheck = Collections.emptySet();
		boolean columnChkFlag = false;
		if(viewColumnInfo !=null  && !"".equals(viewColumnInfo)) {
			columnChkFlag = true;
			viewColumnCheck =  new HashSet<String>(Arrays.asList(viewColumnInfo.toUpperCase().split(",")));
		}

		Map<String,Integer> columnKeyCheck = new HashMap<String,Integer>();

		int idx = 0;
		int columnType=-1;
		String columnTypeName = "";
		GridColumnInfo columnInfo=null;
		int columnWidth = count > 10 ? 70 : 0;
		boolean useColumnLabel = dbInfo.isUseColumnLabel();

		for (int i = 0; i < count; i++) {
			idx = i+1;
			columnName= useColumnLabel ? rsmd.getColumnLabel(idx) : rsmd.getColumnName(idx);
			if(columnChkFlag  && !viewColumnCheck.contains(columnName.toUpperCase())) {
				continue ;
			}

			columnType = rsmd.getColumnType(idx);
			columnTypeName = rsmd.getColumnTypeName(idx);

			if(columnKeyCheck.containsKey(columnName)){
				int idxVal =columnKeyCheck.get(columnName)+1;
				columnKeyCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnKeyCheck.put(columnName, 0);
			}
			columnNameArr[i] = columnName;
			dataTypeArr[i] = dataTypeFactory.getDataType(columnTypeName);

			columnInfo = new GridColumnInfo();
			setColumnTypeInfo(columnType, columnInfo);
			columnInfo.setNo(idx);
			columnInfo.setLabel(columnName);
			if(gridKeyAlias) {
				columnInfo.setKey(columnGridKeyArr[i]);
			}else {
				columnInfo.setKey(columnName);
				columnGridKeyArr[i]= columnName;
			}

			columnInfo.setDbType(columnTypeName);
			columnInfo.setWidth(columnWidth);

			columnInfoList.add(columnInfo);
		}

		columnKeyCheck = null;
		int first = 0,last = maxRow;

		ssrv.setColumn(columnInfoList);

		Map row = null;
		List<Map> rows = new LinkedList<Map>();
		int totalCnt = 0;
		try {
			while (rs.next()) {
				row = new LinkedHashMap(count);
				for (int colIdx = 0; colIdx < count; colIdx++) {
					DataType dataType = dataTypeArr[colIdx]; 
					if(dataType != null) {
						row.put(columnInfoList.get(colIdx).getKey(), dataType.getResultSetHandler().getValue(dataType, rs, colIdx+1));
					}else {
						row.put(columnInfoList.get(colIdx).getKey(), null);
					}
				}
				rows.add(row);
				++first;
				totalCnt++;

				if(first >= last) break;
			}
		}catch(SQLException e) {
			ssrv.setData(rows);
			ssrv.setResultCnt(totalCnt);
			ssrv.setResultMessage(e.getMessage());
			throw new ResultSetConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e, ssrv);
		}
		ssrv.setData(rows);
		ssrv.setResultCnt(totalCnt);

		return ssrv;
	}
	
	public static void resultSetHandler(ResultSet rs, SqlStatementInfo sqlExecuteInfo, AbstractSQLExecutorHandler baseExecutorHandler) throws SQLException {
		resultSetHandler(rs, sqlExecuteInfo, baseExecutorHandler, false);
	}

	public static void resultSetHandler(ResultSet rs, SqlStatementInfo sqlExecuteInfo, AbstractSQLExecutorHandler baseExecutorHandler, boolean gridKeyAlias) throws SQLException{
		if (rs == null) {
			return ;
		}
		
		DataTypeFactory dataTypeFactory = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(sqlExecuteInfo.getDbType())).getDataTypeImpl();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int count = rsmd.getColumnCount();
		String [] columnNameArr = new String[count];
		DataType [] dataTypeArr = new DataType[count];
		String [] columnGridKeyArr = GridUtils.getAliasKeyArr(count);

		int columnType=-1;
		String columnTypeName = "";
		GridColumnInfo columnInfo=null;
		List<GridColumnInfo> columnInfoList = new LinkedList<GridColumnInfo>();
		String columnName = "";

		String viewColumnInfo = sqlExecuteInfo.getColumnInfo();

		Set<String> viewColumnCheck = Collections.emptySet();
		boolean columnChkFlag = false;
		if(viewColumnInfo !=null  && !"".equals(viewColumnInfo)) {
			columnChkFlag = true;
			viewColumnCheck =  new HashSet<String>(Arrays.asList(viewColumnInfo.toUpperCase().split(",")));
		}

		Map<String,Integer> columnKeyCheck = new HashMap<String,Integer>();

		int idx = 0;
		int columnWidth = count > 10 ? 70 : 0;
		for (int i = 0; i < count; i++) {
			idx = i+1;
			columnName= rsmd.getColumnName(idx);
			if(columnChkFlag  && !viewColumnCheck.contains(columnName.toUpperCase())) {
				continue ;
			}

			columnType = rsmd.getColumnType(idx);
			columnTypeName = rsmd.getColumnTypeName(idx);
			
			if(columnKeyCheck.containsKey(columnName)){
				int idxVal =columnKeyCheck.get(columnName)+1;
				columnKeyCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnKeyCheck.put(columnName, 0);
			}
			columnNameArr[i] = columnName;
			dataTypeArr[i] = dataTypeFactory.getDataType(columnTypeName);

			columnInfo = new GridColumnInfo();
			setColumnTypeInfo(columnType, columnInfo);
			
			if (gridKeyAlias) {
				columnInfo.setKey(columnGridKeyArr[i]);
			} else {
				columnInfo.setKey(columnName);
				columnGridKeyArr[i] = columnName;
			}

			columnInfo.setNo(idx);
			columnInfo.setLabel(columnName);
			columnInfo.setDbType(columnTypeName);
			columnInfo.setWidth(columnWidth);

			columnInfoList.add(columnInfo);
		}

		columnKeyCheck = null;

		try {
			Map row = null;
			while (rs.next()) {
				row = new LinkedHashMap(count);
				for (int colIdx = 0; colIdx < count; colIdx++) {
					DataType dataType = dataTypeArr[colIdx]; 
					if(dataType != null) {
						row.put(columnInfoList.get(colIdx).getKey(), dataType.getResultSetHandler().getValue(dataType, rs, colIdx+1));
					}else {
						row.put(columnInfoList.get(colIdx).getKey(), null);
					}
				}
				boolean addFlag = baseExecutorHandler.handle(SQLHandlerParameter.builder().rowObject(row).columnInfoList(columnInfoList).build());

				if(addFlag) {
					baseExecutorHandler.addTotalCount();
				}else {
					baseExecutorHandler.addFailCount();
				}
			}
		}catch(SQLException e) {
			throw new ResultSetConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e);
		}
	}

	private static void setColumnTypeInfo(int columnType, GridColumnInfo columnInfo) {
		String varsqlColumnType = "";
		columnInfo.setAlign("left");
		boolean isLob =  false;
		boolean isNumber =  false;
		
		if(columnType ==Types.NUMERIC || columnType ==Types.DECIMAL){
			varsqlColumnType = "decimal";
			isNumber = true; 
		}else if(columnType ==Types.REAL || columnType == Types.FLOAT) {
			varsqlColumnType = "float";
			isNumber = true;
		}else if(columnType ==Types.DOUBLE) {
			varsqlColumnType = "double";
			isNumber = true;
		}else if(columnType == Types.INTEGER||columnType ==Types.BIGINT
				||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){
			varsqlColumnType = "number";
			
			isNumber = true; 
		}else if(columnType == Types.DATE ){
			varsqlColumnType = "date";
		}else if(columnType == Types.TIME ){
			varsqlColumnType = "time";
		}else if(columnType == Types.TIMESTAMP ){
			varsqlColumnType = "timestamp";
		}else if(columnType == Types.BLOB ){
			varsqlColumnType = "blob";
			isLob =  true;
		}else if(columnType == Types.CLOB ){
			varsqlColumnType = "clob";
			isLob =  true;
		}else if(columnType == Types.REF ){
			varsqlColumnType = "ref";
		}else if(columnType == Types.NCLOB ){
			varsqlColumnType = "nclob";
			isLob =  true;
		}else if(columnType == Types.VARBINARY ||columnType == Types.BINARY || columnType == Types.LONGVARBINARY){
			varsqlColumnType = "binary";
			isLob =  true;
		}else if(columnType == Types.SQLXML ){
			varsqlColumnType = "sqlxml";
			isLob =  true;
		}else{
			varsqlColumnType = "string";
		}
		
		if(isNumber) {
			columnInfo.setAlign("right");
			columnInfo.setNumber(true);
		}
		
		columnInfo.setLob(isLob);
		columnInfo.setType(varsqlColumnType);
	}
}
