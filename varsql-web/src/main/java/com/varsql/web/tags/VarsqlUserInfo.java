package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.core.auth.User;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.exception.VarsqlTagException;
import com.vartech.common.utils.VartechReflectionUtils;

public class VarsqlUserInfo extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String var;
	
	private String property;

	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();

		try {
			Object propVal = null;
			if(SecurityUtil.isAnonymous()) {
				propVal =  User.ANONYMOUS_USERNAME;
			}else {
				propVal = VartechReflectionUtils.getProperty(SecurityUtil.loginInfo(), property);
			}
			
			if(var ==null){
				jw.write(String.valueOf(propVal));
			}else{
				pageContext.setAttribute(var, propVal);
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

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
