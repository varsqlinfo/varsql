package com.varsql.web.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.GridUtils;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.resultset.handler.ResultSetHandler;
import com.varsql.web.dto.sql.SqlExecuteDTO;
import com.varsql.web.exception.VarsqlResultConvertException;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlResultUtils.java
* @DESC		: sql result utils
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 10. 31. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class SqlResultUtils {

	private SqlResultUtils(){}
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
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlExecuteDTO sqlExecuteInfo, int maxRow) throws SQLException{
		return resultSetHandler(rs, ssrv, sqlExecuteInfo, maxRow, true);
	}
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, SqlExecuteDTO sqlExecuteInfo, int maxRow, boolean gridKeyAlias) throws SQLException{
		if (rs == null) {
			return ssrv;
		}

		ResultSetMetaData rsmd = rs.getMetaData();

		ResultSetHandler resultsetHandler = MetaControlFactory.getConnidToDbInstanceFactory(sqlExecuteInfo.getConuid()).getResultsetHandler();

		int count = rsmd.getColumnCount();
		String [] columnNameArr = new String[count];
		String [] columnGridKeyArr = GridUtils.getAliasKeyArr(count);
		String [] columnTypeArr = new String[count];
		String [] columnTypeNameArr = new String[count];

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
			columnNameArr[i] = columnName;

			columnInfo = new GridColumnInfo();
			columnInfo.setLabel(columnName);
			if(gridKeyAlias) {
				columnInfo.setKey(columnGridKeyArr[i]);

			}else {
				columnInfo.setKey(columnName);
				columnGridKeyArr[i]= columnName;
			}

			columnInfo.setDbType(columnTypeName);

			if(count > 10) columnInfo.setWidth(70);

			columnInfo.setAlign("left");

			boolean isNumber = false;

			if(columnType == Types.INTEGER||columnType ==Types.NUMERIC||columnType ==Types.BIGINT||columnType ==Types.DECIMAL
					||columnType ==Types.DOUBLE||columnType ==Types.FLOAT||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){
				columnTypeArr[i] = "number";
				columnInfo.setAlign("right");
				isNumber = true;
			}else if(columnType == Types.DATE ){
				columnTypeArr[i] = "date";
			}else if(columnType == Types.TIME ){
				columnTypeArr[i] = "time";
			}else if(columnType == Types.TIMESTAMP ){
				columnTypeArr[i] = "timestamp";
			}else if(columnType == Types.BLOB ){
				columnTypeArr[i] = "blob";
			}else if(columnType == Types.CLOB ){
				columnTypeArr[i] = "clob";
			}else if(columnType == Types.REF ){
				columnTypeArr[i] = "ref";
			}else if(columnType == Types.NCLOB ){
				columnTypeArr[i] = "nclob";
			}else if(columnType == Types.VARBINARY ||columnType == Types.BINARY || columnType == Types.LONGVARBINARY){
				columnTypeArr[i] = "binary";
			}else if(columnType == Types.SQLXML ){
				columnTypeArr[i] = "sqlxml";
			}else{
				columnTypeArr[i] = "string";
			}
			columnTypeNameArr[i] = columnTypeName;

			columnNumberTypeFlag.add(isNumber);

			columnInfo.setType(columnTypeArr[i]);
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
		try {
			while (rs.next()) {
				row = new LinkedHashMap(count);
				for (int colIdx = 0; colIdx < count; colIdx++) {
					row = resultsetHandler.getDataValue(row, columnGridKeyArr[colIdx], columnNameArr[colIdx], rs, colIdx+1, columnTypeArr[colIdx], columnTypeNameArr[colIdx]);
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
			throw new VarsqlResultConvertException(VarsqlAppCode.EC_SQL_RESULT_CONVERT.code(), ssrv, e);
		}
		ssrv.setData(rows);
		ssrv.setResultCnt(totalCnt);

		return ssrv;
	}
}
