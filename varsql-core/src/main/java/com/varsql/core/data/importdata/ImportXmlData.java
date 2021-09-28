package com.varsql.core.data.importdata;

import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.executor.SqlUpdateExecutor;

public class ImportXmlData extends ImportDataAbstract{

	private final Logger logger = LoggerFactory.getLogger(ImportXmlData.class);

	public ImportXmlData(File importFilePath) {
		super(importFilePath);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startImport(AbstractImportDataHandler importDataHandler) {

		File importFile = getImportFilePath();
		logger.debug("import file : {}" , importFile.getAbsolutePath());

		XMLStreamReader xmlreader =null;
		XMLInputFactory factory= XMLInputFactory.newInstance();

		try (FileReader reader = new FileReader(importFile)) {
			// xmlreader 생성
			xmlreader = factory.createXMLStreamReader(reader);
			// iterator 패턴으로 태그를 반복적으로 취득

			while (xmlreader.hasNext()) {
				xmlreader.next();

				// 태그의 타입 별로 출력 방법을 구분한다.
				switch (xmlreader.getEventType()) {
					// 시작 태그
					case XMLStreamReader.START_ELEMENT: {
							String localName = xmlreader.getLocalName();

							if("metadata".equals(localName)) {
								getExportInfo(xmlreader, importDataHandler);
							}else if("items".equals(localName)) {
								getRowData(xmlreader, importDataHandler);
							}
						}
						break;
					// 닫기 태그
					case XMLStreamReader.END_ELEMENT: {

						}
						break;
					default:
						break;
				}
			}
			// xmlreader 닫기
			xmlreader.close();
		} catch (Throwable e) {
			logger.error("import xml data " , e);
		}finally {
			if(xmlreader != null)try {xmlreader.close();} catch (XMLStreamException e) {}
		}
	}

	private void getRowData(XMLStreamReader xmlreader, AbstractImportDataHandler importDataHandler) throws XMLStreamException, SQLException {
		Map rowInfo = null;
		boolean rowStartFlag = false;
		while (xmlreader.hasNext()) {

			switch (xmlreader.getEventType()) {
				case XMLStreamReader.START_ELEMENT: {
						String localName = xmlreader.getLocalName();

						if ("row".equals(localName)) {
							rowStartFlag= true;
							rowInfo = new HashMap();
							break;
						}

						if(rowStartFlag) {
							rowInfo.put(localName, xmlreader.getElementText());
						}
					}
					break;
				// 닫기 태그
				case XMLStreamReader.END_ELEMENT: {
						String localName = xmlreader.getLocalName();

						if ("row".equals(localName)) {
							rowStartFlag = false;
							importDataHandler.handler(rowInfo);
						}
					}
					break;
				default:
					break;
			}
			xmlreader.next();
		}
	}

	private static void getExportInfo(XMLStreamReader xmlreader, AbstractImportDataHandler importDataHandler) throws XMLStreamException {

		List<ExportColumnInfo> columns = new ArrayList<ExportColumnInfo>();
		ExportColumnInfo eci = null;

		boolean breakFlag= false;

		while (xmlreader.hasNext()) {

            switch (xmlreader.getEventType()) {
				case XMLStreamReader.START_ELEMENT: {
						String localName = xmlreader.getLocalName();

						if("tableName".equals(localName)) {
							importDataHandler.setTableName(xmlreader.getElementText());
						}

						if("item".equals(localName)) { // column info start
							eci = new ExportColumnInfo();

							for (int i = 0; i < xmlreader.getAttributeCount(); i++) {
								String attrName = xmlreader.getAttributeLocalName(i);
								String attrVal = xmlreader.getAttributeValue(i);

								if("name".equals(attrName)) {
									eci.setName(attrVal);
								}else if("type".equals(attrName)) {
									eci.setType(attrVal);
								}else if("number".equals(attrName)) {
									eci.setNumber(Boolean.parseBoolean(attrVal));
								}else if("lob".equals(attrName)) {
									eci.setLob(Boolean.parseBoolean(attrVal));
								}
							}
						}
					}
					break;
				// 닫기 태그
				case XMLStreamReader.END_ELEMENT: {
						String localName = xmlreader.getLocalName();

						if("item".equals(localName)) {
							columns.add(eci);
						}

						if("columns".equals(localName)) {
							breakFlag = true;
						}
					}
					break;
				default:
					break;
            }
            xmlreader.next();
            if(breakFlag) break;
        }
		importDataHandler.setColumns(columns);
	}

}
