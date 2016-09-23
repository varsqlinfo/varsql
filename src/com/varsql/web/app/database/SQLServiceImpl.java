package com.varsql.web.app.database;
import java.io.Reader;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.type.ResultType;
import com.varsql.common.util.DataExportUtil;
import com.varsql.db.ConnectionFactory;
import com.varsql.format.VarsqlFormatterDb2;
import com.varsql.sql.SQLUtil;
import com.varsql.sql.builder.SqlSource;
import com.varsql.sql.builder.SqlSourceBuilder;
import com.varsql.sql.builder.SqlSourceResultVO;
import com.varsql.sql.builder.VarsqlCommandType;
import com.varsql.sql.builder.VarsqlStatementType;
import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;

/**
 * 
 * @FileName  : SQLServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class SQLServiceImpl implements SQLService{
	private static final Logger logger = LoggerFactory.getLogger(SQLServiceImpl.class);
	
	@Autowired
	SQLDAO sqlDAO ;
	
	private Calendar calendar = Calendar.getInstance();
	
	public String selectQna(DataCommonVO paramMap) {
		return "";
	}
	
	/**
	 * 
	 * @Method Name  : sqlFormat
	 * @Method 설명 : sql 포멧 맞추기
	 * @Method override : @see com.varsql.web.app.database.SQLService#sqlFormat(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 6. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String sqlFormat(DataCommonVO paramMap) throws Exception {
		
		List<SqlSource> sqlList=new SqlSourceBuilder().parse(paramMap.getString(VarsqlParamConstants.SQL));
		StringBuffer sqlFormatSb = new StringBuffer();
		for (SqlSource tmpSqlSource : sqlList) {
			
			sqlFormatSb.append(new VarsqlFormatterDb2().execute(tmpSqlSource.getQuery())).append("\n");
		}
		
		return sqlFormatSb.toString();
	}
	
	/**
	 * 
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 데이타 보기. 
	 * @Method override : @see com.varsql.web.app.database.SQLService#sqlData(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 9. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String sqlData(DataCommonVO paramMap) throws Exception {
		String reqSql = paramMap.getString(VarsqlParamConstants.SQL);
		List<SqlSource> sqlList=new SqlSourceBuilder().parse(reqSql);
		
		
		String vconnid = paramMap.getString(VarsqlParamConstants.VCONNID);
		int maxRow = paramMap.getInt(VarsqlParamConstants.LIMIT, VarsqlParamConstants.SQL_MAX_ROW);
		
		ArrayList reLst = new ArrayList();
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			
			for (SqlSource tmpSqlSource : sqlList) {
				
				getRequestSqlData(paramMap,conn,tmpSqlSource , maxRow);
				
				reLst.add(tmpSqlSource.getResult());
			}
			
			return VarsqlUtil.objectToString(reLst);
		} catch (SQLException e) {
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		return "";
	}
	
	/**
	 * 
	 * @Method Name  : getStatement
	 * @Method 설명 : 실행할 statement 얻기
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param maxRow
	 * @return
	 * @throws SQLException
	 */
	private Statement getStatement(Connection conn, SqlSource tmpSqlSource, int maxRow) throws SQLException {
		
		Statement stmt = conn.prepareStatement(tmpSqlSource.getQuery());
		if(VarsqlStatementType.STATEMENT.equals(tmpSqlSource.getStatementType())){
			stmt = conn.createStatement();
			setMaxRow(stmt, maxRow);
			stmt.execute(tmpSqlSource.getQuery());
		}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
			CallableStatement callStatement = conn.prepareCall(tmpSqlSource.getQuery());
			setMaxRow(callStatement, maxRow);
			callStatement.execute();
			stmt = callStatement; 
		}else{
			PreparedStatement pstmt = conn.prepareStatement(tmpSqlSource.getQuery());
			setMaxRow(pstmt, maxRow);
			pstmt.execute();
			stmt = pstmt; 
		}
		return stmt;
	}
	
	/**
	 * 
	 * @Method Name  : setMaxRow
	 * @Method 설명 : row 갯수 셋팅
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param stmt
	 * @param maxRow
	 * @throws SQLException
	 */
	private void setMaxRow(Statement stmt, int maxRow) throws SQLException {
		stmt.setMaxRows(maxRow);
	}
	
	/**
	 * 
	 * @param paramMap 
	 * @Method Name  : getResultData
	 * @Method 설명 : 데이타 얻기
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param maxRow
	 * @return
	 */
	protected Object getRequestSqlData(DataCommonVO paramMap, Connection conn, SqlSource tmpSqlSource, int maxRow) {
		Statement stmt = null;
		ResultSet rs  = null;
		Object reVal=null;
		SqlSourceResultVO ssrv = new SqlSourceResultVO();
		
		ssrv.setStarttime(System.currentTimeMillis());
		
	    try{
			stmt  = getStatement(conn, tmpSqlSource, maxRow);
			
			if(VarsqlCommandType.SELECT.equals(tmpSqlSource.getCommandType())){
				resultSetHandler(stmt.getResultSet(), ssrv);
				ssrv.setViewType("grid");
				ssrv.setResultMessage("success result count : "+ssrv.getResultCnt());
			}else if(VarsqlCommandType.CALL.equals(tmpSqlSource.getCommandType())){
				
			}else{
				ssrv.setViewType("msg");
				ssrv.setResultCnt(stmt.getUpdateCount());
				ssrv.setResultMessage("success update count : "+ ssrv.getResultCnt());
			}
			    
			ssrv.setResultType(ResultType.SUCCESS.name());
			
	    }catch(SQLException e){
	    	ssrv.setViewType("msg");
	    	ssrv.setResultType(ResultType.FAIL.name());
	    	//e.fillInStackTrace()
	    	ssrv.setResultMessage("error : "+e.getSQLState()+": "+ e.getLocalizedMessage());
	    	logger.error(getClass().getName()+" sqlData", e);
		} finally {
	    	SQLUtil.close(stmt, rs);
	    }
	    
	    ssrv.setEndtime(System.currentTimeMillis());
		ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
		ssrv.setResultMessage((ssrv.getDelay())/1000.0 +" SECOND : "+ssrv.getResultMessage());
		
	    tmpSqlSource.setResult(ssrv);
	    
	    sqlLogInsert(paramMap,tmpSqlSource , ssrv);
	    return reVal;
	}
	
	/**
	 * 
	 * @Method Name  : sqlLogInsert
	 * @Method 설명 : 사용자 sql 로그 저장
	 * @작성일   : 2015. 5. 6. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @param tmpSqlSource
	 * @param ssrv
	 */
	private void sqlLogInsert(DataCommonVO paramMap, SqlSource tmpSqlSource, SqlSourceResultVO ssrv) {
		try{
	    	DataCommonVO logInfoMap = new DataCommonVO();
	    	
	    	java.sql.Timestamp stime = new java.sql.Timestamp(ssrv.getStarttime());
	    	calendar.setTime(stime);
	    	
		    logInfoMap.put("vconnid", paramMap.getString(VarsqlParamConstants.VCONNID));
		    logInfoMap.put("uid", paramMap.getString(UserConstants.UID));
		    logInfoMap.put("start_time", stime);
		    logInfoMap.put("s_mm", calendar.get(Calendar.MONTH)+1);
		    logInfoMap.put("s_dd", calendar.get(Calendar.DATE));
		    logInfoMap.put("s_hh", calendar.get(Calendar.HOUR_OF_DAY));
		    logInfoMap.put("end_time", new java.sql.Timestamp(ssrv.getEndtime()));
		    logInfoMap.put("delay_time", ssrv.getDelay());
		    logInfoMap.put("log_sql", tmpSqlSource.getQuery());
		    logInfoMap.put("result_count", ssrv.getResultCnt());
		    logInfoMap.put("command_type", tmpSqlSource.getCommandType());
		    
		    System.out.println("-------------------------");
		    System.out.println("logInfoMap : "+ logInfoMap);
		    System.out.println("-------------------------");
		    sqlDAO.insertSqlUserLog(logInfoMap);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}

	/**
	 * 데이타 내보내기.
	 */
	public void dataExport(DataCommonVO paramMap, HttpServletResponse res) throws Exception {
		
		String exportType = paramMap.getString(VarsqlParamConstants.EXPORT_TYPE);
		String tmpName = paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME); 
		String reqSql = "select "+ paramMap.getString(VarsqlParamConstants.EXPORT_COLUMN_INFO) + " from "+tmpName;
		SqlSource sqlSource = new SqlSourceBuilder().getSqlSource(reqSql);
		
		String vconnid = paramMap.getString(VarsqlParamConstants.VCONNID);
		int maxRow = paramMap.getInt(VarsqlParamConstants.LIMIT, VarsqlParamConstants.SQL_MAX_ROW);
		
		Connection conn = null;
		List result = new ArrayList();
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			getRequestSqlData(paramMap,conn,sqlSource , maxRow);
			result = sqlSource.getResult().getData();
		} catch (SQLException e) {
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ";");
		res.setHeader("Content-Transfer-Encoding", "binary;");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1;");
		
		if("csv".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".csv;");
			DataExportUtil.toCSVWrite(result, '\t', res.getOutputStream());
		}else if("json".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".json;");
			new ObjectMapper().writeValue(res.getOutputStream(), result);
		}else if("insert".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".sql;");
			DataExportUtil.toInsertQueryWrite(result, sqlSource.getResult().getNumberTypeFlag(), tmpName, res.getOutputStream());
		}else if("xml".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".xml;");
			DataExportUtil.toXmlWrite(result, "row", res.getOutputStream());
		}
	}
	
	public void columnInfoExport(DataCommonVO paramMap,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * resultSet을  리스트로 만드는 방법 
	 * 리스트 형식 List<Map> rows = new ArrayList<Map>();
	 * @param rs
	 * @param ssrv 
	 * @return
	 * @throws SQLException 
	 */
	protected SqlSourceResultVO resultSetHandler(ResultSet rs, SqlSourceResultVO ssrv) throws SQLException{
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
		
		Map columns = null;
		Reader input = null;
		char[] buffer  = null;
		int byteRead=-1;
		long cnt =0; 
		while (rs.next()) {
			++cnt;
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
		}
		ssrv.setResultCnt(cnt);
		ssrv.setNumberTypeFlag(columnNumberTypeFlag);
		ssrv.setColumn(columnNameArr);
		ssrv.setData(rows);
		
		return ssrv;
	}
}