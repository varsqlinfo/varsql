package com.varsql.core.common.beans;

//TODO 2022
public class ProgressInfo {
	
	private String requid;

	private String name;
	
	private int itemIdx;
	private int totalItemSize;
	
	private int progressContentLength;
	private int totalContentLength;
	
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

	public int getProgressContentLength() {
		return progressContentLength;
	}

	public void setProgressContentLength(int progressContentLength) {
		this.progressContentLength = progressContentLength;
	}

	public int getTotalContentLength() {
		return totalContentLength;
	}

	public void setTotalContentLength(int totalContentLength) {
		this.totalContentLength = totalContentLength;
	}
	
	
	
}
