package com.varsql.core.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.sql.beans.SqlExecuteBean;
import com.varsql.core.sql.util.JdbcUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SqlExecuteManager.java
* @desc		: sql execute manager 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2022. 1. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlExecuteManager {
	
	private final Logger logger = LoggerFactory.getLogger(SqlExecuteManager.class);
	
	private final Map<String, SqlExecuteBean> SQL_EXECUTE_INFO= new ConcurrentHashMap<String, SqlExecuteBean>(); 
	
	private SqlExecuteManager() {}
	
	public static SqlExecuteManager getInstance() {
		return SqlExecuteManageroHolder.instance;
	}
	
	private static class SqlExecuteManageroHolder {
		private static final SqlExecuteManager instance = new SqlExecuteManager();
	}

	public synchronized void addStatementInfo(String requid, Statement statement) {
		addStatementInfo(requid, statement, Thread.currentThread().getId());
	}
	
	public synchronized void addStatementInfo(String requid, Statement statement, long threadId) {
		
		if(requid == null) return ; 
		
		if(!SQL_EXECUTE_INFO.containsKey(requid)) {
			SqlExecuteBean seb = new SqlExecuteBean();
			
			seb.setThreadId(threadId);
			seb.setStartTime(System.currentTimeMillis());
			seb.addStatement(statement);
			SQL_EXECUTE_INFO.put(requid, seb);
		}else {
			SQL_EXECUTE_INFO.get(requid).addStatement(statement);
		}
	}
	
	public SqlExecuteBean getStatementInfo(String requid) {
		if(requid==null) {
			return null; 
		}
		return SQL_EXECUTE_INFO.get(requid);
	}
	
	/**
	 * remove request
	 * 
	 * @param requid
	 */
	public synchronized void removeStatementInfo(String requid) {
		removeStatementInfo(requid, true);
	}
	
	/**
	 * remove request
	 * 
	 * @param requid request uid
	 * @param statementCancelFlag statement cancel (true = cancel, false = item remove)
	 */
	public synchronized void removeStatementInfo(String requid, boolean statementCancelFlag) {
		if(requid==null) return ; 
		
		if(statementCancelFlag) {
			SqlExecuteBean seb = getStatementInfo(requid);
			
			if(seb == null) return ; 
			
			if (seb.getStatement() != null) {
				for(Statement statement : seb.getStatement()) {
					try {
						if(statement != null && !statement.isClosed()) {
							statement.cancel();
							JdbcUtils.close(statement);
						}
					} catch (SQLException e) {
						// ignore 
					}
				} 
			}
		}
		SQL_EXECUTE_INFO.remove(requid);
	}
	
	/**
	 * request cancel
	 * @param requid
	 */
	public synchronized void cancelStatementInfo(String requid) {
		SqlExecuteBean seb = getStatementInfo(requid);
		
		logger.info("cancel : {} , seb : {}", requid, seb);
		if (seb == null ||seb.getStatement()== null) {
			return ; 
		}

		try {
			boolean statementClose = false; 
			for(Statement statement : seb.getStatement()) {
				if(statement != null) {
					statementClose = true; 
					statement.cancel();
					JdbcUtils.close(statement);
				}
			}
			if(statementClose) {
				removeStatementInfo(requid, false);
				return ; 
			}
		} catch (Throwable e) {
			logger.error("cancelStatementInfo statement cancel : {}", e.getMessage(), e);
		}

		// 2.thread kill 조금더 확인해볼것.
		ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();

		while (root.getParent() != null) {
			root = root.getParent();
		}

		int numThreads = root.activeCount();

		Thread[] threads = new Thread[numThreads];

		numThreads = root.enumerate(threads);

		long threadId = seb.getThreadId();
		
		logger.info("cancel thread id : {}", threadId);

		if (threadId != -1) {
			try {
				for (int i = 0; i < numThreads; i++) {
					Thread thread = threads[i];

					//System.out.println("thread.getId() == killThreadId " + thread.getId() + " ;; " + threadId);

					if (thread.getId() == threadId) {
						thread.interrupt();
						// thread.stop();
						break;
					}
				}
			} catch (Exception e) {
				logger.error("connectionCancel thread kill : {}", e.getMessage());
			}
		}
		
		removeStatementInfo(requid, false);
	}
}
