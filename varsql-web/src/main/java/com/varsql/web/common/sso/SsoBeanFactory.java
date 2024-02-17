package com.varsql.web.common.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.core.sso.SsoHandler;
import com.varsql.web.constants.ResourceConfigConstants;

@Component(ResourceConfigConstants.APP_SSO_BEAN_FACTORY)
public class SsoBeanFactory {

	private static final Logger logger = LoggerFactory.getLogger(SsoBeanFactory.class);

	private final BeanFactory beanFactory;

	@Autowired
	public SsoBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public SsoHandler getSsoBean() {
		try {
			return beanFactory.getBean(VarsqlWebConfig.getInstance().getSsoConfig().getComponentName(), SsoHandler.class);
		}catch(BeansException e) {
			logger.error("sso handler name: '{}' not found" , VarsqlWebConfig.getInstance().getSsoConfig().getComponentName() , e);
			return null;
		}
	}
}