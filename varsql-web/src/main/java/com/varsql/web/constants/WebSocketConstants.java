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
		USER("user")
		,DATABASE("database");
		//,FILE("file");
		
		String clientDestination; 
		String endPoint; 
		String destMatcher;
		
		Type(String dest){
			this(dest, dest);
		}
		
		Type(String dest, String endPoint){
			this(dest, endPoint, dest);
		}
		
		Type(String dest, String endPoint, String destMatcher){
			this.clientDestination =  String.format("%s/%s", USER_DESTINATION_PREFIX, dest);
			
			if(endPoint != null) {
				this.endPoint = String.format("%s/%s", ENDPOINT_PREFIX, dest);
			}
			
			if(dest.equals(destMatcher)) {
				this.destMatcher = String.format("%s/%s%s", USER_DESTINATION_PREFIX, destMatcher, "/**");
			}else {
				this.destMatcher = String.format("%s/%s", USER_DESTINATION_PREFIX, destMatcher);
			}
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
