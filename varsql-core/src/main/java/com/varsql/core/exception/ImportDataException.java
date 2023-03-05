package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

public class ImportDataException extends VarsqlException {

	private static final long serialVersionUID = 1L;

	public ImportDataException(String errorMessage) {
		super(VarsqlAppCode.COMM_FILE_IMPORT_ERROR, errorMessage);
	}

	public ImportDataException(String errorMessage, Exception e) {
		super(VarsqlAppCode.COMM_FILE_IMPORT_ERROR, errorMessage, e);
	}
	
	public ImportDataException(Exception e) {
		super(VarsqlAppCode.COMM_FILE_IMPORT_ERROR, e.getMessage(), e);
	}
}