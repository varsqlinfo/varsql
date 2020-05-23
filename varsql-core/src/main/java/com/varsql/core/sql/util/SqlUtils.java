package com.varsql.core.sql.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlUtils {
	public static void close(ResultSet rs) {
		close(null, null, rs);
	}
	
	public static void close(Statement stmt) {
		close(null, stmt, null);
	}
	
	public static void close(Connection con) {
		close(con, null, null);
	}

	public static void close(Statement stmt, ResultSet rs) {
		close(null, stmt, rs);
	}
	
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try {if (rs != null)  rs.close();} catch (SQLException e) {}
		try {if (stmt != null) stmt.close();} catch (Exception ex) {}
		try {if (con != null) con.close();} catch (SQLException e) {}
	}
	
	/**
	 * 컬럼명 가져오기
	 * @Method Name  : columnName
	 * @Author : ytkim
	 * @Method desc :
	 * @History : 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static String [] columnName(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = null;
		try {
			rsmd = rs.getMetaData();
		
			int count = rsmd.getColumnCount();
			String [] columns_key = new String[count];
			
			for (int i = 1; i <= count; i++) {
				columns_key[i - 1] = rsmd.getColumnName(i);
			}
			return columns_key ; 
		}catch(SQLException e){
			throw e; 
		}
	}
	
	/**
	 * resultset 객체로 변환
	 * @Method Name  : resultsetForObject
	 * @Author : ytkim
	 * @Method desc :
	 * @History : 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static Object resultsetForObject(ResultSet rs) throws IllegalArgumentException, Exception{
		return resultsetForObject(rs, null);
	}
	
	public static Object resultsetForObject(ResultSet rs , Class clazz) throws IllegalArgumentException, Exception{
		return resultsetForObject(rs, clazz , true);
	}
	public static Object resultsetForObject(ResultSet rs , Class clazz , boolean closeFlag) throws IllegalArgumentException, Exception{
		ResultSetMetaData rsmd = null;
		try {
			rsmd = rs.getMetaData();

			int count = rsmd.getColumnCount();
			String[] tmp_columns_key = new String[count];

			for (int i = 0; i < count; i++) {
				tmp_columns_key[i] = rsmd.getColumnName(i + 1);
			}
			String[] columns_key = tmp_columns_key;

			String key = null;
			Object val = null;
			
			rs.next();

			if (clazz != null && !(clazz.isAssignableFrom(Map.class)) ) {
				Field[] fieldArr = clazz.getDeclaredFields();
				
				Map<String, Field> fieldMap = new HashMap<String, Field>();
				for (int i = 0; i < fieldArr.length; i++) {
					fieldMap.put(fieldArr[i].getName().toLowerCase(), fieldArr[i]);
				}

				Field temp_f = null;
				Object reObj = clazz.newInstance();
				
				for (int j = 0; j < tmp_columns_key.length; j++) {
					key = tmp_columns_key[j];
					temp_f = fieldMap.get(key.toLowerCase());
					
					if(temp_f==null) continue; 
					
					val = rs.getObject(key);
					
					if(val ==null) continue; 
					
					temp_f.setAccessible(true);
					
					temp_f.set(reObj, getResultsetVal(rs , key , val , temp_f));
				}

				return reObj;
			}
			
			Map columns = new LinkedHashMap(count);
			for (int i = 0; i < count; i++) {				
				key = columns_key[i];
				val=rs.getObject(key);
				
				key = key.toUpperCase();
				columns.put(key, getResultsetVal(rs , key , val , null));
			}
		
			if(closeFlag) if(rs !=null ) rs.close();
			
			return columns; 
		}finally{
			if(closeFlag){
				if(rs != null ) rs.close();
			}
		}
	}
	
	/**
	 * resultset value 꺼내기
	 * @Method Name  : resultsetForList
	 * @Author : ytkim
	 * @Method desc :
	 * @History : 
	 * @param rs
	 * @param clazz
	 * @param closeFlag
	 * @return
	 * @throws Exception
	 */
	public static List resultsetForList(ResultSet rs) throws Exception{
		return resultsetForList(rs, null);
	}
	
	public static List resultsetForList(ResultSet rs , Class clazz) throws Exception{
		return resultsetForList(rs, clazz , true);
	}
	
	public static List resultsetForList(ResultSet rs , Class clazz , boolean closeFlag) throws Exception{
		ResultSetMetaData rsmd = null;
		try {
			rsmd = rs.getMetaData();
			
			int count = rsmd.getColumnCount();
			String[] tmp_columns_key = new String[count];
			
			for (int i = 0; i < count; i++) {
				tmp_columns_key[i] = rsmd.getColumnName(i + 1);
			}
			String[] columns_key = tmp_columns_key;
			
			String key = null;
			Object val = null;
			int byteRead=-1;
			
			List result = new ArrayList();
			
			if (clazz != null && !(clazz.isAssignableFrom(Map.class)) ) {
				Field[] fieldArr = clazz.getDeclaredFields();
				
				Map<String, Field> fieldMap = new HashMap<String, Field>();
				for (int i = 0; i < fieldArr.length; i++) {
					fieldMap.put(fieldArr[i].getName().toLowerCase(), fieldArr[i]);
				}
				
				Field temp_f = null;
				
				Object reObj = null;
				while(rs.next()){
					reObj = clazz.newInstance();
			
					for (int j = 0; j < tmp_columns_key.length; j++) {
						key = tmp_columns_key[j];
						temp_f = fieldMap.get(key.toLowerCase());
						
						if(temp_f==null) continue; 
						
						val = rs.getObject(key);
						
						if(val ==null) continue; 
						
						
						temp_f.set(reObj, getResultsetVal(rs , key , val , temp_f));
					}
					result.add(reObj);
				}
				return result;
			}
			
			Map columns = null;
			
			while(rs.next()){
				columns = new HashMap(count);
				for (int i = 0; i < count; i++) {				
					key = columns_key[i];
					val=rs.getObject(key);
					
					key = key.toUpperCase();
					columns.put(key, getResultsetVal(rs, key , val , null));
				}
				
				result.add(columns);
			}
			
			if(closeFlag) if(rs !=null ) rs.close();
			
			return result; 
		}finally{
			if(closeFlag){
				if(rs != null ) rs.close();
			}
		}
	}
	
	/**
	 * result set value
	 * @Method Name  : getResultsetVal
	 * @Author : ytkim
	 * @Method desc :
	 * @History : 
	 * @param rs
	 * @param key
	 * @param val
	 * @param temp_f
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws Exception
	 */
	private static Object getResultsetVal(ResultSet rs, String key , Object val , Field temp_f) throws SQLException, IOException {
		int byteRead=-1;
						
		if ((val instanceof Clob)) {
			StringBuffer output = new StringBuffer();
			Reader input = rs.getCharacterStream(key);
			char[] buffer = new char[1024];
			while ((byteRead = input.read(buffer, 0, 1024)) != -1) {
				output.append(buffer, 0, byteRead);
			}
			input.close();
			
			val = output.toString();
		}
		
		if(temp_f != null){
			temp_f.setAccessible(true);
			if (temp_f.getClass().isAssignableFrom(Double.class)) {
				val = new Double(rs.getString(key)); 
			} else if (temp_f.getClass().isAssignableFrom(Long.class)) {
				val = new Long(rs.getString(key)); 
			} else if (temp_f.getClass().isAssignableFrom(Integer.class)) {
				val = Integer.parseInt(rs.getString(key));
			} else if (temp_f.getClass().isAssignableFrom(Timestamp.class)) {
				val = rs.getTimestamp(key);
			}
		}
		return val;
		
	}
}
