package com.varsql.core.data.importdata;

import java.io.File;

public abstract class ImportDataAbstract implements ImportData{

	private File importFilePath;

	protected ImportDataAbstract(File importFilePath) {
		this.importFilePath = importFilePath;
	}

	public File getImportFilePath() {
		return importFilePath;
	}
}
