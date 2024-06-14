package com.varsql.web.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.util.FieldUtils;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.model.mapper.GenericMapper;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.PagingUtil;

public final class VarsqlUtils {

	private VarsqlUtils() {}

	public static boolean isAjaxRequest(HttpServletRequest request){
		String headerInfo = request.getHeader("X-Requested-With");

		if("XMLHttpRequest".equals(headerInfo)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 *
	 * @Method Name  : setResponseDownAttr
	 * @Method 설명 : 파일 다운로드  송성 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 23.
	 * @변경이력  :
	 * @param res
	 * @param fileName
	 */
	
	public static void setResponseDownAttr(HttpServletResponse res, String fileName) throws UnsupportedEncodingException {
		setResponseDownAttr(res, null, fileName);
	}

	public static void setResponseDownAttr(HttpServletResponse res, HttpServletRequest req, String fileName)  throws UnsupportedEncodingException {
		res.setContentType("application/octet-stream");

		if (req != null) {
			fileName = FileServiceUtils.getDownloadFileName(req, fileName);
		}
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", fileName));
		res.setCharacterEncoding(VarsqlConstants.CHAR_SET);
		res.setHeader("Content-Transfer-Encoding", "binary;");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1;");
	}

	/**
	 *
	 * @Method Name  : textDownload
	 * @Method 설명 : 텍스트 다운로드.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24.
	 * @변경이력  :
	 * @param output
	 * @param cont
	 * @throws IOException
	 */
	public static void textDownload(OutputStream output, String cont) throws IOException{
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output,VarsqlConstants.CHAR_SET))){
			out.write(cont);
			out.newLine();
			out.close();
		}
	}

	public static String getVonnid (HttpServletRequest req) {
		return (String)req.getAttribute(HttpParamConstants.VCONNID);
	}

	public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

	public static boolean isRuntimeLocal() {
		return "local".equals(VarsqlConstants.RUNTIME);
	}

	/**
	 *
	 * @Method Name  : getIncludeDefaultParam
	 * @Method 설명  : 기본 파리미터 포함.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	public static DataMap getIncludeDefaultParam(HttpServletRequest req) {
		DataMap parameter = HttpUtils.getServletRequestParam(req);
		return setDefaultParam(parameter);
	}

	/**
	 *
	 * @Method Name  : setDefaultParam
	 * @Method 설명 : 기본정보 셋팅.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 20.
	 * @변경이력  :
	 * @param paramInfo
	 * @return
	 */
	public static DataMap setDefaultParam(DataMap paramInfo) {
		// dp  = default parameter;
		paramInfo.put("dp_viewId", SecurityUtil.userViewId());
		return paramInfo;
	}

	/**
	 * @method  : convertSearchInfoToPage
	 * @desc : searchparameter 를 페이지 정보로  변환.
	 * @author   : ytkim
	 * @date   : 2020. 4. 14.
	 * @param searchInfo
	 * @return
	 */
	public static Pageable convertSearchInfoToPage(SearchParameter searchInfo) {
		return convertSearchInfoToPage(searchInfo, "regDt");
	}

	public static Pageable convertSearchInfoToPage(SearchParameter searchInfo, Sort sort) {
		return PageRequest.of(searchInfo.getPageNo() -1, searchInfo.getCountPerPage(), sort);
	}

	public static Pageable convertSearchInfoToPage(SearchParameter searchInfo, String ... sort) {
		return PageRequest.of(searchInfo.getPageNo() -1, searchInfo.getCountPerPage(), searchInfo.isSortAscFlag() ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
	}

	public static ResponseResult getResponseResult(Page<?> result, SearchParameter searchParameter) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(result.getContent());
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));
		return responseResult;
	}

	public static ResponseResult getResponseResult(List <?> result, long totalCount , SearchParameter searchParameter) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(result);
		responseResult.setPage(PagingUtil.getPageObject(totalCount, searchParameter));
		return responseResult;
	}


	/**
	 * @method  : getResponseResultItemOne
	 * @desc : item 1개 return
	 * @author   : ytkim
	 * @date   : 2020. 4. 20.
	 * @param obj
	 * @return
	 */
	public static ResponseResult getResponseResultItemOne(Object obj) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemOne(obj);
		return responseResult;
	}

	public static ResponseResult getResponseResultItemList(List<?> list) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(list);
		return responseResult;
	}

	/**
	 * @method  : mapToJsonObjectString
	 * @desc : key  & json vlaue ->  json object string
	 * @author   : ytkim
	 * @date   : 2020. 4. 17.
	 * @param result
	 * @return
	 */
	public static String mapToJsonObjectString(Map<String,String>  info) {

		StringBuffer returnVal = new StringBuffer();
		boolean firstFlag = true;
		returnVal.append("{");

		for( Map.Entry<String, String> item : info.entrySet() ){
			String key = item.getKey();
			String prefVal = item.getValue();

			returnVal.append(firstFlag?"" : ",").append("\"").append(key).append("\":").append(prefVal);

			firstFlag = false;
        }

		returnVal.append("}");

		return returnVal.toString();
	}

	public static ResponseResult getResponseResultValidItem(BindingResult result) {
		return getResponseResultValidItem(new ResponseResult(), result);
	}
	public static ResponseResult getResponseResultValidItem(ResponseResult resultObject, BindingResult result) {
		resultObject.setResultCode(RequestResultCode.DATA_NOT_VALID);

		List<FieldError> fieldErrors = result.getFieldErrors();

		String errorMessage = "";
		if(fieldErrors.size() >0) {
			FieldError errorInfo = fieldErrors.get(0);
			errorMessage= errorMessage(errorInfo);

			resultObject.setItemOne(errorInfo.getField());
		}else {
			List<ObjectError> allErrors = result.getAllErrors();
			if(allErrors.size() >0) {
				ObjectError errorInfo = allErrors.get(0);
				resultObject.setItemOne(errorInfo.getCode());
				errorMessage= String.format("code : %s, message : %s", errorInfo.getCode() , errorInfo.getDefaultMessage());
			}else {
				resultObject.setItemOne(result.getGlobalError().getCode());
				errorMessage= String.format("code : %s, message : %s", result.getGlobalError().getCode() ,result.getGlobalError().getDefaultMessage());
			}
		}

		resultObject.setMessage(errorMessage);
		return resultObject;
	}

	public static ResponseResult getResponseResult(Page<?> result, SearchParameter searchParameter, GenericMapper instance) {

		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(result.stream().map(item -> instance.toDto(item)).collect(Collectors.toList()));
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));
		return responseResult;
	}

	public static ResponseResult getResponseResult(List<?> result, GenericMapper instance) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(result.stream().map(item -> instance.toDto(item)).collect(Collectors.toList()));
		return responseResult;
	}
	
	/**
	 * 언어 변경. 
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param changeLocale 변경할 locale
	 */
	public static void changeLocale(HttpServletRequest request, HttpServletResponse response, Locale changeLocale) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		
		request.getSession().setAttribute(HttpSessionConstants.USER_LOCALE, LocaleConstants.localeToLocaleCode(changeLocale));
		
		if(localeResolver != null) {
			localeResolver.setLocale(request, response, changeLocale);
		}else {
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, changeLocale);
		}
	}
	
	/**
	 * 현재 다국어 locale
	 *  
	 * @param request HttpServletRequest
	 * @return locale 	app locale 정보
	 */
	public static Locale getAppLocale(HttpServletRequest request) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		
		if(localeResolver != null) {
			return localeResolver.resolveLocale(request);
		}else {
			Object locale = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME); 
			if(locale instanceof Locale) {
				return (Locale)locale;
			}
			
			return null;
		}
	}
	
	public static List<FieldError> validationCheck(Object value, String... propertyNames) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<FieldError> result = new LinkedList<>();
		
		if(propertyNames.length > 0) {
			for (String propertyName : propertyNames) {
				validationCheck(PropertyUtils.getProperty(value, propertyName), result);
			}
		}else {
			validationCheck(value, result);
		}
		
	    return result; 
	}
	
	/**
	 * validation check
	 * @param value
	 * @param result
	 * @return
	 */
	private static List<FieldError> validationCheck(Object value, List<FieldError> result){
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
	    Set<ConstraintViolation<Object>> violations = validator.validate(value);
	    if (!violations.isEmpty()) {
	    	Iterator<ConstraintViolation<Object>> iter = violations.iterator();
	    	while(iter.hasNext()) {
	    		ConstraintViolation errorInfo = iter.next();
	    		String propertyPath = errorInfo.getPropertyPath().toString();
	    		result.add(new FieldError(value.getClass().getName(), propertyPath, errorInfo.getInvalidValue(), false,null, null, errorInfo.getMessage()));
	    	}
	    }
	    
	    return result; 
	}

	public static String errorMessage(FieldError errorInfo) {
		return String.format("field : %s\nmessage : %s\nvalue : %s", errorInfo.getField(), errorInfo.getDefaultMessage() , errorInfo.getRejectedValue());
	}
}