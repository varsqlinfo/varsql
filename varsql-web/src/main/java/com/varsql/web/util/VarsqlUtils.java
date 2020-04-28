package com.varsql.web.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.model.converter.DomainMapper;
import com.varsql.web.model.entity.user.UserEntity;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.PagingUtil;

public final class VarsqlUtils {

	private VarsqlUtils() {}

	public static String generateUUID (){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static boolean isAjaxRequest(HttpServletRequest request){
		String headerInfo = request.getHeader("X-Requested-With");

		if("XMLHttpRequest".equals(headerInfo)){
			return true;
		}else{
			return false;
		}
	}

	public static String getVconnID(HttpServletRequest req) {
		return (String) req.getAttribute(VarsqlParamConstants.VCONNID);
	}

	public static <T> T stringToObject(String jsonString) {
		return (T) stringToObject(jsonString, ParamMap.class);
	}

	public static <T> T stringToObject(String jsonString, Class<T> valueType) {
		return stringToObject(jsonString, valueType, false);
	}

	/**
	 *
	 * @Method Name : stringToObject
	 * @Method 설명 : string to object , 프로퍼티 업을때 err여부.
	 * @작성자 : ytkim
	 * @작성일 : 2018. 10. 12.
	 * @변경이력 :
	 * @param jsonString
	 * @param valueType
	 * @param ignoreProp
	 * @return
	 */
	public static <T> T stringToObject(String jsonString, Class<T> valueType, boolean ignoreProp) {
		try {
			ObjectMapper om = new ObjectMapper();
			if (ignoreProp) {
				om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			} else {
				om.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			}
			return om.readValue(jsonString, valueType);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String objectToString(Object json) {
		try {
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return objectMapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}


	/**
	 *
	 * @Method Name  : setPagingParam
	 * @Method 설명 : 페이징 파라미터 셋팅
	 * @작성일   : 2015. 4. 27.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public static DataCommonVO setPagingParam(DataCommonVO paramMap) {
		int page = paramMap.getInt(VarsqlParamConstants.SEARCH_NO, VarsqlParamConstants.SEARCH_DEFAULT_FIRST);
		int rows = paramMap.getInt(VarsqlParamConstants.SEARCH_ROW,VarsqlParamConstants.SEARCH_DEFAULT_ROW);

		int first = (page-1)*rows ;

		paramMap.put(VarsqlParamConstants.SEARCH_FIRST, first);
		paramMap.put(VarsqlParamConstants.SEARCH_ROW, rows);

		return paramMap;
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
	public static void setResponseDownAttr(HttpServletResponse res, String fileName){
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";",fileName));
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
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output));
		out.write(cont);
		out.newLine();
		out.close();
	}

	public static String getCurrentTimestamp(){
		return getCurrentTimestamp(System.currentTimeMillis());
	}

	public static String getCurrentTimestamp(long time){
		return new Timestamp(time).toString();
	}

	public static String getClientIP(HttpServletRequest request) {
	     String ip = request.getHeader("X-FORWARDED-FOR");

	     if (ip == null || ip.length() == 0) {
	         ip = request.getHeader("Proxy-Client-IP");
	     }

	     if (ip == null || ip.length() == 0) {
	         ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
	     }

	     if (ip == null || ip.length() == 0) {
	         ip = request.getRemoteAddr() ;
	     }

	     return ip;
	}

	public String getClientIpAddr(HttpServletRequest request) {
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

	public static boolean isRuntimelocal() {
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
	public static ParamMap getIncludeDefaultParam(HttpServletRequest req) {
		ParamMap parameter = HttpUtils.getServletRequestParam(req);
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
	public static ParamMap setDefaultParam(ParamMap paramInfo) {
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
	public static org.springframework.data.domain.PageRequest convertSearchInfoToPage(SearchParameter searchInfo) {
		return convertSearchInfoToPage(searchInfo, "regDt");
	}

	public static org.springframework.data.domain.PageRequest convertSearchInfoToPage(SearchParameter searchInfo, String ... sort) {
		return org.springframework.data.domain.PageRequest.of(searchInfo.getPageNo() -1, searchInfo.getCountPerPage(), searchInfo.isSortAscFlag() ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
	}

	public static ResponseResult getResponseResult(Page<?> result, SearchParameter searchParameter) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemList(result.getContent());
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalPages(), searchParameter));
		return responseResult;
	}

	public static ResponseResult getResponseResult(Page<?> result, SearchParameter searchParameter, DomainMapper domainMapper, Class<?> mapperClass) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemList(result.getContent().stream().map(item -> domainMapper.convertToDomain(item, mapperClass)).collect(Collectors.toList()));
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalPages(), searchParameter));
		return responseResult;
	}
	
	public static ResponseResult getResponseResult(List <?> result, DomainMapper domainMapper, Class<?> mapperClass) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemList(result.stream().map(item -> domainMapper.convertToDomain(item, mapperClass)).collect(Collectors.toList()));
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
		responseResult.setItemList(list);
		return responseResult;
	}

	/**
	 * @method  : getErrorCodeMessage
	 * @desc : binding error 메시지 처리.
	 * @author   : ytkim
	 * @date   : 2020. 4. 17.
	 * @param result
	 * @return
	 */
	public static String getErrorCodeMessage(BindingResult result) {
		ObjectError errorInfo = result.getAllErrors().get(0);
		return String.format("%s", errorInfo.getDefaultMessage());
	}
}