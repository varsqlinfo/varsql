package com.varsql.core.common.beans;

/**
 * data download progress info
* 
* @fileName	: ProgressInfo.java
* @author	: ytkim
 */
public class ProgressInfo {
	
	private String requid;

	private String name;
	
	private int itemIdx;
	private int totalItemSize;
	
	private long progressContentLength;
	private long totalContentLength;
	
	public String getRequid() {
		return requid;
	}
	
	public void setRequid(String requid) {
		this.requid = requid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getItemIdx() {
		return itemIdx;
	}

	public void setItemIdx(int itemIdx) {
		this.itemIdx = itemIdx;
	}

	public int getTotalItemSize() {
		return totalItemSize;
	}

	public void setTotalItemSize(int totalItemSize) {
		this.totalItemSize = totalItemSize;
	}

	public long getProgressContentLength() {
		return progressContentLength;
	}

	public void setProgressContentLength(long progressContentLength) {
		this.progressContentLength = progressContentLength;
	}

	public long getTotalContentLength() {
		return totalContentLength;
	}

	public void setTotalContentLength(int totalContentLength) {
		this.totalContentLength = totalContentLength;
	}
	
	
	
}
