package com.varsql.core.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.GridUtils;
import com.varsql.core.db.DBType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.ResultSetConvertException;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;
import com.varsql.core.sql.executor.handler.SQLHandlerParameter;
import com.varsql.core.sql.resultset.handler.ResultSetHandler;

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
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlStatementInfo sqlExecuteInfo, int maxRow) throws SQLException{
		return resultSetHandler(rs, ssrv, sqlExecuteInfo, maxRow, true);
	}
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlStatementInfo sqlExecuteInfo, int maxRow, boolean gridKeyAlias) throws SQLException{
		if (rs == null) {
			return ssrv;
		}

		ResultSetMetaData rsmd = rs.getMetaData();

		int count = rsmd.getColumnCount();
		String [] columnNameArr = new String[count];
		String [] columnGridKeyArr = GridUtils.getAliasKeyArr(count);

		List<GridColumnInfo> columnInfoList = new ArrayList<GridColumnInfo>();
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
		ArrayList rows = new ArrayList();
		int totalCnt = 0;
		try {
			ResultSetHandler resultsetHandler = MetaControlFactory.getDbInstanceFactory(DBType.getDBType(sqlExecuteInfo.getDbType())).getResultsetHandler();

			while (rs.next()) {
				row = new LinkedHashMap(count);
				for (int colIdx = 0; colIdx < count; colIdx++) {
					if(columnNameArr[colIdx] != null) {
						row = resultsetHandler.getDataValue(rs, row, columnInfoList.get(colIdx));
						//row = resultsetHandler.getDataValue(row, columnGridKeyArr[colIdx], columnNameArr[colIdx], rs, colIdx+1, columnTypeArr[colIdx], columnTypeNameArr[colIdx]);
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

	public static void resultSetHandler(ResultSet rs, SqlStatementInfo sqlExecuteInfo, AbstractSQLExecutorHandler baseExecutorHandler) throws SQLException{
		if (rs == null) {
			return ;
		}

		ResultSetMetaData rsmd = rs.getMetaData();

		int count = rsmd.getColumnCount();
		String [] columnNameArr = new String[count];

		int columnType=-1;
		String columnTypeName = "";
		GridColumnInfo columnInfo=null;
		List<GridColumnInfo> columnInfoList = new ArrayList<GridColumnInfo>();
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

			columnInfo = new GridColumnInfo();
			setColumnTypeInfo(columnType, columnInfo);

			columnInfo.setNo(idx);
			columnInfo.setLabel(columnName);
			columnInfo.setKey(columnName);
			columnInfo.setDbType(columnTypeName);
			columnInfo.setWidth(columnWidth);

			columnInfoList.add(columnInfo);
		}

		columnKeyCheck = null;

		try {
			ResultSetHandler resultsetHandler = MetaControlFactory.getDbInstanceFactory(DBType.getDBType(sqlExecuteInfo.getDbType())).getResultsetHandler();
			Map row = null;
			while (rs.next()) {
				baseExecutorHandler.addTotalCount();
				row = new LinkedHashMap(count);
				for (int colIdx = 0; colIdx < count; colIdx++) {
					if(columnNameArr[colIdx] != null) {
						row = resultsetHandler.getDataValue(rs, row, columnInfoList.get(colIdx));
					}
				}
				baseExecutorHandler.handle(SQLHandlerParameter.builder().rowObject(row).columnInfoList(columnInfoList).build());
			}
		}catch(SQLException e) {
			throw new ResultSetConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e);
		}
	}

	private static void setColumnTypeInfo(int columnType, GridColumnInfo columnInfo) {
		String varsqlColumnType = "";
		columnInfo.setAlign("left");
		boolean isLob =  false;
		if(columnType == Types.INTEGER||columnType ==Types.NUMERIC||columnType ==Types.BIGINT||columnType ==Types.DECIMAL
				||columnType ==Types.DOUBLE||columnType ==Types.FLOAT||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){

			varsqlColumnType = "number";
			columnInfo.setAlign("right");
			columnInfo.setNumber(true);
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
		columnInfo.setLob(isLob);
		columnInfo.setType(varsqlColumnType);
	}
}
