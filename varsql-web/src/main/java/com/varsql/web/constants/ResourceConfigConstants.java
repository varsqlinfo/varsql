package com.varsql.web.constants;

public interface ResourceConfigConstants {

	final String APP_DB_RESOURCE ="varsqlAppSqlSession";
	
	final String APP_DATASOURCE ="varsqlDataSource";

	final String APP_TRANSMANAGER ="transactionManager";

	final String APP_BATCH_TRANSMANAGER ="varBatchTransManager";

	//web socket task executor
	final String APP_WEB_SOCKET_TASK_EXECUTOR ="varsqlWebSocketTaskExecutor";

	final String APP_LOG_TASK_EXECUTOR ="varsqlLogTaskExecutor";

	final String APP_PASSWORD_ENCODER ="varsqlPasswordEncoder";

	final String APP_SSO_FILTER ="varsqlSsoFilter";

	final String APP_SSO_BEAN_FACTORY ="ssoBeanFactory";

	final String APP_SSO_COMPONENT = "ssoComponent";

	final String CACHE_CONDITION_COMPONENT = "varsqlCacheCondition";
	
	final String CACHE_MANAGER = "varsqlCacheManager";
	
	final String USER_DETAIL_SERVICE = "varsqlUserDetailService";
	
	final String REMEMBERME_USER_DETAIL_SERVICE = "rememberMeUserService";
	
	final String MAIL_SERVICE = "mainService";
	
	final String APP_SCHEDULER = "varsqlScheduler";
	
	final String APP_SECURITY_LOG_SERVICE = "securityLogService";
	
	final String APP_USER_COMMON_SERVICE = "userCommonService";
	
	final String APP_MESSAGE_SOURCE = "messageSource";
}
