package com.varsql.core.data.importdata.handler;

import java.util.List;

import com.varsql.core.sql.beans.ExportColumnInfo;

public abstract class AbstractImportDataHandler implements ImportDataHandler{

	private String tableName;

	private List<ExportColumnInfo> columns;

	private String sql;

	public AbstractImportDataHandler() {}

	public AbstractImportDataHandler(String sql) {
		this.sql = sql;
	}

	public AbstractImportDataHandler(String tableName, List<ExportColumnInfo> columns) {
		setTableName(tableName);
		setColumns(columns);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ExportColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(List<ExportColumnInfo> columns) {
		this.columns = columns;
		StringBuffer querySb = new StringBuffer();

		querySb.append("insert into ").append(this.tableName).append(" (");

		StringBuilder paramSb =new StringBuilder();
		boolean firstFlag = true;
		for (ExportColumnInfo gci : this.columns) {
			if(firstFlag) {
				querySb.append(gci.getName());
				paramSb.append("?");
				firstFlag = false;
			}else {
				querySb.append(", ").append(gci.getName());
				paramSb.append(", ?");
			}
		}

		querySb.append(") values ( ").append(paramSb.toString()).append(") ") ;

		this.sql = querySb.toString();
	}

	public String getSql() {
		return sql;
	}
}
