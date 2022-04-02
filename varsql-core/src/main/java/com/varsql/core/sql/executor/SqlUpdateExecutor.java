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
import com.varsql.core.db.DBVenderType;
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
public class SqlUpdateExecutor extends UpdateExecutor{

	private final Logger logger = LoggerFactory.getLogger(SqlUpdateExecutor.class);

	@Override
	public SQLExecuteResult execute(SqlStatementInfo statementInfo, AbstractSQLExecutorHandler resultHandler) throws SQLException {

		SQLExecuteResult result = new SQLExecuteResult();

		Map sqlParamMap = VartechUtils.jsonStringToObject(statementInfo.getSqlParam(), HashMap.class);

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(statementInfo.getSql(), sqlParamMap, DBVenderType.getDBType(statementInfo.getDbType()));

		List<SqlSource> sqlList = parseInfo.getItems();

		result.setStartTime(System.currentTimeMillis());

		SqlSource tmpSqlSource =null;
		int sqlIdx =0, sqlSize = sqlList.size();
		int addCount = 0;
		Connection conn = null;
		Statement statement = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection(statementInfo.getVconnid());
			conn.setAutoCommit(false);

			statement = conn.createStatement();

			boolean handleFlag = (resultHandler != null);
			for (sqlIdx =0; sqlIdx <sqlSize; sqlIdx++){

				tmpSqlSource = sqlList.get(sqlIdx);
				if(handleFlag){
					resultHandler.addTotalCount();
					if(!resultHandler.handle(SQLHandlerParameter.builder().sql(tmpSqlSource.getQuery()).parameter(sqlParamMap).build())) {
						continue ;
					}
				};

				statement.addBatch(tmpSqlSource.getQuery());
				addCount++;

				if(addCount % getBatchCount()==0) {
					statement.executeBatch();
					statement.clearBatch();
				}
			}

			if(addCount % getBatchCount() != 0) {
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
				JdbcUtils.close(conn, statement);
			}
		}
		result.setTotalCount(resultHandler.getTotalCount());
		result.setEndTime(System.currentTimeMillis());
		result.setExecuteCount(addCount);
		result.setResult(addCount);

		return result;
	}


}
