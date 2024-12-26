package com.varsql.core.exception;

import java.sql.SQLException;

/**
 * Batch Exception
 * 
 * @fileName : BatchException.java
 * @author : ytkim
 */
public class BatchException extends SQLException {
	private static final long serialVersionUID = 1L;
	
	private long failIdx;
	
	private long failCount;

	public BatchException(String s, Throwable cause) {
		this(s, cause, -1);
	}

	public BatchException(String s, Throwable cause, long failIdx) {
		this(s, cause, -1, 0);
	}
	
	public BatchException(String s, Throwable cause, long failIdx, long failCount) {
		super(s, cause);
		this.failIdx = failIdx;
		this.failCount = failCount;
	}

	public long getFailIdx() {
		return failIdx;
	}

	public long getFailCount() {
		return failCount;
	}
}
