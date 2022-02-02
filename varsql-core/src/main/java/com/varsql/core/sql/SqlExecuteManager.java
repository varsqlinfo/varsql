package com.varsql.core.sql;

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

	public synchronized void setStatementInfo(String uid, Statement statement) {
		setStatementInfo(uid, statement, Thread.currentThread().getId());
	}
	
	public synchronized void setStatementInfo(String uid, Statement statement, long threadId) {
		
		if(uid == null) return ; 
		
		if(!SQL_EXECUTE_INFO.containsKey(uid)) {
			SqlExecuteBean seb = new SqlExecuteBean();
			
			seb.setThreadId(threadId);
			seb.setStartTime(System.currentTimeMillis());
			seb.addStatement(statement);
			SQL_EXECUTE_INFO.put(uid, seb);
		}else {
			SQL_EXECUTE_INFO.get(uid).addStatement(statement);
		}
	}
	
	public SqlExecuteBean getStatementInfo(String uid) {
		return SQL_EXECUTE_INFO.get(uid);
	}

	public synchronized void removeStatementInfo(String uid) {
		SQL_EXECUTE_INFO.remove(uid);
	}

	public synchronized void cancelStatementInfo(String uid) {
		SqlExecuteBean seb = getStatementInfo(uid);
		
		if (seb == null ||seb.getStatement()== null) {
			return ; 
		}

		try {
			for(Statement statement : seb.getStatement()) {
				if(statement != null) {
					statement.cancel();
					JdbcUtils.close(statement);
				}
			}
			
			removeStatementInfo(uid);
			return ; 
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
		
		removeStatementInfo(uid);
	}
}
