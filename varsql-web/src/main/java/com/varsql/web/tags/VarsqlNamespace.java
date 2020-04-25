package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.core.common.util.UUIDUtil;
import com.vartech.common.utils.VartechUtils;

public class VarsqlNamespace extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	final private String prefix= "varsql_";
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();
		
		try {
			
			Object namespace = pageContext.getAttribute("$namespace$",PageContext.PAGE_SCOPE);
			
			if(namespace==null) {
				String simpleName = pageContext.getPage().getClass().getSimpleName();
				namespace = UUIDUtil.nameUUIDFromBytes(simpleName);
				pageContext.setAttribute("$namespace$",namespace);
				
			}
		
			jw.write(String.format("%s%s",prefix, namespace));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
			
	
}
