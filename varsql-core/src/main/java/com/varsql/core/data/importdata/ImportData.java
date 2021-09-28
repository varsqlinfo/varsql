package com.varsql.core.data.importdata;

import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;

public interface ImportData{
	public void startImport(AbstractImportDataHandler handler);
}
