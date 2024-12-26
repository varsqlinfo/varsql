package com.varsql.web.constants;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: WebSocketConstants.java
* @DESC		: web socket constants 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 10. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface WebSocketConstants {
	final String APP_DESTINATION_PREFIX = "/app";
	final String USER_DESTINATION_PREFIX = "/user";
	final String ENDPOINT_PREFIX = "/ws";
	
	//final String DESTINATION_PREFIX = 
	enum Type{
		USER_TOPIC("topic")
		,DATABASE("database");
		//,FILE("file");
		
		String clientDestination; 
		String endPoint; 
		String destMatcher;
		
		Type(String dest){
			this.clientDestination =  String.format("%s/%s", USER_DESTINATION_PREFIX, dest);
			this.endPoint = String.format("%s/%s", ENDPOINT_PREFIX, dest);
			this.destMatcher = String.format("%s/%s%s", USER_DESTINATION_PREFIX, dest, "/**");
		}

		public String getClientDestination() {
			return clientDestination;
		}

		public String getEndPoint() {
			return endPoint;
		}

		public String getDestMatcher() {
			return destMatcher;
		}
	}
}
