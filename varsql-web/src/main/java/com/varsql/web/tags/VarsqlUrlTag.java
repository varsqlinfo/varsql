package com.varsql.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.varsql.web.constants.VarsqlURLInfo;
import com.varsql.web.exception.VarsqlTagException;
import com.vartech.common.utils.StringUtils;

public class VarsqlUrlTag extends SimpleTagSupport {
	private String var;

	private String type;
	
	private String suffix;

	@Override
	public void doTag() throws JspException, IOException {
		try {
			VarsqlURLInfo urlInfo = VarsqlURLInfo.get(type);
			String suffixStr = (StringUtils.isBlank(suffix)?"":suffix);
			
			if(urlInfo==null) {
				String contentPath = ((PageContext) getJspContext()).getRequest().getServletContext().getContextPath();
				
				String url = contentPath +"/"+ type +"/" + suffixStr; 
				if (StringUtils.isBlank(this.var)) {
					this.getJspContext().getOut().write(url);
				} else {
					((PageContext) getJspContext()).getRequest().setAttribute(this.var, url);
				}
				
				return ; 
			}

			String url = urlInfo.getUrl() + "" + suffixStr;
			
			if (StringUtils.isBlank(this.var)) {
				this.getJspContext().getOut().write(url);
			} else {
				((PageContext) getJspContext()).getRequest().setAttribute(this.var, url);
			}
		} catch (IOException e) {
			throw new VarsqlTagException("tag error ", e);
		}
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
