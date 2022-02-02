package com.varsql.web.dto;

import java.util.List;

import com.varsql.core.sql.beans.GridColumnInfo;

public class DownloadItemInfo {
	
	private String name;
	
	private List<GridColumnInfo> column;
	
	private String condition;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GridColumnInfo> getColumn() {
		return column;
	}

	public void setColumn(List<GridColumnInfo> column) {
		this.column = column;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
}
