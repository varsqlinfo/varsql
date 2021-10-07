package com.varsql.core.data.importdata;

import java.io.File;

public abstract class AbstractImportData implements ImportData{

	private File importFilePath;

	protected AbstractImportData(File importFilePath) {
		this.importFilePath = importFilePath;
	}

	public File getImportFilePath() {
		return importFilePath;
	}
}
