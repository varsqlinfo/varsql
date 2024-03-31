package com.varsql.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.core.common.constants.LocaleConstants;


public class SupportLocaleTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String var;

	@Override
	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, LocaleConstants.values());
		return SKIP_BODY;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
