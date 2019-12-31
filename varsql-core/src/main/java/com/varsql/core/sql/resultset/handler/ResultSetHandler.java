package com.varsql.core.sql.resultset.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 
 * @FileName  : ResultSetHandler.java
 * @프로그램 설명 : result set handler interface
 * @Date      : 2018. 7. 25. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public interface ResultSetHandler{
	public Map getDataValue(Map addMap, String columnName, ResultSet rs, int columnIdx, String varType , String columnTypeName) throws SQLException ;
	public Number getNumber(ResultSet rs , int columnIdx) throws SQLException ;
	public Number getNumber(ResultSet rs , String columnName) throws SQLException ;
	
	public String getNumberToString(ResultSet rs , int columnIdx) throws SQLException ;
	public String getNumberToString(ResultSet rs , String columnName) throws SQLException ;
	
	public String getClob(ResultSet rs , int columnIdx) throws SQLException ;
	public String getClob(ResultSet rs , String columnName) throws SQLException ;
	
	public String getBlob(ResultSet rs , int columnIdx) throws SQLException ;
	public String getBlob(ResultSet rs , String columnName) throws SQLException ;
	
	public String getTimeStamp(ResultSet rs , int columnIdx) throws SQLException ;
	public String getTimeStamp(ResultSet rs , String columnName) throws SQLException ;
	
	public String getDate(ResultSet rs , int columnIdx) throws SQLException ;
	public String getDate(ResultSet rs , String columnName) throws SQLException ;
	
	public String getTime(ResultSet rs , int columnIdx) throws SQLException ;
	public String getTime(ResultSet rs , String columnName) throws SQLException ;
	
	public String getString(ResultSet rs , int columnIdx)throws SQLException ;
	public String getString(ResultSet rs , String columnName)throws SQLException ;
	
	public String getObject(ResultSet rs , int columnIdx)throws SQLException ;
	public String getObject(ResultSet rs , String columnName)throws SQLException ;
	
	public String getSQLXML(ResultSet rs , int columnIdx)throws SQLException ;
	public String getSQLXML(ResultSet rs , String columnName)throws SQLException ;
	
	public String getBinary(ResultSet rs, int columnIdx) throws SQLException ;
	public String getBinary(ResultSet rs, String columnName) throws SQLException ;
	
	public String getNCLOB(ResultSet rs, int columnIdx) throws SQLException ;
	public String getNCLOB(ResultSet rs, String columnName) throws SQLException ;
	
	public String getRAW(ResultSet rs, int columnIdx) throws SQLException ;
	public String getRAW(ResultSet rs, String columnName) throws SQLException ;
}