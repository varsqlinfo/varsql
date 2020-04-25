package com.varsql.web.util;

import org.springframework.context.ApplicationContext;

import com.varsql.web.configuration.ApplicationContextProvider;

public class BeanUtils {
    public static Object getBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }
}
