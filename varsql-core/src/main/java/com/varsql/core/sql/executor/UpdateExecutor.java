package com.varsql.core.sql.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBType;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;
import com.varsql.core.sql.executor.handler.SQLHandlerParameter;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.VartechUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: BaseExecutor.java
* @desc		: base sql executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class UpdateExecutor implements SQLExecutor{

	private final Logger logger = LoggerFactory.getLogger(UpdateExecutor.class);

	private int BATCH_COUNT = 1000;

	@Override
	public SQLExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {
		return execute(statementInfo , null);
	}


	@Override
	public SQLExecuteResult execute(SqlStatementInfo statementInfo, AbstractSQLExecutorHandler resultHandler) throws SQLException {

		SQLExecuteResult result = new SQLExecuteResult();

		Map sqlParamMap = VartechUtils.jsonStringToObject(statementInfo.getSqlParam(), HashMap.class);

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(statementInfo.getSql(), sqlParamMap, DBType.getDBType(statementInfo.getDbType()));

		List<SqlSource> sqlList = parseInfo.getItems();

		result.setStartTime(System.currentTimeMillis());

		SqlSource tmpSqlSource =null;
		int sqlIdx =0, sqlSize = sqlList.size();
		int addCount = 0;
		Connection conn = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection(statementInfo.getVconnid());
			conn.setAutoCommit(false);

			Statement statement = conn.createStatement();

			boolean handleFlag = (resultHandler != null);
			for (sqlIdx =0; sqlIdx <sqlSize; sqlIdx++){

				tmpSqlSource = sqlList.get(sqlIdx);
				resultHandler.addTotalCount();
				if(handleFlag){
					if(!resultHandler.handle(SQLHandlerParameter.builder().sql(tmpSqlSource.getQuery()).parameter(sqlParamMap).build())) {
						continue ;
					}
				};

				statement.addBatch(tmpSqlSource.getQuery());
				addCount++;

				if(addCount % BATCH_COUNT==0) {
					statement.executeBatch();
					statement.clearBatch();
				}
			}

			if(addCount % BATCH_COUNT != 0) {
				statement.executeBatch();
				statement.clearBatch();
			}

			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage("errorLine : "+sqlIdx +", error message :  "+  e.getMessage());

			logger.error("update : {} ", e.getMessage(), e);
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				JdbcUtils.close(conn);
			}
		}
		result.setTotalCount(resultHandler.getTotalCount());
		result.setEndTime(System.currentTimeMillis());
		result.setResult(addCount);

		return result;
	}


}
