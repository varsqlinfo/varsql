package com.varsql.app.tags;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.core.common.constants.LocaleConstants;

public class VarsqlResourceLocaleName extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private String name; 
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();
		
		try {
			
			Locale uLocale = pageContext.getResponse().getLocale();
			
			uLocale = LocaleConstants.parseLocaleString(uLocale.toString());
			
			if(uLocale==null) {
				jw.write(name);
			}else {
				
				jw.write(name+"."+uLocale.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
			
	
}
