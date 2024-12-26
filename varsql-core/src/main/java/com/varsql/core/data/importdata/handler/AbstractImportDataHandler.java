package com.varsql.core.data.importdata.handler;

import java.util.List;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.beans.GenQueryInfo;
import com.varsql.core.sql.util.SQLUtils;

public abstract class AbstractImportDataHandler implements ImportDataHandler{

	private String tableName;

	private List<ExportColumnInfo> columns;

	private String sql;
	
	private MetaControlBean metaControlBean;

	public AbstractImportDataHandler(MetaControlBean metaControlBean) {
		this.metaControlBean = metaControlBean; 
	}

	public AbstractImportDataHandler(MetaControlBean metaControlBean, String sql) {
		this.metaControlBean = metaControlBean;
		this.sql = sql;
	}

	public AbstractImportDataHandler(MetaControlBean metaControlBean, String tableName, List<ExportColumnInfo> columns) {
		this.metaControlBean = metaControlBean;
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
		
		GenQueryInfo insertQueryInfo = SQLUtils.generateInsertQuery(this.tableName, columns, this.metaControlBean.getDataTypeImpl());
		this.columns = insertQueryInfo.getColumns();
		if(this.sql == null) this.sql = insertQueryInfo.getSql();
	}

	public String getSql() {
		return sql;
	}

	public MetaControlBean getMetaControlBean() {
		return metaControlBean;
	}
}
