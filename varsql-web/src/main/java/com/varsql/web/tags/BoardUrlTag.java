package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.varsql.web.constants.VarsqlParamConstants;


public class BoardUrlTag extends SimpleTagSupport {

	private String addUrl;
	private String boardCode;
	
	private boolean contextPath =true;
	
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter jw = this.getJspContext().getOut();
		
		StringBuilder sb = new StringBuilder();
		
		if(contextPath) {
			sb.append(((PageContext)getJspContext()).getServletContext().getContextPath());
		}
		
		sb.append("/board/");
		
		if(boardCode != null) {
			sb.append(boardCode);
		}else {
			sb.append(((PageContext)getJspContext()).getRequest().getAttribute(VarsqlParamConstants.BOARD_CODE));
		}
		
		if(addUrl != null) {
			sb.append("/").append(addUrl);
		}
		
		jw.write(sb.toString());
		
		return ;
	}

	public String getAddUrl() {
		return addUrl;
	}

	public void setAddUrl(String suffix) {
		this.addUrl = suffix;
	}

	public boolean isContextPath() {
		return contextPath;
	}

	public void setContextPath(boolean contextPath) {
		this.contextPath = contextPath;
	}

	public String getBoardCode() {
		return boardCode;
	}

	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	
}
