package com.varsql.core.pattern.convert;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import com.varsql.core.pattern.convert.CommentRemoveConverter;
import com.varsql.core.pattern.convert.CommentRemoveConverter.CommentType;
import com.varsql.core.test.BaseTest;

public class CommentRemoveUtilTest extends BaseTest{
	
	private boolean allLogView = false; 
	CommentRemoveConverter.CommentType logViewType = null;//CommentRemoveUtil.CommentType.JAVASCRIPT;
	@Test
	public void testJAVA() {
		String cont = getResourceContent("/comment/program/java.txt");
		
		CommentRemoveConverter cru = new CommentRemoveConverter();
		
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.JAVA);
		viewResult(result, CommentRemoveConverter.CommentType.JAVA);
		
		assertNotEquals(result.indexOf("/*"), -10);
		assertNotEquals(result.indexOf("//"), -10);
		
	}
	
	@Test
	public void testJSP() {
		String cont = getResourceContent("/comment/program/jsp.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.JSP);
		
		viewResult(result, CommentRemoveConverter.CommentType.JSP);
		
		assertNotEquals(result.indexOf("<%--"), -10);
		assertNotEquals(result.indexOf("<!--"), -10);
	}
	
	@Test
	public void testJAVASCRIPT(){
		String cont = getResourceContent("/comment/program/javascript.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.JAVASCRIPT);
		
		viewResult(result, CommentRemoveConverter.CommentType.JAVASCRIPT);
		
		assertFalse(result.indexOf("/*") > -1, result+ "\n // multi line comment not remove");
	}
	
	@Test
	public void testCSS(){
		String cont = getResourceContent("/comment/program/css.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.CSS);
		
		viewResult(result, CommentRemoveConverter.CommentType.CSS);
		
		assertNotEquals(result.indexOf("/*"), -10);
	}
	
	@Test
	public void testHTML(){
		String cont = getResourceContent("/comment/program/html.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.HTML);
		
		viewResult(result, CommentRemoveConverter.CommentType.HTML);
		
		assertNotEquals(result.indexOf("<!--"), -10);
	}
	
	@Test
	public void testXML(){
		String cont = getResourceContent("/comment/program/xml.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.XML);
		
		viewResult(result, CommentRemoveConverter.CommentType.XML);
		
		assertNotEquals(result.indexOf("<!--"), -10);
	}
	
	@Test
	public void testPROPERTY(){
		String cont = getResourceContent("/comment/program/property.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.PROPERTY);
		
		viewResult(result ,CommentRemoveConverter.CommentType.PROPERTY);
		
		assertNotEquals(result.indexOf("#"), -10);
	}
	
	private void viewResult(String result, CommentType property) {
		
		if(allLogView) {
			System.out.println(result);
		}else {
			if(property.equals(logViewType)) {
				System.out.println(result);
			}
		}
		
	}
}
