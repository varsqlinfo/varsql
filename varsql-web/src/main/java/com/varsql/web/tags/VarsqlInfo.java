package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.varsql.web.constants.VarsqlInfoConstants;
import com.varsql.web.exception.VarsqlTagException;

public class VarsqlInfo extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String attribute;

	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();

		try {
			if("version".equalsIgnoreCase(attribute)) {
				jw.write(VarsqlInfoConstants.VERSION);
			}else if("email".equalsIgnoreCase(attribute)) {
				jw.write(VarsqlInfoConstants.EMAIL);
			}else if("homepage".equalsIgnoreCase(attribute)) {
				jw.write(VarsqlInfoConstants.HOME_PAGE);
			}else if("name".equalsIgnoreCase(attribute)) {
				jw.write(VarsqlInfoConstants.APP_NAME);
			}
		} catch (IOException e) {
			throw new VarsqlTagException("tag error ",e); 
		}

		return SKIP_BODY;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}



}
