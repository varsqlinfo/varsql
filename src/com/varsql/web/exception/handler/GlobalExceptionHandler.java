package com.varsql.web.exception.handler;

import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.varsql.web.exception.VarsqlAppException;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 
	 * @Method Name  : sqlExceptionHandle
	 * @Method 설명 : sql exception 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value=SQLException.class)
	public @ResponseBody ResponseResult sqlExceptionHandle(SQLException ex, HttpServletResponse response){
		
		logger.error("sqlExceptionHandle "+ getClass().getName(),ex);
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.OK.value());
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ResultConst.CODE.ERROR.toInt());
		result.setMessage(ex.getMessage());
		return result; 
	}
	
	/**
	 * 
	 * @Method Name  : runtimeExceptionHandle
	 * @Method 설명 : 실행시 에러 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value=RuntimeException.class)
	public @ResponseBody ResponseResult runtimeExceptionHandle(RuntimeException ex, HttpServletResponse response){
		
		logger.error("runtimeExceptionHandle : ", getClass().getName(),ex);
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.OK.value());
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ResultConst.CODE.ERROR.toInt());
		result.setMessage(ex.getMessage());
		return result; 
	}
	
	/**
	 * 
	 * @Method Name  : epptlExceptionHandle
	 * @Method 설명 : 포틀릿 에러 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value=VarsqlAppException.class)
	public @ResponseBody ResponseResult epptlExceptionHandle(VarsqlAppException ex, HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.OK.value());
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ex.getErrorCode() > 0 ? ex.getErrorCode() : ResultConst.CODE.ERROR.toInt());
		result.setMessageCode(ex.getMessageCode());
		result.setMessage(ex.getErrorMessage());
		return result; 
	}
	
}
