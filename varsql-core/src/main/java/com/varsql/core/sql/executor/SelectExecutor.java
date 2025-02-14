package com.varsql.core.sql.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.job.JobExecuteResult;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.SqlExecuteManager;
import com.varsql.core.sql.beans.ExecuteStatementInfo;
import com.varsql.core.sql.beans.SqlExecuteBean;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLParamUtils;
import com.varsql.core.sql.util.SQLResultSetUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SelectExecutor.java
* @desc		: base sql executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SelectExecutor implements Executor{

	private final Logger logger = LoggerFactory.getLogger(SelectExecutor.class);

	@Override
	public JobExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {
		return execute(statementInfo, null);
	}

	public JobExecuteResult execute(SqlStatementInfo statementInfo, SelectExecutorHandler resultHandler) throws SQLException {
		JobExecuteResult result = new JobExecuteResult();

		ResponseResult parseInfo = SqlSourceBuilder.parseResponseResult(statementInfo.getSql(), statementInfo.getSqlParam(), DBVenderType.getDBType(statementInfo.getDatabaseInfo().getType()));

		List<SqlSource> sqlList = parseInfo.getList();

		result.setStartTime(System.currentTimeMillis());

		SqlSource tmpSqlSource =sqlList.get(0);

		if(resultHandler==null) {
			resultHandler = getDefaultSelectHandler();
		}
		
		String requid = statementInfo.getRequid$$(); 

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String executeQuery = tmpSqlSource.getQuery();
			conn = ConnectionFactory.getInstance().getConnection(statementInfo.getDatabaseInfo().getVconnid());

			SqlExecuteManager.getInstance().addStatementInfo(SqlExecuteBean.builder().reqUid(requid).ip("select-executor").build(), ExecuteStatementInfo.builder().conn(conn).build());
			logger.debug("execute query: {}", executeQuery);
			
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(executeQuery);
			
			// request 실행 취소 정보 추가
			SqlExecuteManager.getInstance().addStatementInfo(SqlExecuteBean.builder().reqUid(requid).ip("select-executor").build(), ExecuteStatementInfo.builder().statement(pstmt).sql(executeQuery).build());
			
			if(statementInfo.getLimit() > 0) {
				pstmt.setMaxRows(statementInfo.getLimit());
			}
			
			JdbcUtils.setStatementFetchSize(pstmt, statementInfo.getLimit());
			
			SQLParamUtils.setSqlParameter(pstmt, tmpSqlSource);

			rs = pstmt.executeQuery();

			SQLResultSetUtils.resultSetHandler(rs, statementInfo, resultHandler, statementInfo.isUseColumnAlias());
		} catch (Throwable e ) {
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage(" error message :  "+  e.getMessage());

			logger.error("select : {} ", e.getMessage(), e);
		}finally{
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				JdbcUtils.close(conn, pstmt, rs);
			}
			// request 실행 취소 정보 제거
			SqlExecuteManager.getInstance().removeStatementInfo(requid);
		}
		result.setTotalCount(resultHandler.getTotalCount());
		result.setEndTime(System.currentTimeMillis());
		result.setResult(resultHandler.getResult());

		return result;
	}

	private SelectExecutorHandler getDefaultSelectHandler() {
		return new SelectExecutorHandler(null) {
			List selectList = new ArrayList();

			@Override
			public Object getResult() {
				return selectList;
			}

			@Override
			public boolean handle(SelectInfo selectInfo) {
				selectList.add(selectInfo.getRowObject());
				return true;
			}
		};
	}
}
