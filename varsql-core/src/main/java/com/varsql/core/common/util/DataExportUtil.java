package com.varsql.core.common.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.vartech.common.excel.ExcelExport;
import com.vartech.common.excel.ExcelReportVO;
import com.vartech.common.utils.VartechUtils;

public class DataExportUtil {
	private static CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator('\n');

	/**
	 * object -> xml 변경하기
	 * @param object
	 * @return
	 */
	public static String toXml(Object object){
		return toXml(object,"item", null);
	}

	public static String toXml(Object object, List<GridColumnInfo> columnInfos){
		return toXml(object, "item" , columnInfos);
	}
	public static String toXml(Object object, String rowName, List<GridColumnInfo> columnInfos){
		XStream magicApi = new XStream();

		magicApi.alias(rowName, Map.class);
		magicApi.registerConverter(new MapEntryConverter());
		return magicApi.toXML(object);
	}

	/**
	 * object -> xml 변경후 write
	 * @param object
	 * @param output
	 */
	public static void toXmlWrite(Object object, OutputStream output){
		toXmlWrite(object, "item", output);
	}

	public static void toXmlWrite(Object object,String rowName, OutputStream output){
		toXmlWrite(object, rowName, null, output);
	}

	public static void toXmlWrite(Object object, List<GridColumnInfo> columnInfos, OutputStream output){
		toXmlWrite(object, "item", columnInfos, output);
	}

	public static void toXmlWrite(Object object, String rowName, List<GridColumnInfo> columnInfos,OutputStream output){
		XStream magicApi = new XStream();
		magicApi.alias(rowName, Map.class);
		magicApi.registerConverter(new MapEntryConverter());
		magicApi.toXML(object, output);
	}

	/**
	 *
	 * @Method Name  : toXmlJsonString
	 * @Method 설명 : jsonstring - > xml 변경 다운로드.
	 * @작성일   : 2019. 8. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param jsonString
	 * @param columnInfos
	 * @param output
	 * @throws IOException
	 */
	public static void jsonStringToXml(String jsonString, List<GridColumnInfo> columnInfos,OutputStream output) throws IOException{
		jsonStringToXml(jsonString, "item", columnInfos, output);
	}
	public static void jsonStringToXml(String jsonString, String rowName, List<GridColumnInfo> columnInfos,OutputStream output) throws IOException{

		Map<String,String> columnKeyLabel = getKeyMap(columnInfos);
		JsonParser parser = null;
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output, VarsqlConstants.CHAR_SET));) {
			XStream magicApi = new XStream(new DomDriver(VarsqlConstants.CHAR_SET, new XmlFriendlyNameCoder("-_", "_")));
			magicApi.alias(rowName, Map.class);
			magicApi.registerConverter(new MapEntryConverter());

			Map rowInfo =null;
			JsonFactory factory = new JsonFactory();
			
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.newLine();
			out.write("<items>");
			out.newLine();
	    	parser = factory.createParser(jsonString);
	        parser.nextToken();                                     //start reading the file
	        while (parser.nextToken() != JsonToken.END_ARRAY) {    //loop until "}"
	        	rowInfo = new HashMap();
	        	boolean addFlag= false; 
	        	while (parser.nextToken() != JsonToken.END_OBJECT) {
	        		String key = getColumnKeyLabelInfo(parser.getCurrentName(), columnKeyLabel); 
	        		if(key != null) {
	        			addFlag= true; 
	        			rowInfo.put(key, parser.getText());
	        		}
	        	}
	        	if(addFlag) {
		        	out.write(magicApi.toXML(rowInfo));
					out.newLine();
	        	}
	        }
	        out.write("</items>");
	        parser.close();
	        out.close();
		}finally {
			if(parser != null)parser.close();
		}
	}

	private static Map<String, String> getKeyMap(List<GridColumnInfo> columnInfos) {
		Map<String,String> columnKeyLabel = new HashMap<>();
		columnInfos.stream().forEach(item ->{
			columnKeyLabel.put(item.getKey(),item.getLabel());
		});
		return columnKeyLabel;
	}

	/**
	 *
	 * @Method Name  : toCSVWrite
	 * @Method 설명 : cvs 내보내기
	 * @작성일   : 2015. 4. 23.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param allInfo
	 * @param output
	 * @throws IOException
	 */
	public static void toCSVWrite(List allInfo, OutputStream output) throws IOException{
		toCSVWrite(allInfo ,BlankConstants.TAB_CHAR , output);
	}

	public static void toCSVWrite(List allInfo,char delimiter,OutputStream output) throws IOException{
		toCSVWrite(allInfo ,delimiter, null ,output);
	}

	public static void toCSVWrite(List allInfo,List<GridColumnInfo> columnInfos, OutputStream output) throws IOException{
		toCSVWrite(allInfo ,BlankConstants.TAB_CHAR,columnInfos, output);
	}

	public static void toCSVWrite(List allInfo,char delimiter, List<GridColumnInfo> columnInfos,  OutputStream output) throws IOException{

		try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter( output ,VarsqlConstants.CHAR_SET),csvFormat.withDelimiter(delimiter))){
			int size =allInfo.size();
			if(size > 0){
				Map recordMap = (Map)allInfo.get(0);
				printer.printRecord(recordMap.keySet());
				for (int i = 0; i < size; i++) {
					recordMap =(Map)allInfo.get(i);
					printer.printRecord(recordMap.values());
				}
			}
			printer.close();
		}
	}

	/**
	 *
	 * @Method Name  : toCsvTOJsonString
	 * @Method 설명 : json string 으로 csv 만들기
	 * @작성일   : 2019. 8. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param jsonString
	 * @param delimiter
	 * @param columnInfos
	 * @param output
	 * @throws IOException
	 */
	public static void jsonStringToCsv(String jsonString,List<GridColumnInfo> columnInfos,  OutputStream output) throws IOException{
		jsonStringToCsv(jsonString ,BlankConstants.TAB_CHAR,columnInfos, output);
	}
	public static void jsonStringToCsv(String jsonString,char delimiter, List<GridColumnInfo> columnInfos,  OutputStream output) throws IOException{
		JsonParser parser =null;
    	try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter( output ,VarsqlConstants.CHAR_SET),csvFormat.withDelimiter(delimiter));) {
    		
    		Map rowInfo =null;
    		boolean firstFlag = true;
    		Map<String,String> columnKeyLabel = getKeyMap(columnInfos);
    		
    		parser =  new JsonFactory().createParser(jsonString);
	        parser.nextToken();                                     //start reading the file
	        while (parser.nextToken() != JsonToken.END_ARRAY) {    //loop until "}"
	        	rowInfo = new HashMap();
	        	boolean addFlag= false; 
	        	while (parser.nextToken() != JsonToken.END_OBJECT) {
	        		String key = getColumnKeyLabelInfo(parser.getCurrentName(), columnKeyLabel); 
	        		if(key != null) {
	        			addFlag= true; 
	        			rowInfo.put(key, parser.getText());
	        		}
	        	}
	        	
	        	if(firstFlag) {
	        		firstFlag = false;
	        		printer.printRecord(rowInfo.keySet());
	        	}
	        	
	        	if(addFlag) {
	        		printer.printRecord(rowInfo.values());
	        	}
	        }
	        printer.close();
	        parser.close();
    	}finally{
    		if(parser!=null)parser.close();
    	}
	}

	private static String getColumnKeyLabelInfo(String fieldName, Map<String, String> columnKeyLabel) {
		return columnKeyLabel.get(fieldName);
	}

	public static void toExcelWrite(List allInfo, List<GridColumnInfo> columnInfos,  OutputStream output) throws IOException{
		int colSize =columnInfos.size();
		ExcelReportVO[] columnArr = new ExcelReportVO[colSize];
		for(int i =0 ; i< colSize; i++){
			GridColumnInfo column = columnInfos.get(i);
			columnArr[i]  = new ExcelReportVO(column.getKey() , column.getLabel());
		}

		ExcelExport export = new ExcelExport("data", columnArr);
		export.addListRow(allInfo);
		export.write(output);
	}

	public static void jsonStringToExcel(String jsonString, List<GridColumnInfo> columnInfos,  OutputStream output) throws IOException{

		int colSize = columnInfos.size() ;

		ExcelReportVO[] columnArr = new ExcelReportVO[colSize];
		for(int i =0; i< colSize; i++){
			GridColumnInfo column = columnInfos.get(i);
			columnArr[i]  = new ExcelReportVO(column.getKey() , column.getLabel());
		}

		ExcelExport export = new ExcelExport("data", columnArr);

		Map rowInfo =null;
		JsonFactory factory = new JsonFactory();

    	JsonParser parser = null;

    	try {
    		parser = factory.createParser(jsonString);
	        parser.nextToken();                                     //start reading the file
	        while (parser.nextToken() != JsonToken.END_ARRAY) {    //loop until "}"
	        	rowInfo = new HashMap();
	        	while (parser.nextToken() != JsonToken.END_OBJECT) {
	        		String fieldName = parser.getCurrentName();
	        		rowInfo.put(fieldName, parser.getText());
	        	}

	        	export.addRow(rowInfo);
	        }

	        export.write(output);
	        parser.close();
    	}finally{
    		if(parser!=null)parser.close();
    	}
	}

	/**
	 *
	 * @Method Name  : toInsertQueryJsonString
	 * @Method 설명 : insert query
	 * @작성일   : 2019. 8. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param jsonString
	 * @param columnInfoList
	 * @param tmpName
	 * @param output
	 * @throws IOException
	 */
	public static void jsonStringToInsertQuery(String jsonString, List<Boolean> columnInfoList,String tmpName, OutputStream output) throws IOException{
		JsonParser parser = null;
    	try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output, VarsqlConstants.CHAR_SET));){
    		StringBuffer insQuery = new StringBuffer("insert into "+tmpName+" ( ");
    		JsonFactory factory = new JsonFactory();
        	
    		parser = factory.createParser(jsonString);
	        parser.nextToken();
	        
	        boolean firstFlag = true;
			int keyLen = -1;
	        Map rowInfo =null;
	        String insertPrefix ="";
	        while (parser.nextToken() != JsonToken.END_ARRAY) {
	        	rowInfo = new HashMap();
	        	while (parser.nextToken() != JsonToken.END_OBJECT) {
	        		String fieldName = parser.getCurrentName();
	        		rowInfo.put(fieldName, parser.getText());
	        	}

	        	if(firstFlag) {
	        		firstFlag = false;

	    			Object[] keyArr = rowInfo.keySet().toArray();
	    			keyLen = keyArr.length;
	    			boolean firstCommaFlag =true;
	    			for (int i= 0; i <keyLen; i++) {
	    				insQuery.append(firstCommaFlag?"":",").append(keyArr[i]);
	    				firstCommaFlag = false;
	    			}

	    			insQuery.append(" ) values ( ");
	    			insertPrefix = insQuery.toString();
	        	}

				Object [] valueArr = null;
				Object val = null;
				valueArr = rowInfo.values().toArray();
				insQuery.setLength(0);
				insQuery.append(insertPrefix);
				firstFlag =true;
				for(int j=0 ;j<keyLen; j++){
					val = valueArr[j];
					if(columnInfoList.get(j)){
						insQuery.append(firstFlag?"":",").append("".equals(val)?0:val);
					}else{
						insQuery.append(firstFlag?"":",").append("'").append(escape(val)).append("'");
					}
					firstFlag =false;
				}
				insQuery.append(");");
				out.write(insQuery.toString());
				out.newLine();
	        }
	        parser.close();
	        out.close();
    	}finally{
    		if(parser!=null)parser.close();
    	}
	}


	public static void jsonStringToJson(String jsonString, List<GridColumnInfo> columnInfos, OutputStream os) throws IOException{

		JsonParser parser = null;
		
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (os, VarsqlConstants.CHAR_SET));) {
			Map<String,String> columnKeyLabel = getKeyMap(columnInfos);
			
			Map rowInfo =null;
			JsonFactory factory = new JsonFactory();
			out.write("[");
	    	parser = factory.createParser(jsonString);
	        parser.nextToken();                        //start reading the file
	        boolean firstFlag = true;
	        while (parser.nextToken() != JsonToken.END_ARRAY) {    //loop until "}"
	        	rowInfo = new HashMap();
	        	
	        	boolean addFlag= false; 
	        	while (parser.nextToken() != JsonToken.END_OBJECT) {
	        		String key = getColumnKeyLabelInfo(parser.getCurrentName(), columnKeyLabel); 
	        		if(key != null) {
	        			addFlag= true; 
	        			rowInfo.put(key, parser.getText());
	        		}
	        	}
	        	if(addFlag) {
		        	out.write((firstFlag ?"":",")+ VartechUtils.objectToJsonString(rowInfo));
		        	if(firstFlag) firstFlag = false;
	        	}
	        }
	    	out.write("]");
	        parser.close();
	        out.close();
		}finally{
    		if(parser!=null)parser.close();
    	}
	}

	public static void toInsertQueryWrite(List allInfo, List<Boolean> columnInfoList,String tmpName, OutputStream output) throws IOException{
		int size =allInfo.size();

		if(size > 0){
			try(BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output,  VarsqlConstants.CHAR_SET));){
				StringBuffer insQuery = new StringBuffer("insert into "+tmpName+" ( ");
	
				Map recordMap = (Map)allInfo.get(0);
	
				Object[] keyArr = recordMap.keySet().toArray();
				int keyLen = keyArr.length;
				boolean firstFlag =true;
				for (int i= 0; i <keyLen; i++) {
					insQuery.append(firstFlag?"":",").append(keyArr[i]);
					firstFlag = false;
				}
	
				insQuery.append(" ) values ( ");
				String insertPrefix = insQuery.toString();
	
				Object val = null;
				for (int i = 0; i < size; i++) {
					recordMap = (Map) allInfo.get(i);
					insQuery.setLength(0);
					insQuery.append(insertPrefix);
					firstFlag =true;
					for(int j=0 ;j<keyLen; j++){
						val = recordMap.get(keyArr[j]);
						if(columnInfoList.get(j)){
							insQuery.append(firstFlag?"":",").append("".equals(val)?0:val);
						}else{
							if(val==null) {
								insQuery.append(firstFlag?"":",").append("null");
							}else {
								insQuery.append(firstFlag?"":",").append("'").append(escape(val)).append("'");
							}
	
						}
						firstFlag =false;
					}
					insQuery.append(");");
					out.write(insQuery.toString());
					out.newLine();
				}
				out.close();
			}
		}
	}

	/**
	 *
	 * @Method Name  : toTextWrite
	 * @Method 설명 : text download
	 * @작성일   : 2019. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param fileName
	 * @param downText
	 * @param output
	 * @throws IOException
	 */
	public static void toTextWrite(String downText ,OutputStream output) throws IOException{
		
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output, VarsqlConstants.CHAR_SET));) {
			out.write(downText);
			out.close();
		}
	}

	private static String escape(Object str) {
		if(str==null) return null;

		String s = str.toString();

		StringBuffer sb = new StringBuffer();
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '\'':
				sb.append("''");
				break;
			case '\\':
				sb.append("\\");
				break;
			case '\b':
				sb.append("\b");
				break;
			case '\f':
				sb.append("\f");
				break;
			case '\n':
				sb.append("\n");
				break;
			case '\r':
				sb.append("\r");
				break;
			case '\t':
				sb.append("\t");
				break;
			case '/':
				sb.append("/");
				break;
			default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}

class MapEntryConverter implements Converter {
    public boolean canConvert(Class clazz) {
        return AbstractMap.class.isAssignableFrom(clazz);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        AbstractMap map = (AbstractMap) value;
        for (Object obj : map.entrySet()) {
            Entry entry = (Entry) obj;
            writer.startNode(entry.getKey().toString());
            writer.setValue(String.valueOf(entry.getValue()));
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Map<String, String> map = new HashMap<String, String>();
        while(reader.hasMoreChildren()) {
            reader.moveDown();
            map.put(reader.getNodeName(), reader.getValue());
            reader.moveUp();
        }
        return map;
    }
}
