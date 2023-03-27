package com.varsql.web.configuration.scheduler;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.varsql.core.configuration.Configuration;

/**
 * 
 * 
 * @fileName : SchedulerCondition.java
 * @author : ytkim
 */
public class SchedulerCondition implements Condition {
	
	final static public String SYSTEM_PROP_CONFIG_KEY = "varsql.quartz.config.disable";

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		if("Y".equals(System.getProperty(SYSTEM_PROP_CONFIG_KEY))) {
			return false; 
		}
	
		return Configuration.getInstance().isScheduleEnable();
	}
}