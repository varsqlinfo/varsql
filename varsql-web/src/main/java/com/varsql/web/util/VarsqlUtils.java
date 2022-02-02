package com.varsql.web.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.model.mapper.base.GenericMapper;
import com.vartech.common.app.beans.ParamMap;
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
			res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", FileServiceUtils.getDownloadFileName(req, fileName)));
		} else {
			res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", fileName));
		}
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
		return (String)req.getAttribute(VarsqlParamConstants.VCONNID);
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
		responseResult.setItemList(result.getContent());
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));
		return responseResult;
	}

	public static ResponseResult getResponseResult(List <?> result, long totalCount , SearchParameter searchParameter) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemList(result);
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
		responseResult.setItemList(list);
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

	public static ResponseResult getResponseResultValidItem(ResponseResult resultObject, BindingResult result) {
		resultObject.setResultCode(RequestResultCode.DATA_NOT_VALID);

		List<FieldError> fieldErrors = result.getFieldErrors();

		String errorMessage = "";
		if(fieldErrors.size() >0) {
			FieldError errorInfo = fieldErrors.get(0);
			errorMessage= String.format("field : %s\nmessage : %s\nvalue : %s", errorInfo.getField(), errorInfo.getDefaultMessage() , errorInfo.getRejectedValue());

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
		responseResult.setItemList(result.stream().map(item -> instance.toDto(item)).collect(Collectors.toList()));
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));
		return responseResult;
	}

	public static ResponseResult getResponseResult(List<?> result, GenericMapper instance) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setItemList(result.stream().map(item -> instance.toDto(item)).collect(Collectors.toList()));
		return responseResult;
	}
}