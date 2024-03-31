package com.varsql.web.common.controller;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.common.service.MessageResolveService;
import com.varsql.web.configuration.AppResourceMessageBundleSource;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.StringUtils;

@Controller
public class MessageController {
	@Autowired
	private MessageResolveService messageResolveService;

	@RequestMapping(value = "/i18nMessage", produces = "application/javascript; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> message(@RequestParam(value = "lang", defaultValue = "") String lang, HttpServletRequest request, HttpServletResponse response) {
		
		Locale locale;
		if(StringUtils.isBlank(lang)) {
			locale = LocaleConstants.parseLocaleString(LocaleContextHolder.getLocale().getLanguage());
		}else {
			locale = LocaleConstants.parseLocaleString(lang);
		}

		if(VarsqlUtils.isRuntimeLocal()){
			return ResponseEntity.ok()
					.body(messageResolveService.getMessages(locale));
		}else {
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
					.lastModified(AppResourceMessageBundleSource.getLastLoadTime(locale))
					.body(messageResolveService.getMessages(locale));
		}

		
	}
}
