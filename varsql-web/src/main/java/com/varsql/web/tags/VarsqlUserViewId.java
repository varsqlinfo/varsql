package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.core.auth.User;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.exception.VarsqlTagException;

public class VarsqlUserViewId extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String var;
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();

		try {
			String viewId = null;
			if(SecurityUtil.isAnonymous()) {
				viewId =  User.ANONYMOUS_USERNAME;
			}else {
				viewId = SecurityUtil.userViewId();
			}
			
			if(var ==null){
				jw.write(viewId);
			}else{
				pageContext.setAttribute(var, viewId);
			}
		} catch (IOException e) {
			throw new VarsqlTagException("tag error ",e); 
		}

		return SKIP_BODY;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
