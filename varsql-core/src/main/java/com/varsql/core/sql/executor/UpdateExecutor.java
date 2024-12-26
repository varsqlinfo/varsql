package com.varsql.core.sql.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.job.JobExecuteResult;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.BatchException;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.executor.handler.UpdateExecutorHandler;
import com.varsql.core.sql.executor.handler.UpdateInfo;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
 * -----------------------------------------------------------------------------
* @fileName		: UpdateExecutor.java
* @desc		: update executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class UpdateExecutor implements Executor{
	
	private static final int DEFAULT_BATCH_COUNT = 1000;
	
	private int batchCount;
	
	private final Logger logger = LoggerFactory.getLogger(UpdateExecutor.class);
	
	public UpdateExecutor() {
		this(DEFAULT_BATCH_COUNT);
	}
	
	public UpdateExecutor(int batchCount) {
		this.batchCount = batchCount;
	}

	public int getBatchCount() {
		return batchCount;
	}

	@Override
	public JobExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {
		return execute(statementInfo , null);
	}

	public JobExecuteResult execute(SqlStatementInfo statementInfo, UpdateExecutorHandler updateHandler) throws SQLException {

		JobExecuteResult result = new JobExecuteResult();

		Map sqlParamMap = statementInfo.getSqlParam();
		
		if(updateHandler==null) {
			updateHandler = new UpdateExecutorHandler() {
				@Override
				public boolean handle(UpdateInfo sqlResultVO) {
					return true;
				}
			};
		}

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(statementInfo.getSql(), sqlParamMap, DBVenderType.getDBType(statementInfo.getDatabaseInfo().getType()));

		List<SqlSource> sqlList = parseInfo.getList();

		result.setStartTime(System.currentTimeMillis());

		SqlSource tmpSqlSource =null;
		int sqlIdx =0, sqlSize = sqlList.size();
		int addCount = 0;
		Connection conn = null;
		Statement statement = null;
		
		final HandlerVariable handlerVariable = new HandlerVariable();
		try {
			conn = ConnectionFactory.getInstance().getConnection(statementInfo.getDatabaseInfo().getVconnid());
			
			conn.setAutoCommit(false);

			statement = conn.createStatement();

			boolean handleFlag = (updateHandler != null);
			for (sqlIdx =0; sqlIdx <sqlSize; sqlIdx++){

				tmpSqlSource = sqlList.get(sqlIdx);
				if(handleFlag){
					updateHandler.addTotalCount();
					if(!updateHandler.handle(UpdateInfo.builder().sql(tmpSqlSource.getQuery()).parameter(sqlParamMap).build())) {
						continue ;
					}
				};

				statement.addBatch(tmpSqlSource.getQuery());
				addCount++;

				if(addCount % getBatchCount()==0) {
					setExecuteBatch(statement, handlerVariable, getBatchCount());
				}
			}
			
			int lastBatchCount = addCount % getBatchCount(); 
			if(lastBatchCount != 0) {
				setExecuteBatch(statement, handlerVariable, lastBatchCount);
			}
			
			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage("errorLine : "+handlerVariable.getFailIdx()  +", error message :  "+  e.getMessage()+ ", sql : "+handlerVariable.getSql());

			logger.error("execute sql : {} ", handlerVariable.getSql());
			logger.error("execute error message : {} ", e.getMessage(), e);
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				JdbcUtils.close(conn, statement);
			}
		}
		result.setTotalCount(updateHandler.getTotalCount());
		result.setEndTime(System.currentTimeMillis());
		result.setExecuteCount(addCount);
		result.setResult(addCount);

		return result;
	}
	
	protected void setExecuteBatch(Statement statement, HandlerVariable handlerVariable, int batchCount) throws SQLException {
		try {
			SQLUtils.executeBatch(statement, handlerVariable.getTotalCount(), batchCount);
		} catch (BatchException bue) {
			handlerVariable.setFailIdx(bue.getFailIdx());
			throw bue;
		} catch (SQLException se) {
			throw se;
		}
	    
	    
	}
}
