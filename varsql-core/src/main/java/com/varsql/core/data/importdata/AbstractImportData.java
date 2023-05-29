package com.varsql.core.data.importdata;

import java.io.File;

import com.varsql.core.sql.beans.ExportColumnInfo;

public abstract class AbstractImportData implements ImportData{

	private File importFilePath;

	protected AbstractImportData(File importFilePath) {
		this.importFilePath = importFilePath;
	}

	public File getImportFilePath() {
		return importFilePath;
	}
	
	
	public ExportColumnInfo setExportColumnInfo(ExportColumnInfo eci, String fieldName, String val) {
		if ("name".equals(fieldName)) {
			eci.setName(val);
		} else if ("type".equals(fieldName)) {
			eci.setType(val);
		} else if ("typeCode".equals(fieldName)) {
			eci.setTypeCode(Integer.parseInt(val));
		} else if ("number".equals(fieldName)) {
			eci.setNumber(Boolean.parseBoolean(val));
		} else if ("lob".equals(fieldName)) {
			eci.setLob(Boolean.parseBoolean(val));
		} else if ("alias".equals(fieldName)) {
			eci.setAlias(val);
		}
		
		return eci; 
	}
}
