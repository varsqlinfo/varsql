package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.ObjectUtils;

import com.varsql.web.constants.VarsqlParamConstants;


public class BoardCodeTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		Object boardCode = ((PageContext)getJspContext()).getRequest().getAttribute(VarsqlParamConstants.BOARD_CODE);
		
		if(ObjectUtils.isEmpty(boardCode)) {
			this.getJspContext().getOut().write("null");
		}else {
			this.getJspContext().getOut().write(boardCode.toString());
		}
		return ;
	}

}
