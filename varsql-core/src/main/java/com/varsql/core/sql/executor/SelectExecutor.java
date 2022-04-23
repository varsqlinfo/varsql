package com.varsql.core.sql.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLParamUtils;
import com.varsql.core.sql.util.SQLResultSetUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.VartechUtils;

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
	public SQLExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {
		return execute(statementInfo, null);
	}

	public SQLExecuteResult execute(SqlStatementInfo statementInfo, SelectExecutorHandler resultHandler) throws SQLException {
		SQLExecuteResult result = new SQLExecuteResult();

		Map sqlParamMap = VartechUtils.jsonStringToObject(statementInfo.getSqlParam(), HashMap.class);

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(statementInfo.getSql(), sqlParamMap, DBVenderType.getDBType(statementInfo.getDbType()));

		List<SqlSource> sqlList = parseInfo.getItems();

		result.setStartTime(System.currentTimeMillis());

		SqlSource tmpSqlSource =sqlList.get(0);

		if(resultHandler==null) {
			resultHandler = getDefaultSelectHandler();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection(statementInfo.getVconnid());

			logger.debug("execute query: {} ", tmpSqlSource.getQuery());

			pstmt = conn.prepareStatement(tmpSqlSource.getQuery());
			pstmt.setMaxRows(statementInfo.getLimit());

			SQLParamUtils.setSqlParameter(pstmt, tmpSqlSource);

			rs = pstmt.executeQuery();

			SQLResultSetUtils.resultSetHandler(rs, statementInfo, resultHandler, statementInfo.isUseColumnAlias());
		} catch (Throwable e ) {
			result.setResultCode(VarsqlAppCode.EC_SQL_EXECUTOR);
			result.setMessage(" error message :  "+  e.getMessage());

			logger.error("select : {} ", e.getMessage(), e);
		}finally{
			JdbcUtils.close(conn, pstmt, rs);
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
