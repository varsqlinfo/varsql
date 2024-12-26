package com.varsql.core.task.sql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.ResultSetConvertException;
import com.varsql.core.sql.SqlExecuteManager;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.core.task.Task;
import com.varsql.core.task.transfer.TaskResult;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.StringUtils.EscapeType;

public class SQLTask implements Task{
	
	private final static Logger logger = LoggerFactory.getLogger(SQLTask.class);
	
	private ResponseResult result; 
	private SqlStatementInfo ssi; 
	
	public SQLTask(SqlStatementInfo ssi) {
		this.ssi = ssi; 
	}

	@Override
	public void submit() throws Exception {
		
		ResponseResult parseInfo = SqlSourceBuilder.parseResponseResult(ssi.getSql(), ssi.getSqlParam(), DBVenderType.getDBType( ssi.getDatabaseInfo().getType() ));

		List<SqlSource> sqlList = parseInfo.getList();

		int limit = ssi.getLimit();
		limit = limit <= 0 ? SqlDataConstants.DEFAULT_LIMIT_ROW_COUNT : limit;

		ArrayList<SqlSourceResultVO> reLst = new ArrayList<SqlSourceResultVO>();

		Connection conn = null;
		SqlSourceResultVO ssrv =null;
		
		String vconnid = ssi.getDatabaseInfo().getVconnid();

		SqlSource tmpSqlSource =null;
		int sqldx =0,sqlSize = sqlList.size();
		
		String errorMsg = "";
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			
			conn.setAutoCommit(false);

			for (sqldx =0;sqldx <sqlSize; sqldx++) {
				tmpSqlSource = sqlList.get(sqldx);
				
				logger.info("sql task execute : {}", tmpSqlSource.getQuery());

				ssrv = new SqlSourceResultVO();
				reLst.add(ssrv);
				tmpSqlSource.setResult(ssrv);
				ssrv.setStarttime(System.currentTimeMillis());

				ssrv = SQLUtils.sqlExecute(ssi, conn, tmpSqlSource, true);

				ssrv.setEndtime(System.currentTimeMillis());
				ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
				ssrv.setResultMessage((ssrv.getDelay())+" SECOND : "+StringUtils.escape(ssrv.getResultMessage(), EscapeType.html));

				if(SqlDataConstants.VIEWTYPE.GRID.val().equals(ssrv.getViewType())) {
					break;
				}
			}
			
			result.setList(reLst);
			conn.commit();
		} catch (Throwable e ) {
			
			if (conn != null && !conn.isClosed()) conn.rollback(); 

			errorMsg = e.getMessage();

			if(e instanceof ResultSetConvertException){
				result.setResultCode(VarsqlAppCode.EC_SQL_RESULT_CONVERT);
				ssrv= ((ResultSetConvertException)e).getSsrv();

				if(ssrv != null) {
					ssrv= new SqlSourceResultVO();
				}
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
			}else {
				boolean ssrvNullFlag = false;
				if(ssrv==null) {
					ssrvNullFlag = true;
					ssrv = new SqlSourceResultVO();
				}

				ssrv.setEndtime(System.currentTimeMillis());
				String tmpMsg = parseInfo.getMessage();
				tmpMsg = (tmpMsg  == null || "".equals(tmpMsg) ?"" :StringUtils.escape(parseInfo.getMessage(), EscapeType.html)+"<br/>");

				if(e instanceof ConnectionFactoryException) {
					if(((ConnectionFactoryException)e).getErrorCode() == VarsqlAppCode.EC_DB_POOL_CLOSE) {
						result.setResultCode(VarsqlAppCode.EC_DB_POOL_CLOSE);
					}else {
						result.setResultCode(VarsqlAppCode.EC_DB_POOL);
					}
				}else {
					result.setResultCode(VarsqlAppCode.EC_SQL);
				}

				result.setMessage(tmpMsg+StringUtils.escape(ssrv.getResultMessage(), EscapeType.html));

				if(ssrvNullFlag) {
					result.setMessage(errorMsg);
				}
			}

			result.addCustomMapAttribute("errorLine", sqldx);
			result.setItemOne(tmpSqlSource==null?sqlList.get(0):tmpSqlSource);

			LoggerFactory.getLogger("sqlErrorLog").error("sqlData errorLine : {}", sqldx,e);
		}finally{
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				JdbcUtils.close(conn);
			}
			
			// request 실행 취소 정보 제거
			if(!StringUtils.isBlank(ssi.getRequid$$())) {
				SqlExecuteManager.getInstance().removeStatementInfo(ssi.getRequid$$());
			}
		}
	}

	@Override
	public TaskResult result() {
		return TaskResult.builder()
				.customResult(this.result)
				.build();
	}
	
}
