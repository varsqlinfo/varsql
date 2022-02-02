package com.varsql.core.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.varsql.core.common.beans.ClientPcInfo;
import com.varsql.core.common.constants.VarsqlConstants;

public final class CommUtils {

	final static String HOSTNAME;
	static {
		String hostName = "";
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME")) {
	    	hostName= env.get("COMPUTERNAME");
	    }else if (env.containsKey("HOSTNAME")) {
	    	hostName=env.get("HOSTNAME");
	    }

	    if("".equals(hostName)) {
	    	try {
				hostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {

			}
	    }

	    HOSTNAME = hostName;
	}

	private CommUtils() {}
	/**
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean numberChk(String str){
	    char c;

	    if("".equals(str)) return false;

	    for(int i = 0 ; i < str.length() ; i++){
	        c = str.charAt(i);
	        if(c < 48 || c > 59){
	        	return false;
	        }
	    }
	    return true;
	}

	/**
	 * remove "," in string.
	 * @return String
	 * @param s java.lang.String
	 */
	public static String removeComma(String s){
		if ( s == null ){
			return null;
		}
		return s.replaceAll("\\,", "");
	}

	/**
	 * space remove
	 * "te st"
	 * @param s
	 * @return String "test"
	 */
	public static String trim(String s) {

		if ( s == null ) return "";

		return s.replaceAll("\\p{Space}", "");
	}

	/**
	 * @param key
	 * @param initVal
	 * @param delim
	 * @return String[]
	 */
	public static String[] split(String str, String delim){

		if("".equals(str)) return new String[0];

		int position=0;
		int delimiterIdx = 0;
		int strLen = str.length();
		List<String> resultList = new ArrayList<String>();

		int len = delim.length();
		while(position <= strLen){
			delimiterIdx = str.indexOf(delim,position);
			if(delimiterIdx > -1){
				resultList.add(str.substring(position, delimiterIdx));
			}else {
				resultList.add(str.substring(position, strLen));
				break;
			}
			position = delimiterIdx+len;
		}

		return (String[]) resultList.toArray(new String[]{});
	}

	/**
	 *
	 * @Method Name  : getExceptionStr
	 * @Method 설명 : get exception content string
	 * @작성일   : 2019. 4. 15.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param e
	 * @return
	 */
	public static String getExceptionStr(Throwable e) {
		return ExceptionUtils.getStackTrace(e);
	}

	public static String getHostname () {
		return HOSTNAME;
	}

	/**
	 *
	 * @Method Name  : getClientPcInfo
	 * @Method 설명 : client info
	 * @작성일   : 2019. 9. 21.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param request
	 * @return
	 */

	public static ClientPcInfo getClientPcInfo(HttpServletRequest request) {
		String userAgent = "Unknown";
	    String osType = "Unknown";
	    String browser = "Unknown";
	    String deviceType = "pc";

        userAgent = request.getHeader("User-Agent");

        if (userAgent.indexOf("Windows NT") >= 0) {
            osType = "Windows";
        } else if (userAgent.indexOf("Mac OS") >= 0) {
            osType = "Mac";

            if(userAgent.indexOf("iPhone") >= 0) {
                deviceType = "iPhone";
            } else if(userAgent.indexOf("iPad") >= 0) {
                deviceType = "iPad";
            }

        } else if (userAgent.indexOf("X11") >= 0) {
            osType = "Unix";
        } else if (userAgent.indexOf("android") >= 0) {
            osType = "Android";
            deviceType = "Android";
        }

        String userAgentLower = userAgent.toLowerCase();

        if (userAgentLower.contains("msie") || userAgentLower.contains("rv")) {
        	browser= "msie";
        } else if (userAgentLower.contains("safari") && userAgentLower.contains("version")) {
        	browser= "Safari";
        } else if (userAgentLower.contains("opr") || userAgentLower.contains("opera")) {
        	browser= "opera";
        } else if(userAgentLower.contains("edge")){
            browser = "edge";
        } else if (userAgentLower.contains("chrome")) {
        	browser= "chrome";
        } else if ((userAgentLower.indexOf("mozilla/7.0") > -1) || (userAgentLower.indexOf("netscape6") != -1) || (userAgentLower.indexOf(
                "mozilla/4.7") != -1) || (userAgentLower.indexOf("mozilla/4.78") != -1) || (userAgentLower.indexOf(
                "mozilla/4.08") != -1) || (userAgentLower.indexOf("mozilla/3") != -1)) {
            browser = "Netscape";
        } else if (userAgentLower.contains("firefox")) {
        	browser= "firefox";
        } else{
            browser = "UnKnown, More-Info: " + userAgentLower;
        }

        ClientPcInfo cpi = new ClientPcInfo();

        cpi.setUserAgent(userAgent);
        cpi.setOsType(osType);
        cpi.setDeviceType(deviceType);
        cpi.setBrowser(browser.toLowerCase());
        cpi.setIp(getClientIp(request));
		return cpi;
	}

	public static boolean isIE(ClientPcInfo clientPcInfo) {
		return "msie".equalsIgnoreCase(clientPcInfo.getBrowser());
	}

	public static boolean isChrome(ClientPcInfo clientPcInfo) {
		return "chrome".equalsIgnoreCase(clientPcInfo.getBrowser());
	}

	public static boolean isFirefox(ClientPcInfo clientPcInfo) {
		return "firefox".equalsIgnoreCase(clientPcInfo.getBrowser());
	}

	public static boolean isSafari(ClientPcInfo clientPcInfo) {
		return "safari".equalsIgnoreCase(clientPcInfo.getBrowser());
	}

	public static boolean isOpera(ClientPcInfo clientPcInfo) {
		return "opera".equalsIgnoreCase(clientPcInfo.getBrowser());
	}

	/**
	 *
	 * @Method Name  : getClientIp
	 * @Method 설명 : ip 정보.
	 * @작성일   : 2019. 9. 21.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	public static String getClientIp(HttpServletRequest req) {

		String[] headerKeyArr = {  "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP"
				,"HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED"
				,"HTTP_FORWARDED_FOR", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_FORWARDED"
		};

		for (String headerKey : headerKeyArr) {
			String ip = req.getHeader(headerKey);

			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		return req.getRemoteAddr();
	}

	/**
	 *
	 * @Method Name  : getFileIds
	 * @Method 설명 : file id 배열로 자르기
	 * @작성일   : 2020. 9. 21.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	public static String [] getFileIds (String fileIds) {
		return fileIds.split(VarsqlConstants.FILE_ID_DELIMITER);
	}
}
