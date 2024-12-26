package com.varsql.core.sql.executor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.job.JobExecuteResult;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.data.importdata.ImportData;
import com.varsql.core.data.importdata.ImportJsonData;
import com.varsql.core.data.importdata.ImportXmlData;
import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.executor.handler.UpdateExecutorHandler;
import com.varsql.core.sql.executor.handler.UpdateInfo;
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
	public JobExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {
		return execute(statementInfo, new UpdateExecutorHandler() {
			@Override
			public boolean handle(UpdateInfo sqlResultVO) {
				return true;
			}
		});
	}

	@Override
	public JobExecuteResult execute(SqlStatementInfo statementInfo, final UpdateExecutorHandler updateHandler) throws SQLException {

		JobExecuteResult result = new JobExecuteResult();

		File importFile;
		try {
			importFile = ResourceUtils.getResource(String.valueOf(statementInfo.getCustom().get(IMPORT_FILE_PARAM_NAME))).getFile();

			if(!importFile.exists() || importFile.length() == 0 ) {
				result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
				result.setMessage(" import file not valid ");
				return result;
			}
		} catch (IOException e1) {
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage(" error message :  "+  e1.getMessage());
			return result;
		}
		

		result.setStartTime(System.currentTimeMillis());

		Connection conn = ConnectionFactory.getInstance().getConnection(statementInfo.getDatabaseInfo().getVconnid());
		final HandlerVariable handlerVariable = new HandlerVariable(conn);
		try {

			conn.setAutoCommit(false);

			ImportData ijd = null;
			if(VarsqlFileType.JSON.equals(statementInfo.getExportType())) {
				ijd = new ImportJsonData(importFile);
			}else if(VarsqlFileType.XML.equals(statementInfo.getExportType())) {
				ijd = new ImportXmlData(importFile);
			}
			
			MetaControlBean metaControlBean = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(statementInfo.getDatabaseInfo().getType()));
			DataTypeFactory dataTypeFactory = metaControlBean.getDataTypeImpl();

			ijd.startImport(new AbstractImportDataHandler(metaControlBean) {

				boolean firstFlag = true;
				ExportColumnInfo [] columnArr =null;
				private PreparedStatement statement;
				DataType [] dataTypeArr = null;

				@Override
				public void handler(Map rowInfo) throws SQLException {
					
					if(!updateHandler.handle(UpdateInfo.builder().parameter(rowInfo).build())) {
						return ; 
					}
					
					if(firstFlag) {
						statement = handlerVariable.getStatement(getSql());
						firstFlag = false;
						
						List<ExportColumnInfo> columnList = getColumns();
						
						int columnSize = columnList.size();
						
						columnArr = new ExportColumnInfo[columnSize];
						dataTypeArr = new DataType[columnSize];
						
						for (int i = 0; i < columnSize; i++) {
							ExportColumnInfo eci = columnList.get(i);
							columnArr[i] = eci;
							dataTypeArr[i] = dataTypeFactory.getDataType(eci.getTypeCode(), eci.getType());
						}
					}

					for (int i = 0; i < columnArr.length; i++) {
						ExportColumnInfo eci = columnArr[i];
						
						try {
							dataTypeArr[i].getStatementHandler().setParameter(this.statement, i+1, rowInfo.get(eci.getAlias()));
						}catch(Exception e) {
							statement.setObject(i+1, rowInfo.get(eci.getAlias()));
							//e.printStackTrace();
						}
					}
					
					statement.addBatch();
					statement.clearParameters();

					handlerVariable.addTotalCount();

					if(handlerVariable.getTotalCount() % getBatchCount()==0) {
						setExecuteBatch(statement, handlerVariable, getBatchCount());
					}
				}
			});

			int lastBatchCount = handlerVariable.getTotalCount() % getBatchCount(); 
			if(lastBatchCount != 0) {
				setExecuteBatch(handlerVariable.getStatement(), handlerVariable, lastBatchCount);
			}

			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();
			
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage("error row : "+handlerVariable.getFailIdx()  +", error message :  "+  e.getMessage()+ ", sql : "+handlerVariable.getSql());

			logger.error("execute sql : {} ", handlerVariable.getSql(), e);
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				JdbcUtils.close(conn, handlerVariable.getStatement());
			}
		}
		result.setTotalCount(handlerVariable.getTotalCount() );
		result.setEndTime(System.currentTimeMillis());
		result.setExecuteCount(handlerVariable.getTotalCount());
		result.setResult(handlerVariable.getTotalCount());

		return result;
	}
}


