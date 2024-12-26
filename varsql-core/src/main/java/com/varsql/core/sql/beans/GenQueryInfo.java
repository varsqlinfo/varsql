package com.varsql.core.sql.beans;

import java.util.List;

import lombok.Getter;

@Getter
public class GenQueryInfo{
	private String sql;
	
	private List<ExportColumnInfo> columns;
	
	
	public GenQueryInfo(String sql, List<ExportColumnInfo> columns) {
		this.sql = sql; 
		this.columns = columns;
	}
}
