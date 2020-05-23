package com.varsql.core.sql.resultset.handler;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * 
 * @FileName  : DDLScriptAbstract.java
 * @프로그램 설명 : script 생성 클래스
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class ResultSetHandlerImpl implements ResultSetHandler{
	
	private static SimpleDateFormat timestampSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
	private static SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd"); 
	private static SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss.SSS"); 
	
	protected ResultSetHandlerImpl(){}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map getDataValue(Map dataMap, String keyName, String columnName, ResultSet rs, int colIdx, String varsqlType ,String columnTypeName) throws SQLException {
		if("number".equals(varsqlType)){
			dataMap.put(keyName,getNumber(rs, colIdx));
		}else if("string".equals(varsqlType)){
			dataMap.put(keyName,getString(rs, colIdx));
		}else if( "clob".equals(varsqlType)){
			dataMap.put(keyName , getClob(rs, colIdx));
		}else if( "blob".equals(varsqlType)){
			dataMap.put(keyName , getBlob(rs, colIdx));
		}else if("timestamp".equals(varsqlType)){
			dataMap.put(keyName, getTimeStamp(rs, colIdx));
		}else if("date".equals(varsqlType)){
			dataMap.put(keyName, getDate(rs, colIdx));
		}else if("time".equals(varsqlType)){
			dataMap.put(keyName,getTime(rs, colIdx));
		}else if("sqlxml".equals(varsqlType)){
			dataMap.put(keyName,getSQLXML(rs, colIdx));
		}else if("binary".equals(varsqlType)){
			/*
			if("RAW".equals(columnTypeName)) {
				dataMap.put(keyName,getRAW(rs, colIdx));
			}else {
				dataMap.put(keyName,getBinary(rs, colIdx));
			}
			*/
			dataMap.put(keyName,"[Binary]"+columnTypeName);
		}else if("nclob".equals(varsqlType)){
			dataMap.put(keyName,getNCLOB(rs, colIdx));
		}else if("raw".equals(varsqlType)){
			dataMap.put(keyName,getNCLOB(rs, colIdx));
		}else{
			dataMap.put(keyName,getObject(rs, colIdx));
		}
		
		return dataMap; 
	}

	@Override
	public Number getNumber(ResultSet rs, int columnIdx) throws SQLException {
		return (Number)rs.getObject(columnIdx);
	}
	
	@Override
	public Number getNumber(ResultSet rs, String columnName) throws SQLException {
		return (Number)rs.getObject(columnName);
	}

	@Override
	public String getNumberToString(ResultSet rs, int columnIdx)throws SQLException  {
		return rs.getObject(columnIdx)+"";
	}
	@Override
	public String getNumberToString(ResultSet rs, String columnName)throws SQLException  {
		return rs.getObject(columnName)+"";
	}

	@Override
	public String getClob(ResultSet rs, int columnIdx)throws SQLException  {
		return getClob(rs.getCharacterStream(columnIdx));
	}
	
	@Override
	public String getClob(ResultSet rs, String columnName)throws SQLException  {
		return getClob(rs.getCharacterStream(columnName));
	}
	private String getClob(Reader val)throws SQLException  {
		char[] buffer  = null;
		int byteRead=-1;
		
		if(isNull(val)) return null;
		
		StringBuffer output = new StringBuffer();
		try{
			
			buffer = new char[1024];
			while((byteRead=val.read(buffer,0,1024))!=-1){
				output.append(buffer,0,byteRead);
			}
			val.close();
			return output.toString(); 
		}catch(Exception e){
			if(val !=null) try{val.close();}catch(Exception e1){}
			return "Clob" +e.getMessage();
		}
	}
	
	@Override
	public String getNCLOB(ResultSet rs, int columnIdx)throws SQLException  {
		return getNCLOB( rs.getNClob(columnIdx));
	}
	
	@Override
	public String getNCLOB(ResultSet rs, String columnName)throws SQLException  {
		return getNCLOB( rs.getNClob(columnName));
	}
	
	private String getNCLOB(NClob val) throws SQLException  {
		if(isNull(val)) return null; 
		return getClob(val.getCharacterStream());
	}

	@Override
	public String getBlob(ResultSet rs, int columnIdx)throws SQLException  {
		return getBlob(rs.getBlob(columnIdx));
	}
	@Override
	public String getBlob(ResultSet rs, String columnName)throws SQLException  {
		return getBlob(rs.getBlob(columnName));
	}
	
	private String getBlob(Blob val) {
		if(isNull(val)) return null; 
		
		return "blob";
	}
	@Override
	public String getTimeStamp(ResultSet rs, int columnIdx)throws SQLException  {
		return getTimeStamp(rs.getTimestamp(columnIdx));
	}
	@Override
	public String getTimeStamp(ResultSet rs, String columnName)throws SQLException  {
		return getTimeStamp(rs.getTimestamp(columnName));
	}
	
	private String getTimeStamp(Timestamp val){
		if(isNull(val)) return null; 
		return timestampSDF.format(val);
	}

	@Override
	public String getDate(ResultSet rs, int columnIdx) throws SQLException {
		return getDate(rs.getDate(columnIdx));
	}
	@Override
	public String getDate(ResultSet rs, String columnName) throws SQLException {
		return getDate(rs.getDate(columnName));
	}
	
	private String getDate(Date val) {
		if(isNull(val)) return null; 
		return dateSDF.format(val);
	}

	@Override
	public String getTime(ResultSet rs, int columnIdx) throws SQLException {
		return getTime(rs.getTime(columnIdx));
	}
	
	@Override
	public String getTime(ResultSet rs, String columnName) throws SQLException {
		return getTime(rs.getTime(columnName));
	}
	
	private String getTime(Time val){
		if(isNull(val)) return null; 
		return timeSDF.format(val);
	}

	@Override
	public String getString(ResultSet rs, int columnIdx) throws SQLException {
		return rs.getString(columnIdx);
	}
	
	@Override
	public String getString(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}
	
	@Override
	public String getObject(ResultSet rs, int columnIdx) throws SQLException {
		return getObject(rs.getObject(columnIdx));
	}
	@Override
	public String getObject(ResultSet rs, String columnName) throws SQLException {
		return getObject(rs.getObject(columnName));
	}
	
	private String getObject(Object val) {
		if(isNull(val)) return null; 
		return val+"";
	}
	
	@Override
	public String getSQLXML(ResultSet rs, int columnIdx) throws SQLException {
		return getSQLXML(rs.getSQLXML(columnIdx));
	}
	
	@Override
	public String getSQLXML(ResultSet rs, String columnName) throws SQLException {
		return getSQLXML(rs.getSQLXML(columnName));
	}
	
	private String getSQLXML(SQLXML val) throws SQLException {
		if(isNull(val)) return null; 
		return val != null ? val.getString() : null;
	}
	
	@Override
	public String getBinary(ResultSet rs, int columnIdx) throws SQLException {
		return getBinary(rs.getBinaryStream(columnIdx));
	}
	
	@Override
	public String getBinary(ResultSet rs, String columnName) throws SQLException {
		return getBinary(rs.getBinaryStream(columnName));
	}
	
	@Override
	public String getRAW(ResultSet rs, int columnIdx) throws SQLException {
		return getRawVal(rs.getObject(columnIdx));
	}
	
	@Override
	public String getRAW(ResultSet rs, String columnName) throws SQLException {
		return getRawVal(rs.getObject(columnName));
	}
	
	private String getRawVal(Object obj) {
		return "RAW";
	}
	
	private String getBinary(InputStream val) {
		if(isNull(val)) return null; 
		return "BINARY";
	}
	
	
	private boolean isNull(Object obj){
		return obj ==null ? true:false;
	}
}
