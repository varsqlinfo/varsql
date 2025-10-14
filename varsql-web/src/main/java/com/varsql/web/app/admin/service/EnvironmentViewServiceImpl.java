package com.varsql.web.app.admin.service;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.common.service.AbstractService;

/**
 * -----------------------------------------------------------------------------
* @fileName		: EnvironmentViewServiceImpl.java
* @desc		: 설정 환경 정보 보기
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2025. 10. 10. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class EnvironmentViewServiceImpl  extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(EnvironmentViewServiceImpl.class);
	
	/**
	 * app config info
	 * @return
	 */
	public Map appConfigInfo() {
		Map<String, Object> combinedMap = new TreeMap<>();
		
		
		Configuration configInstance = Configuration.getInstance();

        // 제외할 메서드 이름 목록
        Set<String> excludeMethods = new HashSet<>();
        excludeMethods.add("getInstance");
        excludeMethods.add("getMailConfigBean");
        excludeMethods.add("getVarsqlDB");
        excludeMethods.add("getDbPwSecurityKey");


        // 클래스 정보
        Class<?> clazz = configInstance.getClass();

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            
            if(!methodName.startsWith("get")) continue;

            // 필터 조건
            boolean isDeclaredHere = method.getDeclaringClass().equals(clazz);
            boolean isPublic = Modifier.isPublic(method.getModifiers());
            boolean isNoArg = method.getParameterCount() == 0;
            boolean isNotExcluded = !excludeMethods.contains(methodName);
            
            methodName = methodName.replace("get", "");

            if (isDeclaredHere && isPublic && isNoArg && isNotExcluded) {
                try {
                    Object result = method.invoke(configInstance);
                    combinedMap.put(methodName, result);
                } catch (Exception e) {
                	combinedMap.put(methodName, "Error: " + e.getMessage());
                }
            }
        }
        
		combinedMap.put("db", Configuration.getInstance().getVarsqlDB());
		combinedMap.put("mail", Configuration.getInstance().getMailConfigBean());
		
		return combinedMap; 
	}

	public Map systemInfo() {
		Map<String, String> systemInfoMap = getOsInfo(); // TreeMap은 key 기준으로 정렬
		Map<String, String> javaInfoMap = javaEnvInfo(); // TreeMap은 key 기준으로 정렬
		
		Map<String, Object> combinedMap = new LinkedHashMap<>();
		
		combinedMap.putAll(systemInfoMap);
		combinedMap.putAll(javaInfoMap);
		
		return combinedMap;
	}
	
	private Map<String, String> getOsInfo() {
		Map<String, String> systemInfoMap = new LinkedHashMap<>();
		// 운영체제
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        // 언어/로케일
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();               // 예: "ko"
        String country = locale.getCountry();                 // 예: "KR"
        String displayLocale = locale.toString();             // 예: "ko_KR"

        // 타임존 (여러 방법: System property, TimeZone, java.time)
        String propUserTimezone = System.getProperty("user.timezone"); // may be null
        TimeZone tz = TimeZone.getDefault();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        // 출력
        systemInfoMap.put("os.name", osName);        
        systemInfoMap.put("os.arch", osArch);
        systemInfoMap.put("os.version", osVersion);
        
        //systemInfoMap.put("user.name", System.getProperty("user.name"));    // 프로세스 실행 사용자
        //systemInfoMap.put("user.home", System.getProperty("user.home"));    // 사용자 홈 디렉토리
        //systemInfoMap.put("user.dir", System.getProperty("user.dir"));      // 현재 작업 디렉토리);

        systemInfoMap.put("user.language", language);
        systemInfoMap.put("user.country", country);
        systemInfoMap.put("locale", displayLocale);

        systemInfoMap.put("user.timezone", propUserTimezone);
        systemInfoMap.put("TimeZone.getDefault().getID()", tz.getID());
        systemInfoMap.put("ZoneId.systemDefault()", zoneId+"");
        systemInfoMap.put("Current time (zone)", now+"");
        
        return systemInfoMap; 
	}

	
	/**
	 * java 설정 정보 보기
	 * @return
	 */
	public Map<String,String> javaEnvInfo() {

        // 보완: Charset으로 얻기 (더 신뢰성 있음)
        String charsetName = Charset.defaultCharset().name();

        // 보완: RuntimeMXBean으로 VM 버전/명 얻기
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        String vmName = rb.getVmName();      // VM 이름 (예: "OpenJDK 64-Bit Server VM")
        String vmVersion = rb.getVmVersion(); // VM 버전 (예: "17.0.10+7-LTS")

        Map<String,String> info = new LinkedHashMap<>();
        //info.put("java.home", System.getProperty("java.home"));
        info.put("charset", charsetName);
        info.put("file.encoding", System.getProperty("file.encoding"));             // 기본 문자 인코딩 (may be null)
        info.put("java.runtime.version", System.getProperty("java.runtime.name"));       // 예: "OpenJDK Runtime Environment"
        info.put("java.runtime.name", System.getProperty("java.runtime.version")); // JRE / JDK 런타임 버전 문자열
        info.put("vm.name", vmName);
        info.put("vm.version", vmVersion);

		return info;
	}
}