package com.varsql.core.sql.executor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.data.importdata.ImportData;
import com.varsql.core.data.importdata.ImportJsonData;
import com.varsql.core.data.importdata.ImportXmlData;
import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.util.JdbcUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: FileImportExecutor.java
* @desc		: file import executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class FileImportExecutor extends UpdateExecutor{

	private final Logger logger = LoggerFactory.getLogger(FileImportExecutor.class);

	public static String IMPORT_FILE_PARAM_NAME = "importFile";

	@Override
	public SQLExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {

		SQLExecuteResult result = new SQLExecuteResult();

		File importFile;
		try {
			importFile = ResourceUtils.getResource(String.valueOf(statementInfo.getCustom().get(IMPORT_FILE_PARAM_NAME))).getFile();

			if(!importFile.exists() || importFile.length() == 0 ) {
				result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
				result.setMessage(" import file size zero ");
				return result;
			}
		} catch (IOException e1) {
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage(" error message :  "+  e1.getMessage());
			return result;
		}

		result.setStartTime(System.currentTimeMillis());

		Connection conn = ConnectionFactory.getInstance().getConnection(statementInfo.getVconnid());
		final HandlerVariable handlerVariable = new HandlerVariable(conn);
		try {

			conn.setAutoCommit(false);

			ImportData ijd = null;
			if(VarsqlFileType.JSON.equals(statementInfo.getExportType())) {
				ijd = new ImportJsonData(importFile);
			}else if(VarsqlFileType.XML.equals(statementInfo.getExportType())) {
				ijd = new ImportXmlData(importFile);
			}

			ijd.startImport(new AbstractImportDataHandler() {

				boolean firstFlag = true;
				ExportColumnInfo [] columnArr =null;
				private PreparedStatement statement;

				@Override
				public void handler(Map rowInfo) throws SQLException {
					if(firstFlag) {
						statement = handlerVariable.getStatement(getSql());
						firstFlag = false;

						columnArr = getColumns().toArray(new ExportColumnInfo[0]);
					}

					for (int i = 0; i < columnArr.length; i++) {
						ExportColumnInfo eci = columnArr[i];
						statement.setObject(i+1, rowInfo.get(eci.getName()));
					}

					statement.addBatch();
					statement.clearParameters();

					handlerVariable.addCount();

					if(handlerVariable.getCount() % getBatchCount()==0) {
						statement.executeBatch();
						statement.clearBatch();
					}
				}
			});


			if(handlerVariable.getCount()  % getBatchCount() != 0) {
				handlerVariable.getStatement().executeBatch();
				handlerVariable.getStatement().clearBatch();
			}

			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage("errorLine : "+handlerVariable.getCount()  +", error message :  "+  e.getMessage());

			logger.error("update : {} ", e.getMessage(), e);
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				JdbcUtils.close(conn, handlerVariable.getStatement());
			}
		}
		result.setTotalCount(handlerVariable.getCount() );
		result.setEndTime(System.currentTimeMillis());
		result.setExecuteCount(handlerVariable.getCount());
		result.setResult(handlerVariable.getCount());


		return result;
	}


	class HandlerVariable {
		private int count = 0;
		private PreparedStatement statement;
		private Connection conn;

		public HandlerVariable(Connection conn) {
			this.conn = conn;
		}

		public int getCount() {
			return count;
		}
		public void addCount() {
			++this.count;
		}

		public PreparedStatement getStatement() throws SQLException {
			return getStatement(null);
		}
		public PreparedStatement getStatement(String sql) throws SQLException {
			if(statement == null) {
				statement = this.conn.prepareStatement(sql);
			}
			return statement;
		}


	}
}


