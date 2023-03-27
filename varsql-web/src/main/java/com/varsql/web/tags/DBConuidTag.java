package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.exception.VarsqlTagException;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBConuidTag.java
* @desc		: db con uid
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2021. 4. 11. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBConuidTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String var;

	private String property;

	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();

		try {
			Object propVal = pageContext.getRequest().getAttribute(VarsqlParamConstants.CONN_UUID);

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
