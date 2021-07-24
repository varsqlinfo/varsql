package com.varsql.core.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.beans.ClientPcInfo;

public final class CommUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommUtil.class);

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

	private CommUtil() {}
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
        
        String userAgentLower = userAgent.toLowerCase();
        
        if (userAgentLower.indexOf("windows") > -1) {
        	if (userAgentLower.indexOf("windows nt 10.0") > -1) {
        		osType = "win-10";
        	} else if (userAgentLower.indexOf("windows nt 6.1") > -1) {
				osType = "win-7";
			} else if (userAgentLower.indexOf("windows nt 6.2") > -1 || userAgentLower.indexOf("windows nt 6.3") > -1) {
				osType = "win-8";
			} else if (userAgentLower.indexOf("windows nt 6.0") > -1) {
				osType = "win-vista";
			} else if (userAgentLower.indexOf("windows nt 5.1") > -1) {
				osType = "win-xp";
			} else if (userAgentLower.indexOf("windows nt 5.0") > -1) {
				osType = "win-2000";
			} else if (userAgentLower.indexOf("windows nt 4.0") > -1) {
				osType = "win-nt";
			} else {
				osType = "windows";
			}
		}else if(userAgentLower.indexOf("mac os") > -1) {
			osType = "Mac";

            if(userAgentLower.indexOf("iPhone") >= 0) {
                deviceType = "iPhone";
            } else if(userAgentLower.indexOf("iPad") >= 0) {
                deviceType = "iPad";
            }
		}else if (userAgentLower.indexOf("X11") >= 0) {
            osType = "Unix";
        } else if (userAgentLower.indexOf("android") > -1) {
			osType = "Android";
			deviceType = "Android";
		}else if (userAgentLower.indexOf("linux") > -1) {
			osType = "Linux";
		}else {
			osType = "other os";
		}

        if(userAgentLower.indexOf("trident") > -1 || userAgentLower.indexOf("msie") > -1) { //IE
        	
        	if(userAgentLower.indexOf("edge") > -1) {
    			browser = "edge";
    		}else {
    			if(userAgentLower.indexOf("trident/7") > -1) {
        			browser = "ie11";
        		}else if(userAgentLower.indexOf("trident/6") > -1) {
        			browser = "ie10";
        		}else {
        			browser = "ie9";
        		}
    		}
        }else if(userAgentLower.indexOf("edge") > -1) {
        	browser = "edge";
    	}else if(userAgentLower.indexOf("whale") > -1){ // whale
    		browser = "whale";
    	}else if(userAgentLower.indexOf("opera") > -1 || userAgentLower.indexOf("opr") > -1){ //opera
    		browser = "opera";
    	}else if(userAgentLower.indexOf("firefox") > -1){ //firefox
    		browser = "firefox";
    	}else if(userAgentLower.indexOf("safari") > -1 && userAgentLower.indexOf("chrome") == -1 ){ //safari
    		browser = "safari";
    	}else if(userAgentLower.indexOf("chrome") > -1){ //chrome
    		browser = "chrome";
    	}else {
    		browser = "UnKnown, More-Info: " + userAgentLower;
    	}
        
        ClientPcInfo cpi = new ClientPcInfo();

        cpi.setUserAgent(userAgent);
        cpi.setOsType(osType);
        cpi.setDeviceType(deviceType);
        cpi.setBrowser(browser);
        cpi.setIp(getClientIp(request));
        return cpi;
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
}
