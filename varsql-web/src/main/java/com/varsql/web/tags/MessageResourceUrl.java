package com.varsql.web.tags;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.configuration.AppResourceMessageBundleSource;

public class MessageResourceUrl extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String name;

	@Override
	public int doStartTag() throws JspException {
		JspWriter jw= pageContext.getOut();

		try {
			String contextPath = pageContext.getServletContext().getContextPath();
			
			Locale uLocale = LocaleContextHolder.getLocale();
			
			uLocale = LocaleConstants.parseLocaleString(uLocale.getLanguage());

			contextPath = !StringUtils.isBlank(contextPath) ? contextPath : "";
			
			if(uLocale==null) {
				jw.write(contextPath+"/i18nMessage?v="+AppResourceMessageBundleSource.getLastLoadTime(uLocale));
			}else {
				jw.write(contextPath+"/i18nMessage?lang="+uLocale.getLanguage()+"&v="+AppResourceMessageBundleSource.getLastLoadTime(uLocale));
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
