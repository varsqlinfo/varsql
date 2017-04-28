package com.varsql.web.util;

import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.varsql.sql.builder.SqlSourceResultVO;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.utils.PagingUtil;

/**
 *날짜 관련 util
 * @author ytkim 
*/
public class SqlResultUtil {
	/**
	 * resultSet을  리스트로 만드는 방법 
	 * 리스트 형식 List<Map> rows = new ArrayList<Map>();
	 * @param rs
	 * @param ssrv 
	 * @param paramMap 
	 * @param maxRow 
	 * @return
	 * @throws SQLException 
	 */
	public static SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv, ParamMap paramMap, int maxRow) throws SQLException{
		if (rs == null) {
			return ssrv;
		}
		
		ArrayList rows = new ArrayList();
		ResultSetMetaData rsmd = null;
		
		rsmd = rs.getMetaData();
	
		int count = rsmd.getColumnCount();
		String [] columns_key = new String[count];
		
		int columnType=-1;
		HashMap columnMap = null;
		ArrayList columnNameArr = new ArrayList();
		List<Boolean> columnNumberTypeFlag = new ArrayList<Boolean>();
		String columnName = "";
		Object columnValue = "";
		
		for (int i = 1; i <= count; i++) {
			columnName=columns_key[i - 1] = rsmd.getColumnName(i);
			columnType = rsmd.getColumnType(i);
			columnMap = new HashMap();
			columnMap.put("label", columnName);
			columnMap.put("key", columnName);
			if(count > 10) columnMap.put("width", 70);
			
			if(columnType == Types.INTEGER||columnType ==Types.NUMERIC||columnType ==Types.BIGINT||columnType ==Types.DECIMAL
					||columnType ==Types.DOUBLE||columnType ==Types.FLOAT||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){
				columnMap.put("align","right");
				columnNumberTypeFlag.add(true);
			}else{
				columnMap.put("align", "left");
				columnNumberTypeFlag.add(false);
			}
			columnNameArr.add(columnMap);
		}
		
		rs.last(); 
		int totalCnt = rs.getRow();
		rs.beforeFirst();
		int first = 0,last = totalCnt;
		
		ssrv.setTotalCnt(totalCnt);
		ssrv.setNumberTypeFlag(columnNumberTypeFlag);
		ssrv.setColumn(columnNameArr);
		
		
		if(totalCnt > maxRow){
		
			int pageNo = paramMap.getInt(VarsqlParamConstants.SEARCH_NO,1);
			int countPerPage = maxRow;
			first = ((pageNo-1)*countPerPage);
			last = first+countPerPage;
						
			if(first != 0){
				rs.absolute(first);
			}
			
			if(totalCnt > last){
				rs.setFetchSize((maxRow > 1000 ? VarsqlParamConstants.SQL_MAX_ROW : maxRow));
			}else{
				last = totalCnt; 
				rs.setFetchSize(totalCnt);
			}
			ssrv.setResultCnt(last-first);
			ssrv.setPagingInfo(PagingUtil.getPageObject(totalCnt, pageNo, maxRow));
		}else{
			ssrv.setResultCnt(last-first);
			ssrv.setPagingInfo(PagingUtil.getPageObject(totalCnt, 1, maxRow));
		}
		
		Map columns = null;
		Reader input = null;
		char[] buffer  = null;
		int byteRead=-1;
		while (rs.next()) {
			
			columns = new LinkedHashMap(count);
			for (int i = 1; i <= count; i++) {				
				columnName = columns_key[i-1];
				columnValue = rs.getObject(columnName);
				if( columnValue instanceof Clob){
					try{
						StringBuffer output = new StringBuffer();
						input = rs.getCharacterStream(columnName);
						buffer = new char[1024];
						while((byteRead=input.read(buffer,0,1024))!=-1){
							output.append(buffer,0,byteRead);
						}
						input.close();
						columns.put(columnName, output.toString());
					}catch(Exception e){
						columns.put(columnName , "Clob" +e.getMessage());
					}
				}else if( columnValue instanceof Blob){
					columns.put(columnName , "Blob");
				}else{
					columns.put(columnName, columnValue==null?"": columnValue+"");
				}
			}
			rows.add(columns);
			++first;
			
			if(first >= last) break;
		}
		ssrv.setData(rows);
		
		return ssrv;
	}
}
