package com.varsql.core.pattern.convert;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

import com.varsql.core.pattern.convert.CommentRemoveConverter;
import com.varsql.core.pattern.convert.CommentRemoveConverter.CommentType;
import com.varsql.core.test.BaseTest;

public class CommentRemoveUtilTest extends BaseTest{
	
	private boolean allLogView = false; 
	CommentRemoveConverter.CommentType logViewType = null;//CommentRemoveUtil.CommentType.JAVASCRIPT;
	@Test
	public void testJAVA() {
		String cont = getResourceContent("/comment/java.txt");
		
		CommentRemoveConverter cru = new CommentRemoveConverter();
		
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.JAVA);
		viewResult(result, CommentRemoveConverter.CommentType.JAVA);
		
		assertNotEquals(result.indexOf("/*"), -10);
		assertNotEquals(result.indexOf("//"), -10);
		
	}
	
	@Test
	public void testJSP() {
		String cont = getResourceContent("/comment/jsp.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.JSP);
		
		viewResult(result, CommentRemoveConverter.CommentType.JSP);
		
		assertNotEquals(result.indexOf("<%--"), -10);
		assertNotEquals(result.indexOf("<!--"), -10);
	}
	
	@Test
	public void testJAVASCRIPT(){
		String cont = getResourceContent("/comment/javascript.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.JAVASCRIPT);
		
		viewResult(result, CommentRemoveConverter.CommentType.JAVASCRIPT);
		
		assertFalse(result+ "\n // multi line comment not remove", result.indexOf("/*") > -1);
	}
	
	@Test
	public void testCSS(){
		String cont = getResourceContent("/comment/css.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.CSS);
		
		viewResult(result, CommentRemoveConverter.CommentType.CSS);
		
		assertNotEquals(result.indexOf("/*"), -10);
	}
	
	@Test
	public void testHTML(){
		String cont = getResourceContent("/comment/html.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.HTML);
		
		viewResult(result, CommentRemoveConverter.CommentType.HTML);
		
		assertNotEquals(result.indexOf("<!--"), -10);
	}
	
	@Test
	public void testXML(){
		String cont = getResourceContent("/comment/xml.txt");
		CommentRemoveConverter cru = new CommentRemoveConverter();
		String result = cru.convert(cont, CommentRemoveConverter.CommentType.XML);
		
		viewResult(result, CommentRemoveConverter.CommentType.XML);
		
		assertNotEquals(result.indexOf("<!--"), -10);
	}
	
	@Test
	public void testPROPERTY(){
		String cont = getResourceContent("/comment/property.txt");
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
