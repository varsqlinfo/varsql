package com.varsql.core.task.transfer.writer;

import com.varsql.core.task.transfer.TargetVO;

public abstract class AbstractTargetWriter implements TargetWriter, AutoCloseable {
	
	protected TargetVO targetVO;
	
	protected long totalCount = 0;
	protected long updateCount = 0;
	protected long insertCount = 0;
	protected long failCount = 0;
	protected long successCount = 0;
	
	public AbstractTargetWriter(TargetVO targetVO) {
		this.targetVO = targetVO;
	}
	
	
	public long getTotalCount() {
		return this.totalCount;
	}
	
	public void increaseTotalCount() {
		++this.totalCount;
	}
	
	public long getUpdateCount() {
		return updateCount;
	}

	public void increaseUpdateCount() {
		++this.updateCount;
	}

	public long getInsertCount() {
		return insertCount;
	}

	public void increaseInsertCount() {
		++this.insertCount;
	}
	
	public long getFailCount() {
		return failCount;
	}
	
	public void increaseFailCount() {
		++this.failCount;
	}
	
	public long getSuccessCount() {
		return successCount;
	}
	
	public void increaseSuccessCount() {
		++this.successCount;
	}
	
	public void addFailCount(long failCount) {
		this.failCount +=failCount;
		
	}
}
