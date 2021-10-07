package com.varsql.core.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class VarsqlSpringBeanUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		if (applicationContext == null) {
			applicationContext = ctx;
		}
	}

	public static Object getStringBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> requiredType) {
		return (T) applicationContext.getBean(beanName, requiredType);
	}

}
