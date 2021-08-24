package com.varsql.core.data.importdata;

import com.varsql.core.data.importdata.handler.ImportDataHandlerAbstract;

public interface ImportData{
	public void startImport(ImportDataHandlerAbstract handler);
}
