package com.varsql.web.util;


import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.UrlPathHelper;

import com.varsql.web.common.vo.DataCommonVO;

/**
 * 
 * @FileName : HttpUtil.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 10. 18.
 * @프로그램설명:
 * @변경이력	:
 */
public class HttpUtil {
	
	private final static String CHAR_SET = "UTF-8";
	
	/**
	 * You can't call the constructor.
	 */
	private HttpUtil() {}
	/**
	 * Decode a string from <code>x-www-form-urlencoded</code> format.
	 *
	 * @param   s   an encoded <code>String</code> to be translated.
	 * @return  the original <code>String</code>.
	 * @throws UnsupportedEncodingException 
	 * @see		java.net.URLEncoder#encode(java.lang.String)
	 */
	public static String decode(String s) throws UnsupportedEncodingException {
		return decode(s,CHAR_SET);
	}
	public static String decode(String s,String charset) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode(s,charset);
	}
	/**
	 * Translates a string into <code>x-www-form-urlencoded</code> format.
	 *
	 * @param   s   <code>String</code> to be translated.
	 * @return  the translated <code>String</code>.
	 * @see		java.net.URLEncoder#encode(java.lang.String)
	 */
	public static String encode(String s) {
		return java.net.URLEncoder.encode(s);
	}
	
	/**
	 * @param req javax.servlet.http.HttpServletRequest
	 * @param name DataCommonVO name for this SessionBox
	 */
	public static DataCommonVO getAllParameter(HttpServletRequest req)  {
		DataCommonVO DataCommonVO = new DataCommonVO();

		Enumeration e = req.getParameterNames();
		while(e.hasMoreElements()){
			String key = (String)e.nextElement();
			DataCommonVO.put(key, req.getParameter(key));
		}
		return DataCommonVO;
	}
	
	/**
	 * 
	 * @Method Name : getInt
	 * @작성자 : ytkim
	 * @작성일 : 2013. 10. 18.
	 * @Method설명 :
	 */
	public static String getString(HttpServletRequest req , String name)  {
		return getString(req , name ,"");
	}
	
	public static String getString(HttpServletRequest req , String name , String initval)  {
		String v = req.getParameter(name);
		
		return (v = v==null || "".equals(v)? initval: v); 
	}
	
	/**
	 * 
	 * @Method Name : getInt
	 * @작성자 : ytkim
	 * @작성일 : 2013. 10. 18.
	 * @Method설명 :
	 */
	public static String[] getStringValues(HttpServletRequest req , String name)  {
		String[] v = req.getParameterValues(name);
		
		return (v = v==null || "".equals(v)? new String[0]: v); 
	}
	
	public static String[] getStringValues(HttpServletRequest req , String name , String[] initval)  {
		String[] v = req.getParameterValues(name);
		
		return (v = v==null || "".equals(v)? initval: v); 
	}
	
	/**
	 * 
	 * @Method Name : getInt
	 * @작성자 : ytkim
	 * @작성일 : 2013. 10. 18.
	 * @Method설명 :
	 */
	public static int getInt(HttpServletRequest req , String name)  {
		return getInt(req, name, -1);
	}
	public static int getInt(HttpServletRequest req , String name, int initval)  {
		String v = getString(req,name);
		
		try{
			return Integer.parseInt(v);
		}catch(Exception e){
			return initval; 
		}
	}
	
	/*
	 * @return boolean
	 * @param req HttpServletRequest
	 */
	public static boolean isOverIE50(HttpServletRequest req) {
		String user_agent = (String) req.getHeader("user-agent");

		if ( user_agent == null ) 	return false;

		int index = user_agent.indexOf("MSIE");
		if ( index == -1 ) return false;

		int version = 0;
		try {
			version = Integer.parseInt(user_agent.substring(index+5, index+5+1));
		}
		catch(Exception e){}
		if ( version < 5 ) return false;

		return true;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String getOriginatingRequestUri(HttpServletRequest req){
		return new UrlPathHelper().getOriginatingRequestUri(req);
	}
}