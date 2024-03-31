package com.varsql.web.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.varsql.core.configuration.Configuration;

public class InitCondition {

	public static class AppInitCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return Configuration.getInstance().isInit();
		}
	}
}