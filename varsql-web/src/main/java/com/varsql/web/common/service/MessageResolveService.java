package com.varsql.web.common.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import com.varsql.web.configuration.AppResourceMessageBundleSource;
import com.varsql.web.constants.ResourceConfigConstants;

@Service
public class MessageResolveService implements MessageSourceAware{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;
	
	@Autowired
	@Qualifier(ResourceConfigConstants.APP_MESSAGE_SOURCE)
	private AppResourceMessageBundleSource appResourceMessageBundleSource;
	
	public String getMessage(String key, Object[] arguments, Locale locale) {
		String message = "";
		try {
			message = messageSource.getMessage(key, arguments, locale);
		} catch (NoSuchMessageException e) {
			message = key;
			logger.warn("No message found: " + key);
		}
		return message;
	}

	public String getMessages(Locale locale) {
		return appResourceMessageBundleSource.getMergedJsonString(locale);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
