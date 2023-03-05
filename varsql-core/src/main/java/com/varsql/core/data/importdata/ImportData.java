package com.varsql.core.data.importdata;

import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;
import com.varsql.core.exception.ImportDataException;

public interface ImportData{
	public void startImport(AbstractImportDataHandler handler) throws ImportDataException ;
}
