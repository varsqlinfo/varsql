package com.varsql.core.common.beans;

/**
 * 
 * @FileName  : ClientPcInfo.java
 * @프로그램 설명 : client pc info
 * @Date      : 2019. 9. 21. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ClientInfo {
	private String userAgent;
	private String osType;
	private String browser;
	private String deviceType;
	private String ip;
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
