package com.varsql.core.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.ColumnJavaType;
import com.varsql.core.common.util.GridUtils;
import com.varsql.core.connection.ConnectionInfoManager;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataExceptionReturnType;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.ResultSetConvertException;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.VartechUtils;

import lombok.Getter;
import lombok.Setter;

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
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlStatementInfo sqlExecuteInfo) throws SQLException{
		return resultSetHandler(rs, ssrv, sqlExecuteInfo, true);
	}
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlStatementInfo sqlExecuteInfo, boolean gridKeyAlias) throws SQLException{
		if (rs == null) {
			return ssrv;
		}
		
		String viewColumnInfo = sqlExecuteInfo.getColumnInfo();
		
		HashMap<String, DataMap> viewColumnMap = null; 
		boolean columnChkFlag = false;
		if(viewColumnInfo !=null  && !"".equals(viewColumnInfo)) {
			columnChkFlag = true;
			viewColumnMap = getViewColumnMap(viewColumnInfo);
		}
		
		DataTypeFactory dataTypeFactory = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType())).getDataTypeImpl();

		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		List<ResultSetGridInfo> resultSetGridList = new LinkedList<>(); 
		String [] columnGridKeyArr = GridUtils.getAliasKeyArr(count);

		List<GridColumnInfo> columnInfoList = new LinkedList<GridColumnInfo>();

		Map<String,Integer> columnKeyCheck = new HashMap<String,Integer>();

		int gridIdx=0;
		String columnName = "";
		String columnTypeName;
		GridColumnInfo columnInfo;
		DataType dataType;
		int columnWidth = count > 10 ? 70 : 0;
		boolean useColumnLabel = ConnectionInfoManager.getInstance().getConnectionInfo(sqlExecuteInfo.getDatabaseInfo().getVconnid()).isUseColumnLabel();

		for (int idx = 1; idx <= count; idx++) {
			columnName= useColumnLabel ? rsmd.getColumnLabel(idx) : rsmd.getColumnName(idx);
			
			if(columnChkFlag) {
				if(!viewColumnMap.containsKey(columnName.toUpperCase())) {
					continue ;
				}
				columnName = viewColumnMap.get(columnName.toUpperCase()).getString("alias");
			}

			columnTypeName = rsmd.getColumnTypeName(idx);

			if(columnKeyCheck.containsKey(columnName)){
				int idxVal =columnKeyCheck.get(columnName)+1;
				columnKeyCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnKeyCheck.put(columnName, 0);
			}
			
			dataType = dataTypeFactory.getDataType(0, columnTypeName);
			
			columnInfo = new GridColumnInfo();
			setColumnTypeInfo(dataType.getTypeCode(), columnInfo);
			
			if(gridKeyAlias) {
				columnInfo.setKey(columnGridKeyArr[gridIdx]);
			}else {
				columnInfo.setKey(columnName);
			}
			
			resultSetGridList.add(new ResultSetGridInfo(idx, dataType, columnInfo.getKey()));
			
			gridIdx++;
			columnInfo.setNo(gridIdx);
			columnInfo.setLabel(columnName);
			columnInfo.setDbType(dataType == DefaultDataType.OTHER ? columnTypeName :dataType.getTypeName());
			columnInfo.setDbTypeCode(dataType.getTypeCode());
			columnInfo.setWidth(columnWidth);

			columnInfoList.add(columnInfo);
		}

		columnKeyCheck = null;
		int maxRow = sqlExecuteInfo.getLimit();
		int first = 0,last = maxRow;

		ssrv.setColumn(columnInfoList);

		List<Map> rows = new LinkedList<Map>();
		int totalCnt = 0;
		try {
			ResultSetGridInfo[] resultSetGridInfoArr = resultSetGridList.toArray(new ResultSetGridInfo[0]);
			int columnSize = resultSetGridList.size();
			Map row = null;
			ResultSetGridInfo resultSetGridInfo;
			
			while (rs.next()) {
				
				row = new LinkedHashMap(count);
				
				for (int colIdx = 0; colIdx < columnSize; colIdx++) {
					resultSetGridInfo = resultSetGridInfoArr[colIdx];
					dataType = resultSetGridInfo.getDataType();
					row.put(resultSetGridInfo.getColumnName(), dataType.getResultSetHandler().getValue(dataType, rs, resultSetGridInfo.getIdx(), DataExceptionReturnType.ERROR));
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
	
	private static HashMap<String, DataMap> getViewColumnMap(String viewColumnInfo) {
		ArrayList<DataMap> columnInfoList = VartechUtils.jsonStringToObject(viewColumnInfo, new TypeReference<ArrayList<DataMap>>() {});
		HashMap<String, DataMap> viewColumnMap = new HashMap<String, DataMap>();
		columnInfoList.forEach(item->{
			viewColumnMap.put(item.getString("name").toUpperCase() ,item);
		});
		
		return viewColumnMap; 
	}
	
	public static void resultSetHandler(ResultSet rs, SqlStatementInfo sqlExecuteInfo, SelectExecutorHandler selectExecutorHandler) throws SQLException {
		resultSetHandler(rs, sqlExecuteInfo, selectExecutorHandler, false);
	}

	public static void resultSetHandler(ResultSet rs, SqlStatementInfo sqlExecuteInfo, SelectExecutorHandler selectExecutorHandler, boolean gridKeyAlias) throws SQLException{
		if (rs == null) {
			return ;
		}
		
		String viewColumnInfo = sqlExecuteInfo.getColumnInfo();
		
		HashMap<String, DataMap> viewColumnMap = null; 
		boolean columnChkFlag = false;
		if(viewColumnInfo !=null  && !"".equals(viewColumnInfo)) {
			columnChkFlag = true;
			viewColumnMap = getViewColumnMap(viewColumnInfo);
		}
		
		DataTypeFactory dataTypeFactory = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType())).getDataTypeImpl();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();

		String columnTypeName = "";
		GridColumnInfo columnInfo=null;
		List<GridColumnInfo> columnInfoList = new LinkedList<GridColumnInfo>();
		String columnName = "";

		String [] columnGridKeyArr = GridUtils.getAliasKeyArr(count);
		Map<String,Integer> columnNameDuplicateCheck = new HashMap<String,Integer>();
		DataType dataType;
		int gridIdx=0;
		List<ResultSetGridInfo> resultSetGridList = new LinkedList<>(); 
		
		int columnWidth = count > 10 ? 70 : 0;
		for (int idx = 1; idx <= count; idx++) {
			columnName= rsmd.getColumnName(idx);
			if(columnChkFlag) {
				if(!viewColumnMap.containsKey(columnName.toUpperCase())) {
					continue ;
				}
				columnName = viewColumnMap.get(columnName.toUpperCase()).getString("alias");
			}
			
			columnTypeName = rsmd.getColumnTypeName(idx);
			
			if(columnNameDuplicateCheck.containsKey(columnName)){
				int idxVal =columnNameDuplicateCheck.get(columnName)+1;
				columnNameDuplicateCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnNameDuplicateCheck.put(columnName, 0);
			}
			
			dataType = dataTypeFactory.getDataType(0, columnTypeName);
			
			columnInfo = new GridColumnInfo();
			setColumnTypeInfo(dataType.getTypeCode(), columnInfo);
			
			if (gridKeyAlias) {
				columnInfo.setKey(columnGridKeyArr[gridIdx]);
			} else {
				columnInfo.setKey(columnName);
			}
			
			resultSetGridList.add(new ResultSetGridInfo(idx, dataType, columnInfo.getKey()));

			
			gridIdx++;
			columnInfo.setNo(gridIdx);
			columnInfo.setLabel(columnName);
			columnInfo.setDbType(dataType == DefaultDataType.OTHER ? columnTypeName :dataType.getTypeName());
			columnInfo.setDbTypeCode(dataType.getTypeCode());
			columnInfo.setWidth(columnWidth);

			columnInfoList.add(columnInfo);
		}

		columnNameDuplicateCheck = null;

		try {
			ResultSetGridInfo[] resultSetGridInfoArr = resultSetGridList.toArray(new ResultSetGridInfo[0]);
			int columnSize = resultSetGridList.size();
			Map row = null;
			ResultSetGridInfo resultSetGridInfo;
			while (rs.next()) {
				row = new LinkedHashMap(columnSize);
				for (int colIdx = 0; colIdx < columnSize; colIdx++) {
					resultSetGridInfo = resultSetGridInfoArr[colIdx];
					dataType = resultSetGridInfo.getDataType();
					row.put(resultSetGridInfo.getColumnName(), dataType.getResultSetHandler().getValue(dataType, rs, resultSetGridInfo.getIdx(), DataExceptionReturnType.ERROR, sqlExecuteInfo.isFormatValue()));
				}
				boolean addFlag = selectExecutorHandler.handle(SelectInfo.builder().rowObject(row).columnInfoList(columnInfoList).build());

				if(addFlag) {
					selectExecutorHandler.addTotalCount();
				}else {
					selectExecutorHandler.addFailCount();
				}
			}
		}catch(SQLException e) {
			throw new ResultSetConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e);
		}
	}
	
	/**
	 * resultset result return 
	 * 
	 * @param rs ResultSet
	 * @param dbType database type
	 * @return
	 * @throws SQLException
	 */
	public static List<Map> resultList(ResultSet rs, String dbType) throws SQLException{
		return resultList(rs, DBVenderType.getDBType(dbType), false);
	}
	
	/**
	 * resultset result return 
	 * 
	 * @param rs ResultSet
	 * @param dbType database type
	 * @param useColumnLabel  ResultSetMetaData 에서 컬럼값 꺼낼 경우 getColumnLabel 메소드 사용할지 여부
	 * @return
	 * @throws SQLException
	 */
	public static List<Map> resultList(ResultSet rs, DBVenderType dbType, boolean useColumnLabel) throws SQLException{
		if (rs == null) {
			return Collections.EMPTY_LIST;
		}
		
		DataTypeFactory dataTypeFactory = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl();

		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		List<ResultSetGridInfo> resultSetGridList = new LinkedList<>(); 

		Map<String,Integer> columnKeyCheck = new HashMap<String,Integer>();

		String columnName = "";
		String columnTypeName;
		DataType dataType;

		for (int idx = 1; idx <= count; idx++) {
			columnName= useColumnLabel ? rsmd.getColumnLabel(idx) : rsmd.getColumnName(idx);
			columnTypeName = rsmd.getColumnTypeName(idx);

			if(columnKeyCheck.containsKey(columnName)){
				int idxVal =columnKeyCheck.get(columnName)+1;
				columnKeyCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnKeyCheck.put(columnName, 0);
			}
			
			dataType = dataTypeFactory.getDataType(0, columnTypeName);
			resultSetGridList.add(new ResultSetGridInfo(idx, dataType, columnName));
		}

		columnKeyCheck = null;

		List<Map> rows = new LinkedList<Map>();
		try {
			ResultSetGridInfo[] resultSetGridInfoArr = resultSetGridList.toArray(new ResultSetGridInfo[0]);
			int columnSize = resultSetGridList.size();
			Map row = null;
			ResultSetGridInfo resultSetGridInfo;
			
			while (rs.next()) {
				row = new LinkedHashMap(count);
				
				for (int colIdx = 0; colIdx < columnSize; colIdx++) {
					resultSetGridInfo = resultSetGridInfoArr[colIdx];
					dataType = resultSetGridInfo.getDataType();
					row.put(resultSetGridInfo.getColumnName(), dataType.getResultSetHandler().getValue(dataType, rs, resultSetGridInfo.getIdx(), DataExceptionReturnType.ERROR));
				}
				
				rows.add(row);

			}
		}catch(SQLException e) {
			throw new ResultSetConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e);
		}

		return rows;
	}
	
	/**
	 * result one item
	 * @param rs
	 * @param dbType
	 * @return
	 * @throws SQLException
	 */
	public static Map resultOne(ResultSet rs, DBVenderType dbType) throws SQLException{
		return resultOne(rs, dbType, false);
	}
	
	public static Map resultOne(ResultSet rs, DBVenderType dbType, boolean useColumnLabel) throws SQLException{
		if (rs == null) {
			return null;
		}
		
		DataTypeFactory dataTypeFactory = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		List<ResultSetGridInfo> resultSetGridList = new LinkedList<>(); 
		
		Map<String,Integer> columnKeyCheck = new HashMap<String,Integer>();
		
		String columnName = "";
		String columnTypeName;
		DataType dataType;
		
		for (int idx = 1; idx <= count; idx++) {
			columnName= useColumnLabel ? rsmd.getColumnLabel(idx) : rsmd.getColumnName(idx);
			columnTypeName = rsmd.getColumnTypeName(idx);
			
			if(columnKeyCheck.containsKey(columnName)){
				int idxVal =columnKeyCheck.get(columnName)+1;
				columnKeyCheck.put(columnName, idxVal);
				columnName = columnName+"_"+idxVal;
			}else{
				columnKeyCheck.put(columnName, 0);
			}
			
			dataType = dataTypeFactory.getDataType(0, columnTypeName);
			resultSetGridList.add(new ResultSetGridInfo(idx, dataType, columnName));
		}
		
		columnKeyCheck = null;
		
		try {
			ResultSetGridInfo[] resultSetGridInfoArr = resultSetGridList.toArray(new ResultSetGridInfo[0]);
			int columnSize = resultSetGridList.size();
			Map row = null;
			ResultSetGridInfo resultSetGridInfo;
			
			while (rs.next()) {
				row = new LinkedHashMap(count);
				
				for (int colIdx = 0; colIdx < columnSize; colIdx++) {
					resultSetGridInfo = resultSetGridInfoArr[colIdx];
					dataType = resultSetGridInfo.getDataType();
					row.put(resultSetGridInfo.getColumnName(), dataType.getResultSetHandler().getValue(dataType, rs, resultSetGridInfo.getIdx(), DataExceptionReturnType.ERROR));
				}
				
				return row;
			}
		}catch(SQLException e) {
			throw new ResultSetConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e);
		}
		
		return null; 
	}

	private static void setColumnTypeInfo(int columnType, GridColumnInfo columnInfo) {
		columnInfo.setAlign("left");
		ColumnJavaType columnJavaType = ColumnJavaType.getType(columnType); 
		
		if(columnJavaType.isNumber()) {
			columnInfo.setAlign("right");
			columnInfo.setNumber(true);
		}
		
		columnInfo.setLob(columnJavaType.isLob());
		columnInfo.setType(columnJavaType);
	}
}

@Getter
@Setter
class ResultSetGridInfo{
	int idx;
	DataType dataType;
	String columnName;
	
	public ResultSetGridInfo(int idx, DataType dataType, String columnName) {
		this.idx = idx; 
		this.dataType = dataType; 
		this.columnName = columnName; 
	}
}
