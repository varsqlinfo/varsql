package com.varsql.core.common.job;

public abstract class AbstractJobHandler implements JobHandler {
	
	private long total = 0;
	
	private long index = 0;

	private long success = 0;
	
	private long fail = 0;
	
	public long getIndex() {
		return index;
	}
	
	public void increaseIndex() {
		++this.index;
	}

	public long getFail() {
		return fail;
	}

	public void increaseFail() {
		++this.fail;
	}

	public long getSuccess() {
		return success;
	}

	public void increaseSuccess() {
		++this.success;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setTotal(long total) {
		this.total = total;
	}
}
