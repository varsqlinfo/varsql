package com.varsql.core.data.importdata.handler;

import java.util.LinkedList;
import java.util.List;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.sql.beans.ExportColumnInfo;

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
		
		StringBuffer querySb = new StringBuffer();

		querySb.append("insert into ").append(this.tableName).append(" (");

		StringBuilder paramSb =new StringBuilder();
		boolean firstFlag = true;
		
		List<ExportColumnInfo> newColumnList = new LinkedList<ExportColumnInfo>();
		for (ExportColumnInfo eci : columns) {
			
			if(this.metaControlBean.getDataTypeImpl().getDataType(eci.getTypeCode(), eci.getType()).isExcludeImportColumn()) {
				continue;
			}
			
			newColumnList.add(eci);
			if(firstFlag) {
				querySb.append(eci.getName());
				paramSb.append("?");
				firstFlag = false;
			}else {
				querySb.append(", ").append(eci.getName());
				paramSb.append(", ?");
			}
		}

		querySb.append(") values ( ").append(paramSb.toString()).append(") ") ;
		
		this.columns = newColumnList;
		
		if(this.sql == null) this.sql = querySb.toString();
	}

	public String getSql() {
		return sql;
	}

	public MetaControlBean getMetaControlBean() {
		return metaControlBean;
	}
}
