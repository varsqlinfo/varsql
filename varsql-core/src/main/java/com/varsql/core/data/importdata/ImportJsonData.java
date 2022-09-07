package com.varsql.core.data.importdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.vartech.common.utils.StringUtils;

public class ImportJsonData extends AbstractImportData{

	private final Logger logger = LoggerFactory.getLogger(ImportJsonData.class);

	public ImportJsonData(File importFilePath) {
		super(importFilePath);
	}

	@Override
	public void startImport(AbstractImportDataHandler importDataHandler) {
		File importFile = getImportFilePath();
		logger.debug("import file : {}" , importFile.getAbsolutePath());

		try(JsonParser parser =  new JsonFactory().createParser(importFile)){
			parser.nextToken();                          //start reading the file
	        Map<String,Object> rowInfo;
	        JsonToken valueToken;

	        while (parser.nextToken() != JsonToken.END_OBJECT) {    //loop until "}"

	        	String fieldName = parser.getCurrentName();

	        	if ("metadata".equals(fieldName)) {
	        		getMetaInfo(parser, importDataHandler);
	        	}else if ("items".equals(fieldName)) {

	        		parser.nextToken();
	        		while (parser.nextToken() != JsonToken.END_ARRAY) {

	        			rowInfo = new LinkedHashMap<>();
	        			while (parser.nextToken() != JsonToken.END_OBJECT) {
    						fieldName = parser.getCurrentName();

	        				valueToken = parser.currentToken();

							if (valueToken == JsonToken.VALUE_NULL || valueToken == null) {
								rowInfo.put(fieldName, null);
							} else if (valueToken.isNumeric()) {
								rowInfo.put(fieldName, parser.getNumberValue());
							} else if (valueToken.isBoolean()) {
								rowInfo.put(fieldName, parser.getBooleanValue());
							} else {
								rowInfo.put(fieldName, parser.getText());
							}
    					}

	        			importDataHandler.handler(rowInfo);
		        	}
	        	}
	        }

	        parser.close();

		}catch(Exception e) {
			logger.error("import xml data " , e);
		}
	}

	private void getMetaInfo(JsonParser parser, AbstractImportDataHandler importDataHandler) throws IOException {
		String fieldName = null;
		parser.nextToken();

		while (parser.nextToken() != JsonToken.END_OBJECT) {

			fieldName = parser.getCurrentName();

			if("tableName".equals(fieldName)) {
				parser.nextToken();
				importDataHandler.setTableName(parser.getText());
			}else if("columns".equals(fieldName)){
				ExportColumnInfo gci = null;
				List<ExportColumnInfo> columns = new ArrayList<ExportColumnInfo>();
				parser.nextToken();
				while (parser.nextToken() != JsonToken.END_ARRAY) {
					gci= new ExportColumnInfo();

					while (parser.nextToken() != JsonToken.END_OBJECT) {
						fieldName = parser.getCurrentName();

						parser.nextValue();
						
						String attrVal = parser.getText(); 

						if ("name".equals(fieldName)) {
							gci.setName(attrVal);
						} else if ("type".equals(fieldName)) {
							gci.setType(attrVal);
						} else if ("number".equals(fieldName)) {
							gci.setNumber(Boolean.parseBoolean(attrVal));
						} else if ("lob".equals(fieldName)) {
							gci.setLob(Boolean.parseBoolean(attrVal));
						} else if ("alias".equals(fieldName)) {
							gci.setAlias(attrVal);
						}
					}
					
					if(StringUtils.isBlank(gci.getAlias())) {
			        	gci.setAlias(gci.getName());
			        }

					columns.add(gci);
	        	}
				importDataHandler.setColumns(columns);
			}
    	}

		parser.nextToken();

	}
}
