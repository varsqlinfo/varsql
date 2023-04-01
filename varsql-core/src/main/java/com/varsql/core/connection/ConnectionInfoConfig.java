package com.varsql.core.connection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConnectionInfoConfig {
	public BeanType beanType() default BeanType.SPRING;
	
	public String beanName() default "simpleConnectionInfoDao";
	
	public boolean primary() default false; 
}

